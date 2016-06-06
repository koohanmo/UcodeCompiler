package codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import abstractSyntax.Declaration;
import abstractSyntax.Program;
import lexer.Lexer;
import parser.Parser;
import sementic.TypeChecker;

public class CodeGenerator {
	
	public static int blockNumber=1;
	public static int globalStart=1;
	
	
	private HashMap<String, VariableInfo> globalVars = new HashMap<String, VariableInfo>();
	private HashMap<String, VariableInfo> localVars = new HashMap<String, VariableInfo>();
	
	
	private TypeChecker typeChecker;
	String outputFile;
	private static FileWriter writer;
	
	
	public CodeGenerator(String inputFile, String outPutFile) {
		typeChecker = new TypeChecker(new Parser(new Lexer(inputFile)));
		this.outputFile = outPutFile;

		try {
			writer = new FileWriter(new File(outPutFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generate(){
		Program program = typeChecker.check();
		generate(program);
	}
	private void generate(Program program){
		//전역번수 선언 to globalVars
		for(Declaration  d : program.decpart){
			mkUcode(d);
			
		}
		//머리 선언된 함수 선언
		
		
		//메인 구현
		//함수 구현
	}
	
	private void mkUcode(Declaration d){
		
		StringBuilder sb = new StringBuilder("");
		//sb.append(mkSym(blockNumber,globalStart + ));
		
		
		
	}
	
	private String mkSpace(){
		return "           ";
	}
	
	private String mkSym(int start,int size){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append(this.blockNumber+" "+start+" "+size);
		return sb.toString();
		
	}
	
	public static void main(String[] args) {
		
		CodeGenerator codeGen = new CodeGenerator("test.txt", "uFile.uco");
	}
}
