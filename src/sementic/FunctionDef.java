package sementic;

import java.util.HashMap;
import java.util.LinkedList;

import abstractSyntax.Declarations;
import abstractSyntax.Type;

public class FunctionDef {
	
	private int blockNum;
	private int lexicalLevel;
	
	private Type funcType; 
	private HashMap<String, Type> varTable = new HashMap<String, Type>();
	private HashMap<String, Integer> arrayTable = new HashMap<String, Integer>();
	private LinkedList<ParamInfo> params = new LinkedList<ParamInfo>();
	
	public FunctionDef(Type t){
		this.funcType=t;
	}
	
	public void setParams(Declarations arg){
		
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
	
}
