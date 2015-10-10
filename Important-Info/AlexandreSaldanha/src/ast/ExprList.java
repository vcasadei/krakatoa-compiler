/*****************************************************************************
 *                     Laborat�rio de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.*;

public class ExprList {

    public ExprList() {
    	
        exprList = new ArrayList<Expr>();
        
    }
    

    public void addElement( Expr expr ) {
    	
        exprList.add(expr);
        
    }
	
    
	public int size() {
		
		return exprList.size();
		
	}
	
	
	public Iterator<Expr> elements() {
		
		return exprList.iterator();
		
	}
	

    public void genC( PW pw ) {

        int size = exprList.size();
        
        for (Expr e : exprList) {
        	
        	e.genC(pw, false);
        	
            if (--size > 0) {
                pw.print(", ");
            }
        }
        
    }

    
    private ArrayList<Expr> exprList;

}
