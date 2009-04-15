package cs3240.sp09.AbstractSyntaxTree;

import cs3240.sp09.RegularLanguage.*;

public class RegexNode extends ASTNode {
	public DFA dfa;
	public NFA nfa;
	
	public RegexNode(NodeType type) {
		super(type);
	}

}
