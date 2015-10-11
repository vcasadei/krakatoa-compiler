package ast;

import java.util.*;

public class StatementList {

	public StatementList() {
		this.statementList = new ArrayList<Statement>();
	}

	public boolean addElement(Statement s) {
		return statementList.add(s);
	}

	public int getSize() {
		return statementList.size();
	}

	private ArrayList<Statement> statementList;

}
