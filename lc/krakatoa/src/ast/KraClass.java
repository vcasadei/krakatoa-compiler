/**
 * Laborat√≥rio de Compiladores [2015/2] <br>
 * Orienta√ß√£o: Prof. Dr. Jos√© de O. Guimar√£es <br>
 * 
 * @author Maur√≠cio Spinardi | 401874 <br>
 * @author Vitor Casadei | 408301 <br>
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

import java.util.ArrayList;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {

	public KraClass(String name, boolean isFinal, boolean isStatic) {
		super(name);
		this.superclass = null;
		this.instanceVariableList = new InstanceVariableList();
		this.publicMethodList = new MethodList();
		this.privateMethodList = new MethodList();
		this.staticPublicMethodList = new MethodList();
		this.staticPrivateMethodList = new MethodList();
		this.staticVariableList = new InstanceVariableList();
		this.isFinal = isFinal;
		this.isStatic = isStatic;
	}

	public String getCname() {
		return getName();
	}

	public KraClass getSuperclass() {
		return superclass;
	}

	public void setSuperclass(KraClass superclass) {
		this.superclass = superclass;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean containsPublicMethod(String string) {
		return this.publicMethodList.containsMethod(string);
	}

	public boolean containsPrivateMethod(String string) {
		return this.privateMethodList.containsMethod(string);
	}

	public boolean containsStaticPrivateMethod(String string) {
		return this.staticPrivateMethodList.containsMethod(string);
	}

	public boolean containsStaticPublicMethod(String string) {
		return this.staticPublicMethodList.containsMethod(string);
	}

	public boolean containsInstanceVariable(String string) {
		return this.instanceVariableList.constainsVariable(string);
	}

	public boolean containsStaticVariable(String string) {
		return this.staticVariableList.constainsVariable(string);
	}

	public void addStaticVariable(InstanceVariable staticVariable) {
		staticVariableList.addElement(staticVariable);
	}

	public void addInstanceVariable(InstanceVariable instanceVariable) {
		instanceVariableList.addElement(instanceVariable);
	}

	public void addStaticPublicMethod(Method staticPublicMethod) {
		staticPublicMethodList.addElement(staticPublicMethod);
	}

	public void addStaticPrivateMethod(Method staticPrivateMethod) {
		staticPrivateMethodList.addElement(staticPrivateMethod);
	}

	public void addPublicMethod(Method publicMethod) {
		publicMethodList.addElement(publicMethod);
	}

	public void addPrivateMethod(Method privateMethod) {
		privateMethodList.addElement(privateMethod);
	}

	public Method getPublicMethod(String string) {
		return publicMethodList.getMethod(string);
	}

	public Method getPrivateMethod(String string) {
		return privateMethodList.getMethod(string);
	}

	public Method getStaticPublicMethod(String string) {
		return staticPublicMethodList.getMethod(string);
	}

	public Method getStaticPrivateMethod(String string) {
		return staticPrivateMethodList.getMethod(string);
	}

	public Variable getInstanceVariable(String string) {
		return instanceVariableList.getInstanceVariable(string);
	}

	public Variable getStaticVariable(String string) {
		return staticVariableList.getInstanceVariable(string);
	}

	public void genKra(PW pw) {
		if (isStatic) {
			pw.print("static ");
		}
		if (isFinal) {
			pw.print("final ");
		}
		pw.print("class " + this.getName());
		if (superclass != null) {
			pw.print(" extends " + superclass.getName());
		}
		pw.println(" {");
		pw.add();
		if (instanceVariableList != null) {instanceVariableList.genKra(pw);}
		if (staticVariableList != null) {staticVariableList.genKra(pw);}
		if (publicMethodList != null) {publicMethodList.genKra(pw, true);}
		if (privateMethodList != null) {privateMethodList.genKra(pw, false);}
		if (staticPublicMethodList != null) {staticPublicMethodList.genKra(pw, true);}
		if (staticPrivateMethodList != null) {staticPrivateMethodList.genKra(pw, false);}
		pw.sub(); 
		pw.println("}");
	}

	/*
	 * Permite recurs„o para varrer classes superiores e gerar cÛdigo para
	 * as respectivas vari·veis de inst‚ncia
	 */
	private void varC(PW pw) {
		if (superclass != null)
			superclass.varC(pw);
		instanceVariableList.genC(pw, getName());
	}

	public void genC(PW pw) {
		pw.println("typedef");
		pw.add();
		pw.printlnIdent("struct _St_" + getName() + " {");
		pw.add();
		pw.printlnIdent("Func *vt;");
		varC(pw);
		pw.sub();
		pw.printIdent("}");
		pw.println(" _class_" + getName() + ";");
		pw.sub();
		pw.println("");
		
		pw.println("_class_" + getName() + " *new_" + getName() + "(void);");
		pw.println("");
		
		staticPublicMethodList.genC(pw, getName());
		staticPrivateMethodList.genC(pw, getName());
		publicMethodList.genC(pw, getName());
		privateMethodList.genC(pw, getName());
		pw.println("");
		
		pw.println("Func VTclass_" + getName() + "[] = {");
		pw.add();
		
		ArrayList<String> publicMethodNames = publicMethodList.getNames();
		int size = publicMethodNames.size();
				
		for (String name : publicMethodNames) {
			pw.printIdent("_" + getName() + "_" + name);
			if (--size > 0) {
				pw.println(",");
			}
			else {
				pw.println("");
			}
		}
		
		pw.sub();
		pw.println("};");
		pw.println("");
	}
	
	private KraClass superclass;
	private InstanceVariableList instanceVariableList;
	private MethodList publicMethodList, privateMethodList;
	private MethodList staticPublicMethodList, staticPrivateMethodList;
	private InstanceVariableList staticVariableList;
	private boolean isFinal;
	private boolean isStatic;

}
