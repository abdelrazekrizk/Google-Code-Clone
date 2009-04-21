package cs3240.sp09.MicroAWKIntepreter;

@SuppressWarnings("serial")
public class InvalidStatementBlockException extends Exception {
	public InvalidStatementBlockException(){
		super("Invalid end of block (no closing bracket). Discarding rest of program...");
	}
}
