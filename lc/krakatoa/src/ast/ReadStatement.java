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
		pw.print("read(");
		int size = ReadStmt.size();
		for (Variable var : ReadStmt) {
			pw.print(var.getName());
			if (--size > 0)
				pw.print(", ");
		}
	}

	private ArrayList<Variable> ReadStmt;

}
