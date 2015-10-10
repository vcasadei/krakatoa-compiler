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

public class InstanceVariable extends Variable {

	public InstanceVariable(String name, Type type) {
		super(name, type);
	}

	public InstanceVariable(String name, Type type, boolean b) {
		super(name, type);
		this.isStatic = b;
	}

	private boolean isStatic = false;

}