package cs3240.sp09.RegularLanguage;

import cs3240.sp09.DataStrucutres.DynamicList;
import cs3240.sp09.RegularLanguage.Edge.Letter;

/**
 * Represents an adjacency matrix for a given DFA or NFA.
 */
public class AdjMatrix {
	DynamicList<Edge>[] array;
	int stateCount;
	int edgeCount = 0;
	FiniteAutomata fa;
	
	@SuppressWarnings("unchecked")
	public AdjMatrix(FiniteAutomata fa){
		this.fa = fa;
		this.stateCount = fa.getStates().size();
		array = new DynamicList[stateCount * stateCount];
	}
	
	/**
	 * Creates an edge between stateA and stateB.
	 */
	public void set(State stateA, State stateB, Letter letter){
//		set(stateA.index, stateB.index, letter);
		int max = max(stateA.index, stateB.index) + 1;
		if(max > stateCount)
			resize(max);
		
		int loc = stateA.index + stateB.index * stateCount;
		if(array[loc] == null){
			array[loc] = new DynamicList<Edge>();
			array[loc].add(new Edge(letter, edgeCount++, stateB));
		} else {
			array[loc].add(new Edge(letter, edgeCount++, stateB));
		}
	}
	
	public State get(State state, Letter letter){
		DynamicList<Edge> edges = get(state);
		for(Edge edge : edges){
			if(edge.letter == letter){
				return edge.destination;
			}
		}
		return null;
	}
	
//	public void set(int a, int b, Letter letter){
//		// if we can't accommodate either state, resize the internal array.
//		if(max(a, b) + 1 > stateCount){
//			resize(max(a, b) + 1);
//		}
//		
//		int loc = a + b * stateCount;
//		if(array[loc] == null){
//			array[loc] = new DynamicList<Edge>();
//			array[loc].add(new Edge(letter, edgeCount++));
//		} else {
//			array[loc].add(new Edge(letter, edgeCount++));
//		}
//	}
	
	private int max(int num1, int num2) {
		if(num1 > num2) return num1;
		else return num2;
	}

	/**
	 * Gets a DyamicList of edges originating from the given state.
	 */
	public DynamicList<Edge> get(State state){
		return get(state.index);
	}
	
	public DynamicList<Edge> get(int stateIndex){
		DynamicList<Edge> edges = new DynamicList<Edge>();
		for(int i = 0; i < stateCount; i++){
			int loc = stateIndex + i * stateCount;
			if(array[loc] != null){
				for(int j = 0; j < array[loc].size(); j++){
					edges.add(array[loc].get(j));
				}
			}
		}
		return edges;
	}
	
	/**
	 * Resizes the internal matrix to accommodate for more states.
	 */
	@SuppressWarnings("unchecked")
	public void resize(int newStateCount){
		int oldStateCount = this.stateCount;
		DynamicList<Edge>[] oldArray = array;
		
		if(newStateCount < oldStateCount){
			// TODO: throw error
		}
		
		stateCount = newStateCount;
		array = new DynamicList[newStateCount * newStateCount];
		
		// Maps from old coordinates to new coordinates.
		for(int i = 0; i < oldStateCount; i++){
			for(int j = 0; j < oldStateCount; j++){
				array[i + j * newStateCount] = oldArray[i + j * oldStateCount];
			}
		}
	}
	
	/**
	 * Merges two Adjacency Matrices into a new one. Note that the given NFA needs to have its states initialized before
	 * making this call.
	 */
	public static AdjMatrix merge(AdjMatrix adj1, AdjMatrix adj2, FiniteAutomata fa){
		AdjMatrix newAdj = new AdjMatrix(fa);
		newAdj.resize(adj1.stateCount + adj2.stateCount);
		for(int i = 0; i < adj1.stateCount; i++){
			for(int j = 0; j < adj1.stateCount; j++){
				DynamicList<Edge> edges = adj1.array[i + j * adj1.stateCount];
				if(edges != null){
					for(int k = 0; k < edges.size(); k++){
						newAdj.set(fa.getStates().get(i), fa.getStates().get(j), edges.get(k).letter);
					}
				}
			}
		}
		
		for(int i = 0; i < adj2.stateCount; i++){
			for(int j = 0; j < adj2.stateCount; j++){
				DynamicList<Edge> edges = adj2.array[i + j * adj2.stateCount];
				if(edges != null){
					for(int k = 0; k < edges.size(); k++){
						newAdj.set(fa.getStates().get(adj1.stateCount + i), fa.getStates().get(adj1.stateCount + j), edges.get(k).letter);
					}
				}
			}
		}
		
		return newAdj;
	}
	
	public AdjMatrix clone(FiniteAutomata fa){
		AdjMatrix newAdj = new AdjMatrix(this.fa);
		for(int i = 0; i < this.stateCount; i++){
			for(int j = 0; j < this.stateCount; j++){
				DynamicList<Edge> edges = this.array[i + j * this.stateCount];
				if(edges != null){
					for(int k = 0; k < edges.size(); k++){
						newAdj.set(fa.getStates().get(i), fa.getStates().get(j), edges.get(k).letter);
					}
				}
			}
		}
		return newAdj;
	}
	
	public String toString() {
		int colWidth = 6;
		String finalString = "";
		for(int c = 0; c < colWidth; c++) {
			finalString += " ";
		}
		finalString += "|";
		for(int i = 1; i <= stateCount; i++) {
			String tempString = ""+ i;
			for(int c = 0; c < colWidth-tempString.length(); c++) {
				finalString += " ";
			}
			finalString += tempString + "|";
		}
		finalString += "\n";
		// i = row, j = col
		for(int i = 0; i < stateCount; i++) {
			String tempString = "" + (i+1);
			for(int c = 0; c < colWidth-tempString.length(); c++) {
				finalString += " ";
			}
			finalString += tempString + "|";
			for(int j = 0; j < stateCount; j++) {
				int index = i*stateCount + j;
				if(array[index] != null) {
					String tempString2 = "";
					for(Edge k : array[index]) {
						tempString2 += k.number + ",";
					}
					for(int c = 0; c < colWidth-tempString2.length(); c++) {
						finalString += " ";
					}
					finalString += " " + tempString2.substring(0, tempString2.length()-1) + "|";
				}
				else {
					for(int c = 0; c < colWidth; c++) {
						finalString += " ";
					}
					finalString += "|";
				}
			}
			finalString += "\n";
		}
		return finalString;
	}
}
