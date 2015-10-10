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

public class StatementList {

	public StatementList() {
		statementList = new ArrayList<Statement>();
	}

	public boolean addElement(Statement s) {
		return statementList.add(s);
	}

	public int getSize() {
		return statementList.size();
	}

	private ArrayList<Statement> statementList;

}
