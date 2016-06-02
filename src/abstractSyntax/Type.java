package abstractSyntax;

import java.util.ArrayList;


public class Type {
    // Type = int | bool | char | float |bigint
    public final static Type INT = new Type("int");
    public final static Type BOOL = new Type("bool");
    public final static Type CHAR = new Type("char");
    public final static Type FLOAT = new Type("float");
    public final static Type BIGINT = new Type("bigint");
    
    private String id;
    private Type (String t) { id = t; }
    public String toString ( ) { return id; }
}

