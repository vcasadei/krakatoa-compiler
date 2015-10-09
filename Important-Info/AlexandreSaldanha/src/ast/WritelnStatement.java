/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.Iterator;

public class WritelnStatement extends Statement {
	
	public WritelnStatement(ExprList exprList) {
		
		this.exprList = exprList;
		
	}
	
	
	public void genC(PW pw) {
		
		Iterator<Expr> it = exprList.elements();
		
		while (it.hasNext()) {
			Expr e = it.next();
			
			if (e.getType() == Type.intType) {
				pw.printIdent("printf(\"%d \", ");
				e.genC(pw, false);
				pw.println(");");
			}
			else if (e.getType() == Type.stringType) {
				pw.printIdent("puts(");
				e.genC(pw, false);
				pw.println(");");
			}
			
		}
		pw.printlnIdent("printf(\"\\n\");");
		
	}
	
	private ExprList exprList;
}