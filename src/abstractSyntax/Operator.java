package abstractSyntax;

public class Operator {
    // Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
    // BooleanOp = && | ||
    final static String AND = "and";
    final static String OR = "or";
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
    final static String POW="**";
    final static String MOD="%";
    final static String DIV = "/";
    // UnaryOp = !    
    final static String NOT = "!";
    final static String NEG = "-";
    //Increament, Decrement
    final static String INC ="++";
    final static String DEC ="--";
    
    final static String PLUSASSIGN = "+=";
	final static String MINUSASSIGN = "-=";
	final static String TIMESASSIGN = "*=";
	final static String DIVASSIGN = "/=";
	final static String MODASSIGN = "%=";
	final static String ASSIGN = "=";
    // CastOp = int | float | char
    final static String INT = "int";
    final static String FLOAT = "float";
    final static String CHAR = "char";
    final static String BIGINT ="bigint";
    final static String BOOLEAN ="boolean";
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
    
    final static String INT_PLUS_ASSIGN = "INT+=";
	final static String INT_MINUS_ASSIGN = "INT-=";
	final static String INT_TIMES_ASSIGN = "INT*=";
	final static String INT_DIV_ASSIGN = "INT/=";
	final static String INT_MOD_ASSIGN = "INT%=";
	final static String INT_ASSIGN = "INT=";
	
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
    
	final static String FLOAT_PLUS_ASSIGN = "FLOAT+=";
	final static String FLOAT_MINUS_ASSIGN = "FLOAT-=";
	final static String FLOAT_TIMES_ASSIGN = "FLOAT*=";
	final static String FLOAT_DIV_ASSIGN = "FLOAT/=";
	final static String FLOAT_MOD_ASSIGN = "FLOAT%=";
	final static String FLOAT_ASSIGN = "FLOAT=";
	
	
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
    
    public Operator (String s) { val = s; }

    public String toString( ) { return val; }
    public boolean equals(Object obj) { return val.equals(obj); }
    
    public boolean BooleanOp ( ) { return val.equals(AND) || val.equals(OR); }
    public boolean RelationalOp ( ) {
        return val.equals(LT) || val.equals(LE) || val.equals(EQ)
            || val.equals(NE) || val.equals(GT) || val.equals(GE);
    }
    public boolean ArithmeticOp ( ) {
        return val.equals(PLUS) || val.equals(MINUS)
            || val.equals(TIMES) || val.equals(DIV)||val.equals(POW) ||val.equals(MOD);
    }
    
    public boolean IncOp () {return val.equals(INC);}
    public boolean DecOp () {return val.equals(DEC);}
    public boolean NotOp ( ) { return val.equals(NOT) ; }
    public boolean NegateOp ( ) { return val.equals(NEG) ; }
    
    public boolean CastOp(){
    	return val.equals(INT) || val.equals(CHAR) || val.equals(FLOAT)
    			||val.equals(BOOLEAN) || val.equals(BIGINT);
    }
    
    public boolean intOp ( ) { return val.equals(INT); }
    public boolean floatOp ( ) { return val.equals(FLOAT); }
    public boolean charOp ( ) { return val.equals(CHAR); }
    public boolean boolOp ( ) { return val.equals(BOOLEAN); }
    public boolean bigIntOp ( ) { return val.equals(BIGINT); }

    final static String intMap[ ] [ ] = {
        {PLUS, INT_PLUS}, {MINUS, INT_MINUS},
        {TIMES, INT_TIMES}, {DIV, INT_DIV},
        {EQ, INT_EQ}, {NE, INT_NE}, {LT, INT_LT},
        {LE, INT_LE}, {GT, INT_GT}, {GE, INT_GE},
        {NEG, INT_NEG}, {FLOAT, I2F}, {CHAR, I2C},
        {ASSIGN, INT_PLUS_ASSIGN} ,{MINUSASSIGN, INT_MINUS_ASSIGN},
        {TIMESASSIGN, INT_TIMES_ASSIGN}, { DIVASSIGN,INT_DIV_ASSIGN},
        {MODASSIGN,INT_MOD_ASSIGN},{ASSIGN,INT_ASSIGN}
        
    };

    final static String floatMap[ ] [ ] = {
        {PLUS, FLOAT_PLUS}, {MINUS, FLOAT_MINUS},
        {TIMES, FLOAT_TIMES}, {DIV, FLOAT_DIV},
        {EQ, FLOAT_EQ}, {NE, FLOAT_NE}, {LT, FLOAT_LT},
        {LE, FLOAT_LE}, {GT, FLOAT_GT}, {GE, FLOAT_GE},
        {NEG, FLOAT_NEG}, {INT, F2I},
        {ASSIGN, FLOAT_PLUS_ASSIGN} ,{MINUSASSIGN, FLOAT_MINUS_ASSIGN},
        {TIMESASSIGN, FLOAT_TIMES_ASSIGN}, { DIVASSIGN,FLOAT_DIV_ASSIGN},
        {MODASSIGN,FLOAT_MOD_ASSIGN},{ASSIGN,FLOAT_ASSIGN}
    };

    final static String charMap[ ] [ ] = {
        {EQ, CHAR_EQ}, {NE, CHAR_NE}, {LT, CHAR_LT},
        {LE, CHAR_LE}, {GT, CHAR_GT}, {GE, CHAR_GE},
        {INT, C2I},
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