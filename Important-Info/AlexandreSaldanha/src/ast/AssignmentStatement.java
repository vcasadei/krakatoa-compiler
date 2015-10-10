/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class AssignmentStatement extends Statement {

	public AssignmentStatement(Expr left, Expr right) {
		
		this.left = left;
		this.right = right;
		
	}
	
	
	@Override
	public void genC(PW pw) {
		
		pw.printIdent("");
		
		left.genC(pw, false);
		
		pw.print(" = ");
		
		right.genC(pw, false);
		
		pw.println(";");
		
	}
	
	
	private Expr left;
	private Expr right;

}
