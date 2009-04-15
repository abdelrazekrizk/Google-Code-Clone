package cs3240.sp09.RegularLanguage;

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
