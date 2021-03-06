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

public class ReturnStatement extends Statement {

	@Override
	public void genC(PW pw, String className) {
		pw.printIdent("return ");
		expr.genC(pw, false);
		pw.println(";");
	}

	public void genKra(PW pw) {
		pw.print("return (");
		expr.genKra(pw, false);
		pw.println(");");
	}

	public ReturnStatement(Expr expr) {
		this.expr = expr;
	}

	private Expr expr;
}
