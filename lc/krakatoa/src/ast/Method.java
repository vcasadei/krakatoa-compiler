/**
 * Laboratório de Compiladores [2015/2] <br>
 * Orientação: Prof. Dr. José de O. Guimarães <br>
 * 
 * @author Maurício Spinardi | 401874 <br>
 * @author Vitor Casadei | 408301 <br>
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

public class Method {

	public Method(String name, Type type, boolean isStatic, boolean isFinal) {
		this.name = name;
		this.paramList = new ParamList();
		this.localVariableList = new LocalVariableList();
		this.returnType = type;
		this.stmtList = new StatementList();
		this.isStatic = isStatic;
		this.isFinal = isFinal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public StatementList getStatementList() {
		return stmtList;
	}

	public void setStatementList(StatementList stmtList) {
		this.stmtList = stmtList;
	}

	public ParamList getParamList() {
		return paramList;
	}

	public void setParamList(ParamList paramList) {
		this.paramList = paramList;
	}

	public void setLocalVariable(Variable v) {
		this.localVariableList.addElement(v);
	}

	public void addParam(Parameter param) {
		this.paramList.addElement(param);
	}

	public boolean isStatic() {
		return this.isStatic;
	}
	
	public boolean isFinal() {
		return this.isFinal;
	}

	public void genKra(PW pw, boolean publicMethod) {
		if (isStatic) {
			pw.print("static ");
		}
		if (isFinal) {
			pw.print("final ");
		}
		if (publicMethod) {
			pw.print("public ");
		} else {
			pw.print("private ");
		}
		pw.print(returnType.getName() + " ");
		pw.print(name + "(");
		if (paramList != null) {
			paramList.genKra(pw);
		}
		pw.println(") {");
		pw.add();
		if (localVariableList != null) {
			localVariableList.genKra(pw);
		}
		if (stmtList != null) {
			stmtList.genKra(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
	}
	
	public void genC(PW pw, String kraClass) {
		pw.print(returnType.getCname());
		pw.print(" _" + kraClass + "_" + getName());
		
		if (isStatic) {
			pw.print("(");
		}
		else {
			pw.print("(_class_" + kraClass + " *this");
			if (paramList.getSize() > 0) {
				pw.print(", ");
			}
		}
		
		paramList.genC(pw);
		pw.println(") {");
		pw.add();
		localVariableList.genC(pw);
		stmtList.genC(pw);
		pw.sub();
		pw.printlnIdent("}");
		pw.println("");
	}
	
	private String name;
	private ParamList paramList;
	private LocalVariableList localVariableList;
	private Type returnType;
	private StatementList stmtList;
	private boolean isStatic;
	private boolean isFinal;

}
