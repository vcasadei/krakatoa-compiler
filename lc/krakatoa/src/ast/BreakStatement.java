package ast;

public class BreakStatement extends Statement{
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("break;");
	}
	
}
