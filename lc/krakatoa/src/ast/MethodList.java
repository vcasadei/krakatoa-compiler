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

	private ArrayList<Method> methodList;

}
