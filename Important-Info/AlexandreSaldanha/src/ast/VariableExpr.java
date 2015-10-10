/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
    	
        this.v = v;
        
    }
    
    
    public void genC( PW pw, boolean putParenthesis ) {
    	
        pw.print("_" + v.getName());
        
    }
    
    
    public Type getType() {
    	
        return v.getType();
        
    }
    
    
    private Variable v;
}