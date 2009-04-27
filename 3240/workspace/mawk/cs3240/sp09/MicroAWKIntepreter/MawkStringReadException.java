package cs3240.sp09.MicroAWKIntepreter;

/**
 * Exception indicating some sort of error while attempting to read a string from the script file.
 */
@SuppressWarnings("serial")
public class MawkStringReadException extends Exception {
	MawkStringReadException(String msg){
		super(msg);
	}
}
