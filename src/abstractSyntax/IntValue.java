package abstractSyntax;


public class IntValue extends Value {
    private int value = 0;

    IntValue ( ) { type = Type.INT; }

    public IntValue (int v) { this( ); value = v; undef = false; }

    int intValue ( ) {
        assert !undef : "reference to undefined int value";
        return value;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.print("Int: ");
	System.out.println(value);
    }

}