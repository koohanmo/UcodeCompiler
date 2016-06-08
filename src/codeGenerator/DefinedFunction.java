package codeGenerator;

import java.util.ArrayList;

import abstractSyntax.Expression;
import abstractSyntax.FuncCall;
import abstractSyntax.Type;
import abstractSyntax.voidFuncCall;

public class DefinedFunction {
	
	
	public static String[] definedFunc ={"write","read"};
	public static Type[] definedType ={null, Type.INT};
	public static String[] customFunc ={};
 
	public DefinedFunction(){
		
	}
	

	
	//미리 정의된 definedFunc Ucode로 선언.
	public void writeCustomFunctions(){
			
	}
	
	
	//Expr(funcCall) or voidFuncCall로 날라옴
	public void writeUcode(Object o){
		
		String id =null;
		ArrayList<Expression> params;
		
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
			
			
			
			
		}else if(id.equals("read")){
			
			
			
			
			
		}
		
		
	}
	
	
}
