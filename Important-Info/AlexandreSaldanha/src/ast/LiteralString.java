/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
    	
        this.literalString = literalString;
        
    }
    
    
    public Type getType() {
    	
        return Type.stringType;
        
    }
    
    
    public void genC(PW pw, boolean putParenthesis) {
    	
    	pw.print("\"" + literalString + "\"");
    	
    }
    
    
    private String literalString;
}
