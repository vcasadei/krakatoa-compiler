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

import java.util.ArrayList;

/**
 * This class represents a metaobject call as <code>{@literal @}ce(...)</code>
 * in <br>
 * <code>
 * 
 * @ce(5, "'class' expected") <br> clas Program <br> public void run() { } <br>
 *        end <br> </code>
 * 
 @author Jos�
 */
public class MetaobjectCall {

	public MetaobjectCall(String name, ArrayList<Object> paramList) {
		this.name = name;
		this.paramList = paramList;
	}

	public ArrayList<Object> getParamList() {
		return paramList;
	}

	public String getName() {
		return name;
	}

	private String name;
	private ArrayList<Object> paramList;

}
