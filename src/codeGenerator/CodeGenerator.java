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
	public static int LabelCnt=1;
	
	
	private HashMap<String, VariableInfo> globalVars = new HashMap<String, VariableInfo>();
	private HashMap<String, VariableInfo> localVars = new HashMap<String, VariableInfo>();
	private FunctionDef currentFunc =null;
	
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
		mkBgn();
		mkLdp();
		mkCall("main");
		mkEnd();
	}
	
	private void mkUcode(Funcs func){
		for(Funcs f : func){
			if(f instanceof voidFunc) mkUcode((voidFunc) f);
			else mkUcode((Func)f);
		}
	}
	
	private void mkUcode(Func f){
		FunctionDef functionDef =currentFunc = TypeManager.getInstance().funcTable.get(f.id);
		mkProc(functionDef,3);
		int startAddress=1;
		
		for(Declaration arg : f.arguments){
			int varSize = functionDef.getVariableSize(arg.getId());
			writeToUco(mkSym(startAddress,varSize));
			localVars.put(arg.getId(), new VariableInfo(blockNumber, startAddress));
			startAddress+=varSize;
		}
		for(Declaration d : f.decpart){
			int varSize = functionDef.getVariableSize(d.getId());
			writeToUco(mkSym(startAddress,varSize));
			localVars.put(d.getId(), new VariableInfo(blockNumber, startAddress));
			startAddress+=varSize;
		}
		for(Statement st : f.body){
			mkUcode(st);
		}
		
		//리턴
		mkRetv(f.returnExpr);
		
		localVars.clear();
		blockNumber++;
	}
	
	private void mkUcode(voidFunc f){
		FunctionDef functionDef  =currentFunc= TypeManager.getInstance().funcTable.get(f.id);
		mkProc(functionDef,3);
		int startAddress=1;
		
		for(Declaration arg : f.arguments){
			int varSize = functionDef.getVariableSize(arg.getId());
			writeToUco(mkSym(startAddress,varSize));
			localVars.put(arg.getId(), new VariableInfo(blockNumber, startAddress));
			startAddress+=varSize;
		}
		for(Declaration d : f.decpart){
			int varSize = functionDef.getVariableSize(d.getId());
			writeToUco(mkSym(startAddress,varSize));
			localVars.put(d.getId(), new VariableInfo(blockNumber, startAddress));
			startAddress+=varSize;
		}
		for(Statement st : f.body){
			mkUcode(st);
		}
		
		//리턴
		mkEnd();
		
		localVars.clear();
		blockNumber++;
	}
	
	private void mkUcode(MainFunc mainFunc){

		FunctionDef functionDef  =currentFunc= TypeManager.getInstance().funcTable.get("main");
		mkProc(functionDef,2);
		int startAddress=1;

		for(Declaration d : mainFunc.decpart){
			int varSize = functionDef.getVariableSize(d.getId());
			writeToUco(mkSym(startAddress,varSize));
			localVars.put(d.getId(), new VariableInfo(blockNumber, startAddress));
			startAddress+=varSize;
		}
		for(Statement st : mainFunc.body){
			mkUcode(st);
		}
		
		mkEnd();
		
		localVars.clear();
		blockNumber++;
	}
	

	
	private void mkUcode(Statement s){
		// Statement = Skip | Block | Assignment | Conditional | Loop | forLoop | voidFuncCall
		if(s instanceof Skip) return;
		else if(s instanceof Block){
			for(Statement ss : (Block)s){
				mkUcode(ss);
			}
		}else if(s instanceof Assignment){
		    // Assignment = Variable target; Expression source
			// VariableRef target; Expression source			
			Assignment assign = (Assignment)s;
			//Variable | ArrayRef
			if(assign.target instanceof Variable){
				mkUcode(assign.source);
				mkStr(assign.target.getId());
			}else if(assign.target instanceof ArrayRef){
				ArrayRef ar = (ArrayRef)assign.target;
				mkUcode(ar);
				mkUcode(assign.source);
				mkSti();
			}
		}else if(s instanceof Conditional){
			// Conditional = Expression test; Statement thenbranch, elsebranch
			Conditional con = (Conditional)s;
			int ifN = LabelCnt++;
			mkLabel("if"+ifN);
			mkUcode(con.test);
			mkFjp("else"+ifN);
			mkUcode(con.thenbranch);
			mkUjp("endif"+ifN);
			mkLabel("else"+ifN);
			mkUcode(con.elsebranch);
			mkLabel("endif"+ifN);
		}else if(s instanceof Loop){
			Loop loop = (Loop)s;
			int  whileN = LabelCnt++;
			mkLabel("while"+whileN);
			mkUcode(loop.test);
			mkFjp("endwhile"+whileN);
			mkUcode(loop.body);
			mkUjp("while"+whileN);
			mkLabel("endwhile"+whileN);
		}else if(s instanceof ForLoop){
			ForLoop fl = (ForLoop) s;
			int forN = LabelCnt++;
			mkLabel("for"+forN);
			mkUcode(fl.assign);
			mkUcode(fl.test);
			mkFjp("endfor"+forN);
			mkUcode(fl.body);
			mkUcode(fl.third);
			mkUjp("for"+forN);
			mkLabel("endfor"+forN);
		}else if(s instanceof voidFuncCall){
			voidFuncCall vfc = (voidFuncCall)s;
			mkLdp();
			for(Expression e : vfc.param){
				mkUcode(e);
			}
			mkCall(vfc.id);
			
		}else System.err.println("Ucode make Error" + s);	
		
		
		
	}
	
	
	private void mkUcode(Declaration d ,HashMap<String, SymbolElement> map){
		int varSize = map.get(d.getId()).size;
		StringBuilder sb = new StringBuilder("");
		sb.append(mkSym(globalStart,varSize));
		writeToUco(sb.toString());
		globalVars.put(d.getId(), new VariableInfo(blockNumber,globalStart));
		globalStart+=varSize;
	}
	
	
	//load variable
	private void mkUcode(Variable v){
		VariableInfo vi =getVariableInfo(v.id);
		mkLod(vi.blockNumber,vi.offset);
	}
	
	//load ArrayRef
	private void mkUcode(ArrayRef ar){
		mkUcode(ar.index);
		mkLDA(ar.id);
		mkAdd();
	}
	
	
	private void mkUcode(Expression expr){
	    // Expression = Variable | Value | Binary | Unary | FuncCall
		if(expr instanceof Variable){
			mkUcode((Variable)expr);
		}else if(expr instanceof Value){
			mkUcode((Value)expr);
		}else if(expr instanceof Binary){
			Binary bi = (Binary)expr;
			mkUcode(bi);
		}else if(expr instanceof Unary){
			
		}else if(expr instanceof FuncCall){
			
		}else System.err.println("Ucode make Error" + expr);	
	}
	
	private void mkUcode(Binary bi){
		mkUcode(bi.term1);
		mkUcode(bi.term2);	
		Type type=typeToEqual(bi.term1, bi.term2);
		if(bi.op.plusOp()){
			if(type.equals(Type.INT) || type.equals(Type.CHAR)){
				if (bi.term1 instanceof ArrayRef){
					if (bi.term2 instanceof  ArrayRef){
						
					}
					else if (bi.term2 instanceof Variable){
						
					}
					else if (bi.term2 instanceof Value){
						
					}
					
					else System.err.println("Ucode make Error");
						
					
				}
				
				if (bi.term1 instanceof Variable){
					if (bi.term2 instanceof  ArrayRef){
						
					}
					else if (bi.term2 instanceof Variable){
						
					}
					else if (bi.term2 instanceof Value){
						
					}
					else System.err.println("Ucode make Error");
					
				}
				
				if (bi.term1 instanceof Value){
					if (bi.term2 instanceof  ArrayRef){
						
					}
					else if (bi.term2 instanceof Variable){
						
					}
					else if (bi.term2 instanceof Value){
						
					}
					else System.err.println("Ucode make Error");
						
						}
				
			}else if(type.equals(Type.FLOAT)){
				
			}
		}
		else if(bi.op.minusOp()){
			
		}
		else if(bi.op.timesOp()){
			
		}
		else if(bi.op.divOp()){
			
		}
		else if(bi.op.modOp()){
			
		}
		else if(bi.op.powOp()){
			
		}
		else if(bi.op.andOp()){
			
		}
		else if(bi.op.orOp()){
			
		}
		else if(bi.op.gtOp()){
			
		}
		else if(bi.op.ltOp()){
			
		}
		else if(bi.op.leOp()){
			
		}
		else if(bi.op.geOp()){
			
		}
		else if(bi.op.neOp()){
			
		}
		else if(bi.op.eqOp()){
			
		}
		else{
			
		}
	
	}
	
	private void mkUcode(Unary u){
		if(u.op.incOp()){
			
		}
		else if(u.op.decOp()){
			
		}
		else if(u.op.notOp()){
			
		}
		else if(u.op.negOp()){
			
		}
		else{
			
		}
	
	}
	
	private Type typeToEqual(Expression e1, Expression e2){
		Type t1 = TypeManager.getInstance().typeOf(currentFunc, e1);
		Type t2 = TypeManager.getInstance().typeOf(currentFunc, e2);	
		if(t1.equals(t2)) return t1;
		else castType(t1,t2);
		return t1;
	}
	
	
	private void castType(Type to, Type from){
		if(to.equals(Type.INT)){
			if(from.equals(Type.FLOAT )){ 
				mkLdp();
				mkCall("F2I");
			}
		}else if(to.equals(Type.FLOAT)){
			if(from.equals(Type.INT)){
				mkLdp();
				mkCall("I2F");
			}else if(from.equals(Type.CHAR)){
				mkLdp();
				mkCall("I2F");
			}
		}else if(to.equals(Type.CHAR)){
			if(from.equals(Type.FLOAT)){
				mkLdp();
				mkCall("F2I");
			}
		}	
		else if(to.equals(Type.BIGINT)){
			
		}else System.err.println("Ucode make Error : cast" + to +" " + from );	
	}
	
	
	
	private void mkUcode(Value val){
	    // Value = IntValue | BoolValue |
	    //         CharValue | FloatValue | BigIntValue
		if(val instanceof IntValue){
			IntValue iv = (IntValue)val;
			mkLdc(iv.intValue());
		}else if(val instanceof BoolValue){
			BoolValue bv = (BoolValue)val;
			mkLdc(bv.intValue());
		}else if(val instanceof CharValue){
			CharValue cv = (CharValue)val;
			mkLdc((int)cv.charValue());
		}else if(val instanceof FloatValue){
			FloatValue fv = (FloatValue)val;
			mkLdc(Float.floatToRawIntBits(fv.floatValue()));
		}else if(val instanceof BigIntValue){
			//ㅋzㅋㅋㅋㅋ
		}else System.err.println("Ucode make Error");	
	}
	
	
	private String mkSpace(String op){
		StringBuffer sb = new StringBuffer(op);
		int sp = 11-op.length();
		for(int i=0;i<sp;i++) sb.append(" ");
		return sb.toString();
	}
	
	private String mkSpace(){
		return "           ";
	}
	
	
	private void mkBgn(){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("bgn " + globalVars.size());
		writeToUco(sb.toString());
	}
	
	private void mkLabel(String s){
		StringBuilder sb= new StringBuilder(mkSpace(s));
		sb.append("nop");
		writeToUco(sb.toString());
	}
	
	private void mkCall(String funcName){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("call "+funcName);
		writeToUco(sb.toString());
	}
	
	
	private void mkRetv(Expression e){
		mkUcode(e);
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("retv");
		writeToUco(sb.toString());
	}
	
	private void mkEnd(){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("end");
		writeToUco(sb.toString());
	}
	
	private void mkLdc(int val){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("ldc "+val);
		writeToUco(sb.toString());
	}
	
	private void mkLdp(){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("ldp");
		writeToUco(sb.toString());
	}
	
	
	private void mkFjp(String Label){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("fjp " + Label);
		writeToUco(sb.toString());
	}
	
	private void mkUjp(String Label){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("ujp " + Label);
		writeToUco(sb.toString());
	}
	
	private String mkSym(int start,int size){
		StringBuilder sb= new StringBuilder(mkSpace());
		sb.append("sym ");
		sb.append(this.blockNumber+" "+start+" "+size);
		return sb.toString();
	}
	
	private void mkProc(FunctionDef fd, int lexicalLevel){
		StringBuffer sb = new StringBuffer(mkSpace(fd.funcName));
		sb.append("proc ");
		int varN = fd.getAllVariableSize();
		sb.append(varN+" "+blockNumber+" "+lexicalLevel);
		writeToUco(sb.toString());
	}
	
	private void mkStr(String id){
		VariableInfo vi =getVariableInfo(id);
		
		StringBuffer sb = new StringBuffer(mkSpace());
		sb.append("str ");
		sb.append(vi.blockNumber+" "+vi.offset);
		writeToUco(sb.toString()); 
		
	}
	
	private void mkLDA(String id){
		VariableInfo vi =getVariableInfo(id);
		StringBuffer sb = new StringBuffer(mkSpace());
		sb.append("lda ");
		sb.append(vi.blockNumber+" "+vi.offset);
		writeToUco(sb.toString()); 
		
	}
	
	private void mkLod(int block, int startAdd){
		StringBuffer sb = new StringBuffer(mkSpace());
		sb.append("lod ");
		sb.append(block+" "+startAdd);
		writeToUco(sb.toString()); 
		}
	
	private void mkAdd(){
		StringBuffer sb = new StringBuffer(mkSpace());
		sb.append("add");
		writeToUco(sb.toString());
	}
	
	private void mkSti(){
		StringBuffer sb = new StringBuffer(mkSpace());
		sb.append("sti");
		writeToUco(sb.toString());
	}
	
	private   void writeToUco(String code){
		try {
			writer.write(code+"\n");
			System.out.println(code);
		} catch (IOException e) {
			System.err.println("File write error"+ " " + code);
			e.printStackTrace();
		}
	}
	
	private VariableInfo getVariableInfo(String id){
		VariableInfo vi =null;
		if(localVars.containsKey(id)){
			vi =localVars.get(id);
		}else if(globalVars.containsKey(id)){
			vi =localVars.get(id);
		}else System.err.println("Ucode make Error Can't find  : " + id );
		return vi;
	}
	
	
	public static void main(String[] args) {
		
		CodeGenerator codeGen = new CodeGenerator("test.txt", "uFile.uco");
		codeGen.generate();
	}
}
