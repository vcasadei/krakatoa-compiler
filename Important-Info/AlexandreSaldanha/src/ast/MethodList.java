/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class MethodList {
	
	public MethodList() {
		
		methodList = new ArrayList<Method>();
		
	}

	
    public boolean addElement(Method v) {
    	
    	return methodList.add(v);
    	
    }

    
    public Iterator<Method> elements() {
    	
    	return methodList.iterator();
    	
    }
    
    public ListIterator<Method> listElements() {
    	
    	return methodList.listIterator();
    	
    }

    
	public int getSize() {
		
		return methodList.size();
		
    }
	
	
	public Method getMethod(String methodName) {
		
		Iterator<Method> it = methodList.iterator();
		
		while (it.hasNext()) {
			Method m = it.next();
			
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		
		return null;
		
	}
	
	public int getMethodIndex(String methodName) {
		
		Iterator<Method> it = methodList.iterator();
		int i = 0;
		
		while (it.hasNext()) {
			Method m = it.next();
			
			if (m.getName().equals(methodName)) {
				return i;
			}
			
			i++;
		}
		
		return -1;
		
	}
	
	
	public boolean containsMethod(String methodName) {
		
		Iterator<Method> it = methodList.iterator();
		
		while (it.hasNext()) {
			if (it.next().getName().equals(methodName)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	
	public void genC(PW pw, String className) {
		
		for (Method m : methodList) {
			m.genC(pw, className);
		}
		
	}

	
	private ArrayList<Method> methodList;

}
