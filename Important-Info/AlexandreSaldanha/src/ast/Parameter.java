/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;


public class Parameter extends Variable {

    public Parameter( String name, Type type ) {
    	
        super(name, type);
        
    }
    
    
    public void genC(PW pw) {
    	
    	pw.print(getType().getCname() + " _" + getName());
    	
    }
    

}