package ast;

public class WhileStatement extends Statement {
	@Override
	public void genC(PW pw) {
	}

	public WhileStatement(Expr expr, Statement statement) {
		this.expr = expr;
		this.statement = statement;
	}

	private Expr expr;
	private Statement statement;

	@Override
	public void genKra(PW pw) {
		pw.print("while (");
		expr.genKra(pw, false);
		pw.println(") {");
		
		pw.add();
		statement.genKra(pw);
		pw.sub();
		
		pw.println("}");
	}
}
