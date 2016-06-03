package abstractSyntax;

import sementic.TypeManager;

public class MainFunc extends Funcs{
	//MainFunc = Declarations decpart ; Statements body;
	public Declarations decpart;
	public Statements body;
	
	public MainFunc(Declarations d, Statements b){
		decpart = d;
		body = b;
	}
	
	
	public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	       System.out.println("Main :");
	        decpart.display(++k);
        System.out.print("Statements = {");
    	for (int i = 0; i < body.size(); i++)
    		body.get(i).display(k);
        System.out.println("}");
    }
	
    public void validCheck(){
    		TypeManager.getInstance().V(this);
    }
}