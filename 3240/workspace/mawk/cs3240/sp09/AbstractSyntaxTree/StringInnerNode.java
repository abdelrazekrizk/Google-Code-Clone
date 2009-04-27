package cs3240.sp09.AbstractSyntaxTree;

/**
 * An ASTNode that carries some sort of string data.
 */
public class StringInnerNode extends ASTNode {
	String value = "";
	
	public StringInnerNode(NodeType type) {
		super(type);
	}
	
	public void setValue(String str){
		value = str;
	}
	
	public String getValue(){
		return value;
	}

}
