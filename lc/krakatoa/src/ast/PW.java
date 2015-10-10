/**
 * Laborat�rio de Compiladores 2015/2
 * Universidade Federal de S�o Carlos
 * Orienta��o: Prof. Dr. Jos� de O. Guimar�es
 * 
 * @author Maur�cio Spinardi 408174
 * @author Vitor Casadei 408301
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

import java.io.*;

public class PW {

	public void add() {
		currentIndent += step;
	}

	public void sub() {
		currentIndent -= step;
	}

	public void set(PrintWriter out) {
		this.out = out;
		currentIndent = 0;
	}

	public void set(int indent) {
		currentIndent = indent;
	}

	public void printIdent(String s) {
		out.print(space.substring(0, currentIndent));
		out.print(s);
	}

	public void printlnIdent(String s) {
		out.print(space.substring(0, currentIndent));
		out.println(s);
	}

	public void print(String s) {
		out.print(s);
	}

	public void println(String s) {
		out.println(s);
	}

	int currentIndent = 0;
	public int step = 3;
	private PrintWriter out;

	static final private String space = "                                                                                                        ";

}
