package cs3240.sp09.MicroAWKIntepreter;
import cs3240.sp09.AbstractSyntaxTree.*;

/**
 * Parses the given script file into an Abstract Syntax Tree. Most functions here correspond to
 * a CFgrammar in the Micro-Awk.txt CFG specficiation. 
 */
public class ScriptParser {
	
	private ScriptReader reader;
	
	public ScriptParser(String script){
		reader = new ScriptReader(script);
	}
	
	public ASTNode parse() {
		return program();
	}
	
	public ASTNode program(){
		ASTNode programNode = new ASTNode(ASTNode.NodeType.Program);
		try{
			programNode.setLeftChild(statement());
		} catch(MawkSyntaxException e){
			System.err.println("Parse error: " + e.getMessage());
			System.err.println("Dumping partial AST:");
			System.err.println(e.getNode().toString());
			System.err.println("Discarding rest of sentence...");
			programNode.setLeftChild(null);
			try {
				reader.skipAheadToNextProgramLine();
			} catch(InvalidStatementBlockException ex){
				System.err.println(ex.getMessage());
			}
		}
		if(reader.token != (char)-1){ // if not end of file
			programNode.setRightChild(program());
		}
		return programNode;
	}

	public ASTNode statement() throws MawkSyntaxException {
		ASTNode statementNode = new ASTNode(ASTNode.NodeType.Statement);
		try {
			switch(reader.token){
				case 'B': // BEGIN
					statementNode.setLeftChild(begin());
					break;
				case 'E': // END
					statementNode.setLeftChild(end());
					break;
				case 'w': // while
					statementNode.setLeftChild(whileLoop());
					break;
				case 'f': // for
					statementNode.setLeftChild(forLoop());
					break;
				case '{': // no regex sentence
					reader.match('{');
					statementNode.setLeftChild(funcBlock());
					reader.match('}');
					break;
				default: // sentence with regex
					statementNode.setLeftChild(rfPair());
					break;
			}
		} catch (MawkMatchException ex) {
			statementNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(statementNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			statementNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(statementNode, ex.getMessage());
		}
		return statementNode;
	}
	
	public ASTNode regex() throws MawkSyntaxException{
		ASTNode regexNode = new RegexNode(ASTNode.NodeType.Regex);
		try {
			regexNode.setLeftChild(term());
			if(reader.token == '|'){
				reader.match('|');
				regexNode.setRightChild(regex());
			}
		} catch (MawkMatchException ex){
			regexNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(regexNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			regexNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(regexNode, ex.getMessage());
		}
		return regexNode;
	}
	
	public ASTNode term() throws MawkSyntaxException {
		ASTNode termNode = new ASTNode(ASTNode.NodeType.Term);
		try { // left child try block
			termNode.setLeftChild(factor());
		} catch (MawkSyntaxException ex){
			termNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(termNode, ex.getMessage());
		}
		try { // right child try block
			if(reader.token == 'a' || reader.token == 'b' || reader.token == 'c' || reader.token == '('){
				termNode.setRightChild(term());
			}
		} catch (MawkSyntaxException ex){
			termNode.setRightChild(ex.getNode());
			throw new MawkSyntaxException(termNode, ex.getMessage());
		}
		return termNode;
	}
	
	public ASTNode factor() throws MawkSyntaxException {
		ASTNode factorNode = new ASTNode(ASTNode.NodeType.Factor);
		try{ // left child try block
			factorNode.setLeftChild(atom());
		} catch(MawkSyntaxException ex){
			factorNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(factorNode, ex.getMessage());
		}
		try { // right child try block
			if(reader.token == '*' || reader.token == '+' || reader.token == '?'){
				factorNode.setRightChild(metacharacter());
			}
		} catch (MawkSyntaxException ex){
			factorNode.setRightChild(ex.getNode());
			throw new MawkSyntaxException(factorNode, ex.getMessage());
		}
		return factorNode;
	}

	private ASTNode atom() throws MawkSyntaxException  {
		ASTNode atomNode = new ASTNode(ASTNode.NodeType.Atom);
		try {
			if(reader.token == '('){
				reader.match('(');
				atomNode.setLeftChild(regex());
				reader.match(')');
			} else {
				atomNode.setLeftChild(character());
			}
		} catch (MawkMatchException ex){
			atomNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(atomNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			atomNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(atomNode, ex.getMessage());
		}
		return atomNode;
	}

	public ASTNode metacharacter() throws MawkSyntaxException {
		ASTNode metacharacterNode = new ASTNode(ASTNode.NodeType.Metacharacter);
		try {
			switch(reader.token){
			case '*':
				metacharacterNode.setLeftChild(star());
				break;
			case '+':
				metacharacterNode.setLeftChild(oneOrMore());
				break;
			case '?':
				metacharacterNode.setLeftChild(optional());
				break;
			case '#':
				metacharacterNode.setLeftChild(capture());
				break;
			default:
				throw new MawkSyntaxException(errorNode(), "Invalid metacharacter token: " + reader.token);
			}
		} catch(MawkSyntaxException ex){
			metacharacterNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(metacharacterNode, ex.getMessage());
		}
		return metacharacterNode;
	}
	public ASTNode star() throws MawkSyntaxException {
		ASTNode starNode = new ASTNode(ASTNode.NodeType.Star);
		try {
			reader.match('*');
		} catch (MawkMatchException ex) {
			throw new MawkSyntaxException(errorNode(), ex.getMessage());
		}
		return starNode;
	}
	public ASTNode oneOrMore() throws MawkSyntaxException {
		ASTNode oneOrMoreNode = new ASTNode(ASTNode.NodeType.OneOrMore);
		try {
			reader.match('+');
		} catch(MawkMatchException ex){
			throw new MawkSyntaxException(errorNode(), ex.getMessage());
		}
		return oneOrMoreNode;
	}
	public ASTNode optional() throws MawkSyntaxException {
		ASTNode optionalNode = new ASTNode(ASTNode.NodeType.Optional);
		try {
			reader.match('?');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return optionalNode;
	}
	public ASTNode capture() throws MawkSyntaxException {
		ASTNode captureNode = new ASTNode(ASTNode.NodeType.Capture);
		try {
			reader.match('#');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return captureNode;
	}
	
	public ASTNode begin() throws MawkSyntaxException  {
		ASTNode beginNode = new ASTNode(ASTNode.NodeType.Begin);
		try {
			reader.matchString("BEGIN{");
			beginNode.setLeftChild(funcBlock());
			reader.match('}');
		} catch(MawkMatchException ex){
			beginNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(beginNode, ex.getMessage());
		} catch (MawkSyntaxException ex) {
			beginNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(beginNode, ex.getMessage());
		}
		return beginNode;
	}
	
	public ASTNode end() throws MawkSyntaxException  {
		ASTNode endNode = new ASTNode(ASTNode.NodeType.End);
		try {
			reader.matchString("END{");
			endNode.setLeftChild(funcBlock());
			reader.match('}');
		} catch(MawkMatchException ex){
			endNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(endNode, ex.getMessage());
		} catch (MawkSyntaxException ex) {
			endNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(endNode, ex.getMessage());
		}
		return endNode;
	}
	
	public ASTNode rfPair() throws MawkSyntaxException {
		ASTNode rfPairNode = new ASTNode(ASTNode.NodeType.RFPair);
		try {
			try{
				rfPairNode.setLeftChild(regex());
			} catch(MawkSyntaxException e) {
				rfPairNode.setLeftChild(e.getNode());
				throw new MawkSyntaxException(rfPairNode, e.getMessage());
			}
			try {
				reader.match('{');
				rfPairNode.setRightChild(funcBlock());
				reader.match('}');
			} catch(MawkSyntaxException e) {
				rfPairNode.setRightChild(e.getNode());
				throw new MawkSyntaxException(rfPairNode, e.getMessage());
			}
		} catch(MawkMatchException e) {
			rfPairNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(rfPairNode, e.getMessage());
		}
		return rfPairNode;
	}
	
	public ASTNode funcBlock() throws MawkSyntaxException{
		ASTNode funcBlockNode = new ASTNode(ASTNode.NodeType.FunctionBlock);
		try {
			try { // left child try block
				funcBlockNode.setLeftChild(function());
			} catch (MawkSyntaxException ex){
				funcBlockNode.setLeftChild(ex.getNode());
				throw new MawkSyntaxException(funcBlockNode, ex.getMessage());
			}
			try { // right child try block
				if(reader.token == ';'){
					reader.match(';');
					funcBlockNode.setRightChild(funcBlock());
				}
			} catch (MawkSyntaxException ex){
				funcBlockNode.setRightChild(ex.getNode());
				throw new MawkSyntaxException(funcBlockNode, ex.getMessage());
			}
		} catch(MawkMatchException ex){
			funcBlockNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(funcBlockNode, ex.getMessage());
		}
		return funcBlockNode;
	}
	
	public ASTNode function() throws MawkSyntaxException {
		ASTNode functionNode = new ASTNode(ASTNode.NodeType.Function);
		try {
			switch(reader.token){
			case 's':
				functionNode.setLeftChild(substringFunction());
				break;
			case 'i':
				functionNode.setLeftChild(insertFunction());
				break;
			case 'p':
				functionNode.setLeftChild(printFunction());
				break;
			case 'r':
				functionNode.setLeftChild(reFunction());
				break;
			default:
				throw new MawkMatchException("Invalid first character of function name: " + reader.token);
			}
		} catch (MawkMatchException ex){
			functionNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(functionNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			functionNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(functionNode, ex.getMessage());
		}
		return functionNode;
	}
	
	public ASTNode loopBlock() throws MawkSyntaxException  {
		ASTNode loopBlockNode = new ASTNode(ASTNode.NodeType.LoopBlock);
		try {
			try {
				loopBlockNode.setLeftChild(rfPair());
			} catch(MawkSyntaxException e) {
				loopBlockNode.setLeftChild(e.getNode());
				throw new MawkSyntaxException(loopBlockNode, e.getMessage());
			}
			if(reader.token == '}') {
				//This obviously shouldn't fail
				reader.match('}');
				loopBlockNode.setRightChild(null);
			} else {
				try {
					loopBlockNode.setRightChild(loopBlock());
				} catch(MawkSyntaxException e) {
					loopBlockNode.setRightChild(e.getNode());
					throw new MawkSyntaxException(loopBlockNode, e.getMessage());
				}
			}
		} catch(MawkMatchException e) {
			loopBlockNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(loopBlockNode, e.getMessage());
		}
		return loopBlockNode;
	}
	
	public ASTNode whileLoop() throws MawkSyntaxException  {
		ASTNode whileNode = new ASTNode(ASTNode.NodeType.WhileLoop);
		try {
			reader.matchString("while");
			reader.match('(');
			try {
				whileNode.setLeftChild(regex());
			} catch(MawkSyntaxException e) {
				whileNode.setLeftChild(e.getNode());
				throw new MawkSyntaxException(whileNode, e.getMessage());
			}
			reader.match(')');
			reader.match('{');
			try {
				whileNode.setRightChild(loopBlock());
			} catch(MawkSyntaxException e) {
				whileNode.setRightChild(e.getNode());
				throw new MawkSyntaxException(whileNode, e.getMessage());
			}
		} catch(MawkMatchException e) {
			whileNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(whileNode, e.getMessage());
		}
		return whileNode;
	}
	
	public ASTNode forLoop() throws MawkSyntaxException  {
		ASTNode forNode = new ASTNode(ASTNode.NodeType.ForLoop);
		try {
			reader.matchString("for");
			reader.match('(');
			try {
				forNode.setLeftChild(integer());
			} catch(MawkSyntaxException e){
				forNode.setLeftChild(e.getNode());
				throw new MawkSyntaxException(forNode, e.getMessage());
			}
			reader.match(')');
			reader.match('{');
			try {
				forNode.setRightChild(loopBlock());
			} catch(MawkSyntaxException ex) {
				forNode.setRightChild(ex.getNode());
				throw new MawkSyntaxException(forNode, ex.getMessage());
			}
		} catch(MawkMatchException e) {
			forNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(forNode, e.getMessage());
		}
		return forNode;
	}

	public ASTNode substringFunction() throws MawkSyntaxException  {
		ASTNode substringFunctionNode = new ASTNode(ASTNode.NodeType.SubstringFunction);
		try {
			reader.matchString("substring(");
			substringFunctionNode.setLeftChild(integer());
			reader.match(',');
			if(reader.token == 'E'){
				reader.matchString("EOL");
				substringFunctionNode.setRightChild(new ASTNode(ASTNode.NodeType.EndIndex));
			} else {
				substringFunctionNode.setRightChild(integer());
			}
			reader.match(')');
		} catch (MawkMatchException ex){
			substringFunctionNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(substringFunctionNode, ex.getMessage());
		} catch (MawkSyntaxException ex) {
			substringFunctionNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(substringFunctionNode, ex.getMessage());
		}
		return substringFunctionNode;
	}
	
	public ASTNode integer() throws MawkSyntaxException  {
		ASTNode integerNode = new ASTNode(ASTNode.NodeType.Integer);
		try { // left child try block
			integerNode.setLeftChild(number());
		} catch(MawkSyntaxException ex){
			integerNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(integerNode, ex.getMessage());
		}
		try { // right child try block
			if(reader.token >= '0' && reader.token <= '9'){
				integerNode.setRightChild(integer());
			}
		} catch(MawkSyntaxException ex){
			integerNode.setRightChild(ex.getNode());
			throw new MawkSyntaxException(integerNode, ex.getMessage());
		}
		return integerNode;
	}
	
	private ASTNode number() throws MawkSyntaxException  {
		ASTNode numberNode = new ASTNode(ASTNode.NodeType.Number);
		try {
			switch(reader.token){
			case '0':
				numberNode.setLeftChild(zero());
				break;
			case '1':
				numberNode.setLeftChild(one());
				break;
			case '2':
				numberNode.setLeftChild(two());
				break;
			case '3':
				numberNode.setLeftChild(three());
				break;
			case '4':
				numberNode.setLeftChild(four());
				break;
			case '5':
				numberNode.setLeftChild(five());
				break;
			case '6':
				numberNode.setLeftChild(six());
				break;
			case '7':
				numberNode.setLeftChild(seven());
				break;
			case '8':
				numberNode.setLeftChild(eight());
				break;
			case '9':
				numberNode.setLeftChild(nine());
				break;
			default:
				throw new MawkSyntaxException(errorNode(), String.format("Invalid number token: %c", reader.token));
			}
		} catch (MawkSyntaxException ex){
			numberNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(numberNode, ex.getMessage());
		}
		return numberNode;
	}
	private ASTNode zero() throws MawkSyntaxException  {
		try {
			reader.match('0');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Zero);
	}
	private ASTNode one() throws MawkSyntaxException  {
		try {
			reader.match('1');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.One);
	}
	private ASTNode two() throws MawkSyntaxException  {
		try {
			reader.match('2');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Two);
	}
	private ASTNode three() throws MawkSyntaxException  {
		try {
			reader.match('3');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Three);
	}
	private ASTNode four() throws MawkSyntaxException  {
		try {
			reader.match('4');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Four);
	}
	private ASTNode five() throws MawkSyntaxException  {
		try {
			reader.match('5');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Five);
	}
	private ASTNode six() throws MawkSyntaxException  {
		try {
			reader.match('6');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Six);
	}
	private ASTNode seven() throws MawkSyntaxException  {
		try {
			reader.match('7');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Seven);
	}
	private ASTNode eight() throws MawkSyntaxException  {
		try {
			reader.match('8');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Eight);
	}
	private ASTNode nine() throws MawkSyntaxException  {
		try {
			reader.match('9');
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Nine);
	}

	public ASTNode insertFunction() throws MawkSyntaxException  {
		ASTNode insertFunctionNode = new ASTNode(ASTNode.NodeType.InsertFunction);
		try{
			reader.matchString("insert(");
			try { // Left node try block
				if(reader.token == 'E'){
					reader.matchString("EOL");
					insertFunctionNode.setLeftChild(new ASTNode(ASTNode.NodeType.EndIndex));
				} else {
					insertFunctionNode.setLeftChild(integer());
				}
			} catch (MawkSyntaxException ex){
				insertFunctionNode.setLeftChild(ex.getNode());
				throw new MawkSyntaxException(insertFunctionNode, ex.getMessage());
			}
			reader.match(',');
			try { // Right node try block
				insertFunctionNode.setRightChild(character());
			} catch (MawkSyntaxException ex){
				insertFunctionNode.setRightChild(ex.getNode());
				throw new MawkSyntaxException(insertFunctionNode, ex.getMessage());
			}
			reader.match(')');
		} catch(MawkMatchException ex){ // for the various matches throughout the function
			insertFunctionNode.setLeftChild(errorNode());
			insertFunctionNode.setRightChild(errorNode());
			throw new MawkSyntaxException(insertFunctionNode, ex.getMessage());
		}
		return insertFunctionNode;
	}
	
	private ASTNode character() throws MawkSyntaxException  {
		ASTNode character = new ASTNode(ASTNode.NodeType.Character);
		try{
			switch(reader.token){
			case 'a':
				character.setLeftChild(a());
				break;
			case 'b':
				character.setLeftChild(b());
				break;
			case 'c':
				character.setLeftChild(c());
				break;
			default:
				throw new MawkMatchException("Invalid character token: " + reader.token);
			}
		} catch (MawkMatchException ex){
			character.setLeftChild(errorNode());
			throw new MawkSyntaxException(character, ex.getMessage());
		}
		return character;
	}
	private ASTNode a() throws MawkMatchException  {
		reader.match('a');
		return new ASTNode(ASTNode.NodeType.A);
	}
	private ASTNode b() throws MawkMatchException  {
		reader.match('b');
		return new ASTNode(ASTNode.NodeType.B);
	}
	private ASTNode c() throws MawkMatchException  {
		reader.match('c');
		return new ASTNode(ASTNode.NodeType.C);
	}

	public ASTNode printFunction() throws MawkSyntaxException {
		ASTNode printFunctionNode = new ASTNode(ASTNode.NodeType.PrintFunction);
		try {
			reader.matchString("print(");
			if(reader.token == '"'){
				printFunctionNode.setLeftChild(string());
			} else {
				printFunctionNode.setLeftChild(line());
			}
			reader.match(')');
		} catch (MawkMatchException ex){
			printFunctionNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(printFunctionNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			printFunctionNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(printFunctionNode, ex.getMessage());
		}
		return printFunctionNode;
	}
	
	private ASTNode line() throws MawkSyntaxException  {
		try {
			reader.matchString("LINE");
		} catch (MawkMatchException e) {
			throw new MawkSyntaxException(errorNode(), e.getMessage());
		}
		return new ASTNode(ASTNode.NodeType.Line);
	}

	private ASTNode string() throws MawkSyntaxException  {
		ASTNode stringNode = new ASTNode(ASTNode.NodeType.String);
		try {
			reader.match('"');
			stringNode.setLeftChild(stringInner());
			reader.match('"');
		} catch (MawkMatchException ex){
			stringNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(stringNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			stringNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(stringNode, ex.getMessage());
		}
		return stringNode;
	}

	private ASTNode stringInner() throws MawkSyntaxException {
		StringInnerNode stringInnerNode = new StringInnerNode(ASTNode.NodeType.StringInner);
		try {
			String str = "";
			while(reader.token != '"'){
				str += reader.token;
				reader.getcharWithWhitespace();
			}
			stringInnerNode.setValue(str);
		} catch(MawkStringReadException ex){
			throw new MawkSyntaxException(errorNode(), ex.getMessage());
		}
		return stringInnerNode;
	}
	

	public ASTNode reFunction() throws MawkSyntaxException {
		try {
			reader.matchString("re");
		} catch (MawkMatchException ex){
			throw new MawkSyntaxException(errorNode(), ex.getMessage());
		}
		
		try {
			switch(reader.token){
			case 'p':
				return replaceFunction();
			case 'm':
				return removeFunction();
			default:
				throw new MawkSyntaxException(errorNode(), "Invalid function name that starts with re.");
			}
		} catch(MawkSyntaxException ex){
			throw new MawkSyntaxException(ex.getNode(), ex.getMessage());
		}
	}
	private ASTNode removeFunction() throws MawkSyntaxException  {
		ASTNode removeFunctionNode = new ASTNode(ASTNode.NodeType.RemoveFunction);
		try {
			reader.matchString("move(");
			removeFunctionNode.setLeftChild(character());
			reader.match(')');
		} catch (MawkMatchException ex){
			removeFunctionNode.setLeftChild(errorNode());
			throw new MawkSyntaxException(removeFunctionNode, ex.getMessage());
		} catch (MawkSyntaxException ex){
			removeFunctionNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(removeFunctionNode, ex.getMessage());
		}
		return removeFunctionNode;
	}
	private ASTNode replaceFunction() throws MawkSyntaxException  {
		ASTNode replaceFunctionNode = new ASTNode(ASTNode.NodeType.ReplaceFunction);
		try{
			reader.matchString("place(");
			replaceFunctionNode.setLeftChild(character());
			reader.match(',');
			replaceFunctionNode.setRightChild(character());
			reader.match(')');
		} catch(MawkMatchException ex){
			throw new MawkSyntaxException(errorNode(), ex.getMessage());
		} catch(MawkSyntaxException ex){
			replaceFunctionNode.setLeftChild(ex.getNode());
			throw new MawkSyntaxException(replaceFunctionNode, ex.getMessage());
		}
		return replaceFunctionNode;
	}

	private ASTNode errorNode() {
		return new ASTNode(ASTNode.NodeType.Error);
	}
}
