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
