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

	public MessageSendStatement(Expr messageSend) {
		this.messageSend = messageSend;
	}

	public void genC(PW pw, String className) {
		pw.printIdent("");
		messageSend.genC(pw, false);
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.print("");
		messageSend.genKra(pw, false);
		pw.println(";");
	}

	private Expr messageSend;

}
