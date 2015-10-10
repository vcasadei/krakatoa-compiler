package ast;

import java.util.*;

public class ReadStatement extends Statement {

	public ReadStatement(ArrayList<Variable> ReadStmt) {
		this.ReadStmt = ReadStmt;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub

	}

	private ArrayList<Variable> ReadStmt;

}
