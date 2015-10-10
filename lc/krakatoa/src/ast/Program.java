package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<KraClass> classList,
			ArrayList<MetaobjectCall> metaobjectCallList,
			ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}

	public void genKra(PW pw) {
		// Iterates over the classes and generates the Krakatoa code for each of them
		for (KraClass aClass : classList) {
			aClass.genKra(pw);
			pw.printlnIdent("");
		}
	}

	public void genC(PW pw) {
	}

	public ArrayList<KraClass> getClassList() {
		return classList;
	}

	public ArrayList<MetaobjectCall> getMetaobjectCallList() {
		return metaobjectCallList;
	}

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}

	private ArrayList<KraClass> classList;
	private ArrayList<MetaobjectCall> metaobjectCallList;

	ArrayList<CompilationError> compilationErrorList;

}