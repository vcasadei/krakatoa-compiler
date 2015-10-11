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

public class MessageSendToVariable extends MessageSend {

	public MessageSendToVariable(Variable inLocal, Method method,
			ExprList exprList) {
		this.var = inLocal;
		this.method = method;
		this.exprList = exprList;
	}

	public Type getType() {
		return null;
	}

	public void genC(PW pw, boolean putParenthesis) {

	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}

	private Variable var;
	private Method method;
	private ExprList exprList;
}