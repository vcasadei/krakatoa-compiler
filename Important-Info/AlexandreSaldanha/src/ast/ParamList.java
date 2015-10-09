/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.*;

public class ParamList {

    public ParamList() {
    	
       paramList = new ArrayList<Parameter>();
       
    }

    
    public boolean addElement(Parameter v) {
    	
       return paramList.add(v);
       
    }

    
    public Iterator<Parameter> elements() {
    	
        return paramList.iterator();
        
    }

    
    public int getSize() {
    	
        return paramList.size();
        
    }
    
    
    public void genC(PW pw) {
    	
    	int i = paramList.size();
    	
    	for (Parameter p : paramList) {
    		p.genC(pw);
    		
    		if (i != 1) {
    			pw.print(", ");
    		}
    		
    		i--;
    	}
    	
    }

    
    private ArrayList<Parameter> paramList;

}
