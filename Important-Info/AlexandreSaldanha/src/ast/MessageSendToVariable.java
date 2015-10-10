/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.Iterator;


public class MessageSendToVariable extends MessageSend { 

	public MessageSendToVariable(Variable object, Method method, ExprList exprList) {
		
		this.object = object;
		this.method = method;
		this.exprList = exprList;
		
	}

	
    public Type getType() { 
    	
        return method.getReturnType();
        
    }
    
    
    public void genC(PW pw, boolean putParenthesis) {
    	
    	pw.print("((" + method.getReturnType().getCname() + " (*) ");
    	pw.print("(" + object.getType().getCname() + " *");
    	
    	if (exprList.size() > 0) {
    		pw.print(", ");
    	}
    	
    	Iterator<Expr> it = exprList.elements();
    	int size = exprList.size();
    	
    	while (it.hasNext()) {
    		
    		pw.print(it.next().getType().getCname());
    		
    		if (size > 1) {
    			
    			pw.print(", ");
    			
    		}
    		
    		size--;
    		
    	}
    	
    	KraClass aClass = (KraClass) object.getType();
    	int i = aClass.getVtIndex(method.getName());
    	
    	pw.print(")) _" + object.getName() + "->vt[" + i + "]) (_" + object.getName());
    	
    	if (exprList.size() > 0) {
    		pw.print(", ");
    	}
    	
    	exprList.genC(pw);
    	pw.print(")");
    	
    }
	
    
	private Variable object;
	private Method method;
	private ExprList exprList;    
}    