package ast;

public class MessageSendStatement extends Statement {

	public MessageSendStatement(Expr esq) {
		this.messageSend = messageSend;
	}

	public void genC(PW pw) {
		pw.printIdent("");
		// messageSend.genC(pw);
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		
	}
	
	private MessageSend messageSend;

}
