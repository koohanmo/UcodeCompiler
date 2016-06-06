package sementic;

import java.util.ArrayList;
import java.util.HashMap;

import abstractSyntax.*;

public class TypeManager {
	
	private static TypeManager instance;
	private HashMap<String, Type> globalVariables = new HashMap<String, Type>();
	private HashMap<String, Integer> globalArray = new HashMap<String, Integer>();
	private HashMap<String, FunctionDef> funcTable = new HashMap<String, FunctionDef>();
	
	
	public static TypeManager getInstance(){
		if(instance==null) instance = new TypeManager();
		return instance;
	}
	
	public void typeError(String msg){
		System.err.println(msg);
		System.exit(0);
	}
	
	/*
	 *  전역 변수 체크
	 */
	public void setGlobalVariable(Declarations decls){
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(globalVariables.containsKey(current.v.id)) typeError(current.v.id +": 변수명 선언이 중복되었습니다.");
				globalVariables.put(current.v.id, current.t);
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(globalVariables.containsKey(current.v.id)) typeError(current.v.id +": 변수명 선언이 중복되었습니다.");
				globalVariables.put(current.v.id, current.t);
				globalArray.put(current.v.id, current.size);
			}
		}
	}
	
	public void setLocalVariable(FunctionDef f, Declarations arg, Declarations decls){
		//set arg to localVariable
		if(arg!=null){
			for(Declaration d : arg){
				if(d instanceof VariableDecl){
					VariableDecl current = (VariableDecl)d;
					if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
					if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
					f.addVariable(current.v.id, current.t);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
					if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
					f.addVariable(current.v.id, current.t);
					f.addArray(current.v.id, current.size);
				}
			}
		}
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
				if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
				f.addVariable(current.v.id, current.t);
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
				if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 변수명 선언이 중복되었습니다.");
				f.addVariable(current.v.id, current.t);
				f.addArray(current.v.id, current.size);
			}
		}
	}
	
	public void V(Funcs f){
		if(f instanceof Func){
			Func currentF = (Func)f;
			FunctionDef currenfDef = new FunctionDef(currentF.type, currentF.id);
			for(Declaration d : currentF.arguments){
				if(d instanceof VariableDecl){
					VariableDecl current = (VariableDecl)d;
					currenfDef.addParams(current.t);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					currenfDef.addParams(current.t);
				}
			}
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
			V(currenfDef,currentF.returnExpr);
			if(funcTable.containsKey(currentF.id)) typeError(currenfDef.funcName + "  함수명이 중복되었습니다.");  //기본 정의함수랑도 비교해야됨
			else funcTable.put(currentF.id,currenfDef);
		}
		else if(f instanceof voidFunc){
			voidFunc currentF = (voidFunc)f;
			FunctionDef currenfDef = new FunctionDef(null, currentF.id);
			for(Declaration d : currentF.arguments){
				if(d instanceof VariableDecl){
					VariableDecl current = (VariableDecl)d;
					currenfDef.addParams(current.t);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					currenfDef.addParams(current.t);
				}
			}
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey(currentF.id)) typeError(currentF.id + "  함수명이 중복되었습니다.");  //기본 정의함수랑도 비교해야됨
			else funcTable.put(currentF.id,currenfDef);
		}
		else{
			MainFunc currentF = (MainFunc)f;
			FunctionDef currenfDef = new FunctionDef(null, "main");
			setLocalVariable(currenfDef,null,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey("main")) typeError("main" + "  함수명이 중복되었습니다.");  //기본 정의함수랑도 비교해야됨
			else funcTable.put("main",currenfDef);
		}
	}
	
	private FunctionDef getFuncTable(String funcName){
		return funcTable.get(funcName);
	}
	
	//TODO Complete
	//function statements check
	public void V(FunctionDef f, Statements statements){
		if(statements.size()==0) return;
		for(Statement s : statements){
			V(f,s);
		}
	}
	
	public void V(FunctionDef f, Statement s){
		//Skip | Block | Assignment | Conditional | Loop | forLoop | voidFuncCall
		if(s==null) throw new IllegalArgumentException("AST error : null statement");
		if(s instanceof Skip) return;
		else if(s instanceof Assignment){
			//선언이 되었는지, 연산 type이 맞는지 확인.
			Assignment current = (Assignment)s;
			V(f, current.target);
			V(f, current.source);
			//target Type과 source Type 비교 - typeOf
			V(typeOf(f, current.target),typeOf(f, current.source));
			
		}else if(s instanceof Block){
			Block current = (Block)s;
			for(Statement ss:current) V(f,ss);
			
		}else if(s instanceof Conditional){
			//조건문 , statement 검사
			Conditional c = (Conditional)s;
			V(f,c.test);
			V(Type.BOOL,typeOf(f, c.test)); //IF의 Test는 BOOL만 허용
			V(f, c.thenbranch);
			V(f,c.elsebranch);
		}else if(s instanceof Loop){
			//조건문 , statement 검사
			Loop l = (Loop)s;
			V(f, l.test);
			V(Type.BOOL,typeOf(f, l.test)); //Loop의 Test는 BOOL만 허용
			V(f, l.body);
		}else if(s instanceof ForLoop){
			//조건문 3개, statement 검사
			//ForLoop = Assignment assign ; Expression test ; Expression third; Statement body
			ForLoop fo = (ForLoop) s;
			V(f, fo.assign);
			V(f,fo.test);
			V(Type.BOOL, typeOf(f,fo.test)); //ForLoop의 Test는 Bool만 허용
			V(f,fo.test);
			V(f,fo.body);			
		}else if(s instanceof voidFuncCall){
			//선언된 함수인지, 파라미터의 갯수, 타입이 맞는지 확인.
			voidFuncCall vf = (voidFuncCall) s;
			//TODO 기본 함수도 검사 추가해야됨
			if(!funcTable.containsKey(vf.id)) typeError(vf.id +" : 선언되어 있지 않은 함수 입니다.");
			FunctionDef fd=funcTable.get(vf.id);
			V(f, vf.param, fd); //파라미터 검사
			
		}else typeError("statement가 적합하지 않습니다.");
	}
	
	public void V(FunctionDef f, Expression e){
		// Expression =VarableRef |Variable | Value | Binary | Unary | FuncCall
		if(e==null) throw new IllegalArgumentException("AST error : null Expression");
		if(e instanceof Value){ return;
		}else if(e instanceof VariableRef){
			V(f,(VariableRef)e);
		}else if(e instanceof Binary){
			Binary b = (Binary) e;
			V(f,b.term1);
			V(f,b.term2);
			V(typeOf(f,b.term1),typeOf(f,b.term2));
		}else if(e instanceof Unary){
			Unary u = (Unary) e;
			V(f,u.term);
			if(u.op.IncOp() || u.op.DecOp() || u.op.NegateOp()){
				if(typeOf(f,u.term).equals(Type.BOOL)) typeError(f.funcName +": "+ u.op +"의 UnaryOp가 정확하지 않습니다."); 
			}else if(u.op.NotOp()){
				V(Type.BOOL,typeOf(f,u.term));
			}else if(u.op.CastOp()){
				if(u.op.intOp()){
					V(Type.INT,typeOf(f,u.term));
				}else if(u.op.bigIntOp()){
					V(Type.BIGINT,typeOf(f,u.term));
				}else if(u.op.charOp()){
					V(Type.CHAR,typeOf(f,u.term));
				}else if(u.op.floatOp()){
					V(Type.FLOAT,typeOf(f,u.term));
				}else if(u.op.BooleanOp()){
					V(Type.BOOL,typeOf(f,u.term));
				}else typeError(f.funcName +": "+ u.op +"의 UnaryOp가 정확하지 않습니다.");
			}else typeError(f.funcName +": "+ u.op +"의 UnaryOp가 정확하지 않습니다.");
		}else if(e instanceof FuncCall){
			FuncCall fc= (FuncCall) e;
			//TODO 기본 함수도 검사 추가해야됨
			if(!funcTable.containsKey(fc.id)) typeError(f.funcName +": "+fc.id+"가 정의되어 있지 않습니다.");
			FunctionDef fd=funcTable.get(fc.id);
			V(f, fc.params, fd); //파라미터 검사
			
		}else typeError(f.funcName+" : "+"  Expression의 타입이 이상합니다.");
	}
	
	public void V(FunctionDef f, ArrayList<Expression> params, FunctionDef fp){
		if(params.size()!=fp.getParamSize()) typeError(f.funcName+" : "+fp.funcName+" : 파라미터의 수가 일치하지 않습니다.");
		for(Expression e : params) {
			V(f,e);
		}
		for(int i=0; i<params.size();i++){
			if(!typeOf(f, params.get(i)).equals(fp.getParams(i)))  
				typeError(f.funcName+" : "+fp.funcName+" : 파라미터의 타입이 일치하지 않습니다.");
		}
		
		
		
	}
	
	public void V(FunctionDef f, VariableRef v){
		if(v instanceof Variable){
			Variable current = (Variable)v;
			if(!f.isContainVariable(current.id) && !globalVariables.containsKey(current.id))
				typeError(f.funcName+" : "+current.id+": 변수가 선언이 되어 있지 않습니다.");
			
		}
		else{
			ArrayRef current = (ArrayRef)v;
			if(!f.isContainArray(current.id) && !globalVariables.containsKey(current.id))
				typeError(f.funcName+" : "+current.id+": 배열이 선언이 되어 있지 않습니다.");
			//TODO
			if(!typeOf(f,current.index).equals(Type.INT))
				typeError(f.funcName+" : "+current.id+": 배열의 인덱스가 정확하지 않습니다.");
			
		}
	}
	
	
	private Type typeOf(FunctionDef f, Expression e){
		// Expression =VarableRef |Variable | Value | Binary | Unary | FuncCall
		if(e==null) throw new IllegalArgumentException("AST error : null Expression");
		if(e instanceof Value){
			return ((Value)e).type;
		}else if(e instanceof ArrayRef){
			return getVariableType(f,(ArrayRef)e);
		}
		else if(e instanceof Variable){
			return getVariableType(f,(Variable)e);
		}else if(e instanceof Binary){
			Binary b = (Binary)e;
			if(b.op.ArithmeticOp()){
				return typeOf(f,((Binary) e).term1);
			}else if(b.op.RelationalOp() || b.op.BooleanOp())
				return Type.BOOL;
		}else if(e instanceof Unary){
			Unary u = (Unary)e;
			if(u.op.NotOp()) return Type.BOOL;
			else if(u.op.NegateOp() || u.op.IncOp() || u.op.DecOp()) return typeOf(f,u.term);
			else if(u.op.CastOp()){
				if(u.op.intOp()){
					return Type.INT;
				}else if(u.op.charOp()){
					return Type.CHAR;
				}else if(u.op.bigIntOp()){
					return Type.BIGINT;
				}else if(u.op.floatOp()){
					return Type.FLOAT;
				}else if(u.op.BooleanOp()){
					return Type.BOOL;
				}else typeError(f.funcName +": "+ u.op +"의 UnaryOp가 정확하지 않습니다.");
			}
		}else if(e instanceof FuncCall){
			FuncCall fc = (FuncCall)e;
			return funcTable.get(fc.id).getFuncType();
		}else 
			typeError(f.funcName+" : "+"알맞는 Expression이 아닙니다.");
		return null; //진입X
	}
	
	private Type getVariableType(FunctionDef f,ArrayRef v){
		if(f.isContainVariable(v.id)) return f.getVariableType(v.id);
		else if(globalVariables.containsKey(v.id)) return globalVariables.get(v.id);
		typeError(v.id +"는 Type이 정이되어 있지 않습니다.");
		return null;
	}
	
	private Type getVariableType(FunctionDef f,Variable v){
		if(f.isContainVariable(v.id)) return f.getVariableType(v.id);
		else if(globalVariables.containsKey(v.id)) return globalVariables.get(v.id);
		typeError(f.funcName+" : "+v.id +"는 Type이 정이되어 있지 않습니다.");
		return null;
	}
	
	private boolean V(Type t1, Type t2){
		if(t1.equals(Type.INT)){
			if(t2.equals(Type.BOOL) || t2.equals(Type.BIGINT))return false;
			else return true;
		}else if(t1.equals(Type.FLOAT)){
			if(t2.equals(Type.CHAR) || t2.equals(Type.BIGINT) || t2.equals(Type.BOOL)) return false;
			else return true;
		}else if(t1.equals(Type.CHAR)){
			if(t2.equals(Type.BOOL) || t2.equals(Type.BIGINT)) return false;
			else return true;
		}else if(t1.equals(Type.BOOL)){
			if(t2.equals(Type.BOOL)) return true;
			else return false;			
		}else if(t1.equals(Type.BIGINT)){
			if(t2.equals(Type.BOOL)) return false;
			else return true;
		}
		return false;
	}
	
}

