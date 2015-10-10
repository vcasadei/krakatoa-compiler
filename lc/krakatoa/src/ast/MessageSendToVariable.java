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