package abstractSyntax;


public class CharValue extends Value {
    private char value = ' ';

    CharValue ( ) { type = Type.CHAR; }

    public CharValue (char v) { this( ); value = v; undef = false; }

    char charValue ( ) {
        assert !undef : "reference to undefined char value";
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
	System.out.print("CharValue: ");
	System.out.println(value);
    }

}