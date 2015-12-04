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

import java.util.*;

public class WriteStatement extends Statement {

	public WriteStatement(ExprList writeStmt) {
//		for (Expr expr : writeStmt) {
//			if (writeStmt != null) {
//				this.writeStmt.addElement(expr);
//			}
//			
//		}
		this.writeStmt = writeStmt;
	}

	@Override
	public void genC(PW pw) {
		Iterator<Expr> it = this.writeStmt.getElements();
		
		while (it.hasNext()) {
			Expr element = it.next();
			
			if (element.getType() == Type.intType) {
				pw.printIdent("printf(\"%d\", ");
				element.genC(pw, false);
				pw.println(");");
			} else {
				if (element.getType() == Type.stringType) {
					pw.printIdent("puts(");
					element.genC(pw, false);
					pw.println(");");
				}
			}	
		}
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("write(");
		writeStmt.genKra(pw);
		pw.println(");");
	}

	private ExprList writeStmt;

}
