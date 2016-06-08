package sementic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import abstractSyntax.ArrayDecl;
import abstractSyntax.ArrayRef;
import abstractSyntax.Assignment;
import abstractSyntax.Binary;
import abstractSyntax.Block;
import abstractSyntax.Conditional;
import abstractSyntax.Declaration;
import abstractSyntax.Declarations;
import abstractSyntax.Expression;
import abstractSyntax.ForLoop;
import abstractSyntax.Func;
import abstractSyntax.FuncCall;
import abstractSyntax.Funcs;
import abstractSyntax.Loop;
import abstractSyntax.MainFunc;
import abstractSyntax.Skip;
import abstractSyntax.Statement;
import abstractSyntax.Statements;
import abstractSyntax.Type;
import abstractSyntax.Unary;
import abstractSyntax.Value;
import abstractSyntax.Variable;
import abstractSyntax.VariableDecl;
import abstractSyntax.VariableRef;
import abstractSyntax.voidFunc;
import abstractSyntax.voidFuncCall;
import codeGenerator.DefinedFunction;

public class TypeManager {
	
	private static TypeManager instance;
	public HashMap<String, SymbolElement> globalVariables = new HashMap<String, SymbolElement>();
	public HashMap<String, Integer> globalArray = new HashMap<String, Integer>();
	public HashMap<String, FunctionDef> funcTable = new HashMap<String, FunctionDef>();
	public LinkedList<String> definedFuncTable = new LinkedList<String>();
	
	public TypeManager(){
		//기본함수 설정
		for(String f : DefinedFunction.definedFunc){
			definedFuncTable.add(f);
		}		
	}
	
	
	public static TypeManager getInstance(){
		if(instance==null) instance = new TypeManager();
		return instance;
	}
	
	public void typeError(String msg){
		System.err.println(msg);
		System.exit(0);
	}
	
	public static void clearTypeManager(){
		instance=null;
	}
	
	

	/*
	 *  �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 泥댄겕
	 */
	
	public void IdCheck(String id){
		if(definedFuncTable.contains(id)) typeError(id +": 이미 선언되어 있는 이름입니다.");
		if(globalVariables.containsKey(id))  typeError(id +": 이미 선언되어 있는 이름입니다.");
		if(funcTable.containsKey(id))typeError(id +": 이미 선언되어 있는 이름입니다.");
	}
	
	public void setGlobalVariable(Declarations decls){
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				IdCheck(current.v.id);
				globalVariables.put(current.v.id, new SymbolElement(current.t, Type.BIGINT.equals(current.t)? 100:1));
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				IdCheck(current.v.id);
				globalVariables.put(current.v.id, new SymbolElement(current.t, current.size));
				globalArray.put(current.v.id, current.size);
			}
		}
	}
	
	public void setLocalVariable(FunctionDef f, Declarations arg, Declarations decls){
		//set arg to localVariable
		if(arg!=null){
			for(Declaration d : arg){
				if(d instanceof VariableDecl){
					VariableDecl current = (VariableDecl)d;
					if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 이미 선언되어 있는 이름입니다.");
					IdCheck(current.v.id);
					f.addVariable(current.v.id, current.t, Type.BIGINT.equals(current.t)? 100:1);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 이미 선언되어 있는 이름입니다.");
					IdCheck(current.v.id);
					f.addVariable(current.v.id, current.t, current.size);
					f.addArray(current.v.id, current.size);
				}
			}
		}
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 이미 선언되어 있는 이름입니다.");
				IdCheck(current.v.id);
				f.addVariable(current.v.id, current.t, Type.BIGINT.equals(current.t)? 100:1);
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": 이미 선언되어 있는 이름입니다.");
				IdCheck(current.v.id);
				f.addVariable(current.v.id, current.t, current.size);
				f.addArray(current.v.id, current.size);
			}
		}
	}
	
	public void V(Funcs f){
		if(f instanceof Func){
			Func currentF = (Func)f;
			FunctionDef currenfDef = new FunctionDef(currentF.type, currentF.id);
			for(Declaration d : currentF.arguments){
				if(d instanceof VariableDecl){
					VariableDecl current = (VariableDecl)d;
					currenfDef.addParams(current.t ,Type.BIGINT.equals(current.t)? 100:1);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					currenfDef.addParams(current.t, current.size);
				}
			}
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
			V(currenfDef,currentF.returnExpr);
			if(funcTable.containsKey(currentF.id)) typeError(currenfDef.funcName + "  �뜝�뙃�눦�삕�뜝�룞�삕�뜝�룞�삕 �뜝�뙥釉앹삕�뜝�떎�뼲�삕�뜝�룞�삕�뜝�떦�뙋�삕.");  //�뜝�뜦蹂� �뜝�룞�삕�뜝�룞�삕�뜝�뙃�눦�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�뙏�빞�벝�삕
			else funcTable.put(currentF.id,currenfDef);
		}
		else if(f instanceof voidFunc){
			voidFunc currentF = (voidFunc)f;
			FunctionDef currenfDef = new FunctionDef(null, currentF.id);
			for(Declaration d : currentF.arguments){
				if(d instanceof VariableDecl){
					VariableDecl current = (VariableDecl)d;
					currenfDef.addParams(current.t,Type.BIGINT.equals(current.t)? 100:1);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					currenfDef.addParams(current.t,current.size);
				}
			}
			setLocalVariable(currenfDef,currentF.arguments,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey(currentF.id)) typeError(currentF.id + "  �뜝�뙃�눦�삕�뜝�룞�삕�뜝�룞�삕 �뜝�뙥釉앹삕�뜝�떎�뼲�삕�뜝�룞�삕�뜝�떦�뙋�삕.");  //�뜝�뜦蹂� �뜝�룞�삕�뜝�룞�삕�뜝�뙃�눦�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�뙏�빞�벝�삕
			else funcTable.put(currentF.id,currenfDef);
		}
		else{
			MainFunc currentF = (MainFunc)f;
			FunctionDef currenfDef = new FunctionDef(null, "main");
			setLocalVariable(currenfDef,null,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey("main")) typeError("main" + "  �뜝�뙃�눦�삕�뜝�룞�삕�뜝�룞�삕 �뜝�뙥釉앹삕�뜝�떎�뼲�삕�뜝�룞�삕�뜝�떦�뙋�삕.");  //�뜝�뜦蹂� �뜝�룞�삕�뜝�룞�삕�뜝�뙃�눦�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�뙏�빞�벝�삕
			else funcTable.put("main",currenfDef);
		}
	}
	
	private FunctionDef getFuncTable(String funcName){
		return funcTable.get(funcName);
	}
	
	//TODO Complete
	//function statements check
	public void V(FunctionDef f, Statements statements){
		if(statements.size()==0) return;
		for(Statement s : statements){
			V(f,s);
		}
	}
	
	public void V(FunctionDef f, Statement s){
		//Skip | Block | Assignment | Conditional | Loop | forLoop | voidFuncCall
		if(s==null) throw new IllegalArgumentException("AST error : null statement");
		if(s instanceof Skip) return;
		else if(s instanceof Assignment){
			//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�떎�뼲�삕�뜝�룞�삕�뜝�룞�삕, �뜝�룞�삕�뜝�룞�삕 type�뜝�룞�삕 �뜝�듅�뙋�삕�뜝�룞�삕 �솗�뜝�룞�삕.
			Assignment current = (Assignment)s;
			V(f, current.target);
			V(f, current.source);
			//target Type�뜝�룞�삕 source Type �뜝�룞�삕 - typeOf
			V(typeOf(f, current.target),typeOf(f, current.source));
			
		}else if(s instanceof Block){
			Block current = (Block)s;
			for(Statement ss:current) V(f,ss);
			
		}else if(s instanceof Conditional){
			//�뜝�룞�삕�뜝�떎諭꾩삕 , statement �뜝�떙�궪�삕
			Conditional c = (Conditional)s;
			V(f,c.test);
			V(Type.BOOL,typeOf(f, c.test)); //IF�뜝�룞�삕 Test�뜝�룞�삕 BOOL�뜝�룞�삕 �뜝�룞�삕�뜝占�
			V(f, c.thenbranch);
			V(f,c.elsebranch);
		}else if(s instanceof Loop){
			//�뜝�룞�삕�뜝�떎諭꾩삕 , statement �뜝�떙�궪�삕
			Loop l = (Loop)s;
			V(f, l.test);
			V(Type.BOOL,typeOf(f, l.test)); //Loop�뜝�룞�삕 Test�뜝�룞�삕 BOOL�뜝�룞�삕 �뜝�룞�삕�뜝占�
			V(f, l.body);
		}else if(s instanceof ForLoop){
			//�뜝�룞�삕�뜝�떎諭꾩삕 3�뜝�룞�삕, statement �뜝�떙�궪�삕
			//ForLoop = Assignment assign ; Expression test ; Expression third; Statement body
			ForLoop fo = (ForLoop) s;
			V(f, fo.assign);
			V(f,fo.test);
			V(Type.BOOL, typeOf(f,fo.test)); //ForLoop�뜝�룞�삕 Test�뜝�룞�삕 Bool�뜝�룞�삕 �뜝�룞�삕�뜝占�
			V(f,fo.test);
			V(f,fo.body);			
		}else if(s instanceof voidFuncCall){
			voidFuncCall vf = (voidFuncCall) s;
			
			if(definedFuncTable.contains(vf.id)) return;
			//TODO Custom func
			
			if(!funcTable.containsKey(vf.id)) typeError(vf.id +" : �뜝�룞�삕�뜝�룞�삕�벝�뜝占� �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�뙃�눦�삕 �뜝�뙃�땲�뙋�삕.");
			FunctionDef fd=funcTable.get(vf.id);
			V(f, vf.param, fd); //�뜝�떇�씛�삕�뜝�룞�삕�뜝占� �뜝�떙�궪�삕
			
		}else typeError("statement�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
	}
	
	public void V(FunctionDef f, Expression e){
		// Expression =VarableRef |Variable | Value | Binary | Unary | FuncCall
		if(e==null) throw new IllegalArgumentException("AST error : null Expression");
		if(e instanceof Value){ return;
		}else if(e instanceof VariableRef){
			V(f,(VariableRef)e);
		}else if(e instanceof Binary){
			Binary b = (Binary) e;
			V(f,b.term1);
			V(f,b.term2);
			V(typeOf(f,b.term1),typeOf(f,b.term2));
		}else if(e instanceof Unary){
			Unary u = (Unary) e;
			V(f,u.term);
			if(u.op.incOp() || u.op.decOp() || u.op.negOp()){
				if(typeOf(f,u.term).equals(Type.BOOL)) typeError(f.funcName +": "+ u.op +"�뜝�룞�삕 UnaryOp�뜝�룞�삕 �뜝�룞�삕�솗�뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕."); 
			}else if(u.op.notOp()){
				V(Type.BOOL,typeOf(f,u.term));
			}else if(u.op.CastOp()){
				if(u.op.intOp()){
					V(Type.INT,typeOf(f,u.term));
				}else if(u.op.bigIntOp()){
					V(Type.BIGINT,typeOf(f,u.term));
				}else if(u.op.charOp()){
					V(Type.CHAR,typeOf(f,u.term));
				}else if(u.op.floatOp()){
					V(Type.FLOAT,typeOf(f,u.term));
				}else if(u.op.BooleanOp()){
					V(Type.BOOL,typeOf(f,u.term));
				}else typeError(f.funcName +": "+ u.op +"�뜝�룞�삕 UnaryOp�뜝�룞�삕 �뜝�룞�삕�솗�뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
			}else typeError(f.funcName +": "+ u.op +"�뜝�룞�삕 UnaryOp�뜝�룞�삕 �뜝�룞�삕�솗�뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
		}else if(e instanceof FuncCall){
			FuncCall fc= (FuncCall) e;
			
			if(definedFuncTable.contains(fc.id)) return;
			//TODO Custom func
			
			if(!funcTable.containsKey(fc.id)) typeError(f.funcName +": "+fc.id+"�뜝�룞�삕 �뜝�룞�삕�뜝�떎�릺�뼲�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
			FunctionDef fd=funcTable.get(fc.id);
			V(f, fc.params, fd); //�뜝�떇�씛�삕�뜝�룞�삕�뜝占� �뜝�떙�궪�삕
			
		}else typeError(f.funcName+" : "+"  Expression�뜝�룞�삕 ���뜝�룞�삕�뜝�룞�삕 �뜝�떛�궪�삕�뜝�뙆�땲�뙋�삕.");
	}
	
	public void V(FunctionDef f, ArrayList<Expression> params, FunctionDef fp){
		if(params.size()!=fp.getParamSize()) typeError(f.funcName+" : "+fp.funcName+" : �뜝�떇�씛�삕�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕移섇뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
		for(Expression e : params) {
			V(f,e);
		}
		for(int i=0; i<params.size();i++){
			if(!typeOf(f, params.get(i)).equals(fp.getParams(i).type))
	
				typeError(f.funcName+" : "+fp.funcName+" : �뜝�떇�씛�삕�뜝�룞�삕�뜝�룞�삕�뜝占� ���뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕移섇뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
		}
		
		
		
	}
	
	public void V(FunctionDef f, VariableRef v){
		if(v instanceof Variable){
			Variable current = (Variable)v;
			if(!f.isContainVariable(current.id) && !globalVariables.containsKey(current.id))
				typeError(f.funcName+" : "+current.id+": �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�떎�뼲�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
			
		}
		else{
			ArrayRef current = (ArrayRef)v;
			if(!f.isContainArray(current.id) && !globalVariables.containsKey(current.id))
				typeError(f.funcName+" : "+current.id+": �뜝�띁�뿴�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�떎�뼲�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
			//TODO
			if(!typeOf(f,current.index).equals(Type.INT))
				typeError(f.funcName+" : "+current.id+": �뜝�띁�뿴�뜝�룞�삕 �뜝�떥�벝�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�솗�뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
			
		}
	}
	
	
	public Type typeOf(FunctionDef f, Expression e){
		// Expression =VarableRef |Variable | Value | Binary | Unary | FuncCall
		if(e==null) throw new IllegalArgumentException("AST error : null Expression");
		if(e instanceof Value){
			return ((Value)e).type;
		}else if(e instanceof ArrayRef){
			return getVariableType(f,(ArrayRef)e);
		}
		else if(e instanceof Variable){
			return getVariableType(f,(Variable)e);
		}else if(e instanceof Binary){
			Binary b = (Binary)e;
			if(b.op.ArithmeticOp()){
				return typeOf(f,((Binary) e).term1);
			}else if(b.op.RelationalOp() || b.op.BooleanOp())
				return Type.BOOL;
		}else if(e instanceof Unary){
			Unary u = (Unary)e;
			if(u.op.notOp()) return Type.BOOL;
			else if(u.op.negOp() || u.op.incOp() || u.op.decOp()) return typeOf(f,u.term);
			else if(u.op.CastOp()){
				if(u.op.intOp()){
					return Type.INT;
				}else if(u.op.charOp()){
					return Type.CHAR;
				}else if(u.op.bigIntOp()){
					return Type.BIGINT;
				}else if(u.op.floatOp()){
					return Type.FLOAT;
				}else if(u.op.BooleanOp()){
					return Type.BOOL;
				}else typeError(f.funcName +": "+ u.op +"�뜝�룞�삕 UnaryOp�뜝�룞�삕 �뜝�룞�삕�솗�뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
			}
		}else if(e instanceof FuncCall){
			FuncCall fc = (FuncCall)e;
			
			if(definedFuncTable.contains(fc.id)){
				Type t =DefinedFunction.definedType[definedFuncTable.indexOf(fc.id)];
				if(t==null) typeError(fc.id+" : "+"올바르지 않은 타입니다.");
				else return t;
			}
				
			//TODO Custom func
					
			return funcTable.get(fc.id).getFuncType();
		}else 
			typeError(f.funcName+" : "+"�뜝�떙留욌뙋�삕 Expression�뜝�룞�삕 �뜝�떍�떃�땲�뙋�삕.");
		return null; //�뜝�룞�삕�뜝�룞�삕X
	}
	
	private Type getVariableType(FunctionDef f,ArrayRef v){
		if(f.isContainVariable(v.id)) return f.getVariableType(v.id);
		else if(globalVariables.containsKey(v.id)) return globalVariables.get(v.id).type;
		typeError(v.id +"�뜝�룞�삕 Type�뜝�룞�삕 �뜝�룞�삕�뜝�떛�릺�뼲�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
		return null;
	}
	
	private Type getVariableType(FunctionDef f,Variable v){
		if(f.isContainVariable(v.id)) return f.getVariableType(v.id);
		else if(globalVariables.containsKey(v.id)) return globalVariables.get(v.id).type;
		typeError(f.funcName+" : "+v.id +"�뜝�룞�삕 Type�뜝�룞�삕 �뜝�룞�삕�뜝�떛�릺�뼲�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�떗�룞�삕�뜝�떦�뙋�삕.");
		return null;
	}
	
	private boolean V(Type t1, Type t2){
		if(t1.equals(Type.INT)){
			if(t2.equals(Type.BOOL) || t2.equals(Type.BIGINT))return false;
			else return true;
		}else if(t1.equals(Type.FLOAT)){
			if(t2.equals(Type.CHAR) || t2.equals(Type.BIGINT) || t2.equals(Type.BOOL)) return false;
			else return true;
		}else if(t1.equals(Type.CHAR)){
			if(t2.equals(Type.BOOL) || t2.equals(Type.BIGINT)) return false;
			else return true;
		}else if(t1.equals(Type.BOOL)){
			if(t2.equals(Type.BOOL)) return true;
			else return false;			
		}else if(t1.equals(Type.BIGINT)){
			if(t2.equals(Type.BOOL)) return false;
			else return true;
		}
		return false;
	}
	
}

