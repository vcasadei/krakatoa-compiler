package ast;

public class TypeBoolean extends Type {

   public TypeBoolean() {
	   super("boolean");
   }

   @Override
   public String getCname() {
      return "boolean";
   }
   
   public String getKraname() {
	   return "boolean";
   }

}
