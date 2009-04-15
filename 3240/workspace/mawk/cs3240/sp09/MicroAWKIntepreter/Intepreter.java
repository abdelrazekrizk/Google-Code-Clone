package cs3240.sp09.MicroAWKIntepreter;
import java.util.EnumSet;
import java.io.*;

import cs3240.sp09.AbstractSyntaxTree.ASTNode;
import cs3240.sp09.DataStrucutres.DynamicList;


public class Intepreter {
	static enum Options {AST, DFA, NFA, DEBUG };
	static EnumSet<Options> options = EnumSet.noneOf(Options.class);
	static String scriptFile;
	static DynamicList<String> textToProcess = new DynamicList<String>();
	static int argIndex = 0;

	static ASTNode ScriptASTRoot;
	
	public static void main(String[] args){
		parseCommandLine(args);
		Evaluator.evaluate(ScriptASTRoot, textToProcess);
	}
	
	private static void parseCommandLine(String[] args){
		if(args.length < 2 || args.length > 7){
			printUsage();
			System.exit(0);
		}
		parseOptions(args);
		readAndParseScript(args);
		parseInputText(args);
	}
	
	private static void scriptFileError(String scriptFile) {
		System.out.println("Error reading script text from file: " + scriptFile);
		System.exit(0);
	}

	private static void parseInputText(String[] args){
		if(args[argIndex].compareTo("-f") == 0){
			String fileName = args[argIndex + 1];
			try{
				BufferedReader input = new BufferedReader(new FileReader(fileName));
				String line;
				while((line = input.readLine()) != null)
					if(line.length() != 0)
						textToProcess.add(line);
				input.close();
			} catch (IOException ex){
				textFileError(fileName);
			}
		} else {
			textToProcess.add(args[argIndex]);
		}
	}
	
	private static void parseOptions(String[] args){
		while(args[argIndex].charAt(0) == '-'){
			if(args[argIndex].compareTo("-ast") == 0)
				options.add(Options.AST);
			else if (args[argIndex].compareTo("-dfa") == 0)
				options.add(Options.DFA);
			else if (args[argIndex].compareTo("-nfa") == 0)
				options.add(Options.NFA);
			else if (args[argIndex].compareTo("-debug") == 0)
				options.add(Options.DEBUG);
			else
				commandLineSyntaxError();
			argIndex++;
		}
	}
	
	private static void textFileError(String filename) {
		System.out.println("Error reading text from file: " + filename);
		System.exit(0);
	}

	private static void commandLineSyntaxError() {
		System.out.println("Syntax error on the command line. See below for usage information.");
		printUsage();
		System.exit(0);
	} 

	private static void printUsage() {
		String usage = "<<INSERT USAGE HERE>>"; // TODO
		System.out.print(usage);
	}
	
	private static void readAndParseScript(String[] args){
		scriptFile = args[argIndex++];
		String script = "";
		try {
			BufferedReader input = new BufferedReader(new FileReader(scriptFile));
			String line;
			while((line = input.readLine()) != null)
				if(line.length() != 0)
					script += line + "\n";
		} catch (IOException ex){
			scriptFileError(scriptFile);
		}
		ScriptParser parser = new ScriptParser(script);
		ScriptASTRoot = parser.parse();
		if(options.contains(Options.AST))
			System.out.println(ScriptASTRoot);
	}
}
