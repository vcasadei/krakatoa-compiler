/**
 * Laborat�rio de Compiladores 2015/2
 * Universidade Federal de S�o Carlos
 * Orienta��o: Prof. Dr. Jos� de O. Guimar�es
 * 
 * @author Maur�cio Spinardi 408174
 * @author Vitor Casadei 408301
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

public class Method {

	public Method(String name, Type returnType) {
		this.name = name;
		this.returnType = returnType;
		
		paramList = new ParamList();
		statementList = new StatementList();
		localVariableList = new LocalVariableList();
	}

	public Method(String name, Type returnType, boolean b) {
		this.name = name;
		this.returnType = returnType;
		
		paramList = new ParamList();
		statementList = new StatementList();
		localVariableList = new LocalVariableList();
		
		this.isStatic = b;
	}

	public String getName() {
		return name;
	}
	
	public ParamList getParamList() {
		return paramList;
	}
	
	public void setParamList(ParamList paramList) {
		this.paramList = paramList;
	}
	
	public void addParam(Parameter param) {
		this.paramList.addElement(param); 
	}
	
	public LocalVariableList getLocalVariableList() {
		return localVariableList;
	}
	
	public void setLocalVariableList(LocalVariableList localVarList) {
		this.localVariableList = localVarList;
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	public StatementList getStatementList() {
		return statementList;
	}
	
	public void setStatementList(StatementList statementList) {
		this.statementList = statementList;
	}
	
	public void setLocalVariable(Variable v) {
		localVariableList.addElement(v);
	}
	
	private String name;
	private ParamList paramList;
	private LocalVariableList localVariableList;
	private Type returnType;
	private StatementList statementList;
	private boolean isStatic = false;
	
}
