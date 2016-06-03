package abstractSyntax;


public class Variable extends VariableRef
{
	public String id;
	public Variable(String id)
	{
		this.id=id;
	}
	
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Variable " + id);
    }
	
	
}