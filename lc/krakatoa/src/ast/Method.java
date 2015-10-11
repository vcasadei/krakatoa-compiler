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

	public Method(String name, Type type, boolean isStatic) {
		this.name = name;
		this.paramList = new ParamList();
		this.localVariableList = new LocalVariableList();
		this.returnType = type;
		this.stmtList = new StatementList();
		this.isStatic = isStatic;
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

	public void genKra(PW pw) {
		if (isStatic)
			pw.print("static ");
		pw.print(returnType.getCname() + " ");
		pw.print(name + "(");
		paramList.genKra(pw);
		pw.println(") {");
		pw.add();
		localVariableList.genKra(pw);
		stmtList.genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
	}
	
	private String name;
	private ParamList paramList;
	private LocalVariableList localVariableList;
	private Type returnType;
	private StatementList stmtList;
	private boolean isStatic;

}
