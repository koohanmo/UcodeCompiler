package abstractSyntax;


public class Conditional extends Statement {
// Conditional = Expression test; Statement thenbranch, elsebranch

    public Expression test;
    public Statement thenbranch, elsebranch;
    
    public Conditional (Expression t, Statement tp) {
        test = t; thenbranch = tp; elsebranch = new Skip( );
    }
    
    public Conditional (Expression t, Statement tp, Statement ep) {
        test = t; thenbranch = tp; elsebranch = ep;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w); {
            System.out.print("\t\t\t\t");
        }
       System.out.println("Conditional:");
	test.display(++k);
	thenbranch.display(++k);
	elsebranch.display(++k);
    }
}