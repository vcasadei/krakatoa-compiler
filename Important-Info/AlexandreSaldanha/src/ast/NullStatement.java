/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class NullStatement extends Statement {
	
	public void genC(PW pw) {
		
		pw.printlnIdent(";");
		
	}
}