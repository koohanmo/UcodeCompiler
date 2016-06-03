package abstractSyntax;


public class ArrayDecl extends Declaration
{
	//Variable v; Type t; Integer size
	public Variable v;
	public Type t;
	public int size;
	public ArrayDecl(Variable v, Type t, int size)
	{
		this.v=v;
		this.t=t;
		this.size=size;
	}
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\t ArrayDecl:");
	    System.out.println(t+" " + size);
	    v.display(++k);
    }
}