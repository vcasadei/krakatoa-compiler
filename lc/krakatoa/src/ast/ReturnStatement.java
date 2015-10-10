package ast;

public class ReturnStatement extends Statement {

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
	}

	public void genKra(PW pw) {
		pw.printIdent("return ");
		expr.genKra(pw, false);
		pw.println(";");
	}

	public ReturnStatement(Expr expr) {
		this.expr = expr;
	}

	private Expr expr;
}
