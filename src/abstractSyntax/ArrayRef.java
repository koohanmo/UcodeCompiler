package abstractSyntax;



public class ArrayRef extends VariableRef
{
	public String id;
	public Expression index;
	
	public ArrayRef(String id, Expression index)
	{
		this.id=id;
		this.index = index;
	}
	
    public void display(int k) {
        for (int w = 0; w < k; ++w) {
            System.out.print("\t");
        }
	System.out.println("ArrayRef " + id);
	index.display(k++);
    }

	@Override
	public String getId() {
		return id;
	}
}
