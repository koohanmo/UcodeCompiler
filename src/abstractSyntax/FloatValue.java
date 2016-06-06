package abstractSyntax;

import codeGenerator.CodeGenerator;

public class FloatValue extends Value {
    private float value = 0;

    FloatValue ( ) { type = Type.FLOAT; }

    public FloatValue (float v) { this( ); value = v; undef = false; }

    float floatValue ( ) {
        assert !undef : "reference to undefined float value";
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
	System.out.print("FloatValue: ");
	System.out.println(value);
    }

	public void genCode() {
		CodeGenerator.ldc(Float.floatToRawIntBits(value));
	}
}