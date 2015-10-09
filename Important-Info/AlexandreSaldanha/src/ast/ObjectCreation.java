/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class ObjectCreation extends Expr {
	
	public ObjectCreation(KraClass classObj) {
		this.classObj = classObj;
	}
	
	
	public Type getType() {
		return classObj;
	}
	
	public void setCast(KraClass cast) {
		
		this.cast = cast;
		
	}
	
	
	public void genC(PW pw, boolean putParenthesis) {
		if (cast.getName() != classObj.getName()) {
			pw.print("(" + cast.getCname() + " *) ");
		}
		
		pw.print("new_" + classObj.getName() + "()");
		
	}
	
	
	private KraClass classObj;
	private KraClass cast;
	
}