/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class MessageSendToInstance extends MessageSend {
	
	public MessageSendToInstance(Variable object, InstanceVariable instanceVariable) {
		
		this.object = object;
		this.instanceVariable = instanceVariable;
		
	}

	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		
		pw.print(object.getName() + "." + instanceVariable.getName());
		
	}

	
	@Override
	public Type getType() {
		
		return instanceVariable.getType();
		
	}
	
	
	private Variable object;
	private InstanceVariable instanceVariable;

}
