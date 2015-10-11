/**
 * Laboratório de Compiladores [2015/2] <br>
 * Orientação: Prof. Dr. José de O. Guimarães <br>
 * 
 * @author Maurício Spinardi | 401874 <br>
 * @author Vitor Casadei | 408301 <br>
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

import java.util.*;

public class ParamList {

	public ParamList() {
		paramList = new ArrayList<Parameter>();
	}

	public void addElement(Parameter v) {
		paramList.add(v);
	}

	public Iterator<Parameter> getElements() {
		return paramList.iterator();
	}

	public int getSize() {
		return paramList.size();
	}

	public ArrayList<Parameter> getList() {
		return paramList;
	}

	private ArrayList<Parameter> paramList;

}
