package lexer;


import java.io.*;

public class Lexer {
		
	private char ch = ' '; 
	private BufferedReader input;
	private String line = "";
	private int lineno = 1;
	private int col = 1;
	private final String letters = "abcdefghijklmnopqrstuvwxyz"
+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+".$";
	
	private final String digits = "0123456789";
	private final char eolnCh = '\n';
	private final char eofCh = '\004';
	
	public Lexer (String fileName) { // source filename //������ ������	
		try {
			input = new BufferedReader (new FileReader(fileName));
			//���� �����ͼ� ���� input�� �ֱ�
		}
		catch (FileNotFoundException e) {   
			System.out.println("File not found: " + fileName);
			System.exit(1);
		}  	
	}
	
	private char nextChar() { // Return next char	
		if (ch == eofCh)    //������ ���� ���ڰ�����
			error("Attempt to read past end of file");
		col++;
		if (col >= line.length()) {  //col�� ������ ���̸� �Ѿ�� ���� ������ �д´�.
			try {
				line = input.readLine( );  //���� �б�
			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			} // try
			
			if (line == null) // at end of file
				line = "" + eofCh;      //���� ������ ���̶�°��� ǥ���ϱ� ���� ����
			else {
				System.out.println("Line "+lineno);  //���� �ѹ��� �ް���
				lineno++;
				line += eolnCh;   //������ ���� \n�� �����༭ ���� �ٷγѾ
			} // if line
			col = 0;
		} // if col
		return line.charAt(col);	
	}
	public Token next( ) { // Return next token
		
		do {
			if(isLetter(ch)){
				String spelling= concat(letters+digits);
				return Token.keyword(spelling);
			} else if (isDigit(ch)) { // int or float literal
				String number = concat(digits);
				if (ch != '.') // int Literal
					return Token.mkIntLiteral(number);
				number += concat(digits);
				return Token.mkFloatLiteral(number);
				
			} else switch (ch) {
			case ' ': case '\t': case '\r': case eolnCh:
				ch = nextChar();
				break;
			case '/': // divide or comment
				ch = nextChar();
				if (ch != '/') return Token.divideTok;  // �����ð� 1���϶� ������	
// comment
				do {                         //�����ð� �ΰ� �̸� �������� \n�� ���ö����� �׳ɳѾ
					ch = nextChar();
				} while (ch != eolnCh);
				ch = nextChar();      //�׷��� ���� ����
				break;
			case '\'': // char literal
				char ch1 = nextChar();     //��������ǥ�� ����ϰ������
				nextChar(); // get '
				ch = nextChar();
				return Token.mkCharLiteral("" + ch1);
			case eofCh: return Token.eofTok;
			case ',': ch= nextChar();return Token.commaTok;		
			case '(': ch= nextChar(); return Token.leftParenTok;
			case ')': ch= nextChar();return Token.rightParenTok;
			case '{': ch= nextChar();return Token.leftBraceTok;
			case '[': ch= nextChar(); return Token.leftBracketTok;
			case ']': ch= nextChar(); return Token.rightBracketTok;
			case '}': ch= nextChar(); return Token.rightBraceTok;
			case ';': ch= nextChar(); return Token.semicolonTok;
			case '%': ch= nextChar(); return Token.modTok;
			case '!': ch= nextChar(); return Token.notTok;
			case '*': return chkOpt('*',Token.multiplyTok, Token.powTok);
			case '<': return chkOpt('=', Token.ltTok, Token.lteqTok);
			case '>': return chkOpt('=', Token.gtTok, Token.gteqTok);
			case '=': return chkOpt('=', Token.assignTok, Token.eqeqTok);
			case '-': return chkOpt('-',Token.arrowTok,Token.decTok, Token.minusTok);
			case '+': return chkOpt('+',Token.plusTok, Token.incTok);
			default: error("Illegal character " + ch); 
			} // switch
		} while (true);
		
	} // next
	
	private boolean isLetter(char c) {
		return (c>='a' && c<='z' || c>='A' && c<='Z');	
	}
	
	private boolean isDigit(char c) {	
		return (c>='0' && c<='9'); // student exercise
		
	}
	
	
	
	private void check(char c) {
		ch = nextChar();
		if (ch != c) //������ ���� ��ū�� c�� ���� ���� �� �����߻�
			error("Illegal character, expecting " + c);
		ch = nextChar();	
	}
	
	private Token chkOpt(char c, Token one, Token two) {
		ch = nextChar();
		
		if(ch != c){        // =���������� ��ū�� =�� �ƴ� ��� assign 
			return one;	
		}
		else {              //  ==�� ���
			ch = nextChar();
			return two;	
		}	
	}
	
	private Token chkOpt(char c, Token one, Token two, Token three){
		ch=nextChar();
		if(ch =='>'){
			ch=nextChar();
			return one; 
		}
		else if(ch ==c)
		{
			ch=nextChar();
			return two;
		}
		else 
		{
			return three;
		}
		
		
	}
	
	private String concat(String set) {       //letter�� digit string�� ������
		String r = "";
		do {
			r += ch;
			ch = nextChar();
		} while (set.indexOf(ch) >= 0);
		return r;	
	}
	
	public void error (String msg) {
		System.err.print(line);
		System.err.println("Error: column " + col + " " + msg);
		System.exit(1);	
	}
	
	static public void main ( String[] argv ) {
		Lexer lexer = new Lexer("test.txt");
		Token tok = lexer.next( );
		while (tok != Token.eofTok) {
			System.out.println(tok.toString());
			tok = lexer.next( );
		} 
	} // main	
}
