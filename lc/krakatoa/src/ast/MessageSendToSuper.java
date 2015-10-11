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

public class MessageSendToSuper extends MessageSend {

	public Type getType() {
		return methodMessage.getReturnType();
	}

	public void genC(PW pw, boolean putParenthesis) {
		// To do ....
	}

	public MessageSendToSuper(Method methodMessage, ExprList exprList,
			KraClass kraClassSender) {
		this.methodMessage = methodMessage;
		this.exprList = exprList;
		this.kraClassSender = kraClassSender;
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// Calls super and the method
		pw.print("super." + methodMessage.getName() + "(");
		// The list of expressions
		exprList.genKra(pw);
		pw.print(")");
	}

	public Method getMethodMessage() {
		return methodMessage;
	}

	public void setMethodMessage(Method methodMessage) {
		this.methodMessage = methodMessage;
	}

	public ExprList getExprList() {
		return exprList;
	}

	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}

	public KraClass getKraClassSender() {
		return kraClassSender;
	}

	public void setKraClassSender(KraClass kraClassSender) {
		this.kraClassSender = kraClassSender;
	}

	private Method methodMessage;
	private ExprList exprList;
	private KraClass kraClassSender;

}