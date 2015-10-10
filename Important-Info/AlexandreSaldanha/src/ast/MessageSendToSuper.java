/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class MessageSendToSuper extends MessageSend { 

	public MessageSendToSuper(Method message, ExprList exprList, KraClass senderClass) {
		
		this.senderClass = senderClass;
		this.message = message;
		this.exprList = exprList;
		
	}

	
    public Type getType() { 
    	
        return message.getReturnType();
        
    }

    
    public void genC(PW pw, boolean putParenthesis) {
    	
    	KraClass superclass = senderClass.getSuperclass();
    	
    	while (superclass != null) {
    		
    		if (superclass.containsPublicMethod(message.getName())) {
    			
    			break;
    			
    		}
    		
    		superclass = superclass.getSuperclass();
    		
    	}
    	
    	pw.print("_" + superclass.getName() + "_" + message.getName()
    			+ "((" + superclass.getCname() + " * ) this");
    	
    	if (exprList.size() > 0) {
    		
    		pw.print(", ");
    		
    	}
    	
    	exprList.genC(pw);
    	
    	pw.print(")");
    	
    }
    
    
	private Method message;
	private ExprList exprList;
	private KraClass senderClass;
}