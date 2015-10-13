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
		pw.print("\"" + literalString + "\"");
	}
}
