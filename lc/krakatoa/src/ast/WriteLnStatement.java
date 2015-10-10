package ast;

import java.util.*;

public class WriteLnStatement extends Statement {

	public WriteLnStatement(ArrayList<Expr> writeLnStmt) {
		for ( Expr expr : writeLnStmt ) {
			this.writeLnStmt.addElement(expr);
		}
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub

	}

	private ExprList writeLnStmt;
	
}
