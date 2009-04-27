package cs3240.sp09.RegularLanguage;

/**
 * Error that is thrown should a DFA recieve an input that is not a, b, or c.
 */
@SuppressWarnings("serial")
public class InvalidInputStringException extends Exception {
	private char character;
	
	public InvalidInputStringException(char c){
		this.character = c;
	}
	
	public char getChar(){
		return character;
	}
}
