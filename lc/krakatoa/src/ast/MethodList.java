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

public class MethodList {

	public MethodList() {
		this.methodList = new ArrayList<Method>();
	}

	public Method getMethod(String m) {
		for (Method method : methodList)
			if (method.getName().equals(m))
				return method;
		return null;
	}

	public boolean addElement(Method v) {
		return methodList.add(v);
	}

	public boolean containsMethod(String m) {
		for (Method method : methodList)
			if (method.getName().equals(m))
				return true;
		return false;
	}
	
	public void genKra(PW pw, boolean publicMethod) {
		for (Method method : methodList) {
			if (method != null) {
				method.genKra(pw, false);
			}
		}
	}
	
	private ArrayList<Method> methodList;

}
