/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class NullExpr extends Expr {
    
   public void genC(PW pw, boolean putParenthesis) {
      pw.print("NULL");
   }
   
   public Type getType() {
      return Type.undefinedType;
   }
}