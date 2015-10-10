/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass(String name) {
	   
      super(name);
      instanceVariableList = new InstanceVariableList();
      staticVariableList = new InstanceVariableList();
      staticPublicList = new MethodList();
      staticPrivateList = new MethodList();
      privateMethodList = new MethodList();
      publicMethodList = new MethodList();
      
   }
   
   
   public String getCname() {
	   
      return "_class_" + getName();
      
   }
   
   
   public boolean isFinal() {
	   
	   return finalClass;
	   
   }
   
   
   public void setFinal(boolean finalClass) {
	   
	   this.finalClass = finalClass;
	   
   }
   
   public KraClass getSuperclass() {
	   
	   return superclass;
	   
   }
   
   
   public void setSuperclass(KraClass superclass) {
	   
	   this.superclass = superclass;
	   
   }
   
   
   public boolean addInstanceVariable(InstanceVariable instanceVariable) {
	   
	   return instanceVariableList.addElement(instanceVariable);
	   
   }
   
   
   public boolean addStaticVariable(InstanceVariable instanceVariable) {
	   
	   return staticVariableList.addElement(instanceVariable);
	   
   }
   
   public InstanceVariable getInstanceVariable(String name) {
	   
		return instanceVariableList.getVariable(name);
		
   }
   
   public InstanceVariableList getInstanceVariableList() {
	   
	   return instanceVariableList;
	   
   }
   
   
   public boolean containsInstanceVariable(String iVariable) {
	   
		return instanceVariableList.containsVariable(iVariable);
		
   }
   
   
   public boolean containsStaticVariable(String staticVar) {
	   
	   return staticVariableList.containsVariable(staticVar);
	   
   }
   
   
   public boolean addPublicMethod(Method m) {
	   
	   return publicMethodList.addElement(m);
	   
   }
   
   
   public boolean addStaticPublicMethod(Method m) {
	   
	   return staticPublicList.addElement(m);
	   
   }
   
   
   public boolean containsPublicMethod(String methodName) {
	   
		return publicMethodList.containsMethod(methodName);
		
   }
   
   
   public boolean containsStaticPublicMethod(String methodName) {
	   
	   return staticPublicList.containsMethod(methodName);
	   
   }
   
   
   public Method getPublicMethod(String methodName) {
	   
		return publicMethodList.getMethod(methodName);
		
   }
   
   public int getVtIndex(String methodName) {
	   for (int i = 0; i < vtList.length; i++) {
		   if (vtList[i].equals(methodName)) {
			   return i;
		   }
	   }
	   
	   return -1;
   }
   
   public MethodList getPublicMethodList() {
	   
	   return publicMethodList;
	   
   }
   
   
   public Method getStaticPublicMethod(String methodName) {
	   
	   return staticPublicList.getMethod(methodName);
	   
   }
   
   
   public boolean addPrivateMethod(Method m) {
	   
	   return privateMethodList.addElement(m);
	   
   }
   
   
   public boolean addStaticPrivateMethod(Method m) {
	   
	   return staticPrivateList.addElement(m);
	   
   }
   
   
   public boolean containsPrivateMethod(String methodName) {
	   
		return privateMethodList.containsMethod(methodName);
		
   }
   
   
   public boolean containsStaticPrivateMethod(String methodName) {
	   
	   return staticPrivateList.containsMethod(methodName);
	   
   }
	
   
   public Method getPrivateMethod(String methodName) {
	   
	   return privateMethodList.getMethod(methodName);
	   
   }

   
   public Method getStaticPrivateMethod(String methodName) {
	   
	   return staticPrivateList.getMethod(methodName);
	   
   }
   
   
   private ArrayList<String> makeMethodList() {
	    
	    ArrayList<String> methodList = new ArrayList<>();
	    
	    if (superclass != null) {
	     methodList = superclass.makeMethodList();
	    }
	    
	    Iterator<Method> methodIt = this.publicMethodList.elements();
	    
	    while (methodIt.hasNext()) {
	     String currentMethodName = methodIt.next().getName();
	     int index = methodList.indexOf(currentMethodName);
	     if (index > 0) {
	      methodList.remove(index - 1);
	      methodList.add(index - 1, getName());
	     }     
	     else {
		     methodList.add(getName());
		     methodList.add(currentMethodName);
	     }
	     
	}
	    
	    
	    
	    return methodList;
	    
   }
   
   private void genCVarList(PW pw) {
	   
	   if (superclass != null) {
		   superclass.genCVarList(pw);
	   }
	   
	   instanceVariableList.genC(pw, getName());
	   
   }
	
   public void genC(PW pw) {
	   
	   /*if (isFinal()) {
			pw.printIdent("final ");
		}*/
		
	   // Estrutura em C para representar a classe
	   pw.println("typedef struct _St_" + getName() + " {");
	   pw.add();
	   pw.printlnIdent("Func *vt;"); // ponteiro para vetor de métodos da classe
	   
	   // Coloca-se as variaveis da classe pai antes da classe filha e da propria classe
	   genCVarList(pw);
	   
	   pw.sub();
	   pw.println("} " + getCname() + ";");
	   
	   pw.println("");
	   
	   // Protótipo de um método que cria um objeto da classe
	   pw.println(getCname() + " *new_" + getName() + "(void);");
	   
	   pw.println("");
	   
	   ArrayList<String> methodList = makeMethodList();
	   
	   Iterator<String> auxIt = methodList.iterator();
	   int i = 0;
	   vtList = new String[methodList.size() / 2];
	   while (auxIt.hasNext()) {
		   auxIt.next();
		   String methodName = auxIt.next();
		   vtList[i] = methodName;
		   i++;
	   }
	   
	   // Gera o código para cada método da classe, seja ele privado ou publico
	   publicMethodList.genC(pw, getName());
	   privateMethodList.genC(pw, getName());
	   
	   pw.println("");
	   
	   // Vetor de ponteiros para os métodos publicos da classe
	   pw.println("Func VTclass_" + getName() + "[] = {");
	   pw.add();
	   
	   Iterator<String> it = methodList.iterator();
	   while (it.hasNext()) {
		   String className = it.next();
		   String methodName = it.next();
		   
		   pw.printIdent("(void (*) ()) _" + className + "_" + methodName);
		   i++;
		   
		   if (it.hasNext()) {
			   pw.println(",");
		   }
		   else {
			   pw.println("");
		   }
	   }
	   
	   pw.sub();
	   pw.println("};");
	   
	   pw.println("");
	   
	   // Código para o método que cria um 'objeto' da classe em C
	   pw.println(getCname() +" *new_" + getName() + "() {");
	   pw.add();
	   pw.printlnIdent(getCname() + " *t;");
	   pw.printlnIdent("if ((t = malloc(sizeof(" + getCname() + "))) != NULL) {");
	   pw.add();
	   pw.printlnIdent("t->vt = VTclass_" + getName() + ";");
	   pw.sub();
	   pw.printlnIdent("}");
	   pw.printlnIdent("return t;");
	   pw.sub();
	   pw.print("}");
	   
	   pw.println("");
   
   }
   
   
   private KraClass superclass;
   private InstanceVariableList instanceVariableList;
   private boolean finalClass;
   private InstanceVariableList staticVariableList;
   private MethodList staticPublicList;
   private MethodList staticPrivateList;
   private MethodList publicMethodList;
   private MethodList privateMethodList;
   private String[] vtList;
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}
