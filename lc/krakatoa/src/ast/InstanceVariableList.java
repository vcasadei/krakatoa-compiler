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

public class InstanceVariableList {

	public InstanceVariableList() {
		instanceVariableList = new ArrayList<InstanceVariable>();
	}

	public void addElement(InstanceVariable instanceVariable) {
		instanceVariableList.add(instanceVariable);
	}

	public Iterator<InstanceVariable> elements() {
		return this.instanceVariableList.iterator();
	}

	public int getSize() {
		return instanceVariableList.size();
	}

	public Variable getVariable(String varName) {
		for (Variable var : this.instanceVariableList)
			if (var.getName().equals(varName))
				return var;
		return null;
	}

	public boolean containsVariable(String varName) {
		if (this.getVariable(varName) != null)
			return true;
		return false;
	}

	private ArrayList<InstanceVariable> instanceVariableList;

}
