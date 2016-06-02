package abstractSyntax;

public abstract class Statement extends Statements{
    // Statement = Skip | Block | Assignment | Conditional | Loop | forLoop | voidFuncCall
    abstract public void display(int k);
}