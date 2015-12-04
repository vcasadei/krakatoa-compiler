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

public class MessageSendToInstance extends Expr {

	public MessageSendToInstance(Variable inLocal, Variable instanceVariable) {
		this.var = inLocal;
		this.instanceVariable = instanceVariable;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		pw.print(var.getName() + "." + instanceVariable.getName());
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.printIdent(var.getName() + "." + instanceVariable.getName());
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	private Variable var;
	private Variable instanceVariable;
}
