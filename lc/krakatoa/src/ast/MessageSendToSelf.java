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

	public void genC(PW pw, boolean putParenthesis) {
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
	
		if (putParenthesis) {
			pw.print("(");
		}
		
		if (instanceVariable == null && method == null && exprList == null) {
			pw.print("this");
		} else if (instanceVariable != null) {
			pw.print("this." + instanceVariable.getName());
		} else if (instanceVariable == null && method != null) {
			// If is private
			if (kraClass.containsPrivateMethod(method.getName())) {
				pw.print(method.getClass().getName() + "." + method.getName() + "(");
				exprList.genKra(pw);
				pw.print(")");
			} else {
				pw.print("this." + method.getName() + "(");
				exprList.genKra(pw);
				pw.print(")");
			}
		}
		if (putParenthesis) {
			pw.println(")");
		} else {
			pw.println(";");
		}

	}

	private KraClass kraClass;
	private InstanceVariable instanceVariable;
	private Method method;
	private ExprList exprList;

}