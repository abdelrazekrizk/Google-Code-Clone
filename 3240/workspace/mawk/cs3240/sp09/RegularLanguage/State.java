package cs3240.sp09.RegularLanguage;

public class State {
	int index;
	boolean isFinal;
	boolean isCapture;
	
	public State(int index, boolean isFinal){
		this.index = index;
		this.isFinal = isFinal;
		this.isCapture = false;
	}
	
	public State(int index, boolean isFinal, boolean isCapture){
		this.index = index;
		this.isFinal = isFinal;
		this.isCapture = isCapture;
	}
	
	public State clone(){
		return new State(index, isFinal);
	}
}
