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

    public void genKra(PW pw) {
		pw.println(type + name);
	}
    
    private String name;
    private Type type;
    
}