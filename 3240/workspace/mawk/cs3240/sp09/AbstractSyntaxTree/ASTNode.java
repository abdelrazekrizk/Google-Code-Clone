package cs3240.sp09.AbstractSyntaxTree;

/**
 * Represents a node in our Abstract Syntax Tree.
 */
public class ASTNode {
	public static enum NodeType { 
		Program, 
		Statement,
		RFPair,
		LoopBlock,
		FunctionBlock,
		Function,
		SubstringFunction,
		Integer,
		InsertFunction,
		PrintFunction,
		String,
		StringInner,
		ReplaceFunction,
		RemoveFunction,
		WhileLoop,
		ForLoop,
		Regex,
		Term,
		Factor,
		Atom,
		Character,
		Number, 
		Begin,
		End, 
		EndIndex,
		Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine,
		A, B, C,
		Line,
		StringCharacter, 
		Metacharacter, 
		Star, OneOrMore, Optional, Capture,
		Error
	};
	
	public NodeType type;
	public ASTNode parent = null;
	public ASTNode leftChild = null;
	public ASTNode rightChild = null;
	
	public ASTNode(NodeType type){
		this.type = type;
	}
	
	public void setLeftChild(ASTNode node){
		leftChild = node;
		if(node != null){
			node.parent = this;
		}
	}
	
	public void setRightChild(ASTNode node){
		rightChild = node;
		if(node != null){
			node.parent = this;
		}
	}
	
	/**
	 * Constructs a meaningful string representation of this node and all children nodes.
	 * When printed to the console it gives a directory structure-like look.
	 */
	public String printString(String filler, boolean moreChildren){
		String str = "";
		if(moreChildren){
			str += '#'; // +
		} else {
			str += '#'; // |_
		}
		str += type.toString();
		if(leftChild != null){
			if(rightChild == null){
				str = str + "\n" + filler + leftChild.printString(filler + " ", false);
			} else {
				str = str + "\n" + filler + leftChild.printString(filler + "|", true);
				str = str + "\n" + filler + rightChild.printString(filler + " ", false);
			}
		}
		return str;
	}
	
	/**
	 * Returns a meaningful string representation of this node for printing to the console.
	 */
	public String toString(){
		return printString("", false);
	}
}
