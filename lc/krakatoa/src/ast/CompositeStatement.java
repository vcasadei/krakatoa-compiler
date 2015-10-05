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

public class CompositeStatement extends Statement {

	public CompositeStatement(StatementList statementList) {
		this.statementList = statementList;
	}

	public StatementList getStatementList() {
		return statementList;
	}

	public void setStatementList(StatementList statementList) {
		this.statementList = statementList;
	}
	
	public void genC(PW pw) {
		
	}
	
	private StatementList statementList;
	
}
