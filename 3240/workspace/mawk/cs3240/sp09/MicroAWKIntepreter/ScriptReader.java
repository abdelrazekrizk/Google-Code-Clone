package cs3240.sp09.MicroAWKIntepreter;

public class ScriptReader {
	public int position;
	public String script;
	public char token;
	
	public ScriptReader(String script){
		this.script = script;
		token = script.charAt(0);
		position = 1;
	}
	
	public char getchar(){
		while(position < script.length()){
			token = script.charAt(position++);
			if(token != ' ' && token != '\r' && token != '\n' && token != '\t' && (int)token != 10)
				return token;
		}
		return (char)-1;
	}
	
	public char getcharWithWhitespace(){
		while(position < script.length()){
			token = script.charAt(position++);
			return token;
		}
		return (char)-1;
	}
	
	public boolean match(char c) throws MawkParserException {
		if(c == token){
			token = getchar();
			return true;
		} else {
			throw new MawkParserException(String.format("Character %c expected, got %c", c, token));
		}
	}
	
//
//	private int min(int i, int j) {
//		if(i < j) return i;
//		else return j;
//	}
//
//	private int max(int i, int j) {
//		if(i > j) return i;
//		else return j;
//	}

	public boolean matchString(String string) throws MawkParserException {
		for(int i = 0; i < string.length(); i++)
			if (!match(string.charAt(i)))
				return false;
		return true;
	}
	
}
