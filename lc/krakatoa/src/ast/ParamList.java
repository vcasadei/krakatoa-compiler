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
       paramList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       paramList.add(v);
    }

    public Iterator<Variable> elements() {
        return paramList.iterator();
    }

    public int getSize() {
        return paramList.size();
    }
    
    public ArrayList<Variable> getList() {
    	return paramList;
    }

    private ArrayList<Variable> paramList;

}
