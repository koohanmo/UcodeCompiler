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
	
	public Lexer (String fileName) { // source filename //렉서의 생성자	
		try {
			input = new BufferedReader (new FileReader(fileName)); 
			//파일 가져와서 버퍼 input에 넣기
		}
		catch (FileNotFoundException e) {   
			System.out.println("File not found: " + fileName);
			System.exit(1);
		}  	
	}
	
	private char nextChar() { // Return next char	
		if (ch == eofCh)    //다음에 읽을 문자가없음
			error("Attempt to read past end of file");
		col++;
		if (col >= line.length()) {  //col이 라인의 길이를 넘어가면 다음 라인을 읽는다.
			try {
				line = input.readLine( );  //힌줄 읽기
			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			} // try
			
			if (line == null) // at end of file
				line = "" + eofCh;      //끝을 만나면 끝이라는것을 표시하기 위해 넣음
			else {
				System.out.println("Line "+lineno);  //라인 넘버를 메겨줌
				lineno++;
				line += eolnCh;   //라인의 끝에 \n을 더해줘서 다음 줄로넘어감
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
				if (ch != '/') return Token.divideTok;  // 슬래시가 1개일때 나눗셈	
// comment
				do {                         //슬래시가 두개 이면 나머지는 \n이 나올때까지 그냥넘어감
					ch = nextChar();
				} while (ch != eolnCh);
				ch = nextChar();      //그러고 다음 문자
				break;
			case '\'': // char literal
				char ch1 = nextChar();     //작은따옴표를 출력하고싶을떄
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
			case '*': return chkOpt('*',Token.multiplyTok, Token.expTok);
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
		if (ch != c) //다음에 읽을 토큰이 c와 같지 않을 때 에러발생
			error("Illegal character, expecting " + c);
		ch = nextChar();	
	}
	
	private Token chkOpt(char c, Token one, Token two) {
		ch = nextChar();
		
		if(ch != c){        // =다음에오는 토큰이 =이 아닐 경우 assign 
			return one;	
		}
		else {              //  ==일 경우
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
	
	private String concat(String set) {       //letter와 digit string을 합쳐줌
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
