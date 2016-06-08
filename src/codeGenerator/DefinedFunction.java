package codeGenerator;

import java.util.ArrayList;

import abstractSyntax.Expression;
import abstractSyntax.FuncCall;
import abstractSyntax.Type;
import abstractSyntax.VariableRef;
import abstractSyntax.voidFuncCall;
import sementic.FunctionDef;

public class DefinedFunction {
	
	
	public static String[] definedFunc ={"write","read","sin","cos","tan","pinv"};
	public static Type[] definedType ={null, Type.INT, Type.FLOAT, Type.FLOAT, Type.FLOAT, null};
 
	public DefinedFunction(){
		
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
