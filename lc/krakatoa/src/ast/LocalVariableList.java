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

public class LocalVariableList {

	public LocalVariableList() {
		localList = new ArrayList<Variable>();
	}

	public void addElement(Variable v) {
		localList.add(v);
	}

	public Iterator<Variable> elements() {
		return localList.iterator();
	}

	public int getSize() {
		return localList.size();
	}

	public void genKra(PW pw) {
		if (!localList.isEmpty()) {
			for (Variable var : localList) {
				if (var != null) {
					pw.printIdent("");
					pw.print(var.getType().getName() + " ");
					pw.print(var.getName());
					pw.println(";");
				}
			}
		}
	}
	
	public void genC(PW pw) {
		if (!localList.isEmpty()) {
			for (Variable var : localList) {
				if (var != null) {
					if (var.getType() instanceof KraClass) {
						pw.printIdent(var.getType().getCname());
						pw.println(" _" + var.getName() + ";");
					}
					else {
						pw.printIdent(var.getType().getName());
						pw.println(" _" + var.getName() + ";");
					}
				}
			}
		}
	}
	
	private ArrayList<Variable> localList;

}
