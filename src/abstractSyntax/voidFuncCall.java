package abstractSyntax;

import java.util.ArrayList;

public class voidFuncCall extends Statement{
	//voidFuncCall = 
	
	public String id;
	public ArrayList<Expression> param= new ArrayList<Expression>();
	public voidFuncCall(String i, ArrayList<Expression> ex){
		id=i;
		param=ex;
	}
	
	@Override
	public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\tvoidFuncCall:");
	    System.out.println(id);
	    System.out.println("Params = {");
		for (int i = 0; i < param.size(); i++)
			param.get(i).display(k);
	    System.out.println("}");
	    
    }
	
}
