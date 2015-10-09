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
	
	private Expr expr;
	private Statement statementIf;
	private Statement statementElse;
	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		
	}
}
