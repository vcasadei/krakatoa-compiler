/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.Iterator;


public class MessageSendToSelf extends MessageSend {

	public MessageSendToSelf(KraClass object, InstanceVariable instanceVariable, Method method, ExprList exprList) {
		
		this.object = object;
		this.instanceVariable = instanceVariable;
		this.method = method;
		this.exprList = exprList;
		
	}
    
	
    public Type getType() { 
    	
        if (instanceVariable == null && method == null) {
        	return object;
        }
        else if (instanceVariable != null && method == null) {
        	return instanceVariable.getType();
        }
        else {
        	return method.getReturnType();
        }
        
    }
    
    
    public void genC(PW pw, boolean putParenthesis) {
    	
    	if (instanceVariable == null && method == null) {
    		pw.print("this");
    	}
    	else if (instanceVariable != null && method == null) {
    		pw.print("this->_" + object.getName() + "_" + instanceVariable.getName());
    	}
    	else if (instanceVariable == null && method != null) {
    		
    		if (object.containsPrivateMethod(method.getName())) {
    			pw.print("_" + object.getName() + "_" + method.getName()
    					+ "(this");
    			
    			if (exprList.size() > 0) {
    				
    				pw.print(", ");
    				
    			}
    			
    			exprList.genC(pw);
    			
    			pw.print(")");
    		}
    		else {
    			
    			pw.print("((" + method.getReturnType().getCname() + " (*) ");
    	    	pw.print("(" + object.getCname() + " *");
    	    	
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
    	    	
    	    	int i = object.getVtIndex(method.getName());
    	    	
    	    	pw.print(")) this->vt[" + i + "]) ((" + object.getCname() + " *) this");
    	    	
    	    	if (exprList.size() > 0) {
    	    		pw.print(", ");
    	    	}
    	    	
    	    	exprList.genC(pw);
    	    	pw.print(")");
    			
    		}

    	}
    	else {
    		pw.print("((" + method.getReturnType().getCname() + " (*) ");
    		pw.print("(" + instanceVariable.getType().getCname() + " *");
    		
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
    		
    		int i = object.getVtIndex(method.getName());
    		
    		pw.print(")) this->_" + object.getName() + "_" + instanceVariable.getName());
    		
    		pw.print(".vt[" + i + "]) (&this->_");
    		
    		pw.print(object.getName() + "_" + instanceVariable.getName());
    		
    		if (exprList.size() > 0) {
    			pw.print(", ");
    		}
    		
    		exprList.genC(pw);
    		pw.print(")");
    	}
    	
    }
	
    
	private KraClass object;
	private InstanceVariable instanceVariable;
	private Method method;
	private ExprList exprList;
    
    
}