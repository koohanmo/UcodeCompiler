package abstractSyntax;


public class Assignment extends Statement {
    // Assignment = Variable target; Expression source
	//VariableRef target; Expression source
	VariableRef target;
	Expression source;
	
	public Assignment(VariableRef target, Expression source)
	{
		this.target=target;
		this.source = source;
	}
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Assignement :");
	target.display(++k);
	source.display(++k);
    }

}