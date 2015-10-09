/*****************************************************************************
 *                     LaboratÃ³rio de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class Method {
	
	public Method(String name, Type retorno) {
		
		this.name = name;
		this.retorno = retorno;
		
		paramList = new ParamList();
		
		statementList = new StatementList();
		
		localVarList = new LocalVarList();
		
	}
	
	
	public boolean addParameter(Parameter p) {
		
		return paramList.addElement(p);
		
	}
	
	
	public void setStatementList(StatementList statementList) {
		
		this.statementList = statementList;
		
	}
	
	
	public StatementList getStatementList() {
		
		return statementList;
		
	}
	
	
	public boolean addLocalVariable(Variable v) {
		
		return localVarList.addElement(v);
		
	}
	
	
	public Type getReturnType() {
		
		return retorno;
		
	}
	
	
	public String getName() {
		
		return name;
		
	}
	
	
	public ParamList getParamList() {
		
		return paramList;
		
	}
	
	
	public void setPrivate(boolean qualifier) {
		
		this.privateMethod = qualifier;
		
	}
	
	
	public void setStatic(boolean staticMethod) {
		
		this.staticMethod = staticMethod;
		
	}
	
	
	public boolean isPrivate() {
		
		return privateMethod;
		
	}
	
	
	public boolean isStatic() {
		
		return staticMethod;
		
	}
	
	
	public void genC(PW pw, String className) {
		
		/*
		if (qualifier) {
			pw.printIdent("private ");
		}
		else {
			pw.printIdent("public ");
		}
		*/
		
		// Monta o método em C
		pw.print(retorno.getCname() + " _" + className + "_" + getName()
				+ "(_class_" + className + " *this");
		if (paramList.getSize() > 0) {
			pw.print(", ");
		}
		
		paramList.genC(pw);
		
		pw.println(") {");
		
		pw.add();
		
		localVarList.genC(pw);
		
		statementList.genC(pw);
		
		pw.sub();
		
		pw.printlnIdent("}");
		
		pw.println("");
		
	}
	
	
	private String name;
	private ParamList paramList;
	private LocalVarList localVarList;
	private Type retorno;
	private StatementList statementList;
	private boolean staticMethod;	// true se static, falso caso contrario
	private boolean privateMethod; // true se privado, falso caso contrario
}
