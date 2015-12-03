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

public class VariableExpr extends Expr {

	public VariableExpr(Variable v) {
		this.v = v;
	}

	public void genC(PW pw, boolean putParenthesis) {
		if (putParenthesis) {
			pw.print("(");
		}
		pw.print("_" + v.getName());
		if (putParenthesis) {
			pw.print(")");
		}
	}

	public Type getType() {
		return v.getType();
	}

	private Variable v;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		if (putParenthesis) {
			pw.print("(");
			pw.print(v.getName());
			pw.print(")");
		} else
			pw.print(v.getName());
	}

}