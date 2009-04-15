package cs3240.sp09.RegularLanguage;

import cs3240.sp09.DataStrucutres.*;
import cs3240.sp09.RegularLanguage.Edge.Letter;

public class DFA implements FiniteAutomata {
	AdjMatrix adj;
	State startState;
	DynamicList<State> states = new DynamicList<State>();
	DynamicList<State> finalStates = new DynamicList<State>();
	
	
	public DFA(NFA nfa){
		adj = new AdjMatrix(this);
		State start = nfa.getStartState();
		DynamicList<State> eClos = epsilonClosure(nfa, start);
		boolean isStartFinal = false;
		for(State state : eClos){
			if(state.isFinal) isStartFinal = true;
		}
		startState = this.createState(isStartFinal);
		
		// Maps the given DFA state (from this DFA) to a list of NFA states (from the NFA nfa)
		DynamicList<Pair<State, DynamicList<State>>> dfaNfaPairs = new DynamicList<Pair<State, DynamicList<State>>>();
		dfaNfaPairs.add(new Pair<State, DynamicList<State>>(startState, eClos));
		
		// basically a while loop, as size grows it will iterate more (the iterator will not ignore any new elements.)
		for(Pair<State, DynamicList<State>> pair : dfaNfaPairs){
			State dfaState = pair.key;
			DynamicList<State> associatedNfaStates = find(dfaNfaPairs, dfaState).value;
			
			Letter[] alphabet = {Letter.a, Letter.b, Letter.c};
			
			// for each character in the alphabet
			for(Letter character : alphabet){
				DynamicList<State> resultStates = new DynamicList<State>();
				boolean isFinal = false;
				// for each state in the nfa associated with the current dfa state
				for(State nfaState : associatedNfaStates){
					DynamicList<Edge> edges = nfa.getAdjMatrix().get(nfaState);
					// for each edge originating from the nfa state
					for(Edge edge : edges){
						if(edge.letter == character){
							State destinationNfaState = edge.destination;
							DynamicList<State> eClosure = epsilonClosure(nfa, destinationNfaState);
							// for each state in the epsilon closure of our target state
							for(State eclosState : eClosure){
								if(!resultStates.contains(eclosState)){
									resultStates.add(eclosState);
									if(eclosState.isFinal) isFinal = true;
								}
							}
						}
					}
				}
				
				// now we have a list of target states reached in dfaState -(character)-> targetState
				// if the mapping currently contains the target state
				Pair<State, DynamicList<State>> dfaStatePair = pairListContainsAsValue(dfaNfaPairs, resultStates);
				if(dfaStatePair == null){ // state does not exist. create the state, and hook dfaState to the new state on character.
					State newState = createState(isFinal);
					dfaNfaPairs.add(new Pair<State, DynamicList<State>>(newState, resultStates));
					adj.set(dfaState, newState, character);
				} else { // state exists. hook dfaState to the found state on character.
					State state = dfaStatePair.key;
					adj.set(dfaState, state, character);
				}
			}
		}
		return;
	}
	
	/**
	 * Iterates through the list and if any pair contains, as the value, a DynamicList that equals the given list, returns that pair.
	 * Otherwise returns null.
	 */
	private Pair<State, DynamicList<State>> pairListContainsAsValue(DynamicList<Pair<State, DynamicList<State>>> list, DynamicList<State> value){
		for(Pair<State, DynamicList<State>> pair : list){
			if(pair.value.equals(value))
				return pair;
		}
		return null;
	}
	
	/**
	 * Performs lookup for the given state in the given list. Returns the pair if found, null if not.
	 */
	private Pair<State, DynamicList<State>> find(DynamicList<Pair<State, DynamicList<State>>> list, State stateToFind){
		for(Pair<State, DynamicList<State>> pair : list){
			if(stateToFind == pair.key){
				return pair;
			}
		}
		// if not found
		return null;
	}
	
	public DynamicList<State> epsilonClosure(NFA nfa, DynamicList<State> nfaStates){
		DynamicList<State> states = new DynamicList<State>();
		for(State state : nfaStates){
			DynamicList<State> eClosure = epsilonClosure(nfa, state);
			for(State nfaState : eClosure){
				if(!states.contains(nfaState)){
					states.add(nfaState);
				}
			}
		}
		return states;
	}
	
	public DynamicList<State> epsilonClosure(NFA nfa, State s1){
		DynamicList<State> list = new DynamicList<State>();
		
		list.add(s1);
		for(State state : list){
			DynamicList<Edge> edges = nfa.getAdjMatrix().get(state);
			for(Edge edge : edges){
				if(edge.letter == Edge.Letter.epsilon && !list.contains(edge.destination)){
					list.add(edge.destination);
				}
			}
		}
		
		return list;
	}
	
	public State createState(boolean isFinal){
		State state = new State(states.size(), isFinal);
		this.states.add(state);
		if(isFinal) this.finalStates.add(state);
		return state;
	}
	
	/**
	 * Returns true if the DFA recognizes the string input. False otherwise.
	 * @throws InvalidInputStringException 
	 */
	public boolean matches(String input) throws InvalidInputStringException{
		State state = startState;
		for(int pos = 0; pos < input.length(); pos++){
			switch(input.charAt(pos)){
			case 'a':
				state = adj.get(state, Letter.a);
				break;
			case 'b':
				state = adj.get(state, Letter.b);
				break;
			case 'c':
				state = adj.get(state, Letter.c);
				break;
			default:
				throw new InvalidInputStringException(input.charAt(pos));
			}
			if(state == null)
				return false;
		}
		if(state.isFinal)
			return true;
		else
			return false;
	}

	public DynamicList<State> getStates() {
		return states;
	}
}
