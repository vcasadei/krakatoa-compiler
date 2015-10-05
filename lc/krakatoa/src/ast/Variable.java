/**
 * Laboratório de Compiladores 2015/2
 * Universidade Federal de São Carlos
 * Orientação: Prof. Dr. José de O. Guimarães
 * 
 * @author Maurício Spinardi 408174
 * @author Vitor Casadei 408301
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package ast;

public class Variable {

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }

    public Type getType() {
        return type;
    }

    private String name;
    private Type type;
    
}