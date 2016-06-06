package sementic;

import abstractSyntax.Type;

public class SymbolElement {
	
	public Type type;
	public int size;
	
	public SymbolElement(Type t, int s){
		this.type=t;
		this.size=s;
	}

}
