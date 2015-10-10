/**
 * Laborat�rio de Compiladores 2015/2
 * Universidade Federal de S�o Carlos
 * Orienta��o: Prof. Dr. Jos� de O. Guimar�es
 * 
 * @author Maur�cio Spinardi 408174
 * @author Vitor Casadei 408301
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
    
    public void genKra(PW pw) {
    	int paramCounter = paramList.size();
    	for (Parameter p : paramList) {
    		p.genKra(pw);
    		if (paramCounter > 1) {
    			pw.print(", ");
    		}
    		paramCounter--;
    	}
    }

    private ArrayList<Parameter> paramList;

}
