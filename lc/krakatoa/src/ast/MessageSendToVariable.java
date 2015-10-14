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
		if (method != null) {
			return method.getReturnType();
		} else {
			return Type.undefinedType;
		}
	}

	public void genC(PW pw, boolean putParenthesis) {

	}

	public Variable getVar() {
		return var;
	}

	public void setVar(Variable var) {
		this.var = var;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public ExprList getExprList() {
		return exprList;
	}

	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		if (putParenthesis) {
			pw.print("(");
		}
		
		pw.print(var.getName() + ".");
		if (method != null) {
			pw.print(method.getName() + "(");
			exprList.genKra(pw);
			pw.print(")");
		}
		
		
		if (putParenthesis) {
			pw.print(")");
		}
	}

	private Variable var;
	private Method method;
	private ExprList exprList;
}