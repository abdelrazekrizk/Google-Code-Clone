package cs3240.sp09.AbstractSyntaxTree;

import cs3240.sp09.RegularLanguage.*;

/**
 * Represents the special case RegularExpression ASTNode.
 */
public class RegexNode extends ASTNode {
	/**
	 * The DFA that represents this Regex
	 */
	public DFA dfa;
	
	/**
	 * The NFA that represents this Regex
	 */
	public NFA nfa;
	
	public RegexNode(NodeType type) {
		super(type);
	}

}
