package abstractSyntax;



public class Func extends Funcs{
	//Func = Type type; String id ; Declarations arguments ; Declarrations decpart ; Statements body ; Expression returnExpr;
	Type type;
	String id;
	Declarations arguments;
	Declarations decpart;
	Statements body;
	Expression returnExpr;
	
	
	public Func(Type t, String i, Declarations p, Declarations d, Statements b, Expression r){
		type=t;
		id=i;
		arguments=p;
		decpart=d;
		body=b;
		returnExpr=r;
	}
	
    public void display(int k) {
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println(type.toString()+"Function: " + id);
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
    System.out.print("Params = {");
	for (int i = 0; i < arguments.size(); i++)
		arguments.get(i).display(k);
    System.out.println("}");
    
    
    System.out.print("Declarations = {");
	for (int i = 0; i < decpart.size(); i++)
		decpart.get(i).display(k);
    System.out.println("}");
    
    System.out.print("Statements = {");
	for (int i = 0; i < body.size(); i++)
		body.get(i).display(k);
    System.out.println("}");
    
    System.out.print("return = {");
 		returnExpr.display(k);
     System.out.println("}");
    }
}
