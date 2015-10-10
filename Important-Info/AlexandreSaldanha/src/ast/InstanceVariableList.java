/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.*;

public class InstanceVariableList {

    public InstanceVariableList() {
    	
       instanceVariableList = new ArrayList<InstanceVariable>();
       
    }

    
    public boolean addElement(InstanceVariable instanceVariable) {
    	
       return instanceVariableList.add( instanceVariable );
       
    }

    
    public Iterator<InstanceVariable> elements() {
    	
    	return this.instanceVariableList.iterator();
    	
    }
	
    
	public boolean containsVariable(String name) {
		
		Iterator<InstanceVariable> it = instanceVariableList.iterator();
		
		while (it.hasNext()) {
			if (it.next().getName().equals(name)) {
				return true;
			}
		}
		
		return false;
		
	}

	
    public int getSize() {
    	
        return instanceVariableList.size();
        
    }
	
    
	public InstanceVariable getVariable(String name) {
		
		Iterator<InstanceVariable> it = instanceVariableList.iterator();
		
		while (it.hasNext()) {
			InstanceVariable iVariable = it.next();
			
			if (iVariable.getName().equals(name)) {
				return iVariable;
			}
		}
		
		return null;
		
	}
	
	
	public void genC(PW pw, String className) {
		
		for (InstanceVariable instanceVariable : instanceVariableList) {
			instanceVariable.genC(pw, className);
		}
		
	}

	
    private ArrayList<InstanceVariable> instanceVariableList;

}
