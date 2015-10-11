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

public class StatementList {

	public StatementList() {
		this.statementList = new ArrayList<Statement>();
	}

	public boolean addElement(Statement s) {
		return statementList.add(s);
	}

	public int getSize() {
		return statementList.size();
	}

	public void genKra(PW pw) {
		for (Statement stmt : statementList) {
			pw.printIdent("");
			stmt.genKra(pw);
		}
	}
	
	private ArrayList<Statement> statementList;

}
