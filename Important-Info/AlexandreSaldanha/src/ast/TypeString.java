/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class TypeString extends Type {
    
    public TypeString() {
    	
        super("String");
        
    }
    
    
   public String getCname() {
	   
      return "char*";
      
   }

   
}