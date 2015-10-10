/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class InstanceVariable extends Variable {

    public InstanceVariable( String name, Type type ) {
    	
        super(name, type);
        
    }
    
    
    public void setStatic(boolean staticVariable) {
    	
    	this.staticVariable = staticVariable;
    	
    }
    
    
    public void genC(PW pw, String className) {
    	
    	/*
    	if (staticVariable) {
    		pw.printIdent("static private ");
    	}
    	else {
    		pw.printIdent("private ");
    	}*/
    	
    	pw.printlnIdent(getType().getCname() + " _" + className + "_" + getName() + ";");
    	
    }
    
    
    private boolean staticVariable;	// true se static, falso caso contrario

}