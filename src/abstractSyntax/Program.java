package abstractSyntax;

import java.util.HashMap;

import sementic.TypeManager;

public class Program {
    // Program = Declarations decpart ; Funcs funcs; MainFunc mainFunc; 
    public Declarations decpart;
    public Funcs funcs;
    public MainFunc mainFunc;

    public Program (Declarations d, Funcs f, MainFunc m){
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
    
    public void validCheck(){
    	
    	TypeManager.getInstance().setGlobalVariable(decpart);
        funcs.validCheck();
        mainFunc.validCheck();
    }   
    
}
