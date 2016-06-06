package abstractSyntax;

import codeGenerator.CodeGenerator;

public class BoolValue extends Value {
    private boolean value = false;

    BoolValue ( ) { type = Type.BOOL; }

    public BoolValue (boolean v) { this( ); value = v; undef = false; }

    boolean boolValue ( ) {
        assert !undef : "reference to undefined bool value";
        return value;
    }

    int intValue ( ) {
        assert !undef : "reference to undefined bool value";
        return value ? 1 : 0;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.print("BoolValue: ");
	System.out.println(value);
    }

}