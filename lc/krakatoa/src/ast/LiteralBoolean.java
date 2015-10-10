/**
 * Laborat�rio de Compiladores 2015/2
 * Universidade Federal de S�o Carlos
 * Orienta��o: Prof. Dr. Jos� de O. Guimar�es
 * 
 * @author Maur�cio Spinardi 408174
 * @author Vitor Casadei 408301
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
		// TODO Auto-generated method stub

	}
}
