package cs3240.sp09.RegularLanguage;

public class State {
	int index;
	boolean isFinal;
	
	public State(int index, boolean isFinal){
		this.index = index;
		this.isFinal = isFinal;
	}
	
	public State clone(){
		return new State(index, isFinal);
	}
}
