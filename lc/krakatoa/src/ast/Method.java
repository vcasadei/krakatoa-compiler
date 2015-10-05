/**
 * Laboratório de Compiladores 2015/2
 * Universidade Federal de São Carlos
 * Orientação: Prof. Dr. José de O. Guimarães
 * 
 * @author Maurício Spinardi 408174
 * @author Vitor Casadei 408301
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

public class Method {

	public Method(String name, Type returnType) {
		this.name = name;
		this.returnType = returnType;
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
	
	private String name;
	private ParamList paramList;
	private LocalVariableList localVariableList;
	private Type returnType;
	private StatementList statementList;
	
}
