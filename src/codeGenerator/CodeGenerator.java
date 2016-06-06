package codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


import abstractSyntax.Program;
import lexer.Lexer;
import parser.Parser;
import sementic.TypeChecker;

public class CodeGenerator {
	
	protected static final HashMap<String, SymbolTableElement> globalSymbolTable = new HashMap<>();
	protected static final HashMap<String, SymbolTableElement> currentSymbolTable = new HashMap<>();

	private static boolean finishGlobalDec = false;
	private static int variableOffset = 0;
	
	
	private TypeChecker typeChecker;
	String outputFile;
	private static FileWriter writer;
	
	public CodeGenerator(TypeChecker t){
		this.typeChecker=t;
	}
	
	public CodeGenerator(String inputFile, String outPutFile) {
		typeChecker = new TypeChecker(new Parser(new Lexer(inputFile)));
		this.outputFile = outPutFile;

		try {
			writer = new FileWriter(new File(outPutFile));
		} catch (IOException e) {
			System.out.println("error in open or create file");
			e.printStackTrace();
		}
	}

	public void codeGenerate() {
	
		Program AST = typeChecker.check();
		AST.display(0);

		AST.genCode();

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("initialize error. must have two parameter");
			System.exit(1);
		}
		CodeGenerator codeGen = new CodeGenerator("test.txt", "uFile.uco");

		codeGen.codeGenerate();
	}

	
	protected static void genFunc(String name, int size, int block, int lexical) {
		StringBuilder result = new StringBuilder(name)
				.append("\tproc\t")
				.append(size).append(' ')
				.append(block).append(' ')
				.append(lexical).append("\r\n");

		try {
			writer.write(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void genCode(String instr, int... args) {
		StringBuilder result = new StringBuilder("\t").append(instr);

		if (args.length > 0) {
			result.append('\t');

			for (int arg : args) {
				result.append(arg).append(' ');
			}
		}
		result.append("\r\n");

		try {
			writer.write(result.toString());
		} catch (IOException e) {
			System.out.println("error in file write");
			e.printStackTrace();
		}
	}

	protected static void genLabel(String label) {
		StringBuilder result = new StringBuilder(label);

		result.append("\tnop\r\n");

		try {
			writer.write(result.toString());
		} catch (IOException e) {
			System.out.println("error in file write");
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	

	public static void notop() {
		genCode("notop");
	}

	public static void neg() {
		genCode("neg");
	}

	public static void inc() {
		genCode("inc");
	}

	public static void dec() {
		genCode("dec");
	}

	public static void dup() {
		genCode("dup");
	}

	public static void add() {
		genCode("add");
	}

	public static void sub() {
		genCode("sub");
	}

	public static void mult() {
		genCode("mult");
	}

	public static void div() {
		genCode("div");
	}

	public static void mod() {
		genCode("mod");
	}

	public static void swp() {
		genCode("swp");
	}

	public static void and() {
		genCode("and");
	}

	public static void or() {
		genCode("or");
	}

	public static void gt() {
		genCode("gt");
	}

	public static void lt() {
		genCode("lt");
	}

	public static void ge() {
		genCode("ge");
	}

	public static void le() {
		genCode("le");
	}

	public static void eq() {
		genCode("eq");
	}

	public static void ne() {
		genCode("ne");
	}

	public static void lod(String id) {
		SymbolTableElement element = currentSymbolTable.get(id);
		genCode("lod", element.getBlockNum(), element.getStartAddress());
	}
	
	public static void str(String id) {
		SymbolTableElement element = currentSymbolTable.get(id);
		genCode("str", element.getBlockNum(), element.getStartAddress());
	}

	public static void str(String id, int index) {
		SymbolTableElement element = currentSymbolTable.get(id);
		genCode("str", element.getBlockNum(), element.getStartAddress() + index);
	}

	public static void ldc(int val) {
		genCode("ldc", val);
	}

	public static void lda(String id) {
		SymbolTableElement element = currentSymbolTable.get(id);
		genCode("lda", element.getBlockNum(), element.getStartAddress());
	}

	public static void ujp(String label) {
		try {
			writer.write("\tujp\t" + label + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void tjp(String label) {
		try {
			writer.write("\ttjp\t" + label + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fjp(String label) {
		try {
			writer.write("\tfjp\t" + label + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void chkh() {
		// todo
		genCode("chkh");
	}

	public static void chkl() {
		// todo
		genCode("chkl");
	}

	public static void ldi() {
		genCode("ldi");
	}

	public static void sti() {
		genCode("sti");
	}

	public static void call(String label) {
		try {
			writer.write("\tcall\t" + label + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void ret() {
		genCode("ret");
	}

	public static void retv() {
		genCode("retv");
	}

	public static void ldp() {
		genCode("ldp");
	}

	public static void proc(String id, int size) {
		try {
			writer.write(id);
			genCode("proc", size, 2, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void end() {
		genCode("end");
	}

	public static void bgn() {
		genCode("bgn", variableOffset);
		finishGlobalDec = true;
	}

	public static void sym(String id, int block, int size) {
		genCode("sym", block, variableOffset + 1, size);  //sym 코드를 생성하고

		currentSymbolTable.put(id, new SymbolTableElement(block, variableOffset + 1, size)); //symbolTalble에 값을 넣어줌

		variableOffset += size;  //offset에다가 size 만큼 더함
	}


}
