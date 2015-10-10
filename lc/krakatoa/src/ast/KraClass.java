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

public class KraClass extends Type {

	public KraClass( String name ) {
		super(name);
	}

	public String getCname() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public KraClass getSuperclass() {
		return superclass;
	}

	public void setSuperclass(KraClass superclass) {
		this.superclass = superclass;
	}

	public InstanceVariableList getInstanceVariableList() {
		return instanceVariableList;
	}

	public void setInstanceVariableList(InstanceVariableList
			instanceVariableList) {
		this.instanceVariableList = instanceVariableList;
	}
	
	public boolean containsPublicMethod(String methodName) {
		if (this.publicMethodList.containsMethod(methodName)) {
			return true;
		}
		else return false;
	}
	
	public boolean containsPrivateMethod(String methodName) {
		if (this.privateMethodList.containsMethod(methodName)) {
			return true;
		}
		else return false;
	}
	
	public boolean containsInstanceVariable(String varName) {
		if (this.instanceVariableList.containsVariable(varName)) {
			return true;
		}
		else return false;
	}
	
	public void addInstanceVariable(InstanceVariable var) {
		this.instanceVariableList.addElement(var);
	}
	
	public void addPublicMethod(Method method) {
		this.publicMethodList.addElement(method);
	}
	
	public void addPrivateMethod(Method method) {
		this.privateMethodList.addElement(method);
	}
	
	public Method getPublicMethod(String method) {
		return this.publicMethodList.getMethod(method);
	}
	
	public Method getPrivateMethod(String method) {
		return this.privateMethodList.getMethod(method);
	}

	private String name;
	private KraClass superclass;
	private InstanceVariableList instanceVariableList;
	private InstanceVariableList staticVariableList;
	private MethodList publicMethodList, privateMethodList;
	
	public boolean containsStaticVariable(String varName) {
		if (this.staticVariableList.containsVariable(varName)) {
			return true;
		}
		else return false;
	}
	
	public boolean containsStaticPrivateMethod(String methodName) {
		if (this.staticPrivateMethodList.containsMethod(methodName)) {
			return true;
		}
		else return false;
	}
	
	public boolean containsStaticPublicMethod(String methodName) {
		if (this.staticPublicMethodList.containsMethod(methodName)) {
			return true;
		}
		else return false;
	}
	
	public void addStaticVariable(InstanceVariable var) {
		this.staticVariableList.addElement(var);
	}
	
	private MethodList staticPublicMethodList, staticPrivateMethodList;

	public void addStaticPublicMethod(Method method) {
		this.staticPublicMethodList.addElement(method);
	}

	public void addStaticPrivateMethod(Method method) {
		this.staticPrivateMethodList.addElement(method);
	}
	
}
