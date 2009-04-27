package cs3240.sp09.MicroAWKIntepreter;
import java.io.*;
import java.util.EnumSet;

import cs3240.sp09.AbstractSyntaxTree.ASTNode;
import cs3240.sp09.AbstractSyntaxTree.RegexNode;
import cs3240.sp09.AbstractSyntaxTree.ASTNode.NodeType;
import cs3240.sp09.DataStrucutres.DynamicList;

/**
 * The main-class for this program - handles all top-level management.
 *   Command-line options are converted into enum flags and command-line 
 *   files are stored to be processed later.
 */
public class Interpreter {
	// Options flag enum
	static enum Options {AST, DFA, NFA, DEBUG };
	
	// Java is not as convenient as C/C++ - for enums to be treated as flags they must
	// belong to an EnumSet
	static EnumSet<Options> options = EnumSet.noneOf(Options.class);
	
	// The filename of the script
	static String scriptFile;
	
	// The data files to be processed by the script
	static DynamicList<String> textToProcess = new DynamicList<String>();
	
	// Index for keeping track of command-line arg parsing
	static int argIndex = 0;

	// The root ASTNode for the script
	static ASTNode ScriptASTRoot;
	
	public static void main(String[] args){
		parseCommandLine(args);
		Evaluator.evaluate(ScriptASTRoot, textToProcess);
	}
	
	/**
	 * Top level parsing of the command line
	 */
	private static void parseCommandLine(String[] args){
		if(args.length < 2 || args.length > 7){
			printUsage();
			System.exit(0);
		}
		parseOptions(args);
		readAndParseScript(args);
		parseInputText(args);
	}
	
	/**
	 * Error notification indiciating the unavailability of the given script file.
	 * Will halt execution.
	 */
	private static void scriptFileError(String scriptFile) {
		System.out.println("Error reading script text from file: " + scriptFile);
		System.exit(0);
	}

	/**
	 * Parses the command-line args for data input
	 */
	private static void parseInputText(String[] args){
		if(args[argIndex].compareTo("-f") == 0){
			String fileName = args[argIndex + 1];
			try{
				BufferedReader input = new BufferedReader(new FileReader(fileName));
				String line;
				while((line = input.readLine()) != null)
					if(line.length() != 0)
						textToProcess.add(line);
				input.close();
			} catch (IOException ex){
				textFileError(fileName);
			}
		} else {
			textToProcess.add(args[argIndex]);
		}
	}
	
	/**
	 * Parses the command-line args for option flags
	 */
	private static void parseOptions(String[] args){
		while(args[argIndex].charAt(0) == '-'){
			if(args[argIndex].compareTo("-ast") == 0)
				options.add(Options.AST);
			else if (args[argIndex].compareTo("-dfa") == 0)
				options.add(Options.DFA);
			else if (args[argIndex].compareTo("-nfa") == 0)
				options.add(Options.NFA);
			else if (args[argIndex].compareTo("-debug") == 0)
				options.add(Options.DEBUG);
			else
				commandLineSyntaxError();
			argIndex++;
		}
	}
	
	/**
	 * Error indicating some form of IOException while reading the data file. Will halt.
	 */
	private static void textFileError(String filename) {
		System.out.println("Error reading text from file: " + filename);
		System.exit(0);
	}

	/**
	 * Error indicating a syntax error on the command line. Halts execution.
	 */
	private static void commandLineSyntaxError() {
		System.out.println("Syntax error on the command line. See below for usage information.");
		printUsage();
		System.exit(0);
	} 

	/**
	 * Outputs program usage.
	 */
	private static void printUsage() {
		String usage = "[-debug] [-ast] [-nfa] [-dfa] script_file.mawk (-f data_file.data | data_string)\n";
		usage += "-debug:    Traces evaluation of functions at runtime\n";
		usage += "-ast:      Prints AST after parsing script file\n";
		usage += "-nfa/-dfa: Prints the script NFA and/or DFA adjacency tables after construction.\n";
		System.out.print(usage);
	}
	
	/**
	 * Reads in the script file and parses it into an AST.
	 */
	private static void readAndParseScript(String[] args){
		scriptFile = args[argIndex++];
		String script = "";
		try {
			BufferedReader input = new BufferedReader(new FileReader(scriptFile));
			String line;
			while((line = input.readLine()) != null)
				if(line.length() != 0)
					script += line + "\n";
		} catch (IOException ex){
			scriptFileError(scriptFile);
		}
		ScriptParser parser = new ScriptParser(script);
		ScriptASTRoot = parser.parse();
		if(options.contains(Options.AST))
			System.out.println(ScriptASTRoot);
	}
	
	private static void outputNFAs(ASTNode node) {
		if(node.type == NodeType.Regex){
			RegexNode rex = (RegexNode)node;
			System.out.println(rex.nfa);
		}
		else {
			if(node.leftChild != null)
				outputNFAs(node.leftChild);
			if(node.rightChild != null)
				outputNFAs(node.rightChild);	
		}
	}
	
	private static void outputDFAs(ASTNode node) {
		if(node.type == NodeType.Regex){
			RegexNode rex = (RegexNode)node;
			System.out.println(rex.dfa);
		}
		else {
			if(node.leftChild != null)
				outputDFAs(node.leftChild);
			if(node.rightChild != null)
				outputDFAs(node.rightChild);	
		}
	}
}
