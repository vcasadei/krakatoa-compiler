/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public Type getType() {
        return Type.intType;
    }
    
    public void genC(PW pw, boolean putParenthesis) {
    	pw.print(String.valueOf(value));
    }
    
    private int value;
}
