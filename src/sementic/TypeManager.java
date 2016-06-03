package sementic;

import java.util.HashMap;

import abstractSyntax.*;

public class TypeManager {
	
	private static TypeManager instance;
	private HashMap<String, Type> globalVariables = new HashMap<String, Type>();
	private HashMap<String, Integer> globalArray = new HashMap<String, Integer>();
	private HashMap<String, FunctionDef> funcTable = new HashMap<String, FunctionDef>();
	
	public TypeManager(){
		instance = new TypeManager();
	}
	
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
		if(arg!=null || arg.size()>0){
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
		}
		else if(f instanceof voidFunc){
			voidFunc currentF = (voidFunc)f;
			FunctionDef currenfDef = new FunctionDef(null);
			currenfDef.setParams(currentF.arguments);
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
		}
		else{
			MainFunc currentF = (MainFunc)f;
			FunctionDef currenfDef = new FunctionDef(null);
			setLocalVariable(currenfDef,null,currentF.decpart);
			V(currenfDef,currentF.body);
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
			//Skip | Block | Assignment | Conditional | Loop | forLoop | voidFuncCall
			
		}
	}
	
	//TODO Complete
	//return check
	public void V(FunctionDef f, Expression ret){
		
	}
	
}
