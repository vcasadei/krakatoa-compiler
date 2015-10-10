/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.*;

public class LocalVarList {

    public LocalVarList() {
    	
       localList = new ArrayList<Variable>();
       
    }

    
    public boolean addElement(Variable v) {
    	
       return localList.add(v);
       
    }

    
    public Iterator<Variable> elements() {
    	
        return localList.iterator();
        
    }

    
    public int getSize() {
    	
        return localList.size();
        
    }
    
    
    public void genC(PW pw) {
    	
    	for (Variable v : localList) {
    		
    		Type variableType = v.getType();
    		
    		if (variableType instanceof KraClass) {
    			
    			pw.printlnIdent(variableType.getCname() + " *_" + v.getName() + ";");
    			    			
    		}
    		else {
    		
    			pw.printlnIdent(v.getType().getName() + " _" + v.getName() + ";");
    			
    		}
    		
    	}
    	
    }

    
    private ArrayList<Variable> localList;

}
