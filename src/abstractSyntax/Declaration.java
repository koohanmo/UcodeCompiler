package abstractSyntax;

public abstract class Declaration 
{
	//VariableDecl | ArrayDecl
	abstract void display(int k);
	
	public abstract String getId();
}