package cs3240.sp09.MicroAWKIntepreter;

/**
 * Error indicating an invalid statement block during AST parsing.
 */
@SuppressWarnings("serial")
public class InvalidStatementBlockException extends Exception {
	public InvalidStatementBlockException(){
		super("Invalid end of block (no closing bracket). Discarding rest of program...");
	}
}
