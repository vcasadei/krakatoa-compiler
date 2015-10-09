/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class MessageSendStatement extends Statement { 
	
	public MessageSendStatement(Expr messageSend) {
		
		this.messageSend = messageSend;
		
	}


   public void genC( PW pw ) {
	   
      pw.printIdent("");
      messageSend.genC(pw, false);
      //pw.println("");
      pw.println(";");
      
   }

   
   private Expr  messageSend;

}


