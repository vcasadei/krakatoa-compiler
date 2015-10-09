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

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		
	}
	
}
