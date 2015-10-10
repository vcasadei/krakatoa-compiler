package ast;

import java.util.*;

public class WriteStatement extends Statement {

	public WriteStatement(ArrayList<Expr> writeStmt) {
		for ( Expr expr : writeStmt ) {
			this.writeStmt.addElement(expr);
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
	
	private ExprList writeStmt;

}
