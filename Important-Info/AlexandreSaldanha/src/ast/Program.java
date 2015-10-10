/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.*;

public class Program {

	public Program(ArrayList<KraClass> classList) {
		
		this.classList = classList;
		
	}


	public void genC(PW pw) {
		
		// Bibliotecas usadas no código em C
		pw.println("#include <stdio.h>");
		pw.println("#include <stdlib.h>");
		pw.println("#include <malloc.h>");
		
		pw.println("");
		
		// Definição do tipo boolean
		pw.println("typedef int boolean;");
		pw.println("");
		pw.println("#define true 1");
		pw.println("#define false 0");
		
		// Definição do tipo Func, um ponteiro para função
		pw.println("typedef void (*Func)();");
		
		pw.println("");
		
		KraClass classProgram = null;
		// Gera os códigos das classes
		for (KraClass aClass : classList) {
			
			aClass.genC(pw);
			pw.printlnIdent("");
			
			if (aClass.getName().equals("Program")) {
				
				classProgram = aClass;
				
			}
			
		}
		
		// Main
		pw.println("int main() {");
		pw.add();
		pw.printlnIdent("_class_Program *program;");
		pw.printlnIdent("program = new_Program();");
		
		int i = classProgram.getVtIndex("run");
		
		pw.printlnIdent("((void (*) (_class_Program *)) program->vt[" + i + "]) (program);");
		pw.printlnIdent("return 0;");
		pw.sub();
		pw.println("}");
		
	}

	private ArrayList<KraClass> classList;
}