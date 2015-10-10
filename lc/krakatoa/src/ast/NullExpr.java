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

public class NullExpr extends Expr {

	public void genC(PW pw, boolean putParenthesis) {
		pw.printIdent("NULL");
	}

	public void genKra(PW pw, boolean putParenthesis) {
		pw.printIdent("null");
	}

	public Type getType() {
		return Type.undefinedType;
	}
}