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

public class AssignmentStatement extends Statement {

	public AssignmentStatement(Expr esq, Expr dir) {
		this.esq = esq;
		this.dir = dir;
	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("");
		esq.genC(pw, false);
		if (dir != null) {
			pw.print(" = ");
			dir.genC(pw, false);
		}
		pw.println(";");

	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("");
		this.esq.genKra(pw, false);
		if (dir != null) {
			pw.print(" = ");
			dir.genKra(pw, false);
		}
		pw.println(";");
	}

	Expr esq, dir;

}
