package abstractSyntax;

public class VariableDecl extends Declaration
{
	//Variable v; Type t
	public Variable v;
	public Type t;
	public VariableDecl(Variable v, Type t)
	{
		this.v=v;
		this.t=t;
	}
	
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\t VariableDecl:");
	    System.out.println(t+" ");
	    v.display(++k);
	  	
    }

}