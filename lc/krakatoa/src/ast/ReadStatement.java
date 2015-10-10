package ast;

import java.util.*;

public class ReadStatement extends Statement {

	public ReadStatement (ArrayList<InstanceVariable> ReadStmt) {
		for ( Variable var : ReadStmt ) {
			this.ReadStmt.addElement(var);
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
	
	private VariableList ReadStmt; 

}
