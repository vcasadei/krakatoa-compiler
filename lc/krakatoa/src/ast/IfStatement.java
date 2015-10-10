package ast;

public class IfStatement extends Statement{

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
	}
	
	public IfStatement(Expr expr, Statement statementIf, Statement statementElse) {
		this.expr = expr;
		this.statementIf = statementIf;
		this.statementElse = statementElse;
	}
	
	private Expr expr = null;
	private Statement statementIf = null;
	private Statement statementElse = null;
	@Override
	public void genKra(PW pw) {
		pw.printIdent("if (");
		expr.genKra(pw, false);
		pw.println(") {");
		
		pw.add();
		statementIf.genKra(pw);
		pw.sub();
		
		pw.printIdent("}");
		
		if (statementElse != null) {
			pw.println(" else {");
			
			pw.add();
			statementElse.genKra(pw);
			pw.sub();
			
			pw.printIdent("}");
		}
		
	}
}
