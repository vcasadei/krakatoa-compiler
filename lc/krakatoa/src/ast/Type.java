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

abstract public class Type {

	public Type(String name) {
		this.name = name;
	}

	public static Type booleanType = new TypeBoolean();
	public static Type intType = new TypeInt();
	public static Type stringType = new TypeString();
	public static Type voidType = new TypeVoid();
	public static Type undefinedType = new TypeUndefined();

	public String getName() {
		return name;
	}

	abstract public String getCname();

	private String name;
}
