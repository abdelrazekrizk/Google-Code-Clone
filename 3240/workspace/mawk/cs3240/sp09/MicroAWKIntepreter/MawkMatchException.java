package cs3240.sp09.MicroAWKIntepreter;

/**
 * Exception indicating a token match error during AST parsing
 */
@SuppressWarnings("serial")
public class MawkMatchException extends Exception {
	
	public MawkMatchException(String message){
		super(message);
	}
}
