package cs3240.sp09.MicroAWKIntepreter;

@SuppressWarnings("serial")
public class MawkParserException extends Exception {
	public MawkParserException(String errTxt){
		super(errTxt);
	}
}