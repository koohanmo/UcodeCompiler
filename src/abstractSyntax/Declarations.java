package abstractSyntax;

import java.util.ArrayList;


public class Declarations extends ArrayList<Declaration> {
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