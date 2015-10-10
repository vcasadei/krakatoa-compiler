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

public class MessageSendToInstance extends Expr {

	public MessageSendToInstance(Variable inLocal, Variable instanceVariable) {
		this.var = inLocal;
		this.instanceVariable = instanceVariable;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	private Variable var;
	private Variable instanceVariable;
}
