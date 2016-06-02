package abstractSyntax;


public class Unary extends Expression {
    // Unary = Operator op; Expression term
    Operator op;
    Expression term;

    public Unary (Operator o, Expression e) {
        op = o; term = e;
    } // unary
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.print("Unary: ");
	op.display(++k);
	term.display(k);
    }
}