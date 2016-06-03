package codeGenerator;

import lexer.Lexer;
import parser.Parser;
import sementic.TypeChecker;

public class CodeGenerator {
	
	TypeChecker typeChecker;
	
	public CodeGenerator(TypeChecker t){
		this.typeChecker=t;
	}


}
