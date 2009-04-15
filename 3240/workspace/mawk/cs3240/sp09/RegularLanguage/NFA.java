package cs3240.sp09.RegularLanguage;

import cs3240.sp09.AbstractSyntaxTree.*;
import cs3240.sp09.AbstractSyntaxTree.ASTNode.NodeType;
import cs3240.sp09.DataStrucutres.DynamicList;
import cs3240.sp09.RegularLanguage.Edge.Letter; 

public class NFA implements FiniteAutomata {
	private AdjMatrix adj;
	private State startState = null;
	private DynamicList<State> states = new DynamicList<State>();
	private DynamicList<State> finalStates = new DynamicList<State>();
	
	public NFA(){
		adj = new AdjMatrix(this);
	}
	
	public AdjMatrix getAdjMatrix(){
		return adj;
	}
	
	public State getStartState(){
		return startState;
	}
	
	public DynamicList<State> getStates(){
		return states;
	}
	
	/** 
	 * Converts a RegexNode to an NFA.
	 */
	public NFA(ASTNode node){
		switch(node.type){
		case Regex:
			regex(node);
			break;
		case Term:
			term(node);
			break;
		case Factor:
			factor(node);
			break;
		case Atom:
			atom(node);
			break;
		default:
			// Impossible case.
			error();
			break;
		}
	}

	private void regex(ASTNode node) {
		NFA termNFA = new NFA(node.leftChild);
		if(node.rightChild != null){
			NFA regexNFA = new NFA(node.rightChild);
			copyToSelf(termNFA.or(regexNFA));
		} else {
			copyToSelf(termNFA);
		}
	}

	private void term(ASTNode node) {
		NFA factorNFA = new NFA(node.leftChild);
		if(node.rightChild != null){
			NFA termNFA = new NFA(node.rightChild);
			copyToSelf(factorNFA.concatenate(termNFA));
		} else {
			copyToSelf(factorNFA);
		}
	}

	private void factor(ASTNode node) {
		NFA atomNFA = new NFA(node.leftChild);
		if(node.rightChild != null){
			ASTNode metacharacter = node.rightChild;
			switch(metacharacter.leftChild.type){
			case Star:
				copyToSelf(atomNFA.star());
				break;
			case OneOrMore:
				copyToSelf(atomNFA.oneOrMore());
				break;
			case Optional:
				copyToSelf(atomNFA.optional());
				break;
			default:
				// impossible case
				error();
				break;
			}
		} else {
			copyToSelf(atomNFA);
		}
	}

	private void atom(ASTNode node) {
		if(node.leftChild.type == NodeType.Character){
			// switching on the atom's character's type (atom.left => character; character.left => a|b|c)
			Edge.Letter letter = null;
			switch(node.leftChild.leftChild.type){
			case A: letter = Letter.a; break;
			case B: letter = Letter.b; break;
			case C: letter = Letter.c; break;
			default:
				// Impossible case.
				error();
			}
			copyToSelf(baseNFA(letter));
		} else if(node.leftChild.type == NodeType.Regex){
			copyToSelf(new NFA(node.leftChild));
		}
	}
	
	private void copyToSelf(NFA nfa) {
		this.adj = nfa.adj;
		this.finalStates = nfa.finalStates;
		this.startState = nfa.startState;
		this.states = nfa.states;
	}
	
	/**
	 * Creates our primitive NFA - one that is only a start state, final state, and transition between.
	 * @param letter The letter to transition on
	 */
	public static NFA baseNFA(Letter letter){
		NFA nfa = new NFA();
		nfa.startState = nfa.createState(false);
		State finalState = nfa.createState(true);
		nfa.adj.set(nfa.startState, finalState, letter);
		return nfa;
	}
	
	public State createState(boolean isFinal){
		State state = new State(states.size(), isFinal);
		this.states.add(state);
		if(isFinal) this.finalStates.add(state);
		return state;
	}
	
	private NFA or(NFA nfa2) {
		NFA or = new NFA();
		
		for(int i = 0; i < this.states.size(); i++){
			State old = this.states.get(i);
			or.createState(old.isFinal);
		}
		for(int i = 0; i < nfa2.states.size(); i++){
			State old = nfa2.states.get(i);
			or.createState(old.isFinal);
		}
		
		or.adj = AdjMatrix.merge(this.adj, nfa2.adj, or);
		
		State start = or.createState(false);
		
		or.startState = start;
		
		or.adj.set(start, or.states.get(this.startState.index), Letter.epsilon);
		or.adj.set(start, or.states.get(nfa2.startState.index + this.states.size()), Letter.epsilon);
		
		return or;
	}
	
	private NFA concatenate(NFA nfa2) {
		State start2 = null;
		DynamicList<State> final1 = new DynamicList<State>();
		
		NFA concat = new NFA();
		
		for(int i = 0; i < this.states.size(); i++){
			State old = this.states.get(i);
			State newState = concat.createState(false);
			if(this.startState == old) concat.startState = newState;
			if(old.isFinal) final1.add(newState);
		}
		for(int i = 0; i < nfa2.states.size(); i++){
			State old = nfa2.states.get(i);
			State newState = concat.createState(old.isFinal);
			if(nfa2.startState == old) start2 = newState;
		}
		
		concat.adj = AdjMatrix.merge(this.adj, nfa2.adj, concat);
		
		for(int i = 0; i < final1.size(); i++){
			concat.adj.set(final1.get(i), start2, Letter.epsilon);
		}
		
		return concat;
	}
	
	private NFA optional() {
		State oldStart = null;
		DynamicList<State> oldFinals = new DynamicList<State>();
		
		NFA optional = new NFA();		
		
		for(int i = 0; i < states.size(); i++){
			State old = states.get(i);
			State newState = optional.createState(false);
			if(old.isFinal) oldFinals.add(newState);
			if(this.startState == old) oldStart = newState;
		}

		optional.adj = this.adj.clone(optional);
		
		State newStart = optional.createState(false);
		State newFinal = optional.createState(true);
		
		optional.startState = newStart;
		
		optional.adj.set(newStart, oldStart, Letter.epsilon);
		optional.adj.set(newStart, newFinal, Letter.epsilon);
		
		for(int i = 0; i < oldFinals.size(); i++){
			State oldFinal = oldFinals.get(i);
			optional.adj.set(oldFinal, newFinal, Letter.epsilon);
		}
		
		return optional;
	}

	private NFA oneOrMore() {
		DynamicList<State> oldFinals = new DynamicList<State>();
		State oldStart = null;
		
		NFA plus = new NFA();
		
		for(int i = 0; i < states.size(); i++){
			State old = states.get(i);
			State newState = plus.createState(old.isFinal);
			if(old.isFinal) oldFinals.add(newState);
			if(this.startState == old) oldStart = newState; 
		}

		plus.adj = this.adj.clone(plus);
		
		plus.startState = oldStart;
		
		for(int i = 0; i < oldFinals.size(); i++){
			State oldFinal = oldFinals.get(i);
			plus.adj.set(oldFinal, oldStart, Letter.epsilon);
		}
		
		return plus;
	}

	private NFA star() {
		State oldStart = null;
		DynamicList<State> oldFinals = new DynamicList<State>();
		
		NFA star = new NFA();
		
		for(int i = 0; i < states.size(); i++){
			State old = states.get(i);
			State newState = star.createState(false);
			if(old.isFinal) oldFinals.add(newState);
			if(this.startState == old) oldStart = newState;
		}
		
		star.adj = this.adj.clone(star);
		
		State newStart = star.createState(false);
		State newFinal = star.createState(true);
		
		star.startState = newStart;
		
		star.adj.set(newStart, oldStart, Letter.epsilon);
		star.adj.set(newStart, newFinal, Letter.epsilon);
		
		for(int i = 0; i < oldFinals.size(); i++){
			State oldFinal = oldFinals.get(i);
			star.adj.set(oldFinal, newFinal, Letter.epsilon);
			star.adj.set(oldFinal, oldStart, Letter.epsilon);
		}
		
		return star;
	}
	
	public String toString() {
		return adj.toString();
	}
	
	private void error(){
		assert(false);
	}
	
}
