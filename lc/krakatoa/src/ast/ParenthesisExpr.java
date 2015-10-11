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

public class ParenthesisExpr extends Expr {

	public ParenthesisExpr(Expr expr) {
		this.expr = expr;
	}

	public void genC(PW pw, boolean putParenthesis) {
		pw.print("(");
		expr.genC(pw, false);
		pw.printIdent(")");
	}

	public Type getType() {
		return expr.getType();
	}

	private Expr expr;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}
}