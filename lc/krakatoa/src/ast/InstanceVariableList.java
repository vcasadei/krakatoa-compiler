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

	public boolean constainsVariable(String string) {
		for (InstanceVariable instVar : instanceVariableList)
			if (instVar.getName().equals(string))
				return true;
		return false;
	}

	public InstanceVariable getInstanceVariable(String string) {
		for (InstanceVariable instVar : instanceVariableList)
			if (instVar.getName().equals(string))
				return instVar;
		return null;
	}
	
	public void genKra(PW pw) {
		for (InstanceVariable instVar : instanceVariableList) {
			instVar.genKra(pw);
		}
		if(!this.instanceVariableList.isEmpty()) {
            pw.println("");
		}
		
	}

	private ArrayList<InstanceVariable> instanceVariableList;

}
