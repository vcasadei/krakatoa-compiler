/**
 * Laborat�rio de Compiladores 2015/2
 * Universidade Federal de S�o Carlos
 * Orienta��o: Prof. Dr. Jos� de O. Guimar�es
 * 
 * @author Maur�cio Spinardi 408174
 * @author Vitor Casadei 408301
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

import java.util.*;

public class ExprList {

	public ExprList() {
		exprList = new ArrayList<Expr>();
	}

	public void addElement(Expr expr) {
		exprList.add(expr);
	}

	public int getSize() {
		return exprList.size();
	}

	public Iterator<Expr> getElements() {
		return exprList.iterator();
	}

	public void genC(PW pw) {

		int size = this.getSize();
		for (Expr e : exprList) {
			e.genC(pw, false);
			if (--size > 0)
				pw.print(", ");
		}
	}

	public void genKra(PW pw) {

		int size = this.getSize();
		for (Expr e : exprList) {
			e.genKra(pw, false);
			if (--size > 0)
				pw.print(", ");
		}
	}

	private ArrayList<Expr> exprList;

	public ArrayList<Expr> getExprList() {
		return exprList;
	}

}
