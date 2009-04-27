package cs3240.sp09.RegularLanguage;

/**
 * Represents a generic state that can fit within a DFA or NFA.
 */
public class State {
	int index;
	boolean isFinal;
	boolean isCapture;
	int captureNum;
	
	public State(int index, boolean isFinal){
		this.index = index;
		this.isFinal = isFinal;
		this.isCapture = false;
		this.captureNum = 0;
	}
	
	public State(int index, boolean isFinal, boolean isCapture, int captureNum){
		this.index = index;
		this.isFinal = isFinal;
		this.isCapture = isCapture;
		this.captureNum = captureNum;
	}
	
	public State clone(){
		return new State(index, isFinal);
	}
}
