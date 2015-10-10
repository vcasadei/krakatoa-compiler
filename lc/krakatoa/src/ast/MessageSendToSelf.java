package ast;


public class MessageSendToSelf extends MessageSend {
    
    public MessageSendToSelf(KraClass kraClassObject, InstanceVariable instanceVariable, Method method,
			ExprList exprList) {
		this.kraClassObject = kraClassObject;
		this.instanceVariable = instanceVariable;
		this.method = method;
		this.exprList = exprList;
	}

	public Type getType() { 
		// If the instanceVar and methods are null, the type is the object
        if (instanceVariable == null && method == null) {
        	return kraClassObject;
        	// If the instaceVar is not null, the type is the instance var
        } else if (instanceVariable != null && method == null) {
        	return instanceVariable.getType();
        } else {
        	// If none of the above, the method
        	return method.getReturnType();
        }
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print("this." + method.getName() + "(");
		exprList.genKra(pw);
		pw.print(")");
	}
	
	public KraClass getKraClassObject() {
		return kraClassObject;
	}

	public void setKraClassObject(KraClass kraClassObject) {
		this.kraClassObject = kraClassObject;
	}

	public InstanceVariable getInstanceVariable() {
		return instanceVariable;
	}

	public void setInstanceVariable(InstanceVariable instanceVariable) {
		this.instanceVariable = instanceVariable;
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

	private KraClass kraClassObject;
	private InstanceVariable instanceVariable;
	private Method method;
	private ExprList exprList;
    
    
}