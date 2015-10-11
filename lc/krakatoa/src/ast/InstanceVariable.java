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

public class InstanceVariable extends Variable {

	public InstanceVariable(String name, Type type, boolean isStatic) {
		super(name, type);
		this.isStatic = isStatic;
	}

	private boolean isStatic;

}