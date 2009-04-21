package cs3240.sp09.MicroAWKIntepreter;

import cs3240.sp09.AbstractSyntaxTree.ASTNode;

@SuppressWarnings("serial")
public class MawkSyntaxException extends Exception {
	
	private ASTNode node;
	
	/**
	 * Throws a new exception to be caught at a higher scope of execution.
	 * @param node The node in question to 'return' with - eg if in character(), should return a Character node.
	 * @param errTxt Description of the error
	 */
	public MawkSyntaxException(ASTNode node, String errTxt){
		super(errTxt);
		this.node = node;
	}
	
	public ASTNode getNode(){
		return node;
	}
}