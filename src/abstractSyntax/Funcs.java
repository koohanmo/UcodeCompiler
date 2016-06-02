package abstractSyntax;

import java.util.ArrayList;



public class Funcs extends ArrayList<Funcs>{
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
