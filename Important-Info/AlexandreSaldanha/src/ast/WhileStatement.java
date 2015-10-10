/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class WhileStatement extends Statement {

	public WhileStatement(Expr expression, Statement statement) {
		
		this.expression = expression;
		this.statement = statement;
		
	}
	
	
	@Override
	public void genC(PW pw) {
		
		pw.printIdent("while (");
		
		expression.genC(pw, false);
		
		pw.println(") {");
		
		pw.add();
		
		statement.genC(pw);
		
		pw.sub();
		
		pw.printlnIdent("}");

	}
	
	
	private Expr expression;
	private Statement statement;

}
