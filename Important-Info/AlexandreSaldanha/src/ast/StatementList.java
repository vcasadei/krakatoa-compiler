/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class StatementList {
	
	public StatementList() {
		
		statementList = new ArrayList<Statement>();
		
	}
	
	
	public boolean addElement(Statement s) {
		
		return statementList.add(s);
		
	}
	
	
	public Iterator<Statement> elements() {
		
    	return statementList.iterator();
    	
    }

	
	public int getSize() {
		
		return statementList.size();
		
    }
	
	
	public void genC(PW pw) {
		
		for (Statement s : statementList) {
			s.genC(pw);
		}
		
	}
	
	
	private ArrayList<Statement> statementList;
}
