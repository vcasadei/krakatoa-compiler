/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.*;

public class VariableList {

	public VariableList(ArrayList<Variable> variableList) {
		this.variableList = variableList;
	}
	
	public Iterator<Variable> elements() {
		return variableList.iterator();
	}
	
	public int size() {
		return variableList.size();
	}
	
	public void genKra(PW pw) {
		int i = variableList.size();
		
		for (Variable v : variableList) {
			pw.print(v.getName());
			
			if (i != 1) {
				pw.print(", ");
			}
			
			i--;
		}
	}
	
	private ArrayList<Variable> variableList;
}
