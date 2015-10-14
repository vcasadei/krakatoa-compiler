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

public class IfStatement extends Statement {

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
	}

	public IfStatement(Expr expr, Statement statementIf, Statement statementElse) {
		this.expr = expr;
		this.statementIf = statementIf;
		this.statementElse = statementElse;
	}

	private Expr expr = null;
	private Statement statementIf = null;
	private Statement statementElse = null;

	@Override
	public void genKra(PW pw) {
		pw.printIdent("if (");
		expr.genKra(pw, false);
		pw.print(") ");
		if (statementIf instanceof CompositeStatement) {
			statementIf.genKra(pw);
		} else {
			pw.println("");
			if (statementIf != null) {
				pw.add();
				statementIf.genKra(pw);
				pw.sub();
			}
		}
		

		if (statementElse != null){
            pw.printlnIdent("} else {");
            pw.add();
            statementElse.genKra(pw);
            pw.sub();
            pw.printlnIdent("}");
        }

	}
}
