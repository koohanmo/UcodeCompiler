package parser;
import java.util.*;

class Program {
    // Program = Declarations decpart ; Funcs funcs; MainFunc mainFunc; 
    Declarations decpart;
    Funcs funcs;
    MainFunc mainFunc;

    Program (Declarations d, Funcs f, MainFunc m){
        decpart = d;
        funcs = f;
        mainFunc = m;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	        System.out.println("Program (abstract syntax:)");
	        decpart.display(++k);
	        funcs.display(++k);
	        mainFunc.display(++k);
    }
}

class Funcs extends ArrayList<Funcs>{
	//Funcs = Func|voidFunk*
    public void display(int k) {
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("All Functions: ");
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
    System.out.print("Functions = {");
	for (int i = 0; i < size(); i++)
		get(i).display(k);
    System.out.println("}");
    }
}

class Func extends Funcs{
	//Func = Type type; String id ; Declarations arguments ; Declarrations decpart ; Statements body ; Expression returnExpr;
	Type type;
	String id;
	Declarations arguments;
	Declarations decpart;
	Statements body;
	Expression returnExpr;
	
	
	public Func(Type t, String i, Declarations p, Declarations d, Statements b, Expression r){
		type=t;
		id=i;
		arguments=p;
		decpart=d;
		body=b;
		returnExpr=r;
	}
	
    public void display(int k) {
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println(type.toString()+"Function: " + id);
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
    System.out.print("Params = {");
	for (int i = 0; i < arguments.size(); i++)
		arguments.get(i).display(k);
    System.out.println("}");
    
    
    System.out.print("Declarations = {");
	for (int i = 0; i < decpart.size(); i++)
		decpart.get(i).display(k);
    System.out.println("}");
    
    System.out.print("Statements = {");
	for (int i = 0; i < body.size(); i++)
		body.get(i).display(k);
    System.out.println("}");
    
    System.out.print("return = {");
 		returnExpr.display(k);
     System.out.println("}");
    }
	
}

class voidFunc extends Funcs{
	//voidFunc = String id; Declarations arguments ; Declarrations decpart ; Statements body
	String id;
	Declarations arguments;
	Declarations decpart;
	Statements body;
	
	public voidFunc(String i, Declarations p, Declarations d, Statements b){
		id=i;
		arguments=p;
		decpart=d;
		body=b;
	}
	
	
    public void display(int k) {
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Function: " + id);
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
    System.out.print("Params = {");
	for (int i = 0; i < arguments.size(); i++)
		arguments.get(i).display(k);
    System.out.println("}");
    
    
    System.out.print("Declarations = {");
	for (int i = 0; i < decpart.size(); i++)
		decpart.get(i).display(k);
    System.out.println("}");
    
    System.out.print("Statements = {");
	for (int i = 0; i < body.size(); i++)
		body.get(i).display(k);
    System.out.println("}");
    }
	
}

class MainFunc{
	//MainFunc = Declarations decpart ; Statements body;
	Declarations decpart;
	Statements body;
	
	public MainFunc(Declarations d, Statements b){
		decpart = d;
		body = b;
	}
	
	
	public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	       System.out.println("Main :");
	        decpart.display(++k);
        System.out.print("Statements = {");
    	for (int i = 0; i < body.size(); i++)
    		body.get(i).display(k);
        System.out.println("}");
    }
}


class Params extends ArrayList<Expression>{
	//Pram
	
	public void display(int k){}
}
class Declarations extends ArrayList<Declaration> {
    // Declarations = Declaration*

    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Declarations: ");
    for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
    System.out.print("Declarations = {");
	for (int i = 0; i < size(); i++)
		get(i).display(k);
    System.out.println("}");
    }
}

abstract class Declaration 
{
	//VariableDecl | ArrayDecl
	abstract void display(int k);
}

class VariableDecl extends Declaration
{
	//Variable v; Type t
	Variable v;
	Type t;
	VariableDecl(Variable v, Type t)
	{
		this.v=v;
		this.t=t;
	}
	
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\t VariableDecl:");
	    System.out.println(t+" ");
	    v.display(++k);
	  	
    }

}

class ArrayDecl extends Declaration
{
	//Variable v; Type t; Integer size
	Variable v;
	Type t;
	int size;
	ArrayDecl(Variable v, Type t, int size)
	{
		this.v=v;
		this.t=t;
		this.size=size;
	}
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\t ArrayDecl:");
	    System.out.println(t+" " + size);
	    v.display(++k);
    }
}

class Type {
    // Type = int | bool | char | float 
    final static Type INT = new Type("int");
    final static Type BOOL = new Type("bool");
    final static Type CHAR = new Type("char");
    final static Type FLOAT = new Type("float");
    final static Type BIGINT = new Type("bigint");
    
    private String id;
    private Type (String t) { id = t; }
    public String toString ( ) { return id; }
}

class Statements extends  ArrayList<Statement>{
	//Statements = statement*
}


abstract class Statement extends Statements{
    // Statement = Skip | Block | Assignment | Conditional | Loop | forLoop | voidFuncCall
    abstract public void display(int k);
}

class Skip extends Statement {
	public void display(int k){}
}


class ForLoop extends Statement{
	//ForLoop = Expression first ; Expression test ; Expression third; Statement body
	
	Expression first,test,third;
	Statement body;
	public ForLoop(Expression first, Expression second, Expression third, Statement body)
	{
		this.first=first;
		this.test = second;
		this.third=third;
		this.body=body;
	}
	
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	    System.out.println("\tfor:");
	    first.display(++k);
	    test.display(++k);
	    third.display(++k);
		body.display(++k);
    }
}

class voidFuncCall extends Statement{
	//voidFuncCall = 
	
	String id;
	ArrayList<Expression> param= new ArrayList<Expression>();
	public voidFuncCall(String i, ArrayList<Expression> ex){
		id=i;
		param=ex;
	}
	
	@Override
	public void display(int k) { 
		// TODO Auto-generated method stub
		
		
		
	}
	
}

class FuncCall extends Statement{

	String id;
	ArrayList<Expression> param= new ArrayList<Expression>();
	public FuncCall(String i, ArrayList<Expression> ex){
		id=i;
		param=ex;
	}
	
	
	@Override
	public void display(int k) {
		// TODO Auto-generated method stub
		
	}
	
}

class Block extends Statement {
    // Block = Statement*
    //         (a Vector of members)
    public Statements members = new Statements();

    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Display Block (for Statements)");
	//array display look up list array in the API
	for (int i = 0; i < members.size(); i++)
		members.get(i).display(k);
    }
}

class Assignment extends Statement {
    // Assignment = Variable target; Expression source
    Variable target;
    Expression source;

    Assignment (Variable t, Expression e) {
        target = t;
        source = e;
    }
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Assignement :");
	target.display(++k);
	source.display(++k);
    }

}

class Conditional extends Statement {
// Conditional = Expression test; Statement thenbranch, elsebranch

    Expression test;
    Statement thenbranch, elsebranch;
    // elsebranch == null means "if... then"
    
    Conditional (Expression t, Statement tp) {
        test = t; thenbranch = tp; elsebranch = new Skip( );
    }
    
    Conditional (Expression t, Statement tp, Statement ep) {
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

class Loop extends Statement {
// Loop = Expression test; Statement body
    Expression test;
    Statement body;

    Loop (Expression t, Statement b) {
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

abstract class Expression {
    // Expression = Variable | Value | Binary | Unary
    public void display(int k) {
    }

}

class Variable extends Expression {
    // Variable = String id
    private String id;

    Variable (String s) { id = s; }

    public String toString( ) { return id; }
    
    public boolean equals (Object obj) {
        String s = ((Variable) obj).id;
        return id.equals(s); // case-sensitive identifiers
    }
    
    public int hashCode ( ) { return id.hashCode( ); }
    
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("Variable " + id);
    }
}

abstract class Value extends Expression {
    // Value = IntValue | BoolValue |
    //         CharValue | FloatValue
    protected Type type;
    protected boolean undef = true;

    int intValue ( ) {
        assert false : "should never reach here";
        return 0;
    }
    
    boolean boolValue ( ) {
        assert false : "should never reach here";
        return false;
    }
    
    char charValue ( ) {
        assert false : "should never reach here";
        return ' ';
    }
    
    float floatValue ( ) {
        assert false : "should never reach here";
        return 0.0f;
    }

    boolean isUndef( ) { return undef; }

    Type type ( ) { return type; }

    static Value mkValue (Type type) {
        if (type == Type.INT) return new IntValue( );
        if (type == Type.BOOL) return new BoolValue( );
        if (type == Type.CHAR) return new CharValue( );
        if (type == Type.FLOAT) return new FloatValue( );
        if (type == Type.BIGINT) return new BigIntValue();
        throw new IllegalArgumentException("Illegal type in mkValue");
    }	    
/*public void display() {
	System.out.println("Display Value Object");
    }*/
}

class BigIntValue extends Value{
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


class IntValue extends Value {
    private int value = 0;

    IntValue ( ) { type = Type.INT; }

    IntValue (int v) { this( ); value = v; undef = false; }

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

class BoolValue extends Value {
    private boolean value = false;

    BoolValue ( ) { type = Type.BOOL; }

    BoolValue (boolean v) { this( ); value = v; undef = false; }

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

class CharValue extends Value {
    private char value = ' ';

    CharValue ( ) { type = Type.CHAR; }

    CharValue (char v) { this( ); value = v; undef = false; }

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

class FloatValue extends Value {
    private float value = 0;

    FloatValue ( ) { type = Type.FLOAT; }

    FloatValue (float v) { this( ); value = v; undef = false; }

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

}

class Binary extends Expression {
// Binary = Operator op; Expression term1, term2
    Operator op;
    Expression term1, term2;

    Binary (Operator o, Expression l, Expression r) {
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


class Unary extends Expression {
    // Unary = Operator op; Expression term
    Operator op;
    Expression term;

    Unary (Operator o, Expression e) {
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

class Operator {
    // Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
    // BooleanOp = && | ||
    final static String AND = "&&";
    final static String OR = "||";
    // RelationalOp = < | <= | == | != | >= | >
    final static String LT = "<";
    final static String LE = "<=";
    final static String EQ = "==";
    final static String NE = "!=";
    final static String GT = ">";
    final static String GE = ">=";
    // ArithmeticOp = + | - | * | /
    final static String PLUS = "+";
    final static String MINUS = "-";
    final static String TIMES = "*";
    final static String DIV = "/";
    // UnaryOp = !    
    final static String NOT = "!";
    final static String NEG = "-";
    // CastOp = int | float | char
    final static String INT = "int";
    final static String FLOAT = "float";
    final static String CHAR = "char";
    // Typed Operators
    // RelationalOp = < | <= | == | != | >= | >
    final static String INT_LT = "INT<";
    final static String INT_LE = "INT<=";
    final static String INT_EQ = "INT==";
    final static String INT_NE = "INT!=";
    final static String INT_GT = "INT>";
    final static String INT_GE = "INT>=";
    // ArithmeticOp = + | - | * | /
    final static String INT_PLUS = "INT+";
    final static String INT_MINUS = "INT-";
    final static String INT_TIMES = "INT*";
    final static String INT_DIV = "INT/";
    // UnaryOp = !    
    final static String INT_NEG = "-";
    // RelationalOp = < | <= | == | != | >= | >
    final static String FLOAT_LT = "FLOAT<";
    final static String FLOAT_LE = "FLOAT<=";
    final static String FLOAT_EQ = "FLOAT==";
    final static String FLOAT_NE = "FLOAT!=";
    final static String FLOAT_GT = "FLOAT>";
    final static String FLOAT_GE = "FLOAT>=";
    // ArithmeticOp = + | - | * | /
    final static String FLOAT_PLUS = "FLOAT+";
    final static String FLOAT_MINUS = "FLOAT-";
    final static String FLOAT_TIMES = "FLOAT*";
    final static String FLOAT_DIV = "FLOAT/";
    // UnaryOp = !    
    final static String FLOAT_NEG = "-";
    // RelationalOp = < | <= | == | != | >= | >
    final static String CHAR_LT = "CHAR<";
    final static String CHAR_LE = "CHAR<=";
    final static String CHAR_EQ = "CHAR==";
    final static String CHAR_NE = "CHAR!=";
    final static String CHAR_GT = "CHAR>";
    final static String CHAR_GE = "CHAR>=";
    // RelationalOp = < | <= | == | != | >= | >
    final static String BOOL_LT = "BOOL<";
    final static String BOOL_LE = "BOOL<=";
    final static String BOOL_EQ = "BOOL==";
    final static String BOOL_NE = "BOOL!=";
    final static String BOOL_GT = "BOOL>";
    final static String BOOL_GE = "BOOL>=";
    // Type specific cast
    final static String I2F = "I2F";
    final static String F2I = "F2I";
    final static String C2I = "C2I";
    final static String I2C = "I2C";
    
    String val;
    
    Operator (String s) { val = s; }

    public String toString( ) { return val; }
    public boolean equals(Object obj) { return val.equals(obj); }
    
    boolean BooleanOp ( ) { return val.equals(AND) || val.equals(OR); }
    boolean RelationalOp ( ) {
        return val.equals(LT) || val.equals(LE) || val.equals(EQ)
            || val.equals(NE) || val.equals(GT) || val.equals(GE);
    }
    boolean ArithmeticOp ( ) {
        return val.equals(PLUS) || val.equals(MINUS)
            || val.equals(TIMES) || val.equals(DIV);
    }
    boolean NotOp ( ) { return val.equals(NOT) ; }
    boolean NegateOp ( ) { return val.equals(NEG) ; }
    boolean intOp ( ) { return val.equals(INT); }
    boolean floatOp ( ) { return val.equals(FLOAT); }
    boolean charOp ( ) { return val.equals(CHAR); }

    final static String intMap[ ] [ ] = {
        {PLUS, INT_PLUS}, {MINUS, INT_MINUS},
        {TIMES, INT_TIMES}, {DIV, INT_DIV},
        {EQ, INT_EQ}, {NE, INT_NE}, {LT, INT_LT},
        {LE, INT_LE}, {GT, INT_GT}, {GE, INT_GE},
        {NEG, INT_NEG}, {FLOAT, I2F}, {CHAR, I2C}
    };

    final static String floatMap[ ] [ ] = {
        {PLUS, FLOAT_PLUS}, {MINUS, FLOAT_MINUS},
        {TIMES, FLOAT_TIMES}, {DIV, FLOAT_DIV},
        {EQ, FLOAT_EQ}, {NE, FLOAT_NE}, {LT, FLOAT_LT},
        {LE, FLOAT_LE}, {GT, FLOAT_GT}, {GE, FLOAT_GE},
        {NEG, FLOAT_NEG}, {INT, F2I}
    };

    final static String charMap[ ] [ ] = {
        {EQ, CHAR_EQ}, {NE, CHAR_NE}, {LT, CHAR_LT},
        {LE, CHAR_LE}, {GT, CHAR_GT}, {GE, CHAR_GE},
        {INT, C2I}
    };

    final static String boolMap[ ] [ ] = {
        {EQ, BOOL_EQ}, {NE, BOOL_NE}, {LT, BOOL_LT},
        {LE, BOOL_LE}, {GT, BOOL_GT}, {GE, BOOL_GE},
        {AND, AND}, {OR, OR}, {NOT, NOT} 
    };

    final static private Operator map (String[][] tmap, String op) {
        for (int i = 0; i < tmap.length; i++)
            if (tmap[i][0].equals(op))
                return new Operator(tmap[i][1]);
        assert false : "should never reach here";
        return null;
    }

    final static public Operator intMap (String op) {
        return map (intMap, op);
    }

    final static public Operator floatMap (String op) {
        return map (floatMap, op);
    }

    final static public Operator charMap (String op) {
        return map (charMap, op);
    }

    final static public Operator boolMap (String op) {
        return map (boolMap, op);
    }
    public void display(int k) {
	System.out.println(val);
    }
}

