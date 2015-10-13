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

public class ObjectBuilder extends Expr {

	public ObjectBuilder(KraClass obj) {
		this.obj = obj;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// If cast is needed
		if (castObject != null) {
			if (castObject.getName() != obj.getName()) {
				pw.print("(" + castObject.getName() + " ) ");
			}
		}
		
		pw.print("new " + obj.getName() + "();");
	}

	@Override
	public Type getType() {
		return this.obj;
	}

	// Getters and Setters
	public KraClass getObj() {
		return obj;
	}

	public void setObj(KraClass obj) {
		this.obj = obj;
	}

	public KraClass getCastObject() {
		return castObject;
	}

	public void setCastObject(KraClass castObject) {
		this.castObject = castObject;
	}

	private KraClass obj;
	private KraClass castObject;

}
