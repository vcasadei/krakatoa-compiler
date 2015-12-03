/**
 * Laborat√≥rio de Compiladores [2015/2] <br>
 * Orienta√ß√£o: Prof. Dr. Jos√© de O. Guimar√£es <br>
 * 
 * @author Maur√≠cio Spinardi | 401874 <br>
 * @author Vitor Casadei | 408301 <br>
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

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
		for (KraClass kraClass : classList) {
				kraClass.genKra(pw);
				pw.println("");
		}
	}

	public void genC(PW pw) {
		pw.println("/* deve-se incluir alguns headers porque algumas funÁıes");
		pw.println(" * da biblioteca padr„o de C s„o utilizadas "
				+ "na traduÁ„o. */");
		pw.println("#include <malloc.h>");
		pw.println("#include <stdlib.h>");
		pw.println("#include <stdio.h>");
		
		pw.println("");
		pw.println("/* define o tipo boolean */");
		pw.println("typedef int boolean;");
		pw.println("#define true 1");
		pw.println("#define false 0");
		
		pw.println("");
		pw.println("/* define um tipo Func que È um ponteiro para funÁ„o */");
		pw.println("typedef");
		pw.add();
		pw.printlnIdent("void (*Func)();");
		pw.sub();
		
		pw.println("");
		KraClass program = null;
		for (KraClass kraClass : classList) {
			kraClass.genC(pw);
			pw.printlnIdent("");	
			if (kraClass.getName().equals("Program"))
				program = kraClass;
		}
		
		pw.add();
		pw.println("int main() {");
		pw.printlnIdent("_class_Program *program;");
		
		pw.println("");
		pw.printlnIdent("/* crie objeto da classe Program e envie a mensagem"
				+ " run para ele.");
		pw.printlnIdent(" * Nem sempre o n˙mero de run no vetor È 0. */");
		pw.printlnIdent("program = new_Program();");
		pw.printlnIdent("( ( void (*)(_class_Program *) ) program->vt[0] )"
				+ "(program);");
		pw.printlnIdent("return 0");
		pw.sub();
		pw.println("}");
		pw.println("");
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