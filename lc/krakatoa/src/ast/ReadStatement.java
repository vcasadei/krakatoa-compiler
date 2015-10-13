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
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("read(");
		int i = ReadStmt.size();
		for (Variable v : ReadStmt) {
			//pw.print(v.getName());
			v.genKra(pw);
			if (--i > 0) {
				pw.print(", ");
			}
			i++;
		}
		pw.println(");");
	}

	private ArrayList<Variable> ReadStmt;

}
