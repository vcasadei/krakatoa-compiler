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

public class WhileStatement extends Statement {
	@Override
	public void genC(PW pw) {
	}

	public WhileStatement(Expr expr, Statement statement) {
		this.expr = expr;
		this.statement = statement;
	}

	private Expr expr;
	private Statement statement;

	@Override
	public void genKra(PW pw) {
		pw.print("while (");
		expr.genKra(pw, false);
		pw.println(") {");

		pw.add();
		statement.genKra(pw);
		pw.sub();

		pw.printlnIdent("}");
	}
}
