package ast;

public class CompositeStatement extends Statement {

	public CompositeStatement(StatementList statementList) {
		this.statementList = statementList;
	}

	public StatementList getStatementList() {
		return statementList;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		
	}
	
	private StatementList statementList;

}
