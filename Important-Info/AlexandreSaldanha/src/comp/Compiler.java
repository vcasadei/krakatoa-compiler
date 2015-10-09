/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Compiler 																 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/
package comp;

import ast.*;
import lexer.*;

import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		error = new CompilerError(new PrintWriter(outError));
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, error);
		error.setLexer(lexer);

		Program program = null;
		try {
			lexer.nextToken();
			if ( lexer.token == Symbol.EOF )
				error.show("Unexpected end of file");
			program = program();
			if ( lexer.token != Symbol.EOF ) {
				program = null;
				error.show("End of file expected");
			}
		}
		catch (Exception e) {
			// the below statement prints the stack of called methods.
			// of course, it should be removed if the compiler were
			// a production compiler.

			// e.printStackTrace();
			program = null;
		}

		return program;
	}

	/**
	 * program()
	 * Program ::= KraClass { KraClass }
	 */
	private Program program() {
		
		ArrayList<KraClass> classList = new ArrayList<KraClass>();

		classDec();
		
		/* After each class was properly treated,
		 * it is added to a list of classes */
		
		// First class
		if (currentClass != null) {
			classList.add(currentClass);
		} else {
			error.show("Internal error");
		}
		
		// If there is more classes
		while (lexer.token == Symbol.CLASS) {
			classDec();
			
			if (currentClass != null) {
				classList.add(currentClass);
			} else {
				error.show("Internal error");
			}
			
		}
		
		// Verify if there is a class Program and if its the last one
		KraClass lastClass = classList.get(classList.size() - 1);
		if (!lastClass.getName().equals("Program")) {
			error.show("There is no class Program or it is not the last one");
		}
		
		return new Program(classList);
	}

	
	/**
	 * classDec()
	 * KraClass ::= "class" Id [ "extends" Id ] "{" MemberList "}"
	 * MemberList ::= { Qualifier Member } 
	 * Member ::= InstVarDec | MethodDec
	 * InstVarDec ::= Type IdList ";" 
	 * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
	 * Qualifier ::= [ "static" ]  ( "private" | "public" )
	 */
	private void classDec() {
	
		boolean isFinal = false;
		
		currentClass = null;
		currentMethod = null;
		
		// If is a final class
		if (lexer.token == Symbol.FINAL) {
			isFinal = true;
			lexer.nextToken();
		}
		
		if (lexer.token != Symbol.CLASS) {
			error.show("'class' expected");
		}
		
		lexer.nextToken();
		
		if (lexer.token != Symbol.IDENT) {
			error.show(CompilerError.ident_expected);
		}
		
		// Add the new class to the hash table
		String className = lexer.getStringValue();
		KraClass newClass = new KraClass(className);
		newClass.setFinal(isFinal);
		symbolTable.putInGlobal(className, newClass);
		
		// Current class is the new class
		currentClass = newClass;
		
		lexer.nextToken();
		
		// Inheritance
		if (lexer.token == Symbol.EXTENDS) {
			lexer.nextToken();
			
			if ( lexer.token != Symbol.IDENT ) {
				error.show(CompilerError.ident_expected);
			}
			
			// Get the class from the hash table
			String superclassName = lexer.getStringValue();
			
			if (className.equals(superclassName)) {
				error.show("Cant inheritance from the class itself");
			}
			
			KraClass superclass = symbolTable.getInGlobal(superclassName);
			
			// If the super class was not declared before...
			if (superclass == null) {
				error.show("Class " + superclassName + " was not declared");
			}
			
			// A final class cannot be inherited
			if (superclass.isFinal()) {
				error.show("Cant inheritance from a final class");
			}
			
			// Sets the super class of the new class
			currentClass.setSuperclass(superclass);

			lexer.nextToken();			
		}
		else {
			currentClass.setSuperclass(null);
		}
		
		if ( lexer.token != Symbol.LEFTCURBRACKET ) {
			error.show("{ expected", true);
		}
		
		lexer.nextToken();
		
		// Add the instance variables and methods to the new class
		while (lexer.token == Symbol.PRIVATE
				|| lexer.token == Symbol.PUBLIC
				|| lexer.token == Symbol.STATIC) {

			Symbol qualifier;
			boolean isStatic = false;
			
			// Static method or instance variable
			if (lexer.token == Symbol.STATIC) {
				isStatic = true;
				lexer.nextToken();
			}
			
			switch (lexer.token) {
			// Case a instance variable (or method) is private
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
				
			// Case a instance variable (or method) is public	
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
				
			// Error handling
			default:
				error.show("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}
			
			Type t = type();
			
			if ( lexer.token != Symbol.IDENT ) {
				error.show("Identifier expected");
			}
			
			// Get the instance variable (or method) name
			String name = lexer.getStringValue();
			
			lexer.nextToken();
			
			if ( lexer.token == Symbol.LEFTPAR ) {	// Add the method
				methodDec(t, name, qualifier, isStatic);
			} else if ( qualifier != Symbol.PRIVATE ) {
				error.show("Attempt to declare a public instance variable");
			} else {	// Add the instance variables
				instanceVarDec(t, name, isStatic);
			}
			
		}
		
		// Class Program must have a 'run' method
		if (currentClass.getName().equals("Program")) {
			if(!currentClass.containsPublicMethod("run")) {
				error.show("Class Program without a method 'run'");
			}
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			error.show("public/private or \"}\" expected");
		
		lexer.nextToken();

	}

	
	/**
	 * instanceVarDec()
	 * InstVarDec ::= [ "static" ] "private" Type IdList ";"
	 */
	private void instanceVarDec(Type type, String name, boolean staticVar) {

		if (staticVar) {
			// Verify if the static instance variable was already declared
			if (currentClass.containsStaticVariable(name)) {
				error.show("Static variable " + name + " already declared");
			}
			
			// Verify if the static instance variable share its with some
			// method (static or not)
			if (currentClass.containsPrivateMethod(name)
				|| currentClass.containsPublicMethod(name)
				|| currentClass.containsStaticPublicMethod(name)
				|| currentClass.containsStaticPrivateMethod(name)) {
				
				error.show(name + " already declared as a method");
			}
			
			// Add the static instance variable to the class
			InstanceVariable instanceVariable = new InstanceVariable(name, type);
			instanceVariable.setStatic(staticVar);
			currentClass.addStaticVariable(instanceVariable);
		}
		else {		
			// Verify if the instance variable was already declared
			if (currentClass.containsInstanceVariable(name)) {
				error.show("Instance variable " + name + " already declared");
			}
			
			// Verify if the instance variable share its name with some method
			if (currentClass.containsPrivateMethod(name)
				|| currentClass.containsPublicMethod(name)) {
				
				error.show(name + " already declared as a method");
			}
			
			// Add the instance variable to the class
			InstanceVariable instanceVariable = new InstanceVariable(name, type);
			instanceVariable.setStatic(staticVar);
			currentClass.addInstanceVariable(instanceVariable);
		}
			
		if ( lexer.token != Symbol.SEMICOLON ) {
			error.show(CompilerError.semicolon_expected);
		}
		
		lexer.nextToken();
	}

	
	/**
	 * methodDec()
	 * MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{" StatementList "}"
	 */
	private void methodDec(Type type, String name, Symbol qualifier, boolean staticMethod) {

		if (staticMethod) {
			// Verify if the new static method its already declared
			if (currentClass.containsStaticPrivateMethod(name)
				|| currentClass.containsStaticPublicMethod(name)) {
				
				error.show("Static method " + name + " already declared");
			}
		}
		else {		
			// Verify if the new method its already declared
			if (currentClass.containsPrivateMethod(name)
				|| currentClass.containsPublicMethod(name)) {
				
				error.show("Method " + name + " already declared");
			}
		}
		
		// Verify if the new method shares its name with a instance variable
		// (static or not)
		if (currentClass.containsInstanceVariable(name)
			|| currentClass.containsStaticVariable(name)) {
			
			error.show(name + " already declared as an instance variable");
		}
		
		// currentMethod is the new method
		currentMethod = new Method(name, type);
		
		// Set if it is static
		currentMethod.setStatic(staticMethod);
		
		// Set the qualifier of the method and add it to the class
		if (qualifier == Symbol.PRIVATE) {
			currentMethod.setPrivate(true);
			if (staticMethod) {
				currentClass.addStaticPrivateMethod(currentMethod);
			}
			else {
				currentClass.addPrivateMethod(currentMethod);
			}
		}
		else {
			currentMethod.setPrivate(false);
			if (staticMethod) {
				currentClass.addStaticPublicMethod(currentMethod);
			}
			else {
				currentClass.addPublicMethod(currentMethod);
			}
		}

		lexer.nextToken();
		
		// In case the method have parameters
		if ( lexer.token != Symbol.RIGHTPAR ) {
			formalParamDec();
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) {
			error.show(") expected");
		}
		
		// Verify the run method of class Program
		if (currentClass.getName().equals("Program")
			&& currentMethod.getName().equals("run")) {
			
			// Method run must be public
			if (qualifier == Symbol.PRIVATE) {
				error.show("Method 'run' of class Program must be public");
			}
			
			ParamList runParamList = currentMethod.getParamList();
				
			// Method run must be paremeterless
			if (runParamList.getSize() > 0) {
				error.show("Method 'run' of class Program must be paremeterless");
			}
			else {
				// Verify if it have a 'void' return
				if (currentMethod.getReturnType() != Type.voidType) {
					error.show("Method 'run' of class Program must return 'void'");
				}
			}
			
			// Method 'run' cannot be static
			if (currentMethod.isStatic()) {
				error.show("Method 'run' of class Program cannot be static");
			}
		}
		
		// We see if we are overriding a method
		if (currentClass.getSuperclass() != null) {
			KraClass superclass = currentClass.getSuperclass();
			Method m = null;
					
			// We look in all the superclasses
			while (superclass != null) {
				if (superclass.containsPublicMethod(name)) {
					m = superclass.getPublicMethod(name);
					break;
				}
						
				superclass = superclass.getSuperclass();
			}
					
			// We found a method! So we are overriding it
			if (m != null) {
				// We must verify its signature
				// First, the return type
				if (type != m.getReturnType()) {
					error.show("Attempt to override a method changing its signature (return type)");
				}
						
				// Now, the parameters type
				// First, we check the number of parameters
				if (m.getParamList().getSize() != currentMethod.getParamList().getSize()) {
					error.show("Attempt to override a method changing its signature (wrong number of parameters)");
				}
						
				// Now, we check the parameters types, in order
				Iterator<Parameter> superIt = m.getParamList().elements();
				Iterator<Parameter> currentIt = currentMethod.getParamList().elements();
						
				int i = 1;
						
				while (superIt.hasNext() && currentIt.hasNext()) {
					if (superIt.next().getType() != currentIt.next().getType()) {
						error.show("Attempt to override a method changing its signature "
									+ "(parameter " + i
									+ " doesnt match types");
					}
							
					i++;
				}
						
			}
		}

		lexer.nextToken();
		
		if ( lexer.token != Symbol.LEFTCURBRACKET ) {
			error.show("{ expected");
		}

		lexer.nextToken();
		
		foundReturn = false;
		
		// Get the statements of the method
		currentMethod.setStatementList(statementList("method"));
		
		// Verify if there is a return statement, if the method have a return type
		if (type != Type.voidType && !foundReturn) {
			error.show("Method " + currentMethod.getName() + " must have a return");
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			error.show("} expected");
		}
		
		// Clear the local variable hash table
		symbolTable.removeLocalIdent();

		lexer.nextToken();

	}

	
	/**
	 * localDec()
	 * LocalDec ::= Type IdList ";"
	 */
	private void localDec() {
		
		// Get the type
		Type type = type();
		
		if ( lexer.token != Symbol.IDENT ) {
			error.show("Valid identifier expected, found " + lexer.getStringValue());
		}
		
		String variableName = lexer.getStringValue();
		Variable v = new Variable(variableName, type);
		
		// Verify if the variable was not already declared
		// if not, we add it to the local variables
		if (symbolTable.getInLocal(variableName) == null) {
			symbolTable.putInLocal(variableName, v);
			currentMethod.addLocalVariable(v);
		}
		else {
			error.show("Variable " + variableName + " was already declared");
		}
		
		lexer.nextToken();
		
		// In case there is more declarations...
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			
			if ( lexer.token != Symbol.IDENT ) {
				error.show("Valid identifier expected, found " + lexer.getStringValue());
			}
			
			variableName = lexer.getStringValue();
			v = new Variable(variableName, type);
			
			// Verify if the variable was not already declared
			// if not, we add it to the local variables
			if (symbolTable.getInLocal(variableName) == null) {
				symbolTable.putInLocal(variableName, v);
				currentMethod.addLocalVariable(v);
			}
			else {
				error.show("Variable " + variableName + "was already declared");
			}
			
			lexer.nextToken();
		}
		
	}

	private void formalParamDec() {
		// FormalParamDec ::= ParamDec { "," ParamDec }

		// First parameter
		paramDec();
		
		// Case theres more than one parameter
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			paramDec();
		}
	}

	
	/**
	 * paramDec()
	 * ParamDec ::= Type Id
	 */
	private void paramDec() {

		// Get the parameter type
		Type t = type();
		
		if ( lexer.token != Symbol.IDENT ) {
			error.show("Valid identifier expected, found " + lexer.getStringValue());
		}
		
		// Get the parameter name
		String name = lexer.getStringValue();
		
		// Add the parameter to the current method and the local variables
		Parameter p = new Parameter(name, t);
		currentMethod.addParameter(p);
		symbolTable.putInLocal(name, p);
		
		lexer.nextToken();
	}

	
	/**
	 * type()
	 * Type ::= BasicType | Id
	 */
	private Type type() {

		Type result;

		switch (lexer.token) {
		case VOID:
			result = Type.voidType;
			break;
			
		case INT:
			result = Type.intType;
			break;
			
		case BOOLEAN:
			result = Type.booleanType;
			break;
			
		case STRING:
			result = Type.stringType;
			break;
			
		case IDENT:
			// Get the class from the hash table
			String identOfSomething = lexer.getStringValue();
			Object something = symbolTable.get(identOfSomething);
			
			// If there is no class with name identOfSomething
			if (something == null) {
				error.show("Type " + identOfSomething + " was not declared");
				result = null;
			}
			
			// This type must be a class
			if (!(something instanceof KraClass)) {
				error.show(identOfSomething + " must be a class");
				result = null;
			}
			
			result = (KraClass) something;
			break;
			
		default:
			error.show("Type expected");
			result = Type.undefinedType;
		}
		
		lexer.nextToken();
		
		return result;
	}

	
	/**
	 * compositeStatement()
	 * CompStatement ::= "{" { Statement } "}"
	 */
	private CompositeStatement compositeStatement(String superiorStatement) {

		lexer.nextToken();
		
		// Get the statement list
		StatementList aStatementList = statementList(superiorStatement);
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			error.show("} expected");
		} else {
			lexer.nextToken();
		}
		
		return new CompositeStatement(aStatementList);
		
	}

	
	/**
	 * statementList()
	 * StatementList ::= { Statement }
	 */
	private StatementList statementList(String superiorStatement) {

		Symbol tk;
		StatementList aStatementList = new StatementList();
		Statement aStatement;
		
		// statements always begin with an identifier, if, read, write, ...
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
				&& tk != Symbol.ELSE) {
				
			aStatement = statement(superiorStatement);
			
			if (aStatement != null) {
				aStatementList.addElement(aStatement);
			}
		}
		
		return aStatementList;
	}

	
	/**
	 * statement()
	 * Statement ::= Assignment ";" | IfStat | WhileStat | ReturnStat ";'"
					| ReadStat ";" | WriteStat ";" | "break" ";" | ";"
					| CompStatement
	 */
	private Statement statement(String superiorStatement) {

		switch (lexer.token) {
			case THIS:
			case IDENT:
			case SUPER:
			case INT:
			case BOOLEAN:
			case STRING:
			case NUMBER:
				return assignExprLocalDec();
			case RETURN:
				return returnStatement();
			case READ:
				return readStatement();
			case WRITE:
				return writeStatement();
			case WRITELN:
				return writelnStatement();
			case IF:
				return ifStatement(superiorStatement);
			case BREAK:
				return breakStatement(superiorStatement);
			case WHILE:
				return whileStatement();
			case SEMICOLON:
				return nullStatement();
			case LEFTCURBRACKET:
				return compositeStatement(superiorStatement);
			default:
				error.show("Statement expected");
		}
		
		return null;
	}

	
	/**
	 * isType()
	 * See if the given name is the name of a class
	 * @param name The name to search
	 * @return Returns true if the name is of a class, false otherwise
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}
	
	
	/**
	 * isConvertible()
	 * See if a type is convertible to another type
	 * @param firstType The first type to compare
	 * @param secondType The second type to compare
	 * @return Returns true if the second type is convertible to the first type, false otherwise
	 */
	private boolean isConvertible(Type firstType, Type secondType) {
	
		// Case if firstType is a basic type and firstType is secondType
		if ((firstType == Type.intType
			|| firstType == Type.booleanType
			|| firstType == Type.stringType) && firstType == secondType) {
			
			return true;
		}
		
		// Case if secondType is a subclass of firstType
		// First, if firstType is secondType
		if (isType(firstType.getName()) && isType(secondType.getName())) {
			if (firstType == secondType) {
				return true;
			}
			else {
				// Now, the superclasses of the secondType
				KraClass rightClass = symbolTable.getInGlobal(secondType.getName());
				rightClass = rightClass.getSuperclass();
				
				while (rightClass != null) {
					if (rightClass == firstType) {
						return true;
					}
					
					rightClass = rightClass.getSuperclass();
				}
				
				return false;			
			}
		}
		
		// Case firstType is a class and secondType is null
		if (isType(firstType.getName())) {
			if (secondType == Type.undefinedType) {
				return true;
			}
		}
		
		return false;
	}

	
	/**
	 * assignExprLocalDec()
	 * AssignExprLocalDec ::= Expression [ "=" Expression ] | LocalDec
	 */
	private Statement assignExprLocalDec() {

		// Case where declarations occurs (LocalDec part)
		if (lexer.token == Symbol.INT
			|| lexer.token == Symbol.BOOLEAN
			|| lexer.token == Symbol.STRING 
			// Type can be a class
			|| (lexer.token == Symbol.IDENT && isType(lexer.getStringValue())
				&& symbolTable.getInLocal(lexer.getStringValue()) == null)) {
			
			// Get the local variable declarations
			localDec();
			
			if (lexer.token != Symbol.SEMICOLON) {
				error.show(CompilerError.semicolon_expected);
			}
			
			lexer.nextToken();
			
			return null;
		}
		else if (lexer.token == Symbol.NUMBER) {
			error.show("Cant make an assignment to a value");
			return null;
		}
		else {
			Expr left = expr();
			
			Expr right = null;
			
			// We have a assignment
			if (lexer.token == Symbol.ASSIGN) {
				lexer.nextToken();
				
				right = expr();
				
				// Cant assignment a void call to a variable
				if (right.getType() == Type.voidType) {
					error.show("Cant assignment a void call to a variable");
				}
				
				// We verify if the type of left is convertible to the type of right
				if (!isConvertible(left.getType(), right.getType())) {
					error.show("Uncompatible types in assignment");
				}
				
				// new
				if (right instanceof ObjectCreation) {
					((ObjectCreation) right).setCast((KraClass) left.getType());
				}
				
				if ( lexer.token != Symbol.SEMICOLON ) {
					error.show(CompilerError.semicolon_expected);
				}
				
				lexer.nextToken();
				
				return new AssignmentStatement(left, right);
			}
			else {
				// We have only an expression, which can be a message send
				// We verify if the expression can be a statement (message send returns nothing)
				if (left.getType() == Type.voidType) {
					if (lexer.token != Symbol.SEMICOLON) {
						error.show("; expected");
					}
					
					lexer.nextToken();
					
					return new MessageSendStatement(left);
				}
				else if (left instanceof VariableExpr) {
					error.show("Not a valid type in the declaration");
					return null;
				}
				else {
					error.show("Call is an expression, not a statement");
					return null;
				}
			}
		}
	}

	
	/**
	 * realParameters()
	 */
	private ExprList realParameters() {
	
		ExprList anExprList = new ExprList();

		if (lexer.token != Symbol.LEFTPAR) {
			error.show("( expected");
		}
		
		lexer.nextToken();
		
		if (startExpr(lexer.token)) {
			anExprList = exprList();
		}
		
		if (lexer.token != Symbol.RIGHTPAR) {
			error.show(") expected");
		}
		
		lexer.nextToken();
		
		return anExprList;
	}

	
	/**
	 * whileStatement()
	 * WhileStat ::= "while" "(" Expression ")" Statement
	 */
	private WhileStatement whileStatement() {

		lexer.nextToken();
		
		if ( lexer.token != Symbol.LEFTPAR ) {
			error.show("( expected");
		}
		
		lexer.nextToken();
		
		// Get the expression of the while
		Expr e = expr();
		
		// The expression must be boolean
		if (e.getType() != Type.booleanType) {
			error.show("The expression in the while statement must be boolean");
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) {
			error.show(") expected");
		}
		
		lexer.nextToken();
		
		// Get the statement to repeat
		Statement s = statement("while");
		
		return new WhileStatement(e, s);
	}

	
	/**
	 * ifStatement()
	 * IfStat ::= "if" "(" Expression ")" Statement [ "else" Statement ]
	 */
	private IfStatement ifStatement(String superiorStatement) {

		lexer.nextToken();
		
		if ( lexer.token != Symbol.LEFTPAR ) {
			error.show("( expected");
		}
		
		lexer.nextToken();
		
		// Get the expression to evaluate
		Expr e = expr();
		
		// Expr must be boolean
		if (e.getType() != Type.booleanType) {
			error.show("Expression of IF statement must be of boolean type");
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) { 
			error.show(") expected");
		}
		
		lexer.nextToken();
		
		// Get the statement of the if part
		Statement s = statement(superiorStatement);
		
		// Case theres a 'else' part, we get its statement...
		Statement elseStatement = null;
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			
			elseStatement = statement(superiorStatement);
		}
		
		return new IfStatement(e, s, elseStatement);
	}

	
	/**
	 * returnStatement()
	 * ReturnStat ::= "return" Expression
	 */
	private ReturnStatement returnStatement() {

		lexer.nextToken();
		
		// Get the expression to return
		Expr e = expr();
		
		// Void cannot have a return
		if (currentMethod.getReturnType() == Type.voidType) {
			error.show("Cant have a 'return' in a void method");
		}
		
		// Expr must be convertible to the type of return of the method
		if (!isConvertible(currentMethod.getReturnType(), e.getType())) {
			error.show("Return type not compatible with the return of the method");
		}
		
		if ( lexer.token != Symbol.SEMICOLON ) {
			error.show(CompilerError.semicolon_expected);
		}
		
		lexer.nextToken();
		
		foundReturn = true;
		
		return new ReturnStatement(e);
	}

	
	/**
	 * readStatement()
	 * ReadStat ::= "read" "(" LeftValue { "," LeftValue } ")"
	 */
	private ReadStatement readStatement() {
		
		lexer.nextToken();
		
		if ( lexer.token != Symbol.LEFTPAR ) {
			error.show("( expected");
		}
		
		lexer.nextToken();
		
		boolean thisFlag;
		ArrayList<Variable> readList = new ArrayList<Variable>();
		
		while (true) {
			thisFlag = false;
			
			if ( lexer.token == Symbol.THIS ) {
				thisFlag = true;
				
				lexer.nextToken();
				
				if ( lexer.token != Symbol.DOT ) {
					error.show(". expected");
				}
				
				lexer.nextToken();
			}
			if ( lexer.token != Symbol.IDENT ) {
				error.show(CompilerError.ident_expected);
			}

			String name = lexer.getStringValue();
			
			// Search in the instance variables
			if(thisFlag) {
				// No instance variable with name was found				
				if (!currentClass.containsInstanceVariable(name)) {
					error.show("Class " + currentClass.getName()
							+ " have no instance variable '" + name
							+ "'. Wrong usage of 'this'");
				}
				
				Variable iv = currentClass.getInstanceVariable(name);
				
				// Can only read int and String types
				if (iv.getType() != Type.intType
						  && iv.getType() != Type.stringType) {
							error.show("Can only read int and String types");
				}
				// Ok
				else {
					readList.add(iv);
				}
			}
			// Search in the local variables
			else {
				Variable var = symbolTable.getInLocal(name);
				
				if (var == null) {
					error.show("Variable " + name + " was not declared");
				}
				else if (var.getType() != Type.intType
						  && var.getType() != Type.stringType) {
							error.show("Can only read int and String types");
				}
				else {
					readList.add(var);
				}
			}
			
			lexer.nextToken();
			
			if ( lexer.token == Symbol.COMMA ) {
				lexer.nextToken();
			} else {
				break;
			}
			
		}

		if ( lexer.token != Symbol.RIGHTPAR ) {
			error.show(") expected");
		}
		
		lexer.nextToken();
		
		if ( lexer.token != Symbol.SEMICOLON ) {
			error.show(CompilerError.semicolon_expected);
		}
		
		lexer.nextToken();
		
		VariableList variableList = new VariableList(readList);
		
		return new ReadStatement(variableList);
	}

	
	/**
	 * writeStatement
	 * WriteStat ::= "write" "(" ExpressionList ")"
	 */
	private WriteStatement writeStatement() {

		lexer.nextToken();
		
		if (lexer.token != Symbol.LEFTPAR) {
			error.show("( expected");
		}
		
		lexer.nextToken();
		
		// Get the expression(s) to output
		ExprList exprl = exprList();
		
		// Can only write int and String values
		Iterator<Expr> exprIt = exprl.elements();	
		while (exprIt.hasNext()) {
			Expr e = exprIt.next();
			
			if (e.getType() != Type.intType
				&& e.getType() != Type.stringType) {
				
				error.show("Can only write int and String types");
			}
		}
		
		if (lexer.token != Symbol.RIGHTPAR) { 
			error.show(") expected");
		}
		
		lexer.nextToken();
		
		if (lexer.token != Symbol.SEMICOLON) {
			error.show(CompilerError.semicolon_expected);
		}
		
		lexer.nextToken();
		
		return new WriteStatement(exprl);
	}

	
	/**
	 * writelnStatement()
	 * WritelnStat ::= "writeln" "(" ExpressionList ")"
	 */
	private WritelnStatement writelnStatement() {

		lexer.nextToken();
		
		if (lexer.token != Symbol.LEFTPAR) { 
			error.show("( expected");
		}
		
		lexer.nextToken();
		
		// Get the expression(s) to output
		ExprList exprl = exprList();
		
		// Can only write int and String values
		Iterator<Expr> exprIt = exprl.elements();				
		while (exprIt.hasNext()) {
			Expr e = exprIt.next();
					
			if (e.getType() != Type.intType
				&& e.getType() != Type.stringType) {
				
				error.show("Can only write int and String types");
			}
		}
		
		if (lexer.token != Symbol.RIGHTPAR) {
			error.show(") expected");
		}
		
		lexer.nextToken();
		
		if (lexer.token != Symbol.SEMICOLON) {
			error.show(CompilerError.semicolon_expected);
		}
		
		lexer.nextToken();
		
		return new WritelnStatement(exprl);
	}

	
	/**
	 * breakStatement()
	 * "break" ";"
	 */
	private BreakStatement breakStatement(String superiorStatement) {

		lexer.nextToken();
		
		if (superiorStatement != "while") {
			error.show("Wrong usage of \"break\" outside a while statement");
		}
		
		if (lexer.token != Symbol.SEMICOLON) {
			error.show(CompilerError.semicolon_expected);
		}

		lexer.nextToken();		
		
		return new BreakStatement();
	}

	/**
	 * nullStatement()
	 * ";"
	 */
	private NullStatement nullStatement() {
		
		lexer.nextToken();
		
		return new NullStatement();
	}

	
	/**
	 * exprList()
	 * ExpressionList ::= Expression { "," Expression }
	 */
	private ExprList exprList() {

		ExprList anExprList = new ExprList();
		
		// Get the first expression
		anExprList.addElement(expr());
		
		// In case there are more expressions...
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		
		return anExprList;
	}

	
	/**
	 * expr()
	 * Expression ::= SimpleExpression [ Relation SimpleExpression ]
	 */
	private Expr expr() {

		// Get the left side of the expression
		Expr left = simpleExpr();
		
		// Get the relational operator
		Symbol op = lexer.token;
		if (op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT) {
				
			lexer.nextToken();
			
			// Get the right side of the expression
			Expr right = simpleExpr();
			
			// >, >=, <, <= only applies to int variables
			if (op != Symbol.EQ && op != Symbol.NEQ) {
				if (left.getType() != Type.intType
					&& right.getType() != Type.intType) {
					
					error.show("Invalid operation (>, >=, <, <= are only valid to int variables)");
				}
			}
			// == and != operators
			// Only case comparison is possible and types are not convertible is when comparing string and null
			else {
				// Neither side is a string
				if(left.getType() != Type.stringType && right.getType() != Type.stringType) {
					// Types have to be convertible
					if(!isConvertible(left.getType(), right.getType())
						&& !isConvertible(right.getType(), left.getType())	) {
							error.show("Invalid operation (incompatible types in comparison)");
						}
				}
				// One or both sides are strings
				else {
					// If types are different and none of them is null, then it's comparing a string with something else
					if(	left.getType() != right.getType() 
						&& left.getType() != Type.undefinedType 
						&& right.getType() != Type.undefinedType ) {
						
						error.show("Invalid operation (incompatible types in comparison)");
					}
				}
			}
			
			// Create the new composite expression, using the left side created before
			left = new CompositeExpr(left, op, right);
		}
		
		return left;
	}

	
	/**
	 * simpleExpr()
	 * SimpleExpression ::= Term { LowOperator Term }
	 */
	private Expr simpleExpr() {
	
		Symbol op;

		// Get the left side of the expression
		Expr left = term();
		
		// While there is a low operator (+, -, ||)...
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			lexer.nextToken();
			
			// Get the right side of the expression
			Expr right = term();
			
			if (op == Symbol.MINUS || op == Symbol.PLUS) {
				if (left.getType().getName() != "int"
					|| right.getType().getName() != "int") {
					
					error.show("Invalid operation (+ and - accept only int variables)");
				}
			}
			else {
				if (left.getType().getName() != "boolean"
					|| right.getType().getName() != "boolean") {
					
					error.show("Invalid operation (|| accept only boolean variables)");
				}
			}
			
			// Keeps aggregating the right side to the left side
			left = new CompositeExpr(left, op, right);
		}
		
		return left;
	}

	
	/**
	 * term()
	 * Term ::= SignalFactor { HighOperator SignalFactor }
	 */
	private Expr term() {
	
		Symbol op;

		// Get the factor
		Expr left = signalFactor();
		
		// While there is a high operator (*. / , &&)...
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
				
			lexer.nextToken();
			
			// Get the right side of the expression
			Expr right = signalFactor();
			
			if (op == Symbol.DIV || op == Symbol.MULT) {
				if (left.getType().getName() != "int"
					|| right.getType().getName() != "int") {
					
					error.show("Invalid operation (* and / accept only int variables)");
				}
			}
			else {
				if (left.getType().getName() != "boolean"
					|| right.getType().getName() != "boolean") {
					
					error.show("Invalid operation (&& accept only boolean variables)");
				}
			}
			
			
			// Keeps aggregating the right side to the left side
			left = new CompositeExpr(left, op, right);
		}
		
		return left;
	}

	
	/**
	 * signalFactor()
	 * SignalFactor ::= [ Signal ] Factor
	 */
	private Expr signalFactor() {
	
		Symbol op;
		
		if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
			lexer.nextToken();
			
			// [Signal] Factor
			Expr e = factor();
			
			if (e.getType() != Type.intType) {
				error.show("Signals can only be used with int values");
			}
			
			return new SignalExpr(op, e);
		}
		else
			// no Signal
			return factor();
	}

	/**
	 * factor()
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 *      	  ObjectCreation | PrimaryExpr
	 *
	 * BasicValue ::= IntValue | BooleanValue | StringValue 
	 *
	 * BooleanValue ::=  "true" | "false" 
	 *
	 * ObjectCreation ::= "new" Id "(" ")" 
	 *
	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 Id  |
	 *                 Id "." Id | 
	 *                 Id "." Id "(" [ ExpressionList ] ")" |
	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
	 *                 "this" | 
	 *                 "this" "." Id | 
	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {

		Expr e;
		ExprList exprList;
		String messageName, ident;
		boolean found;
		Method m = null;
		ParamList methodParams;

		switch (lexer.token) {
		
			/* BasicValues */
			// IntValue
			case NUMBER:
				return literalInt();
				
			// BooleanValue
			case TRUE:
				// true
				lexer.nextToken();
				
				return LiteralBoolean.True;
			
			// BooleanValue
			case FALSE:
				// false
				lexer.nextToken();
				
				return LiteralBoolean.False;
				
			// StringValue
			case LITERALSTRING:
				String literalString = lexer.getLiteralStringValue();
				
				lexer.nextToken();
				
				return new LiteralString(literalString);
				
			/* "(" Expression ")" */
			case LEFTPAR:
				// (
				lexer.nextToken();
				
				e = expr();
				
				if (lexer.token != Symbol.RIGHTPAR) {
					error.show(") expected");
				}
				
				// )
				lexer.nextToken();
				
				return new ParenthesisExpr(e);

			/* "!" Factor */
			case NOT:
				// !
				lexer.nextToken();
				
				e = expr();
				
				if (e.getType() != Type.booleanType) {
					error.show("Operator ! can only be used to boolean variables");
				}
				
				return new UnaryExpr(e, Symbol.NOT);
	
			/* "null" */
			case NULL:
				// null
				lexer.nextToken();
				
				return new NullExpr();
				
			/* ObjectCreation ::= "new" Id "(" ")" */
			case NEW:
				// new
				lexer.nextToken();
				
				if (lexer.token != Symbol.IDENT) {
					error.show("Identifier expected");
				}

				// Search for the class in the hash table
				String className = lexer.getStringValue();
				
				KraClass aClass = symbolTable.getInGlobal(className);
				if (aClass == null) {
					error.show("Class " + " was not declared");
				}

				lexer.nextToken();
				
				if (lexer.token != Symbol.LEFTPAR) {
					error.show("( expected");
				}
				
				lexer.nextToken();
				
				if (lexer.token != Symbol.RIGHTPAR) {
					error.show(") expected");
				}
				
				lexer.nextToken();
				
				return new ObjectCreation(aClass);

			case SUPER:
				// "super" "." Id "(" [ ExpressionList ] ")"
				
				// Cant call 'super' in static methods
				if (currentMethod.isStatic()) {
					error.show("Cant use 'super' in static methods");
				}
				
				// super
				lexer.nextToken();
				
				if (lexer.token != Symbol.DOT) {
					error.show("'.' expected");
				}
				else {
					lexer.nextToken();
				}
				
				if (lexer.token != Symbol.IDENT) {
					error.show("Identifier expected");
				}
				
				// Search for the method in the super classes of the current class
				messageName = lexer.getStringValue();
				
				KraClass superclass = currentClass.getSuperclass();
				found = false;
				
				if (superclass == null) {
					error.show("Class " + currentClass.getName() + " dont have a superclass");
				}
				
				while (superclass != null) {
					found = superclass.containsPublicMethod(messageName);
					
					if (!found) {
						superclass = superclass.getSuperclass();
					}
					else {
						break;
					}
				}
				
				// Didnt find the method
				if (!found) {
					error.show("Class " + currentClass.getName()
								+ " dont have a super class with a public method "
								+ messageName);
				}
				
				lexer.nextToken();
				
				// Get the list of expressions
				exprList = realParameters();
				
				m = superclass.getPublicMethod(messageName);
				methodParams = m.getParamList();
				
				// See if the number of expressions match the number of parameters
				// of the message
				if (exprList.size() == methodParams.getSize()) {
					Iterator<Expr> exprIt = exprList.elements();
					Iterator<Parameter> paramsIt = methodParams.elements();
					
					int count = 1;
					
					// See if the expressions are convertible to the types of the
					// respective parameters
					while (exprIt.hasNext() && paramsIt.hasNext()) {
						Expr exp = exprIt.next();
						Parameter p = paramsIt.next();
						
						if (!isConvertible(p.getType(), exp.getType())) {
							error.show("Expression type is not convertible to parameter number "
										+ count);
						}
						
						count++;
					}
				}
				else {
					error.show("Wrong number of parameters");
				}
				
				return new MessageSendToSuper(m, exprList, currentClass);
				
			case IDENT:
				String firstId = lexer.getStringValue();
				
				lexer.nextToken();
				
				if (lexer.token != Symbol.DOT) {
					// Id
					
					// Check if firstId is a static method (which cannot be)
					if (currentClass.containsStaticPrivateMethod(firstId)
						|| currentClass.containsStaticPublicMethod(firstId)) {
							error.show("Wrong call of static method " + firstId);
					}
					
					// Check if firstId is a method (which also cannot be)
					if (currentClass.containsPrivateMethod(firstId)
						|| currentClass.containsPublicMethod(firstId)) {
							error.show("Wrong call of method " + firstId + " (need to use 'this'");
					}
					
					// firstId must be declared and cannot be an instance variable
					Variable v = symbolTable.getInLocal(firstId);
					
					if (v == null && currentClass.containsInstanceVariable(firstId)) {
						error.show("Cant use instance variables without 'this'");
					}
					else if (v == null) {
						error.show("Variable " + firstId + " was not declared");
					}

					return new VariableExpr(v);
				}
				// Id "."
				else {
					lexer.nextToken(); // coma o "."
					
					if (lexer.token != Symbol.IDENT) {
						error.show("Identifier expected");
					}
					// Id "." Id
					else {
						// firstId must be declared before
						Variable v = symbolTable.getInLocal(firstId);
						if (v == null) {
							error.show("Variable " + firstId + " was not declared");
						}
						
						// firstId must be an object
						if (!isType(v.getType().getName())) {
							error.show("Variable " + firstId + " is not an object");
						}
						
						ident = lexer.getStringValue();
						
						lexer.nextToken();
						
						if (lexer.token == Symbol.DOT) {
							// Id "." Id "." Id "(" [ ExpressionList ] ")"
							/*
							 * se o compilador permite variáveis estáticas, é possível
							 * ter esta opção, como
							 *     Clock.currentDay.setDay(12);
							 * Contudo, se variáveis estáticas não estiver nas especificações,
							 * sinalize um erro neste ponto.
							 */
							 
							 error.show("Por enquanto não aceita variaveis estaticas!");
							 
							lexer.nextToken();
							
							if (lexer.token != Symbol.IDENT) {
								error.show("Identifier expected");
							}
							
							messageName = lexer.getStringValue();
							lexer.nextToken();
							exprList = this.realParameters();
						}
						else if (lexer.token == Symbol.LEFTPAR) {
							// Id "." Id "(" [ ExpressionList ] ")"
							
							aClass = symbolTable.getInGlobal(v.getType().getName());
							found = false;
							
							/* We must search for the method ident in the class type of firstId.
							 * If ident is private, it cannot be called outside the scope of its class.
							 */
							if (currentClass.getName() == aClass.getName()) {
								// First, we search in the private methods
								found = aClass.containsPrivateMethod(ident);
								
								// If not found, we search in the public methods
								if (!found) {
									found = aClass.containsPublicMethod(ident);
									
									// If still didnt find the method, we search
									// for it in the superclasses
									if (!found) {
										superclass = aClass.getSuperclass();
										
										while (superclass != null) {
											found = superclass.containsPublicMethod(ident);
											
											if (!found) {
												superclass = superclass.getSuperclass();
											}
											else {
												// Found in the methods of the superclasses
												m = superclass.getPublicMethod(ident);
												break;
											}
										}
									}
									else {
										// Found in the public methods
										m = aClass.getPublicMethod(ident);
									}
								}
								else {
									// Found in the private methods
									m = aClass.getPrivateMethod(ident);
								}
								
								// We didnt find the method :(
								if (!found) {
									error.show("No method " + ident + " was found");
								}
							}
							else {
								// We are not in the class scope, so we only search in
								// the public methods
								found = aClass.containsPublicMethod(ident);
								
								// We look for it in the superclasses
								if (!found) {
									superclass = aClass.getSuperclass();
									
									while (superclass != null) {
										found = superclass.containsPublicMethod(ident);
										
										if (!found) {
											superclass = superclass.getSuperclass();
										}
										else {
											// Found in the methods of the superclasses
											m = superclass.getPublicMethod(ident);
											break;
										}
									}
								}
								else {
									// Found in the public methods
									m = aClass.getPublicMethod(ident);
								}
								
								// We didnt find it :(
								if (!found) {
									error.show("No public method " + ident + " was found for the class " + v.getType().getName());
								}
							}
							
							// Now we are OK to continue! :)
							
							exprList = this.realParameters();

							methodParams = m.getParamList();
							
							// See if the number of expressions match the number of parameters
							// of the message
							if (exprList.size() == methodParams.getSize()) {
								Iterator<Expr> exprIt = exprList.elements();
								Iterator<Parameter> paramsIt = methodParams.elements();
								
								int count = 1;
								
								// See if the expressions are convertible to the types of the
								// respective parameters
								while (exprIt.hasNext() && paramsIt.hasNext()) {
									Expr exp = exprIt.next();
									Parameter p = paramsIt.next();
									
									if (!isConvertible(p.getType(), exp.getType())) {
										error.show("Expression type is not convertible to parameter number "
													+ count);
									}
									
									count++;
								}
							}
							else {
								error.show("Wrong number of parameters");
							}
							
							return new MessageSendToVariable(v, m, exprList);
						}
						else {
							// TODO: pensar se isso representa acesso a variavel de instancia mesmo
							// firstId must have an instance variable ident
							aClass = symbolTable.getInGlobal(v.getType().getName());
							
							if (!aClass.containsInstanceVariable(ident)) {
								error.show("Class " + aClass.getName()
								+ " have no instance variable "
								+ ident);
							}
							
							InstanceVariable iVariable = aClass.getInstanceVariable(ident);
							
							return new MessageSendToInstance(v, iVariable);
						}
					}
				}
				break;
				
			case THIS:
				// Cant use 'this' in a static method
				if (currentMethod.isStatic()) {
					error.show("Cant use 'this' in a static method");
				}
				
				lexer.nextToken();
				
				if (lexer.token != Symbol.DOT) {
					return new MessageSendToSelf(currentClass, null, null, null);
				}
				else {
					lexer.nextToken();
					
					if (lexer.token != Symbol.IDENT) {
						error.show("Identifier expected");
					}

					ident = lexer.getStringValue();
					lexer.nextToken();
					
					
					// já analisou "this" "." Id
					if (lexer.token == Symbol.LEFTPAR) {						
						found = false;
						
						// We look in the private methods
						found = currentClass.containsPrivateMethod(ident);
						
						if (!found) {
							// We look in the public methods
							found = currentClass.containsPublicMethod(ident);
							
							if (!found) {
								// We look for it in the superclasses
								superclass = currentClass.getSuperclass();
									
								while (superclass != null) {
									found = superclass.containsPublicMethod(ident);
										
									if (!found) {
										superclass = superclass.getSuperclass();
									}
									else {
										// Found in the methods of the superclasses
										m = superclass.getPublicMethod(ident);
										break;
									}
								}
								
								if (!found) {
									error.show("Class " + currentClass.getName()
												+ " doesnt have a method "
												+ ident);
								}
							}
							else {
								// Found in the public methods
								m = currentClass.getPublicMethod(ident);
							}
						}
						else {
							// Found in the private methods
							m = currentClass.getPrivateMethod(ident);
						}
						
						exprList = this.realParameters();

						methodParams = m.getParamList();
							
						// See if the number of expressions match the number of parameters
						// of the message
						if (exprList.size() == methodParams.getSize()) {
							Iterator<Expr> exprIt = exprList.elements();
							Iterator<Parameter> paramsIt = methodParams.elements();
								
							int count = 1;
								
							// See if the expressions are convertible to the types of the
							// respective parameters
							while (exprIt.hasNext() && paramsIt.hasNext()) {
								Expr ex = exprIt.next();
								Parameter p = paramsIt.next();
								
								if (!isConvertible(p.getType(), ex.getType())) {
									error.show("Expression type is not convertible to parameter number "
												+ count);
								}
								
								count++;
							}
						}
						else {
							error.show("Wrong number of parameters");
						}
						
						return new MessageSendToSelf(currentClass, null, m, exprList);
					}
					else if (lexer.token == Symbol.DOT) {
						// "this" "." Id "." Id "(" [ ExpressionList ] ")"
						lexer.nextToken();
						
						if (lexer.token != Symbol.IDENT) {
							error.show("Identifier expected");
						}
						
						String secondIdent = lexer.getStringValue();
						boolean foundVariable = false;
						
						lexer.nextToken();
						
						// First, we see if the current class have an instance variable ident
						foundVariable = currentClass.containsInstanceVariable(ident);
						
						if (!foundVariable) {
							error.show("Class " + currentClass.getName()
										+ " doenst have an instance variable "
										+ ident);
						}
						
						InstanceVariable instanceVariable = currentClass.getInstanceVariable(ident);
						
						// Now, we must see if ident is an object and get its class
						if (!isType(instanceVariable.getType().getName())) {
							error.show(ident + " is not an object");
						}
						
						aClass = symbolTable.getInGlobal(instanceVariable.getType().getName());
						
						// Finally, we see if aClass have a method called secondIdent
						boolean foundMethod = false;
						
						foundMethod = aClass.containsPublicMethod(secondIdent);
						
						if (!foundMethod) {
							error.show("Class " + aClass.getName() + " doesnt have a public method " + secondIdent);
						}
						
						m = aClass.getPublicMethod(secondIdent);
						
						exprList = this.realParameters();

						methodParams = m.getParamList();
							
						// See if the number of expressions match the number of parameters
						// of the message
						if (exprList.size() == methodParams.getSize()) {
							Iterator<Expr> exprIt = exprList.elements();
							Iterator<Parameter> paramsIt = methodParams.elements();
								
							int count = 1;
								
							// See if the expressions are convertible to the types of the
							// respective parameters
							while (exprIt.hasNext() && paramsIt.hasNext()) {
								Expr e1 = exprIt.next();
								Parameter p = paramsIt.next();
								
								if (!isConvertible(e1.getType(), p.getType())) {
									error.show("Expression type is not convertible to parameter number "
												+ count);
								}
								
								count++;
							}
						}
						else {
							error.show("Wrong number of parameters");
						}
						
						return new MessageSendToSelf(currentClass, instanceVariable, m, exprList);
						
					}
					else {
						
						String name = lexer.getStringValue();
						
						boolean foundVariable = false;
						
						// We see if the current class have an instance variable ident
						foundVariable = currentClass.containsInstanceVariable(name);
						
						if (!foundVariable) {
							error.show("Class " + currentClass.getName()
										+ " dont have an instance variable "
										+ name);
						}
						
						InstanceVariable instanceVariable = currentClass.getInstanceVariable(name);
					
						return new MessageSendToSelf(currentClass, instanceVariable, null, null);
					}
				}
				
			default:
				error.show("Expression expected");
		}
		
		return null;
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;
		int value = lexer.getNumberValue();
		
		lexer.nextToken();
		
		return new LiteralInt(value);
	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE
				|| token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.NUMBER || token == Symbol.SUPER
				|| token == Symbol.LEFTPAR || token == Symbol.NULL
				|| token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private CompilerError	error;
	private KraClass		currentClass;
	private Method			currentMethod;
	private boolean			foundReturn;

}
