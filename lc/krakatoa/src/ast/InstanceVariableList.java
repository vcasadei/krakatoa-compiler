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

	private ArrayList<InstanceVariable> instanceVariableList;

}
