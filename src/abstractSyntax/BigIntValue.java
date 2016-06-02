package abstractSyntax;

public class BigIntValue extends Value{
    private String value = "0";

    BigIntValue ( ) { type = Type.BIGINT; }
    BigIntValue (String v) { this( ); value = v; undef = false; }

    String BigIntValue ( ) {
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
	System.out.print("BigInt: ");
	System.out.println(value);
    }
}