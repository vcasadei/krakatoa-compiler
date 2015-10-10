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

public class NullStatement extends Statement {

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
	}

	public void genKra(PW pw) {
		pw.printlnIdent(";");
	}
}
