package sementic;

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
		System.exit(1);
	}
	
	/*
	 *  전역 변수 체크
	 */
	public void setGlobalVariable(Declarations decls){
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(globalVariables.containsKey(current.v.id)) typeError("변수명 선언이 중복되었습니다.");
				globalVariables.put(current.v.id, current.t);
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(globalVariables.containsKey(current.v.id)) typeError("변수명 선언이 중복되었습니다.");
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
					if(f.isContainVariable(current.v.id)) typeError("변수명 선언이 중복되었습니다.");
					f.addVariable(current.v.id, current.t);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					if(f.isContainVariable(current.v.id)) typeError("변수명 선언이 중복되었습니다.");
					f.addVariable(current.v.id, current.t);
					f.addArray(current.v.id, current.size);
				}
			}
		}
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(f.isContainVariable(current.v.id)) typeError("변수명 선언이 중복되었습니다.");
				f.addVariable(current.v.id, current.t);
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(f.isContainVariable(current.v.id)) typeError("변수명 선언이 중복되었습니다.");
				f.addVariable(current.v.id, current.t);
				f.addArray(current.v.id, current.size);
			}
		}
	}
	
	public void V(Funcs f){
		if(f instanceof Func){
			Func currentF = (Func)f;
			FunctionDef currenfDef = new FunctionDef(currentF.type);
			currenfDef.setParams(currentF.arguments);
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
			V(currenfDef,currentF.returnExpr);
			if(funcTable.containsKey(currentF.id)) typeError("함수명이 중복되었습니다.");  //기본 정의함수랑도 비교해야됨
			else funcTable.put(currentF.id,currenfDef);
		}
		else if(f instanceof voidFunc){
			voidFunc currentF = (voidFunc)f;
			FunctionDef currenfDef = new FunctionDef(null);
			currenfDef.setParams(currentF.arguments);
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey(currentF.id)) typeError("함수명이 중복되었습니다.");  //기본 정의함수랑도 비교해야됨
			else funcTable.put(currentF.id,currenfDef);
		}
		else{
			MainFunc currentF = (MainFunc)f;
			FunctionDef currenfDef = new FunctionDef(null);
			setLocalVariable(currenfDef,null,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey("main")) typeError("함수명이 중복되었습니다.");  //기본 정의함수랑도 비교해야됨
			else funcTable.put("main",currenfDef);
		}
	}
	
	public FunctionDef getFuncTable(String funcName){
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
			
		}else if(s instanceof Conditional){
			//조건문 , statement 검사
			
		}else if(s instanceof Loop){
			//조건문 , statement 검사
			
		}else if(s instanceof ForLoop){
			////조건문 3개, statement 검사
			
		}else if(s instanceof voidFuncCall){
			//선언된 함수인지, 파라미터의 갯수, 타입이 맞는지 확인.
			
		}else typeError("statement가 적합하지 않습니다.");
	}
	
	//TODO Complete
	public void V(FunctionDef f, Expression e){
		
	}
	
}
