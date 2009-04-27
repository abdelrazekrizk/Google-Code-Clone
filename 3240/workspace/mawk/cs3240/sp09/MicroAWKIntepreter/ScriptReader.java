package cs3240.sp09.MicroAWKIntepreter;

/**
 * Script reading class - reads in tokens while preserving the nature of a Recursive Descent
 * parser (ie, single token storage, no peeking). 
 */
public class ScriptReader {
	public int position;
	public String script;
	public char token;
	
	public ScriptReader(String script){
		this.script = script;
		token = script.charAt(0);
		position = 1;
	}
	
	public char getchar(){
		while(position < script.length()){
			token = script.charAt(position++);
			if(token != ' ' && token != '\r' && token != '\n' && token != '\t' && (int)token != 10)
				return token;
		}
		// end of file.
		return (char)-1;
	}
	
	public char getcharWithWhitespace() throws MawkStringReadException{
		while(position < script.length()){
			token = script.charAt(position++);
			return token;
		}
		// end of file.
		throw new MawkStringReadException("No closing parenthesis found when reading string.");
	}
	
	public boolean match(char c) throws MawkMatchException {
		if(c == token){
			token = getchar();
			return true;
		} else {
			throw new MawkMatchException(String.format("Character %c expected, got %c", c, token));
		}
	}
	
//
//	private int min(int i, int j) {
//		if(i < j) return i;
//		else return j;
//	}
//
//	private int max(int i, int j) {
//		if(i > j) return i;
//		else return j;
//	}

	public boolean matchString(String string) throws MawkMatchException {
		for(int i = 0; i < string.length(); i++)
			if (!match(string.charAt(i)))
				return false;
		return true;
	}

	/**
	 * This method should skip the reader ahead to the next program line.
	 * A program line ends on a closing brace "}", so the program skips ahead
	 * to the next closing brace. 
	 * @throws InvalidStatementBlockException 
	 */
	public void skipAheadToNextProgramLine() throws InvalidStatementBlockException {
		char c;
		while((c = getchar()) != '}'){
			if(c == (char)-1)
				throw new InvalidStatementBlockException();
		}
		// grab the character after the closing bracket.
		getchar();
			
	}
	
}
