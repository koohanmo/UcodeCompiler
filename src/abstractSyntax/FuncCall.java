package abstractSyntax;

import java.util.ArrayList;
import abstractSyntax.IntValue;
import abstractSyntax.Value;
import codeGenerator.CodeGenerator;
import abstractSyntax.Expression;


public class FuncCall extends Expression{

	public String id;
	public ArrayList<Expression> params= new ArrayList<Expression>();
	public FuncCall(String i, ArrayList<Expression> ex){
		id=i;
		params=ex;
	}
	@Override
	public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\tFuncCall:" + id);
	    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.print("Params = {");
		for (int i = 0; i < params.size(); i++)
			params.get(i).display(k);
	    System.out.print("}");
	    
    }
	
	public void genCode() {	
		
		CodeGenerator.ldp();
		for(Expression param : params)
		{
			if(param instanceof Variable )
			{
				CodeGenerator.lod(((Variable) param).id);
			}
			
			else if(param instanceof ArrayRef)
			{
				CodeGenerator.lda(((ArrayRef) param).id);
			}
			
			else if (param instanceof Value)
			{
				if(param instanceof IntValue) CodeGenerator.ldc(((Value) param).intValue());
				if(param instanceof CharValue) CodeGenerator.ldc(((Value) param).charValue());
				if(param instanceof FloatValue) CodeGenerator.ldc(Float.floatToRawIntBits(((Value) param).floatValue()));
				if(param instanceof BoolValue) CodeGenerator.ldc(((Value) param).boolValue()? 1:0);
			}
			else if (param instanceof Binary)
			{
			
			}
			
			else if (param instanceof Unary)
			{
				
			}
			else
			{
				
			}		
		}
		
		CodeGenerator.call(id);
		
	}
	
	
}