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

public class MessageSendToSelf extends MessageSend {

	public MessageSendToSelf(KraClass currentClass, InstanceVariable instVar,
			Method method, ExprList exprList) {
		this.kraClass = currentClass;
		this.instanceVariable = instVar;
		this.method = method;
		this.exprList = exprList;
	}

	public Type getType() {
		if (instanceVariable == null && method == null) {
        	return kraClass;
        } else if (instanceVariable != null && method == null) {
        	return instanceVariable.getType();
        } else {
        	return method.getReturnType();
        }
	}
	
	public InstanceVariable getInstanceVariable() {
		return this.instanceVariable;
	}

	public KraClass getKraClass() {
		return kraClass;
	}

	public void setKraClass(KraClass kraClass) {
		this.kraClass = kraClass;
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

	public void genC(PW pw, boolean putParenthesis) {
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
	
		if (putParenthesis) {
			pw.print("(");
		}
		
		pw.print("this");
		
		if (instanceVariable != null) {
			pw.print("." + instanceVariable.getName());
		} else if (method != null) {
			pw.print("." + method.getName());
            pw.print("(");
            this.exprList.genKra(pw);
            pw.print(")");
		}
		if (putParenthesis) {
			pw.print(")");
		}

	}

	private KraClass kraClass;
	private InstanceVariable instanceVariable;
	private Method method;
	private ExprList exprList;

}