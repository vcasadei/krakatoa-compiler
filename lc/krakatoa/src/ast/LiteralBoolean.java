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

public class LiteralBoolean extends Expr {

	public LiteralBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		pw.print(value ? "1" : "0");
	}

	@Override
	public Type getType() {
		return Type.booleanType;
	}

	public static LiteralBoolean True = new LiteralBoolean(true);
	public static LiteralBoolean False = new LiteralBoolean(false);

	private boolean value;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print(value ? "true" : "false");
	}
}
