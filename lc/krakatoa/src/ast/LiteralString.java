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

public class LiteralString extends Expr {

	public LiteralString(String literalString) {
		this.literalString = literalString;
	}

	public void genC(PW pw, boolean putParenthesis) {
		pw.print(literalString);
	}

	public Type getType() {
		return Type.stringType;
	}

	private String literalString;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}
}
