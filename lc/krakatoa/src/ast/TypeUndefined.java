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

public class TypeUndefined extends Type {
	// variables that are not declared have this type

	public TypeUndefined() {
		super("undefined");
	}

	public String getCname() {
		return "int";
	}

}
