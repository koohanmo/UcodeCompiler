package abstractSyntax;


public class ForLoop extends Statement{
	//ForLoop = Expression first ; Expression test ; Expression third; Statement body
	
	Assignment assign;
	Expression test,third;
	Statement body;
	public ForLoop(Assignment assign, Expression second, Expression third, Statement body)
	{
		this.assign=assign;
		this.test = second;
		this.third=third;
		this.body=body;
	}
	
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\tfor:");
	    assign.display(++k);
	    test.display(++k);
	    third.display(++k);
		body.display(++k);
    }
}