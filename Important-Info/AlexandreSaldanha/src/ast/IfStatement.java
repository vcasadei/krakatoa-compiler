/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

public class IfStatement extends Statement {
	
	public IfStatement(Expr expression, Statement ifStatement, Statement elseStatement) {
		
		this.expression = expression;
		this.ifStatement = ifStatement;
		this.elseStatement = elseStatement;
		
	}
	
	
	public Statement getIf() {
		
		return ifStatement;
		
	}
	
	
	public Statement getElse() {
		
		return elseStatement;
		
	}
	

	@Override
	public void genC(PW pw) {
		
		pw.printIdent("if (");
		
		expression.genC(pw, false);
		
		pw.println(") {");
		
		pw.add();
		
		ifStatement.genC(pw);
		
		pw.sub();
		
		pw.printlnIdent("}");
		
		if (elseStatement != null) {
			pw.printlnIdent("else {");
			
			pw.add();
			
			elseStatement.genC(pw);
			
			pw.sub();
			
			pw.printlnIdent("}");
		}

	}
	
	
	private Expr expression;
	private Statement ifStatement;
	private Statement elseStatement;

}
