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

import lexer.*;

public class SignalExpr extends Expr {

	public SignalExpr(Symbol oper, Expr expr) {
		this.oper = oper;
		this.expr = expr;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		if (putParenthesis) {
			pw.print("(");
		}
		pw.print(oper == Symbol.PLUS ? "+" : "-");
		expr.genC(pw, true);
		if (putParenthesis) {
			pw.print(")");
		}
	}

	@Override
	public Type getType() {
		return expr.getType();
	}

	private Expr expr;
	private Symbol oper;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		if (putParenthesis)
			pw.print("(");
		pw.print(oper == Symbol.PLUS ? "+" : "-");
		expr.genKra(pw, true);
		if (putParenthesis)
			pw.print(")");
	}

}
