/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.Iterator;

public class ReadStatement extends Statement {

	public ReadStatement(VariableList readList) {
		
		this.readList = readList;
		
	}
	
	
	@Override
	public void genC(PW pw) {
		
		Iterator<Variable> it = readList.elements();
		
		while (it.hasNext()) {
			
			Variable v = it.next();
			
			if (v.getType() == Type.intType) {
				
				pw.printlnIdent("{");
				pw.add();
				pw.printlnIdent("char __s[512];");
				pw.printlnIdent("gets(__s);");
				pw.printIdent("sscanf(__s, \"%d\", ");
				
				if (v instanceof InstanceVariable) {
					
					pw.print("&this->");
					pw.println(v.getType().getCname() + "_" + v.getName() + ");");
					
				}
				else {
					
					pw.println("&_" + v.getName() + ");");
					
				}
				
				pw.sub();
				pw.printlnIdent("}");
				
			}
			else if (v.getType() == Type.stringType) {
				
				pw.printlnIdent("{");
				pw.add();
				pw.printlnIdent("char __s[512];");
				pw.printlnIdent("gets(__s);");
				
				if (v instanceof InstanceVariable) {
					
					pw.printIdent("&this->");
					pw.print(v.getType().getCname() + "_" + v.getName());
					pw.println(" = malloc (strlen(__s) + 1);");
					pw.printIdent("strcpy (&this->");
					pw.print(v.getType().getCname() + "_" + v.getName());
					pw.println(", __s);");
					
				}
				else {
					
					pw.printlnIdent("_" + v.getName() + " = malloc (strlen(__s) + 1);");
					pw.printlnIdent("strcpy (_" + v.getName() + ", __s);");
					
				}
				
				pw.sub();
				pw.printlnIdent("}");
				
			}
			
		}
		
		//readList.genKra(pw);
		
		
	}
	
	
	private VariableList readList;

}
