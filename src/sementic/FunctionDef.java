package sementic;

import java.util.HashMap;
import java.util.LinkedList;

import abstractSyntax.Declarations;
import abstractSyntax.Type;

public class FunctionDef {
	
	public String funcName;
	
	private int blockNum;
	private int lexicalLevel;
	
	private Type funcType; 
	private HashMap<String, Type> varTable = new HashMap<String, Type>();
	private HashMap<String, Integer> arrayTable = new HashMap<String, Integer>();
	private LinkedList<Type> params = new LinkedList<Type>();
	
	public FunctionDef(Type t, String name){
		this.funcType=t;
		this.funcName=name;
	}
	
	public Type getFuncType(){
		return funcType;
	}
	
	public void addParams(Type type){
		params.add(type);
	}
	
	public Type getParams(int idx){
		return params.get(idx);
	}
	
	public int getParamSize(){
		return params.size();
	}
	
	public Type getVariableType(String id){
		return varTable.get(id);
	}	
	
	public void addVariable(String s, Type t){
		varTable.put(s,t);
	}
	
	public void addArray(String s, int n){
		arrayTable.put(s, n);
	}

	public boolean isContainVariable(String name){
		return varTable.containsKey(name);
	}
	
	public boolean isContainArray(String name){
		return arrayTable.containsKey(name);
	}
	
}
