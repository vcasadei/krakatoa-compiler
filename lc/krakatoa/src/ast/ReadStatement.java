/**
 * Laboratório de Compiladores [2015/2] <br>
 * Orientação: Prof. Dr. José de O. Guimarães <br>
 * 
 * @author Maurício Spinardi | 401874 <br>
 * @author Vitor Casadei | 408301 <br>
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

import java.util.*;

public class ReadStatement extends Statement {

	public ReadStatement(ArrayList<Variable> ReadStmt) {
		this.ReadStmt = ReadStmt;
	}

	@Override
	public void genC(PW pw, String className) {
		int i = ReadStmt.size();
		for (Variable v : ReadStmt) {
			if (v.getType() == Type.intType) {
				pw.printlnIdent("{");
				
				pw.add();
				pw.printlnIdent("char __s[512];");
				pw.printlnIdent("gets(__s);");
				pw.printIdent("sscanf(__s, \"%d\", ");
				
				// If is a variable of the class (instance variable)
				if (v instanceof InstanceVariable) {
					pw.print("&this->");
					pw.println("_" + className + "_" + v.getName() + ");");
				} else {
					// If it's simply a variable of the method
					pw.println("&_" + v.getName() + ");");
				}
				pw.sub();
				pw.printlnIdent("}");
			} else {
				if (v.getType() == Type.stringType) {
					pw.printlnIdent("{");
					
					pw.add();
					pw.printlnIdent("char __s[512];");
					pw.printlnIdent("gets(__s);");
					pw.printlnIdent("_" + v.getName() + " = malloc(strlen(__s) + 1);");
					pw.println("strcpy(_" + v.getName() + ", __s);");
					pw.sub();
					
					pw.printlnIdent("}");
				}
			}
			
			
			
		}

	}

	@Override
	public void genKra(PW pw) {
		pw.print("read(");
		int i = ReadStmt.size();
		for (Variable v : ReadStmt) {
			pw.print(v.getName());
			if (--i > 0) {
				pw.print(", ");
			}
			i++;
		}
		pw.println(");");
	}

	private ArrayList<Variable> ReadStmt;

}
