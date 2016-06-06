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
	private HashMap<String, SymbolElement> varTable = new HashMap<String, SymbolElement>();
	private HashMap<String, Integer> arrayTable = new HashMap<String, Integer>();
	private LinkedList<SymbolElement> params = new LinkedList<SymbolElement>();
	
	public FunctionDef(Type t, String name){
		this.funcType=t;
		this.funcName=name;
	}
	
	public Type getFuncType(){
		return funcType;
	}
	
	public void addParams(Type type , int size){
		params.add(new SymbolElement(type,size));
	}
	
	public SymbolElement getParams(int idx){
		return params.get(idx);
	}
	
	public int getParamSize(){
		return params.size();
	}
	
	public Type getVariableType(String id){
		return varTable.get(id).type;
	}	
	
	public void addVariable(String s, Type t ,int size){
		varTable.put(s,new SymbolElement(t,size));
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
