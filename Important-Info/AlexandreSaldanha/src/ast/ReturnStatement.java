/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class ReturnStatement extends Statement {

	public ReturnStatement(Expr expression) {
		
		this.expression = expression;
		
	}
	
	
	@Override
	public void genC(PW pw) {
		
		pw.printIdent("return ");
		
		expression.genC(pw, false);
		
		pw.println(";");
		
	}
	
	
	private Expr expression;

}
