/**
 * Laboratório de Compiladores 2015/2
 * Universidade Federal de São Carlos
 * Orientação: Prof. Dr. José de O. Guimarães
 * 
 * @author Maurício Spinardi 408174
 * @author Vitor Casadei 408301
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

import java.util.*;

public class MethodList {

	public MethodList() {
		methodList = new ArrayList<Method>();
	}

	public void addElement(Method v) {
		methodList.add(v);
	}

	public int getSize() {
		return methodList.size();
	}

	public Method getMethod(String methodName) {
		for (Method method : methodList)
			if (method.getName().equals(methodName)) return method;
		return null;
	}

	public boolean containsMethod(String methodName) {
		if (this.getMethod(methodName) != null) return true;
		return false;
	}

	private ArrayList<Method> methodList;
	
}