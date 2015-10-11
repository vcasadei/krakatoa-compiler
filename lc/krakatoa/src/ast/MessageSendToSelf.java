package ast;

public class MessageSendToSelf extends MessageSend {

	public MessageSendToSelf(KraClass currentClass, InstanceVariable instVar,
			Method method, ExprList exprList) {
		this.kraClass = currentClass;
		this.instanceVariable = instVar;
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

	private KraClass kraClass;
	private InstanceVariable instanceVariable;
	private Method method;
	private ExprList exprList;

}