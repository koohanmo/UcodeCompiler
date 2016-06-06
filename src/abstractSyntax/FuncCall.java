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
	
	
	
}