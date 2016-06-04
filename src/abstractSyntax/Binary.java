package abstractSyntax;

public class Binary extends Expression {
// Binary = Operator op; Expression term1, term2
    public Operator op;
    public Expression term1, term2;

    public Binary (Operator o, Expression l, Expression r) {
        op = o; term1 = l; term2 = r;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.print("Binary: ");
	op.display(++k);
	term1.display(k);
	term2.display(k);
    } // binary
    public String toString() {
        return ("Binary: op="+op+" term1="+term1+" term2="+term2);
    }
}