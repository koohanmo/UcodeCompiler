package abstractSyntax;

import java.util.ArrayList;


public class FuncCall extends Expression{

	String id;
	ArrayList<Expression> param= new ArrayList<Expression>();
	public FuncCall(String i, ArrayList<Expression> ex){
		id=i;
		param=ex;
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
		for (int i = 0; i < param.size(); i++)
			param.get(i).display(k);
	    System.out.print("}");
	    
    }
	
}