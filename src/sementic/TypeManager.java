package sementic;

import java.util.ArrayList;
import java.util.HashMap;

import abstractSyntax.*;

public class TypeManager {
	
	private static TypeManager instance;
	private HashMap<String, SymbolElement> globalVariables = new HashMap<String, SymbolElement>();
	private HashMap<String, Integer> globalArray = new HashMap<String, Integer>();
	private HashMap<String, FunctionDef> funcTable = new HashMap<String, FunctionDef>();
	
	
	public static TypeManager getInstance(){
		if(instance==null) instance = new TypeManager();
		return instance;
	}
	
	public void typeError(String msg){
		System.err.println(msg);
		System.exit(0);
	}
	
	/*
	 *  ���� ���� üũ
	 */
	public void setGlobalVariable(Declarations decls){
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(globalVariables.containsKey(current.v.id)) typeError(current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
				globalVariables.put(current.v.id, new SymbolElement(current.t, Type.BIGINT.equals(current.t)? 100:1));
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(globalVariables.containsKey(current.v.id)) typeError(current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
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
					if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
					if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
					f.addVariable(current.v.id, current.t, Type.BIGINT.equals(current.t)? 100:1);
				}
				else{
					ArrayDecl current = (ArrayDecl)d;
					if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
					if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
					f.addVariable(current.v.id, current.t, current.size);
					f.addArray(current.v.id, current.size);
				}
			}
		}
		for(Declaration d : decls){
			if(d instanceof VariableDecl){
				VariableDecl current = (VariableDecl)d;
				if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
				if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
				f.addVariable(current.v.id, current.t, Type.BIGINT.equals(current.t)? 100:1);
			}
			else{
				ArrayDecl current = (ArrayDecl)d;
				if(f.isContainVariable(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
				if(globalVariables.containsKey(current.v.id)) typeError(f.funcName+" : "+current.v.id +": ������ ������ �ߺ��Ǿ����ϴ�.");
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
			if(funcTable.containsKey(currentF.id)) typeError(currenfDef.funcName + "  �Լ����� �ߺ��Ǿ����ϴ�.");  //�⺻ �����Լ����� ���ؾߵ�
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
			if(funcTable.containsKey(currentF.id)) typeError(currentF.id + "  �Լ����� �ߺ��Ǿ����ϴ�.");  //�⺻ �����Լ����� ���ؾߵ�
			else funcTable.put(currentF.id,currenfDef);
		}
		else{
			MainFunc currentF = (MainFunc)f;
			FunctionDef currenfDef = new FunctionDef(null, "main");
			setLocalVariable(currenfDef,null,currentF.decpart);
			V(currenfDef,currentF.body);
			if(funcTable.containsKey("main")) typeError("main" + "  �Լ����� �ߺ��Ǿ����ϴ�.");  //�⺻ �����Լ����� ���ؾߵ�
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
			//������ �Ǿ�����, ���� type�� �´��� Ȯ��.
			Assignment current = (Assignment)s;
			V(f, current.target);
			V(f, current.source);
			//target Type�� source Type �� - typeOf
			V(typeOf(f, current.target),typeOf(f, current.source));
			
		}else if(s instanceof Block){
			Block current = (Block)s;
			for(Statement ss:current) V(f,ss);
			
		}else if(s instanceof Conditional){
			//���ǹ� , statement �˻�
			Conditional c = (Conditional)s;
			V(f,c.test);
			V(Type.BOOL,typeOf(f, c.test)); //IF�� Test�� BOOL�� ���
			V(f, c.thenbranch);
			V(f,c.elsebranch);
		}else if(s instanceof Loop){
			//���ǹ� , statement �˻�
			Loop l = (Loop)s;
			V(f, l.test);
			V(Type.BOOL,typeOf(f, l.test)); //Loop�� Test�� BOOL�� ���
			V(f, l.body);
		}else if(s instanceof ForLoop){
			//���ǹ� 3��, statement �˻�
			//ForLoop = Assignment assign ; Expression test ; Expression third; Statement body
			ForLoop fo = (ForLoop) s;
			V(f, fo.assign);
			V(f,fo.test);
			V(Type.BOOL, typeOf(f,fo.test)); //ForLoop�� Test�� Bool�� ���
			V(f,fo.test);
			V(f,fo.body);			
		}else if(s instanceof voidFuncCall){
			//����� �Լ�����, �Ķ������ ����, Ÿ���� �´��� Ȯ��.
			voidFuncCall vf = (voidFuncCall) s;
			//TODO �⺻ �Լ��� �˻� �߰��ؾߵ�
			if(!funcTable.containsKey(vf.id)) typeError(vf.id +" : ����Ǿ� ���� ���� �Լ� �Դϴ�.");
			FunctionDef fd=funcTable.get(vf.id);
			V(f, vf.param, fd); //�Ķ���� �˻�
			
		}else typeError("statement�� �������� �ʽ��ϴ�.");
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
			if(u.op.IncOp() || u.op.DecOp() || u.op.NegateOp()){
				if(typeOf(f,u.term).equals(Type.BOOL)) typeError(f.funcName +": "+ u.op +"�� UnaryOp�� ��Ȯ���� �ʽ��ϴ�."); 
			}else if(u.op.NotOp()){
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
				}else typeError(f.funcName +": "+ u.op +"�� UnaryOp�� ��Ȯ���� �ʽ��ϴ�.");
			}else typeError(f.funcName +": "+ u.op +"�� UnaryOp�� ��Ȯ���� �ʽ��ϴ�.");
		}else if(e instanceof FuncCall){
			FuncCall fc= (FuncCall) e;
			//TODO �⺻ �Լ��� �˻� �߰��ؾߵ�
			if(!funcTable.containsKey(fc.id)) typeError(f.funcName +": "+fc.id+"�� ���ǵǾ� ���� �ʽ��ϴ�.");
			FunctionDef fd=funcTable.get(fc.id);
			V(f, fc.params, fd); //�Ķ���� �˻�
			
		}else typeError(f.funcName+" : "+"  Expression�� Ÿ���� �̻��մϴ�.");
	}
	
	public void V(FunctionDef f, ArrayList<Expression> params, FunctionDef fp){
		if(params.size()!=fp.getParamSize()) typeError(f.funcName+" : "+fp.funcName+" : �Ķ������ ���� ��ġ���� �ʽ��ϴ�.");
		for(Expression e : params) {
			V(f,e);
		}
		for(int i=0; i<params.size();i++){
			if(!typeOf(f, params.get(i)).equals(fp.getParams(i).type))
	
				typeError(f.funcName+" : "+fp.funcName+" : �Ķ������ Ÿ���� ��ġ���� �ʽ��ϴ�.");
		}
		
		
		
	}
	
	public void V(FunctionDef f, VariableRef v){
		if(v instanceof Variable){
			Variable current = (Variable)v;
			if(!f.isContainVariable(current.id) && !globalVariables.containsKey(current.id))
				typeError(f.funcName+" : "+current.id+": ������ ������ �Ǿ� ���� �ʽ��ϴ�.");
			
		}
		else{
			ArrayRef current = (ArrayRef)v;
			if(!f.isContainArray(current.id) && !globalVariables.containsKey(current.id))
				typeError(f.funcName+" : "+current.id+": �迭�� ������ �Ǿ� ���� �ʽ��ϴ�.");
			//TODO
			if(!typeOf(f,current.index).equals(Type.INT))
				typeError(f.funcName+" : "+current.id+": �迭�� �ε����� ��Ȯ���� �ʽ��ϴ�.");
			
		}
	}
	
	
	private Type typeOf(FunctionDef f, Expression e){
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
			if(u.op.NotOp()) return Type.BOOL;
			else if(u.op.NegateOp() || u.op.IncOp() || u.op.DecOp()) return typeOf(f,u.term);
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
				}else typeError(f.funcName +": "+ u.op +"�� UnaryOp�� ��Ȯ���� �ʽ��ϴ�.");
			}
		}else if(e instanceof FuncCall){
			FuncCall fc = (FuncCall)e;
			return funcTable.get(fc.id).getFuncType();
		}else 
			typeError(f.funcName+" : "+"�˸´� Expression�� �ƴմϴ�.");
		return null; //����X
	}
	
	private Type getVariableType(FunctionDef f,ArrayRef v){
		if(f.isContainVariable(v.id)) return f.getVariableType(v.id);
		else if(globalVariables.containsKey(v.id)) return globalVariables.get(v.id).type;
		typeError(v.id +"�� Type�� ���̵Ǿ� ���� �ʽ��ϴ�.");
		return null;
	}
	
	private Type getVariableType(FunctionDef f,Variable v){
		if(f.isContainVariable(v.id)) return f.getVariableType(v.id);
		else if(globalVariables.containsKey(v.id)) return globalVariables.get(v.id).type;
		typeError(f.funcName+" : "+v.id +"�� Type�� ���̵Ǿ� ���� �ʽ��ϴ�.");
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

