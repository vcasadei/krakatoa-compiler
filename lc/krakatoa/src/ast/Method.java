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
	
	private String name;
	private ParamList paramList;
	private LocalVariableList localVariableList;
	private Type returnType;
	private StatementList stmtList;
	private boolean isStatic;

}
