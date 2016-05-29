package parser;

import java.util.ArrayList;

import lexer.*;

public class Parser {

    Token token;          // current token from the input stream
    Lexer lexer;
  
    public Parser(Lexer ts) { // Open the C++Lite source program
        lexer = ts;                          // as a token stream, and
        token = lexer.next();            // retrieve its first Token
    }
    private String match (TokenType t) {
        String value = token.value();
        if (token.type().equals(t))
            token = lexer.next();
        else
            error(t);
        return value;
    }
    private void error(TokenType tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token + " ");
        System.exit(1);
    }
    private void error(String tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token+ " ");
        System.exit(1);
    }
    public Program program() {
        // Program --> Declarations Funcs MainFunc
	Declarations decs = declarations();
	Funcs fs = funcs();
	MainFunc mF = mainFunc();
	return new Program(decs, fs, mF);  // student exercise
    }
    
    
    private Funcs funcs(){
    	Funcs fs = new Funcs();
    	match(TokenType.Function);
    	while(isType() || isVoid()){
    		Funcs f;
    		if(isVoid())
    			f=voidfunc(); 
    		else f = func();
    		fs.add(f);
    	}
    	return fs;
    }
    
    private Func func(){
    	//function Type Identifier ->{Declarations}{Declarations Statements  Return}

    	Type t = type();
    	String  id = match(TokenType.Id);
    	match(TokenType.Arrow);
    	match(TokenType.LeftBrace);
    	Declarations arg = declarations();
    	match(TokenType.RightBrace);
    	match(TokenType.LeftBrace);
    	Declarations dec = declarations();
    	Statements b = statements();
    	Expression rE = expression();
    	match(TokenType.RightBrace);
    	
    	return new Func(t,id,arg,dec,b,rE);
    }
    private voidFunc voidfunc(){
  
    	match(TokenType.Void);
    	String  id = match(TokenType.Id);
    	match(TokenType.Arrow);
    	match(TokenType.LeftBrace);
    	Declarations arg = declarations();
    	match(TokenType.RightBrace);
    	match(TokenType.LeftBrace);
    	Declarations dec = declarations();
    	Statements b = statements();
    	match(TokenType.RightBrace);
    	
    	return new voidFunc(id,arg,dec,b);
    }
    
    private MainFunc mainFunc(){
    	
    	Declarations decpart = declarations();
    	Statements body = statements();
    	return new MainFunc(decpart,body);
    }
    
    private Declarations declarations() {
        // Declarations --> { Declaration }
	Declarations ds = new Declarations(); 
	while (isType()){
		declaration(ds);
	}
        return ds;
    }
  
    private void declaration (Declarations ds) {
	Type t = type();
	Variable v= new Variable(match(TokenType.Id));
	Declaration d;
	//배열일 경우
	if(isLeftBracket()) 
		d= arrayDecl(t,v);
	else
		d = new VariableDecl(v, t);
	
	ds.add(d);
	while(isComma())
	{
		token = lexer.next();
		Variable v2= new Variable(match(TokenType.Id));
		Declaration d2;
		//배열일 경우
		if(isLeftBracket()) 
			d2= arrayDecl(t,v2);
		else
			d2 = new VariableDecl(v2, t);
		
		ds.add(d2);
	}
	
	match(TokenType.Semicolon);
	}
  

    
    
    private ArrayDecl arrayDecl(Type t, Variable v){
		token=lexer.next(); //왼쪽 [ 제거
		int size = Integer.parseInt(match(TokenType.IntLiteral));
		if(!isRightBracket())
			error(TokenType.RightBracket);
		else
			token=lexer.next();
		return new ArrayDecl(v,t,size);
	}
    
    private Type type () {
        // Type  -->  int | bool | float | char | bigint
	Type t = null;
	if (token.type().equals(TokenType.Int)) {
            t = Type.INT;		
//			System.out.println(" Type  is int");
	} else if (token.type().equals(TokenType.Bool)) {
			t = Type.BOOL;
//			System.out.println(" Type  is bool");
	} else if (token.type().equals(TokenType.Float)) {
			t = Type.FLOAT;
//			System.out.println(" Type  is float");
	} else if (token.type().equals(TokenType.Char)) {
			t = Type.CHAR;
//			System.out.println(" Type  is char");
	} else if (token.type().equals(TokenType.BigInteger)){
			t= Type.BIGINT;
	}else error ("Error in Type construction");
	token = lexer.next();
	return t;          
    }
    
    private Statements statements(){
    	Statements s = new Statements();
		Statement st;
		while (isStatement()) {
			st = statement();
			s.add(st);
		}
	    	return s;
    }
    
  
    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement | for | voidFuncCall
	Statement s = null;
//	System.out.println("starting statment()");
	if (token.type().equals(TokenType.Semicolon))
		s = new Skip();
	else if (token.type().equals(TokenType.LeftBrace)) //block
		s = progstatements();
//		System.out.println("block data " + s);}
	else if (token.type().equals(TokenType.If))    //if
		s = ifStatement();
	else if (token.type().equals(TokenType.While)) //while
		s = whileStatement();
	else if (token.type().equals(TokenType.Id)) 
		s = assignment();
	else if (token.type().equals(TokenType.For))
		s = forStatement();
	else if (token.type().equals(TokenType.Call))
		s = voidFuncCall();
	else error("Error in Statement construction");
        return s;
    }
    
	private Block progstatements( ) {
        // Block --> '{' Statements '}'
	match(TokenType.LeftBrace);
	Block b = new Block();
	Statement s;
	while (isStatement()) {
		s = statement();
		b.members.add(s);
	}
      match(TokenType.RightBrace);
        return b;
    }
  
    private Assignment assignment( ) {
        // Assignment --> Id = Expression ;
//	System.out.println("Starting assignment()");
	Expression source; 
	Variable target; 
	
	target = new Variable(match(TokenType.Id));
	match(TokenType.Assign);
	source = expression();
	match(TokenType.Semicolon);
        return new Assignment(target, source); 
    }
    
    private ForLoop forStatement()
    {
    	Expression first,test,third;
    	Statement body;
    	
    	match(TokenType.For);
    	match(TokenType.LeftParen);
    	first = expression();
    	match(TokenType.Semicolon);
    	test = expression();
    	match(TokenType.Semicolon);
    	third = expression();
    	match(TokenType.RightParen);
    	body = statement();
    	return new ForLoop(first,test,third,body);
    }
    
    private FuncCall funcCall()
    {
    	match(TokenType.Call);
    	String id = match(TokenType.Id);
    	ArrayList<Expression> param= new ArrayList<Expression>();
    	match(TokenType.LeftParen);
    	do{
    		Expression e = expression();
    		token = lexer.next();
    	}while(isComma());
    	match(TokenType.RightParen);
    	return new FuncCall(id,param);
    	
    }
  
    private voidFuncCall voidFuncCall()
    {
    	match(TokenType.Call);
    	String id = match(TokenType.Id);
    	ArrayList<Expression> param= new ArrayList<Expression>();
    	match(TokenType.LeftParen);
    	do{
    		Expression e = expression();
    		token = lexer.next();
    	}while(isComma());
    	match(TokenType.RightParen);
    	return new voidFuncCall(id,param);
    	
    }
    
    private Conditional ifStatement() {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
	Conditional con;
	Statement s;
	Expression test;
	
	match(TokenType.If);
	match(TokenType.LeftParen);
	test = expression();
	match(TokenType.RightParen);
	s = statement();
	
	if (token.type().equals(TokenType.Else)) {
		match(TokenType.Else);
		Statement elsestate = statement();
		con = new Conditional(test, s, elsestate);
	}
	else {
		con = new Conditional(test, s);
	}
	return con;
    }
  
    private Loop whileStatement() {
        // WhileStatement --> while ( Expression ) Statement
  	Statement body;
	Expression test;

	match(TokenType.While);
	match(TokenType.LeftParen);
	test = expression();
	match(TokenType.RightParen);
	body = statement();
	return new Loop(test, body);
	
    }

    private Expression expression() {
        // Expression --> Conjunction { || Conjunction }
//	System.out.println("expression() start");
	Expression c = conjunction();
	while (token.type().equals(TokenType.Or)) {
		Operator op = new Operator(match(token.type()));
		Expression e = expression();
		c = new Binary(op, c, e);
	}
	return c;  // student exercise
    }
  
    private Expression conjunction() {
        // Conjunction --> Equality { && Equality }
//	System.out.println("coonjunction() start");
	Expression eq = equality();
	while (token.type().equals(TokenType.And)) {
		Operator op = new Operator(match(token.type()));
		Expression c = conjunction();
		eq = new Binary(op, eq, c);
	}
        return eq;  
    }
  
    private Expression equality() {
        // Equality --> Relation [ EquOp Relation ]
//	System.out.println("equality() start");
	Expression rel = relation();
	while (isEqualityOp()) {
		Operator op = new Operator(match(token.type()));
		Expression rel2 = relation();
		rel = new Binary(op, rel, rel2);
	}
        return rel;  // student exercise
    }

    private Expression relation() {
        // Relation --> Addition [RelOp Addition] 
//	System.out.println("relation() start");
	Expression a = addition();
	while (isRelationalOp()) {
		Operator op = new Operator(match(token.type()));
		Expression a2 = addition();
		a = new Binary(op, a, a2);
	}
        return a;  // student exercise
    }
  
    private Expression addition() {
        // Addition --> Term { AddOp Term }
//	System.out.println("addition() start");
        Expression e = term();
        while (isAddOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = term();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression term() {
        // Term --> Factor { MultiplyOp Factor }
//	System.out.println("term() start");
        Expression e = factor();
        while (isMultiplyOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = factor();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary 
//	System.out.println("factor() start");
        if (isUnaryOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term = primary();
             return new Unary(op, term);
        }
        else return primary();
    }
  
    private Expression primary () {
        // Primary --> Id | Literal | ( Expression )
        //             | Type ( Expression )
//	System.out.println("(primary) start");
        Expression e = null;
        if (token.type().equals(TokenType.Id)) {
            e = new Variable(match(TokenType.Id));
        } else if (isLiteral()) {
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) {
            token = lexer.next();
            e = expression();       
            match(TokenType.RightParen);
        } else if (isType( )) {
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen);
            Expression term = expression();
            match(TokenType.RightParen);
            e = new Unary(op, term);
        } else error("Id | Literal | ( | Type");
        return e;
    }

    private Value literal( ) { // take the stringy part and convert it to the correct return new  typed value. cast it to the correct value
	Value value = null;
	String stval = token.value();
	if (token.type().equals(TokenType.IntLiteral)) {
		value = new IntValue (Integer.parseInt(stval));
		token = lexer.next();
//		System.out.println("found int literal");
	}
	else if (token.type().equals(TokenType.FloatLiteral))  {
		value = new FloatValue(Float.parseFloat(stval));
		token = lexer.next();
	}
	else if (token.type().equals(TokenType.CharLiteral))  {
		value = new CharValue(stval.charAt(0));
		token = lexer.next();
	}
    else if (token.type().equals(TokenType.True))  {
        value = new BoolValue(true);
        token = lexer.next();
    }
    else if (token.type().equals(TokenType.False))  {
        value = new BoolValue(false);
        token = lexer.next();
    }
     else error ("Error in literal value contruction");
	return value;
    }
  
    private boolean isBooleanOp() {
	return token.type().equals(TokenType.And) || 
	    token.type().equals(TokenType.Or);
    } 

    private boolean isAddOp( ) {
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isMultiplyOp( ) {
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide)||
               token.type().equals(TokenType.Mod)||
               token.type().equals(TokenType.Pow);
    }
    
    private boolean isUnaryOp( ) {
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus)||
               token.type().equals(TokenType.Increment)||
               token.type().equals(TokenType.Decrement);
    }
    
    private boolean isEqualityOp( ) {
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }
    
    private boolean isRelationalOp( ) {
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) || 
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }
    
    private boolean isType( ) {
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool) 
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char)
        	|| token.type().equals(TokenType.BigInteger);
    }
    
    private boolean isVoid(){
    	return token.type().equals(TokenType.Void);
    }
    
    private boolean isLiteral( ) {
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }
    
    private boolean isBooleanLiteral( ) {
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }

    private boolean isComma( ) {
	return token.type().equals(TokenType.Comma);
    }
   
    private boolean isSemicolon( ) {
	return token.type().equals(TokenType.Semicolon);
    }

    private boolean isLeftBrace() {
	return token.type().equals(TokenType.LeftBrace);
    } 
 
    private boolean isRightBrace() {
	return token.type().equals(TokenType.RightBrace);
    } 

    private boolean isStatement() {
	return 	isSemicolon() ||
		isLeftBrace() ||
		token.type().equals(TokenType.If) ||
		token.type().equals(TokenType.While) ||
		token.type().equals(TokenType.Id); 
    }
 
    private boolean isLeftBracket()
	{
		return 	token.type().equals(TokenType.LeftBracket); 
	}
	private boolean isRightBracket()
	{
		return token.type().equals(TokenType.RightBracket);
	}
    
    public static void main(String args[]) {
        Parser parser  = new Parser(new Lexer("test.txt"));
        Program prog = parser.program();
        prog.display(0);           // display abstract syntax tree
    } //main

} // Parser

