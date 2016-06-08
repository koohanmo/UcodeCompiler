	package codeGenerator;
	
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
	
	import abstractSyntax.*;
	import lexer.Lexer;
	import parser.Parser;
	import sementic.*;
	
	public class CodeGenerator {
		
		public final int LASTGENERATE = 609;
		
		public  int blockNumber=1;
		public  int globalStart=1;
		public  int LabelCnt=1;
		
		DefinedFunction definedFunction;
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
			
			try {
				writer.flush();
				writer.close();
				TypeManager.clearTypeManager();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("File flush Error");
				e.printStackTrace();
			}
		}
		private void generate(Program program){
			//전역번수 선언 to globalVars
			for(Declaration  d : program.decpart){
				mkUcode(d, TypeManager.getInstance().globalVariables);
			}
			//머리 선언된 함수 선언
			definedFunction = new DefinedFunction();
			writeCustomFunctions();
			
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
				for(Statement interS : ((Block) s).members){
						mkUcode(interS);
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
				if(TypeManager.getInstance().definedFuncTable.contains(vfc.id)){
					writeUcode(s);
					return;
				}
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
			mkLdi();
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
				Unary u =(Unary)expr;
				mkUcode(u);
			}else if(expr instanceof FuncCall){
				FuncCall f = (FuncCall) expr;
				if(TypeManager.getInstance().definedFuncTable.contains(f.id)){
					writeUcode(expr);
					return;
				}
				mkUcode(f);
			}else if(expr instanceof ArrayRef){
				ArrayRef ar=(ArrayRef) expr;
				mkUcode(ar);
			}else System.err.println("Ucode make Error" + expr);	
		}
		

		private void mkUcode(FuncCall f){
			mkLdp();
			for(Expression e: f.params){
				mkUcode(e);
			}
			mkCall(f.id);
			
		}
			
		private void mkUcode(Binary bi){
			mkUcode(bi.term1);
			mkUcode(bi.term2);
			Type type=typeToEqual(bi.term1, bi.term2);
			//if(type.equals(Type.INT) || type.equals(Type.CHAR)){							
				if(bi.op.orOp()){
					mkOr();
				}
				else if(bi.op.andOp())
				{	
					mkAnd();
				}
				else if(bi.op.neOp() || bi.op.eqOp())
				{	
				
					if(bi.op.neOp()) mkNe();
					else if(bi.op.eqOp())mkEq();
					else System.err.println("error equop");
				}
				else if(bi.op.leOp()|| bi.op.geOp()|| bi.op.ltOp() ||bi.op.gtOp())
				{	
					if(bi.op.leOp()) mkLe();
					else if(bi.op.geOp())mkGe();
					else if(bi.op.ltOp())mkLt();
					else if(bi.op.gtOp())mkGt();
					else System.err.println("error relop");
				}
				
				else if(bi.op.plusOp() || bi.op.minusOp())
				{	
					if(bi.op.plusOp()) mkAdd();
					else if(bi.op.minusOp())mkSub();
					else System.err.println("error addop");
				}
				else if(bi.op.timesOp() || bi.op.divOp()||bi.op.modOp() ||bi.op.powOp())
				{	
					
					if(bi.op.timesOp()) mkMul();
					else if(bi.op.divOp())mkDiv();
					else if(bi.op.modOp())mkMod();
					else if(bi.op.powOp());
					else System.err.println("error mulop");
				}				
			}
		//}
		
		private boolean bool(Operator op) {
			// TODO Auto-generated method stub
			return false;
		}
		
		private void mkUcode(Unary u){	
				if(u.op.incOp()){
					if(u.term instanceof ArrayRef){
						ArrayRef ar =(ArrayRef)u.term;
						mkUcode(ar);
						mkLdc(1);
						mkAdd();
						mkStr(ar.id);
					}
					else if(u.term instanceof Variable){
						Variable var =(Variable)u.term;
						mkUcode(var);
						mkLdc(1);
						mkAdd();
						mkStr(var.id);
					}
					else System.err.println("UnaryOp incOp Error");
				}
				
				else if(u.op.decOp()){
					if(u.term instanceof ArrayRef){
						ArrayRef ar =(ArrayRef)u.term;
						mkUcode(ar);
						mkLdc(1);
						mkSub();
						mkStr(ar.id);
					}
					else if(u.term instanceof Variable){
						Variable var =(Variable)u.term;
						mkUcode(var);
						mkLdc(1);
						mkSub();
						mkStr(var.id);
					}
					else System.err.println("UnaryOp decOp Error");
					
				}
				else if(u.op.notOp()){
					if(u.term instanceof ArrayRef){
						ArrayRef ar =(ArrayRef)u.term;
						mkUcode(ar);
						mkNotOp();
						mkStr(ar.id);
					}
					else if(u.term instanceof Variable)
					{
						Variable ar =(Variable)u.term;
						mkUcode(ar);
						mkNotOp();
						mkStr(ar.id);
					}
					
					else System.err.println("notOp Error");
				}
				else if(u.op.negOp()){
					if(u.term instanceof ArrayRef){
						ArrayRef ar =(ArrayRef)u.term;
						mkUcode(ar);
						mkLdc(-1);
						mkMul();
						mkStr(ar.id);
					}
					else if(u.term instanceof Variable)
					{
						Variable var =(Variable)u.term;
						mkUcode(var);
						mkLdc(-1);
						mkMul();
						mkStr(var.id);
					}
					else System.err.println("UnaryOp negOp Error");
					
					
				}
				else System.err.println("Ucode make Error ");
				
			}
	
		
		private Type typeToEqual(Expression e1, Expression e2){
			Type t1 = TypeManager.getInstance().typeOf(currentFunc, e1);
			Type t2 = TypeManager.getInstance().typeOf(currentFunc, e2);	
			if(t1.equals(t2)) return t1;  //타입이 같으면 타입 리턴
			else castType(t1,t2);		//타입이 다르면 t2를 t1으로 타입캐스팅
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
		
		private void mkGt(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("gt");
			writeToUco(sb.toString());
		}
		
		private void mkAnd(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("and");
			writeToUco(sb.toString());
		}
		
		private void mkOr(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("or");
			writeToUco(sb.toString());
		}
		
		private void mkLt(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("lt");
			writeToUco(sb.toString());
		}
		private void mkGe(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("ge");
			writeToUco(sb.toString());
		}
		private void mkLe(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("le");
			writeToUco(sb.toString());
		}
		private void mkEq(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("eq");
			writeToUco(sb.toString());
		}
		private void mkNe(){
			StringBuilder sb= new StringBuilder(mkSpace());
			sb.append("ne");
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
		
		
		private void mkLod(String id){
			VariableInfo vi =getVariableInfo(id);
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("lod ");
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
		private void mkSub(){
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("sub");
			writeToUco(sb.toString());
		}
		
		private void mkMul(){
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("mul");
			writeToUco(sb.toString());
		}
		
		private void mkDiv(){
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("div");
			writeToUco(sb.toString());
		}
		
		private void mkMod(){
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("mod");
			writeToUco(sb.toString());
		}
		
		private void mkPow(){
			
		}
		
		private void mkLdi(){
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("ldi");
			writeToUco(sb.toString());
		}
		

		private void mkNotOp(){
			StringBuffer sb = new StringBuffer(mkSpace());
			sb.append("notop");
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
		

		//미리 정의된 definedFunc Ucode로 선언.
		public void writeCustomFunctions(){


			
		}
		public void writeUcode(Object o){
			
			String id =null;
			ArrayList<Expression> params = null;
			
			if(o instanceof FuncCall){
				FuncCall e = (FuncCall)o;
				id=e.id;
				params=e.params;
			}else if(o instanceof voidFuncCall){
				voidFuncCall v = (voidFuncCall)o;
				id=v.id;
				params=v.param;
			}
			
			if(id.equals("write")){
			mkLdp();
			for (Expression e : params){
				VariableRef varef= (VariableRef) e;
				mkLod(varef.getId());
			}
			mkCall("write");
			
			}else if(id.equals("read")){
					for (Expression e : params)
					{
						VariableRef varef = (VariableRef) e;
						if(e instanceof Variable){
							mkLDA(varef.getId());
					}
				}
				mkCall("read");
			}
			//sin, cos, tan, pinv
			else if(id.equals("sin") || id.equals("cos")||id.equals("tan")){
				mkLdp();
				if(params.size()!=1) TypeManager.getInstance().typeError("함수의 파라미터 수가 일치하지 않습니다.");
				mkUcode(params.get(0));
				mkCall(id);
			}else if(id.equals("pinv")){
				mkLdp();
				if(params.size()!=1) TypeManager.getInstance().typeError("함수의 파라미터 수가 일치하지 않습니다.");
				Expression exp = params.get(0);
				if(!(exp instanceof ArrayRef)) TypeManager.getInstance().typeError("pinv는 배열의 주소를 참조하여야 합니다.");
				mkUcode((ArrayRef)exp);
				mkCall("pinv");
			}
		
			
			
		}

		public static void main(String[] args) {
			
			CodeGenerator codeGen = new CodeGenerator("test.txt", "uFile.uco");
			codeGen.generate();
			
		}
}
