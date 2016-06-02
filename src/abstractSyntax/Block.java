package abstractSyntax;


public class Block extends Statement {
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