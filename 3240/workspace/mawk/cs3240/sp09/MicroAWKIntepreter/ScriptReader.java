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
	
	public boolean match(char c){
		if(c == token){
			token = getchar();
			return true;
		} else {
			// TODO: Error handling
			try {
				throw new Exception();
			} catch (Exception e){
				System.err.println("MATCH ERROR at " + position + ": Expected " + c + ", got " + token + "[ASCII:" + (int)token + "]");
				System.err.println("Surrounding text: " + script.substring(max(0, position - 5), position) + "[" + script.charAt(position) + "]" + script.substring(position+1, min(position+5, script.length())));
				e.printStackTrace();
				System.exit(0);
			}
			return false;
		}
	}

	private int min(int i, int j) {
		if(i < j) return i;
		else return j;
	}

	private int max(int i, int j) {
		if(i > j) return i;
		else return j;
	}

	public boolean matchString(String string) {
		for(int i = 0; i < string.length(); i++)
			if (!match(string.charAt(i)))
				return false;
		return true;
	}
}
