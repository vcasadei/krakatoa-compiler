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

	public InstanceVariable(String name, Type type, boolean isStatic, boolean isFinal) {
		super(name, type);
		this.isStatic = isStatic;
		this.isFinal = isFinal;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public boolean isFinal() {
		return isFinal;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public void genKra(PW pw) {
		pw.printIdent("");
		if(this.isStatic) {
            pw.print("static ");
        }
		if(isFinal) {
            pw.print("final ");
        }
        pw.print("private ");
        pw.print(this.getType().getName());
        pw.print(" ");
        pw.print(this.getName());
        pw.println(";");
	}
	
	public void genC(PW pw, String kraClass) {
		pw.printIdent(getType().getCname());
		pw.println(" _" + kraClass + "_" + getName() + ";");
	}
	
	private boolean isStatic;
	private boolean isFinal;

}