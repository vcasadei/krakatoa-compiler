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

public class LiteralInt extends Expr {

	public LiteralInt(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void genC(PW pw, boolean putParenthesis) {
		pw.printIdent("" + value);
	}

	public Type getType() {
		return Type.intType;
	}

	private int value;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print("" + value);
	}
}
