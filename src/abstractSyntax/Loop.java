package abstractSyntax;



public class Loop extends Statement {
// Loop = Expression test; Statement body
    public Expression test;
    public Statement body;

    public Loop (Expression t, Statement b) {
        test = t; body = b;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
    System.out.println("\twhile:");
    test.display(++k);
	body.display(++k);
    }
	
}