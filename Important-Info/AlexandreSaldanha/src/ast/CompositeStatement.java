/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class CompositeStatement extends Statement {
	
	public CompositeStatement(StatementList statementList) {
		
		this.statementList = statementList;
		
	}
	
	
	public void genC(PW pw) {
		
		statementList.genC(pw);
		
	}
	
	
	public StatementList getStatementList() {
		
		return statementList;
		
	}
	
	
	private StatementList statementList;
}