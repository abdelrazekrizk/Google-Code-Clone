package cs3240.sp09.RegularLanguage;

/**
 * Represents an edge in either an NFA or DFA.
 * @author Anthony Marion
 */
public class Edge {
	/**
	 * Represents a 'letter', which is an element of the alphabet (Sigma-sub-epsilon)
	 */
	public static enum Letter { a, b, c, epsilon }; 
	
	/**
	 * The letter that translates from the previous state to the next on the edge
	 */
	public Letter letter;
	/**
	 * The number of this edge. Useful when enumerating edges.
	 */
	public int number;
	
	/**
	 * The destination state of this edge.
	 */
	public State destination;
	
	public Edge(Letter letter, int number, State dest){
		this.letter = letter;
		this.number = number;
		this.destination = dest;
	}
	
//	public Edge clone(){
//		return new Edge(letter, number, destination.clone());
//	}
}
