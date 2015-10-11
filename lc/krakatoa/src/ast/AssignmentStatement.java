package ast;

public class AssignmentStatement extends Statement {

	public AssignmentStatement(Expr esq, Expr dir) {
		this.esq = esq;
		this.dir = dir;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub

	}

	Expr esq, dir;

}
