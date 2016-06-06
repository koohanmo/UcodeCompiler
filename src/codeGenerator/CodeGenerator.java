package codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import abstractSyntax.*;
import lexer.Lexer;
import parser.Parser;
import sementic.*;

public class CodeGenerator {
	
	public final int LASTGENERATE = 609;
	
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
			mkUcode(d, TypeManager.getInstance().globalVariables);
		}
		//머리 선언된 함수 선언
		DefinedFunction definedFunction = new DefinedFunction();
		definedFunction.writeDefinedFunctions();
	
		//메인 구현
		mkUcode(program.mainFunc);
		
		//함수 구현
		mkUcode(program.funcs);
		
		//메인 호출
		mkUcode(this.LASTGENERATE);
	}
	
	private void mkUcode(Funcs func){
		for(Funcs f : func){
			if(f instanceof voidFunc) mkUcode((voidFunc) f);
			else mkUcode((Func)f);
		}
	}
	
	private void mkUcode(Func f){
		
	}
	
	private void mkUcode(voidFunc f){
		
	}
	
	private void mkUcode(MainFunc mainFunc){
		
	}
	
	//마무리 구현.ㄴㄴ
	private void mkUcode(int op){
		if(op == this.LASTGENERATE){
			//전역번수 갯수.
			
			
			
			
		}
	}
	
	private void mkUcode(Declaration d ,HashMap<String, SymbolElement> map){
		int varSize = map.get(d.getId()).size;
		StringBuilder sb = new StringBuilder("");
		sb.append(mkSym(globalStart,varSize));
		globalVars.put(d.getId(), new VariableInfo(blockNumber,globalStart));
		globalStart+=varSize;
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
