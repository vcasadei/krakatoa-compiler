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

public class CompositeStatement extends Statement {

	public CompositeStatement(StatementList statementList) {
		this.statementList = statementList;
	}

	public StatementList getStatementList() {
		return statementList;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub

	}

	private StatementList statementList;

}
