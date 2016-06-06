
package sementic;

import abstractSyntax.Program;
import lexer.Lexer;
import parser.Parser;

public class TypeChecker {
		
		Parser parser;
	
		public TypeChecker(Parser p){
			parser=p;
		}
		
		public Program check(){
			Program p = parser.program();
			p.validCheck();
			return p;       //abstract tree에 program을 반환
		}

	
		public static void main(String[] args) {
			TypeChecker typeChecker = new TypeChecker(new Parser(new Lexer("test.txt")));
			Program Program = typeChecker.check();
			
		}
	

}
