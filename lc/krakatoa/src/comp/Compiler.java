/**
 * Laboratório de Compiladores [2015/2] <br>
 * Orientação: Prof. Dr. José de O. Guimarães <br>
 * 
 * @author Maurício Spinardi | 401874 <br>
 * @author Vitor Casadei | 408301 <br>
 * 
 * @see http://www.cyan-lang.org/jose/courses/15-2/lc/index.htm
 */

package comp;

import ast.*;
import lexer.*;

import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new SignalError(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;

	}

	/**
	 * Program ::= KraClass { KraClass } <br>
	 * 
	 * @param compilationErrorList
	 * @return
	 */
	private Program program(ArrayList<CompilationError> compilationErrorList) {

		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		Program program = new Program(kraClassList, metaobjectCallList,
				compilationErrorList);
		try {
			while (lexer.token == Symbol.MOCall) {
				metaobjectCallList.add(metaobjectCall());
			}
			
			if (lexer.token == Symbol.FINAL) {
				lexer.nextToken();
				classDec(true);
			} else {
				classDec(false);
			}
			if (currentClass != null)
				kraClassList.add(currentClass);
			
			String token = lexer.getStringValue();
			while (lexer.token == Symbol.FINAL || lexer.token == Symbol.CLASS) {
				if (lexer.token == Symbol.FINAL) {
					lexer.nextToken();
					classDec(true);
				} else {
					classDec(false);
				}
				token = lexer.getStringValue();
				if (currentClass != null) {
					kraClassList.add(currentClass);
				}
			}

			/**
			 * "Every class can only refer to the classes that appear textually before it."
			 * Considerando que Program é a classe principal, então Program deve
			 * ser a última classe na lista de classes.
			 */
			int lastClass = kraClassList.size() - 1;
			if (!kraClassList.get(lastClass).getName().equals("Program")) {
				signalError.show("The last class must be the Program class");
			}

			if (lexer.token != Symbol.EOF) {
				signalError.show("End of file expected");
			}
		} catch (RuntimeException e) {
			// if there was an exception, there is a compilation signalError
		}
		return program;

	}

	/**
	 * parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
	 * <code>
	 * 
	 * @ce(5, "'class' expected") <br> clas Program <br> public void run() { }
	 *        <br> end <br> </code>
	 */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		String name = lexer.getMetaobjectName();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		if (lexer.token == Symbol.LEFTPAR) {
			// metaobject call with parameters
			lexer.nextToken();
			while (lexer.token == Symbol.LITERALINT
					|| lexer.token == Symbol.LITERALSTRING
					|| lexer.token == Symbol.IDENT) {
				switch (lexer.token) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case IDENT:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if (lexer.token == Symbol.COMMA)
					lexer.nextToken();
				else
					break;
			}
			if (lexer.token != Symbol.RIGHTPAR)
				signalError
						.show("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if (name.equals("nce")) {
			if (metaobjectParamList.size() != 0)
				signalError.show("Metaobject 'nce' does not take parameters");
		} else if (name.equals("ce")) {
			if (metaobjectParamList.size() != 3
					&& metaobjectParamList.size() != 4)
				signalError
						.show("Metaobject 'ce' take three or four parameters");
			if (!(metaobjectParamList.get(0) instanceof Integer))
				signalError
						.show("The first parameter of metaobject 'ce' should be an integer number");
			if (!(metaobjectParamList.get(1) instanceof String)
					|| !(metaobjectParamList.get(2) instanceof String))
				signalError
						.show("The second and third parameters of metaobject 'ce' should be literal strings");
			if (metaobjectParamList.size() >= 4
					&& !(metaobjectParamList.get(3) instanceof String))
				signalError
						.show("The fourth parameter of metaobject 'ce' should be a literal string");

		}

		return new MetaobjectCall(name, metaobjectParamList);
	}

	/**
	 * ClassDec ::= "class" Id [ "extends" Id ] "{" MemberList "}" <br>
	 * MemberList ::= { Qualifier Member } <br>
	 * Qualifier ::= ["final"] ["static"] ("private"|"public") <br>
	 * Member ::= InstVarDec | MethodDec <br>
	 */
	private void classDec(boolean isFinal) {

		boolean isStatic = false;

		if (lexer.token != Symbol.CLASS)
			signalError.show("'class' expected");

		lexer.nextToken();

		if (lexer.token != Symbol.IDENT)
			signalError.show(SignalError.ident_expected);

		String className = lexer.getStringValue();

		// verifica se a classe foi declarada anteriormente.
		if (symbolTable.getInGlobal(className) != null)
			signalError.show("Class " + className + " already declared");

		currentClass = new KraClass(className, isFinal);
		symbolTable.putInGlobal(className, currentClass);

		lexer.nextToken();

		if (lexer.token == Symbol.EXTENDS) {

			lexer.nextToken();

			if (lexer.token != Symbol.IDENT)
				signalError.show(SignalError.ident_expected);

			String superclassName = lexer.getStringValue();

			// valida a superclasse
			if (className.equals(superclassName))
				signalError.show("Cant inheritance from the class itself");

			if (symbolTable.getInGlobal(superclassName) == null)
				signalError.show("Class " + superclassName
						+ " was not declared");

			switch (superclassName) {
			case "int":
			case "boolean":
			case "String":
			case "void":
				signalError.show("Cant inheritance from basic types");
			}

			currentClass.setSuperclass(symbolTable.getInGlobal(superclassName));

			lexer.nextToken();
		}

		if (lexer.token != Symbol.LEFTCURBRACKET)
			signalError.show("{ expected", true);

		lexer.nextToken();

		while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC
				|| lexer.token == Symbol.STATIC || lexer.token == Symbol.FINAL) {

			isFinal = false;
			isStatic = false;

			if (lexer.token == Symbol.STATIC) {
				isStatic = true;
				lexer.nextToken();
			}

			if (lexer.token == Symbol.FINAL) {
				isFinal = true;
				lexer.nextToken();
			}

			Symbol qualifier;
			switch (lexer.token) {
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
			default:
				signalError.show("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}

			Type t = type();

			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");

			String name = lexer.getStringValue();
			lexer.nextToken();

			if (lexer.token == Symbol.LEFTPAR)
				methodDec(t, name, qualifier, isStatic, isFinal);
			else if (qualifier != Symbol.PRIVATE)
				signalError
						.show("Attempt to declare a public instance variable");
			else
				instanceVarDec(t, name, isStatic, isFinal);
		}

		if (lexer.token != Symbol.RIGHTCURBRACKET)
			signalError.show("public/private or \"}\" expected");

		/**
		 * "Every program must have a class named Program with a parameterless
		 * method called run. To start the execution of a program, the runtime
		 * system creates an object of class Program and sends to it a message
		 * run."
		 */
		if (currentClass.getName().equals("Program")) {
			boolean run = currentClass.containsPublicMethod("run");
			if (!run)
				signalError.show("Class Program without a method 'run'");
		}

		lexer.nextToken();
		String token = lexer.getStringValue();

	}

	/**
	 * Type IdList ";" <br>
	 * 
	 * @param type
	 * @param name
	 * @param isStatic
	 * @param isFinal
	 */
	private void instanceVarDec(Type type, String name, boolean isStatic,
			boolean isFinal) {

		if (isStatic) {
			if (currentClass.containsStaticVariable(name))
				signalError
						.show("Static variable " + name + "already declared");
			if (currentClass.containsStaticPrivateMethod(name)
					|| currentClass.containsStaticPublicMethod(name)
					|| currentClass.containsPrivateMethod(name)
					|| currentClass.containsPublicMethod(name))
				signalError.show(name + " already declared as a method");

			InstanceVariable staticVariable = new InstanceVariable(name, type,
					isStatic);
			currentClass.addStaticVariable(staticVariable);
			symbolTable.putInLocal(name, staticVariable);
		} else {
			if (currentClass.containsInstanceVariable(name))
				signalError
						.show("Static variable " + name + "already declared");
			if (currentClass.containsStaticPrivateMethod(name)
					|| currentClass.containsStaticPublicMethod(name)
					|| currentClass.containsPrivateMethod(name)
					|| currentClass.containsPublicMethod(name))
				signalError.show(name + " already declared as a method");

			InstanceVariable instanceVariable = new InstanceVariable(name,
					type, isStatic);
			currentClass.addInstanceVariable(instanceVariable);
			symbolTable.putInLocal(name, instanceVariable);
		}

		while (lexer.token == Symbol.COMMA) {

			lexer.nextToken();

			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");

			name = lexer.getStringValue();
			lexer.nextToken();

			if (isStatic) {
				if (currentClass.containsStaticVariable(name))
					signalError.show("Static variable " + name
							+ "already declared");
				if (currentClass.containsStaticPrivateMethod(name)
						|| currentClass.containsStaticPublicMethod(name)
						|| currentClass.containsPrivateMethod(name)
						|| currentClass.containsPublicMethod(name))
					signalError.show(name + " already declared as a method");

				InstanceVariable staticVariable = new InstanceVariable(name,
						type, isStatic);
				currentClass.addStaticVariable(staticVariable);
				symbolTable.putInLocal(name, staticVariable);
			} else {
				if (currentClass.containsInstanceVariable(name))
					signalError.show("Static variable " + name
							+ "already declared");
				if (currentClass.containsStaticPrivateMethod(name)
						|| currentClass.containsStaticPublicMethod(name)
						|| currentClass.containsPrivateMethod(name)
						|| currentClass.containsPublicMethod(name))
					signalError.show(name + " already declared as a method");

				InstanceVariable instanceVariable = new InstanceVariable(name,
						type, isStatic);
				currentClass.addInstanceVariable(instanceVariable);
				symbolTable.putInLocal(name, instanceVariable);
			}
		}
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
	}

	/**
	 * MethodDec ::= Type Id "(" [FormalParamDec] ")" "{" StatementList "} <br>
	 * 
	 * @param type
	 * @param name
	 * @param qualifier
	 * @param isStatic
	 * @param isFinal
	 */
	private void methodDec(Type type, String name, Symbol qualifier,
			boolean isStatic, boolean isFinal) {

		if (currentClass.containsStaticPrivateMethod(name)
				|| currentClass.containsStaticPublicMethod(name))
			signalError.show("Static method " + name + " already declared");
		if (currentClass.containsPrivateMethod(name)
				|| currentClass.containsPublicMethod(name))
			signalError.show("Method " + name + " already declared");
		if (currentClass.containsStaticVariable(name)
				|| currentClass.containsInstanceVariable(name))
			signalError.show(name + " already declared as a variable");

		currentMethod = new Method(name, type, isStatic, isFinal);
		
		if (isFinal && currentClass.isFinal()) {
			signalError.show("'final' method '" + name + "' in a 'final' class '" + currentClass.getName() + "'");
		}

		lexer.nextToken();
		if (lexer.token != Symbol.RIGHTPAR)
			formalParamDec();
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");

		if (isStatic) {
			if (currentClass.getName().equals("Program")
					&& currentMethod.getName().equals("run"))
				signalError
						.show("Method 'run' of class Program cannot be static");
			if (qualifier == Symbol.PUBLIC)
				currentClass.addStaticPublicMethod(currentMethod);
		} else {
			if (currentClass.getName().equals("Program")
					&& currentMethod.getName().equals("run")) {
				if (currentMethod.getParamList().getSize() > 0)
					signalError
							.show("Method 'run' of class Program must be parameterless");
				if (currentMethod.getReturnType() != Type.voidType)
					signalError
							.show("Method 'run' of class Program must return 'void'");
			}
			if (qualifier == Symbol.PUBLIC)
				currentClass.addPublicMethod(currentMethod);
			else {	
				if (currentClass.getName().equals("Program")
						&& currentMethod.getName().equals("run")) {
					signalError
							.show("Method 'run' of class Program cannot be private");
				}
				currentClass.addPrivateMethod(currentMethod);
			}
		}

		Method superclassMethod = null;

		KraClass superclass = currentClass.getSuperclass();
		while (superclass != null) {
			if (superclass.containsPublicMethod(name)) {
				superclassMethod = superclass.getPublicMethod(name);
				break;
			} else {
				superclass = superclass.getSuperclass();
			}
		}

		if (superclassMethod != null) {
			if (superclassMethod.isFinal()) {
				signalError
				.show("Redeclaration of final method '" + superclassMethod.getName() + "'");
			}
			if (type != superclassMethod.getReturnType())
				signalError
						.show("Attempt to override a method changing its signature (return type)");
			if (superclassMethod.getParamList().getSize() != currentMethod
					.getParamList().getSize())
				signalError
						.show("Attempt to override a method changing its signature (wrong number of parameters");

			ParamList superMethodPL = superclassMethod.getParamList();
			ParamList currentMethodPL = currentMethod.getParamList();

			int i = 0;
			for (Variable param : superMethodPL.getList()) {
				if (param.getType() != currentMethodPL.getList().get(i)
						.getType())
					signalError
							.show("Attempt to override a method changing its signature (parameter doesnt match types)");
				i++;
			}
		}

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTCURBRACKET)
			signalError.show("{ expected");

		lexer.nextToken();
		hasReturnStmt = false;
		StatementList stmtList = statementList();
		if (stmtList != null) {
			currentMethod.setStatementList(stmtList);
		}

		if (type != Type.voidType
				&& !hasReturnStmt) {
			signalError.show("Method '" + name + "' must have a return");
		}

		if (lexer.token != Symbol.RIGHTCURBRACKET) {
			signalError.show("} expected");
		}
		// Clears the local symbol table
		symbolTable.removeLocalIdent();
		
		lexer.nextToken();

	}

	/**
	 * LocalDec ::= Type IdList ";" <br>
	 */
	private void localDec() {

		Type type = type();
		String currentTokenValue;
		Variable v;
		if (lexer.token != Symbol.IDENT) {
			signalError.show("Identifier expected");
		}
		// get current token string value
		currentTokenValue = lexer.getStringValue();
		// creates variable
		v = new Variable(lexer.getStringValue(), type);
		// checks if it was previously declared or if is an instance variable
		if (!isInLocal(currentTokenValue) || currentClass.containsInstanceVariable(currentTokenValue)) {
			symbolTable.putInLocal(currentTokenValue, v);
			currentMethod.setLocalVariable(v);
		} else {
			signalError.show("=> Duplicated declariation of variable "
					+ currentTokenValue);
		}

		lexer.nextToken();
		
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			
			if ( lexer.token != Symbol.IDENT ) {
				signalError.show("Identifier expected, but found " + lexer.getStringValue());
			}
			
			currentTokenValue = lexer.getStringValue();
			v = new Variable(currentTokenValue, type);

			if (!isInLocal(currentTokenValue)) {
				symbolTable.putInLocal(currentTokenValue, v);
				currentMethod.setLocalVariable(v);
			} else {
				signalError.show("=> Duplicated declariation of variable "
						+ currentTokenValue);
			}
			
			lexer.nextToken();
		}
		if (lexer.token != Symbol.SEMICOLON) {
			signalError.show("=> ';' expected after Idlist ", true);
		}
	}

	// verifies if a key (variable) is already in the local symbol table
	private boolean isInLocal(String key) {

		if (symbolTable.getInLocal(key) == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * FormalParamDec ::= ParamDec { "," ParamDec } <br>
	 */
	private void formalParamDec() {

		paramDec();
		// If there is more than one parameter
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken(); // consome ','
			paramDec();
		}

	}

	/**
	 * ParamDec ::= Type Id <br>
	 */
	private void paramDec() {
		// ParamDec ::= Type Id

		Type t = type();
		// Expects the token to be an identifier
		String currentTokenValue = lexer.getStringValue();
		if (lexer.token != Symbol.IDENT) {
			signalError.show("Identifier expected");
		}
		Parameter param = new Parameter(currentTokenValue, t);
		currentMethod.addParam(param);
		symbolTable.putInLocal(currentTokenValue, param);
		
		lexer.nextToken();

	}

	/**
	 * Type ::= BasicType | Id <br>
	 * BasicType ::= "void" | "int" | "boolean" | "String" <br>
	 * 
	 * @return
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
			String currentTokenValue = lexer.getStringValue();
			Object o = null;
			if (isType(currentTokenValue)) {
				o = symbolTable.get(currentTokenValue);
			} else {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Class "
						+ currentTokenValue + " was not declared before use.");
				result = null;
			}

			if (o == null) {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Class "
						+ currentTokenValue + " was not declared before use.");
				result = null;
			}

			// Expect current token to be a class instance
			if (!isClass(o)) {
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected identifier " + currentTokenValue
						+ " to be a class.");
				result = null;
			}
			// Finally casts the object to a class instance
			result = (KraClass) o;
			break;
		default:
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Expected type, but got " + lexer.getStringValue());
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;

	}

	// verifies if some object is an instance of a class, returns true if it is
	private boolean isClass(Object obj) {

		return (obj instanceof KraClass);

	}

	/**
	 * CompositeStatement ::= "{" { Statement } "}" <br>
	 */
	private CompositeStatement compositeStatement() {

		lexer.nextToken(); // consome '{'
		StatementList stmtList = statementList();
		if (lexer.token != Symbol.RIGHTCURBRACKET) {
			signalError.show("} expected");
		} else
			lexer.nextToken(); // consome '}'

		return new CompositeStatement(stmtList);

	}

	/**
	 * StatementList ::= { Statement } <br>
	 * 
	 * @return
	 */
	private StatementList statementList() {

		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...

		StatementList stmtList = new StatementList();
		Statement stmt;

		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
				&& tk != Symbol.ELSE) {
			if ((stmt = statement()) != null) {
				stmtList.addElement(stmt);
			}
		}

		return stmtList;
	}

	/**
	 * Statement ::= AssignExprLocalDec ";" | IfStat | WhileStat | ReturnStat
	 * ";" | ReadStat ";" | WriteStat ";" | "break" ";" | ";" | CompStatement <br>
	 */
	private Statement statement() {
		
		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:
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
			return ifStatement();
		case BREAK:
			return breakStatement();
		case WHILE:
			return whileStatement();
		case SEMICOLON:
			return nullStatement();
		case LEFTCURBRACKET:
			return compositeStatement();
		default:
			signalError.show("Statement expected");
		}

		return null;

	}

	
	// retorne true se 'name' é uma classe declarada anteriormente.
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}

	/**
	 * AssignExprLocalDec ::= Expression [ "=" Expression ] | LocalDec <br>
	 * 
	 * @return
	 */
	private Statement assignExprLocalDec() {

		if (lexer.token == Symbol.INT
				|| lexer.token == Symbol.BOOLEAN
				|| lexer.token == Symbol.STRING
				|| (lexer.token == Symbol.IDENT && isType(lexer
						.getStringValue())
						&& symbolTable.getInLocal(lexer.getStringValue()) == null)) {

			localDec();
			
			if (lexer.token != Symbol.SEMICOLON) {
				signalError.show("';' expected");
			}
			
			lexer.nextToken();
			return null;

		} else {

			Expr esq;
			Expr dir = null;

			esq = expr();
			
			
			if (lexer.token == Symbol.ASSIGN) {
				lexer.nextToken();
				dir = expr();

				// verifica a compatibilidade dos tipos da atribuição.

				if (dir.getType() == Type.voidType) {
					signalError
							.show("Cant assignment a void call to a variable");
				}
				if (!canConvertType(esq.getType(), dir.getType())) {
					signalError.show("Uncompatible types in assignment");
				} else {
					// TODO CASTING
				}
				
				if (dir instanceof ObjectBuilder) {
					((ObjectBuilder) dir).setCastObject((KraClass) esq.getType());
				}

				if (lexer.token != Symbol.SEMICOLON)
					signalError.show("';' expected", true);
				else
					lexer.nextToken();

				return new AssignmentStatement(esq, dir);
			} else {

				// "A method that does not return a value can be used as a statement."
				// Otherwise, "must be called only within an expression".

				Type debugType = esq.getType();
				
				// Checks is the return type is used
				if (esq instanceof MessageSendToVariable) {
					// If it is a message send to variable
					if (esq.getType() != null && dir == null) {
						// If the return type of the method is not null
						if (((MessageSendToVariable) esq).getMethod().getReturnType() != Type.voidType) {
							signalError.show("Message send '" + ((MessageSendToVariable)esq).getVar().getName()
									+ "." + ((MessageSendToVariable)esq).getMethod().getName() + "()'"
									+ " returns a value that is not used");
						}
						
					}
				}
				
				if (esq.getType() == Type.voidType) {
					if (lexer.token != Symbol.SEMICOLON)
						signalError.show("';' expected", true);
					else {
						lexer.nextToken(); // consome ';'
						

						return new MessageSendStatement(esq);
					}
				} else {
					signalError.show("Call is an expression, not a statement");
				}
			}
		}

		return null;

	}

	// verifies if a type can be converted to another type
	private boolean canConvertType(Type type1, Type type2) {
		// If both types are the same
		if (type1 == type2) {
			// And are basic types
			if (type1 == Type.intType || type1 == Type.stringType
					|| type1 == Type.booleanType) {
				return true;
			}
		}
		
		// If are classes in hierarchy
		if (isType(type1.getName()) && isType(type2.getName())) {
			if (type1 == type2) {
				return true;
			} else {
				// Gets the supperclass of type2
				KraClass secondClass = symbolTable.getInGlobal(type2
						.getName());
				secondClass = secondClass.getSuperclass();
				// Runs on all hierarchy looking for a superclass compatible
				// with type1
				while (secondClass != null) {
					if (secondClass == type1) {
						// If found a compatible superclass
						return true;
					}
					secondClass = secondClass.getSuperclass();
				}
				// If it came here, no compatible class was found
				return false;
			}
		}

		// If the first type is a class and the second type is undefined
		if (isType(type1.getName()) && type2 == Type.undefinedType) {
			return true;
		}

		return false;

	}

	private ExprList realParameters() {

		ExprList anExprList = new ExprList();

		// Expects to find leftpar
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		// Initiates the exprList
		if (startExpr(lexer.token))
			anExprList = exprList();
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		return anExprList;

	}

	/**
	 * WhileStat ::= "while" "(" Expression ")" Statement <br>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private WhileStatement whileStatement() {

		/*
		 * lexer.nextToken(); if (lexer.token != Symbol.LEFTPAR)
		 * signalError.show("( expected"); lexer.nextToken(); expr(); if
		 * (lexer.token != Symbol.RIGHTPAR) signalError.show(") expected");
		 * lexer.nextToken(); statement();
		 */

		// If a while statement is found, it's added an item to the while stack
		whileStatements.push(new Integer(1));

		lexer.nextToken(); // consome 'while'

		String currentTokenValue = lexer.getStringValue();
		// Expects to find leftpar
		if (lexer.token != Symbol.LEFTPAR) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Expected to find '(' but got " + currentTokenValue
					+ " instead.");
		}

		lexer.nextToken(); // consome '('

		Expr expr = expr();
		if (expr.getType() != Type.booleanType) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Expected expression to be boolean, but got "
					+ expr.getType().getName() + " instead.");
		}
		currentTokenValue = lexer.getStringValue();
		// Expects to find rightpar
		if (lexer.token != Symbol.RIGHTPAR) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Expected to find ')' but got " + currentTokenValue
					+ " instead.");
		}

		lexer.nextToken(); // consome ')'

		Statement statement = statement();

		// After the code, pops the item from the stack
		whileStatements.pop();
		return new WhileStatement(expr, statement);

	}

	/**
	 * IfStat ::= "if" "(" Expression ")" Statement [ "else" Statement ] <br>
	 * 
	 * @return
	 */
	private IfStatement ifStatement() {

		lexer.nextToken();

		// Expect left par
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();

		Expr expr = expr();

		// Expect the expression to be a boolean
		if (expr.getType() != Type.booleanType) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Expected expression to be boolean, but got "
					+ expr.getType().getName() + " instead.");
		}

		// Expect a right par
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();

		// Create a ifStatement and elseStatement
		Statement statementIf = statement();
		Statement statementElse = null;
		// If there is an else
		if (lexer.token == Symbol.ELSE) {

			lexer.nextToken();

			statementElse = statement();
		}
		// Returns the instance of statement
		return new IfStatement(expr, statementIf, statementElse);

	}

	/**
	 * ReturnStat ::= "return" Expression <br>
	 * 
	 * @return
	 */
	private ReturnStatement returnStatement() {

		lexer.nextToken();

		// Gets the expression
		Expr returnExpr = expr();

		// Expect to find ;
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);

		// The type of the method with return must not be void
		if (currentMethod.getReturnType() == Type.voidType) {
			signalError
					.show("LINE:"
							+ lexer.getCurrentLine()
							+ "=> Return statement found on void method. Cannot return a void method");
		}
		// Or at least be convertible between one and another
		if (!canConvertType(currentMethod.getReturnType(), returnExpr.getType())) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Method type not convertible to return type."
					+ " The method type is " + currentMethod.getReturnType()
					+ " and the return type is " + returnExpr.getType());
		}
		hasReturnStmt = true;

		lexer.nextToken();
		
		return new ReturnStatement(returnExpr);

	}

	/**
	 * ReadStat ::= "read" "(" LeftValue { "," LeftValue } ")" <br>
	 * LeftValue ::= [ ("this"|Id) "." ] Id <br>
	 * 
	 * @return
	 */
	private ReadStatement readStatement() {

		ArrayList<Variable> ReadStatement = new ArrayList<Variable>();
		lexer.nextToken();

		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();

		while (true) {
			Symbol classVariable = null;

			if (lexer.token == Symbol.THIS) {
				classVariable = Symbol.THIS;
				lexer.nextToken();
				if (lexer.token != Symbol.DOT)
					signalError.show(". expected");
				lexer.nextToken();
			}
			if (lexer.token != Symbol.IDENT)
				signalError.show(SignalError.ident_expected);

			String name = lexer.getStringValue();

			if (classVariable == Symbol.THIS) {
				boolean found = currentClass.containsInstanceVariable(name);
				if (!found) {
					signalError.show("Class " + currentClass.getName()
							+ " have no instance variable '" + name
							+ "'. Wrong usage of 'this'");
				}

				Variable classVar = currentClass.getInstanceVariable(name);

				// só é possível ler tipos 'int' e 'String'.
				if (classVar.getType() != Type.intType
						&& classVar.getType() != Type.stringType) {
					signalError.show("Can only read int and String types");
				}

				ReadStatement.add(classVar);
			} else {
				Variable local = symbolTable.getInLocal(name);
				if (local == null)
					signalError.show("Variable " + name + " was not declared");
				if (local.getType() != Type.intType
						&& local.getType() != Type.stringType)
					signalError.show("Can only read int and String types");

				ReadStatement.add(local);
			}

			lexer.nextToken();
			if (lexer.token == Symbol.COMMA)
				lexer.nextToken();
			else
				break;
		}

		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();

		return new ReadStatement(ReadStatement);

	}

	/**
	 * WriteStat ::= "write" "(" ExpressionList ")" <br>
	 */
	private WriteStatement writeStatement() {

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR) {
			signalError.show("( expected");
		}
		lexer.nextToken();

//		ArrayList<Expr> exprList = exprList().getExprList();
		ExprList exprl = exprList();
		Iterator<Expr> exprIt = exprl.getElements();	
		while (exprIt.hasNext()) {
			Expr e = exprIt.next();
			
			if (e.getType() != Type.intType
				&& e.getType() != Type.stringType) {
				
				signalError.show("Can only write int and String types");
			}
		}

		if (lexer.token != Symbol.RIGHTPAR) {
			signalError.show(") expected");
		}
		lexer.nextToken();
		if (lexer.token != Symbol.SEMICOLON) {
			signalError.show(SignalError.semicolon_expected);
		}
		lexer.nextToken();

		return new WriteStatement(exprl);

	}

	/**
	 * WriteStat ::= "write" "(" ExpressionList ")" <br>
	 */
	private WriteLnStatement writelnStatement() {

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR) {
			signalError.show("( expected");
		}
		lexer.nextToken();
		ArrayList<Expr> exprList = exprList().getExprList();
		for (Expr expr : exprList) {
			if (expr.getType() != Type.intType
					&& expr.getType() != Type.stringType) {
				signalError.show("Can only write int and String types");
			}
		}

		if (lexer.token != Symbol.RIGHTPAR) {
			signalError.show(") expected");
		}
		lexer.nextToken();
		if (lexer.token != Symbol.SEMICOLON) {
			signalError.show(SignalError.semicolon_expected);
		}
		lexer.nextToken();

		return new WriteLnStatement(exprList);

	}

	/**
	 * BreakStat ::= "break" ";" <br>
	 */
	private BreakStatement breakStatement() {

		// whileStatements
		lexer.nextToken();

		// If there is no while statements on the stack
		// It means that the break statement is outside a while
		if (whileStatements.empty()) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Break statement called outside while statement.");
		}
		// Expected to have ; after break
		if (lexer.token != Symbol.SEMICOLON) {
			signalError.show("LINE:" + lexer.getCurrentLine()
					+ "=> Expected to find ';' but got "
					+ lexer.getStringValue() + " instead.");
		}
		lexer.nextToken();
		return new BreakStatement();

	}

	private NullStatement nullStatement() {

		lexer.nextToken();
		return new NullStatement();

	}

	/**
	 * ExpressionList ::= Expression { "," Expression } <br>
	 * 
	 * @return
	 */
	private ExprList exprList() {

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
	}

	/**
	 * Expression ::= SimpleExpression [ Relation SimpleExpression] <br>
	 * 
	 * @return
	 */
	private Expr expr() {

		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if (op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT) {
			lexer.nextToken();
			Expr right = simpleExpr();

			/**
			 * Verifica a combinação de tipos para os tipos básicos.
			 */
			switch (op) {
			case LE:
			case LT:
			case GE:
			case GT:
				if (left.getType() != Type.intType
						|| right.getType() != Type.intType) {
					signalError
							.show("Invalid operation (>, >=, <, <= are only valid to int variables)");
				}
				break;
			case EQ:
			case NEQ:
				/**
				 * Verifica se não são o tipo básico que suporta os operadores.
				 * Verifica, também, se são convertíveis e/ou diferentes.
				 */
				if (left.getType() != Type.stringType
						&& right.getType() != Type.stringType) {
					if (!canConvertType(left.getType(), right.getType())
							&& !canConvertType(right.getType(), left.getType())) {
						signalError
								.show("Invalid operation (incompatible types in comparison)");
					}
				} else {
					if (left.getType() != Type.undefinedType
							&& right.getType() != Type.undefinedType
							&& left.getType() != right.getType()) {
						signalError
								.show("Invalid operation (incompatible types in comparison)");
					}
				}
				break;
			default:
				// não deverá chegar a esse caso
			}

			left = new CompositeExpr(left, op, right);
		}
		return left;

	}

	/**
	 * SimpleExpression ::= Term { LowOperator Term } <br>
	 * 
	 * @return
	 */
	private Expr simpleExpr() {

		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			lexer.nextToken();
			Expr right = term();

			/**
			 * Verifica a combinação de tipos para os tipos básicos.
			 */
			switch (op) {
			case MINUS:
			case PLUS:
				if (left.getType() != Type.intType
						|| right.getType() != Type.intType) {
					signalError
							.show("Invalid operation (+ and - accept only int variables)");
				}
				break;
			case OR:
				if (left.getType() != Type.booleanType
						|| right.getType() != Type.booleanType) {
					signalError
							.show("Invalid operation (|| accept only boolean variables)");
				}
				break;
			default:
				// não deverá chegar a esse caso
			}

			left = new CompositeExpr(left, op, right);

		}

		return left;
	}

	/**
	 * Term ::= SignalFactor { HighOperator SignalFactor } <br>
	 * 
	 * @return
	 */
	private Expr term() {

		Symbol op;

		Expr leftExpr = signalFactor();
		// If there is a div, mult or add operator
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
			lexer.nextToken();
			Expr rightExpr = signalFactor();
			// If the operator is arithmetic (div or mult)
			if (op == Symbol.DIV || op == Symbol.MULT) {
				// The type of both expressions must be int
				if (leftExpr.getType() != Type.intType
						|| rightExpr.getType() != Type.intType) {
					signalError
							.show("LINE:"
									+ lexer.getCurrentLine()
									+ "=> Exprected int type, but got "
									+ leftExpr.getType()
									+ " and "
									+ rightExpr.getType()
									+ " instead."
									+ " Mult(*) and Div(/) operations only accept variables of type int");
				}
			} else {
				// If the operator is not div or mult, it is add
				// It only accepts boolean types
				if (leftExpr.getType() != Type.booleanType
						|| rightExpr.getType() != Type.booleanType) {
					signalError
							.show("=> Expected boolean type, but got '"
									+ leftExpr.getType().getName()
									+ "' and '"
									+ rightExpr.getType().getName()
									+ "' instead."
									+ " Type 'int' does not support operator '&&'");
				}
			}
			// Composes continuously the expr on the left
			leftExpr = new CompositeExpr(leftExpr, op, rightExpr);
		}
		return leftExpr;

	}

	/**
	 * SignalFactor ::= [ Signal ] Factor <br>
	 * Signal ::= "+" | "-" <br>
	 * 
	 * @return
	 */
	private Expr signalFactor() {
		Symbol op;
		// If there is a signal
		if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
			lexer.nextToken();
			Expr expr = factor();
			// Signals only work with int type
			if (expr.getType() != Type.intType) {
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected int type, but got " + expr.getType()
						+ " instead.");
			}

			return new SignalExpr(op, expr);
		} else {
			// If there is no signal
			return factor();
		}
	}

	/**
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 * ObjectCreation | PrimaryExpr <br>
	 * 
	 * @return
	 */
	private Expr factor() {

		Expr e;
		ExprList exprList;
		String messageName, ident;

		Method method = null;
		ParamList paramListMethod;

		switch (lexer.token) {

		case LITERALINT:
			return literalInt();

		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;

		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;

		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);

		case LEFTPAR:
			lexer.nextToken();
			e = expr();
			if (lexer.token != Symbol.RIGHTPAR)
				signalError.show(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(e);

		case NULL:
			lexer.nextToken();
			return new NullExpr();

		case NOT:
			lexer.nextToken();
			e = expr();
			// Not operator only works with a boolean expression
			if (e.getType() != Type.booleanType) {
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected type to be boolean, but got "
						+ e.getType() + " instead."
						+ " Not (!) operator accepts only boolean values.");
			}

			return new UnaryExpr(e, Symbol.NOT);

		case NEW:
			lexer.nextToken();
			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");

			String className = lexer.getStringValue();

			// Finds the className on globalSymbolTable KraClass
			KraClass aClass = symbolTable.getInGlobal(className);
			if (aClass == null) {
				signalError
						.show("LINE:"
								+ lexer.getCurrentLine()
								+ "=> Class "
								+ className
								+ " was used before it was declared or was not declared.");
			}

			lexer.nextToken();
			if (lexer.token != Symbol.LEFTPAR)
				signalError.show("( expected");
			lexer.nextToken();
			if (lexer.token != Symbol.RIGHTPAR)
				signalError.show(") expected");
			lexer.nextToken();

			return new ObjectBuilder(aClass);

		case SUPER:
			lexer.nextToken();

			if (lexer.token != Symbol.DOT)
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected to find '.', but found "
						+ lexer.getStringValue() + " instead.");
			lexer.nextToken();

			if (lexer.token != Symbol.IDENT)
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected to find 'identifier', but found "
						+ lexer.getStringValue() + " instead.");

			messageName = lexer.getStringValue();

			// Search on superclass in order to find messageName
			KraClass superKraClass = currentClass.getSuperclass();

			// The class should have a superclass
			if (superKraClass == null) {
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected " + currentClass.getName()
						+ " to have a superclass.");
			}

			boolean superKraClassFound = false;

			// Iterates over all superclasses on the hierarchy until a valid
			// superclass is found (or not)
			while (superKraClass != null) {
				superKraClassFound = superKraClass
						.containsPublicMethod(messageName);
				if (superKraClassFound) {
					break;
				} else {
					superKraClass = superKraClass.getSuperclass();
				}
			}

			// If couldn't find any
			if (!superKraClassFound) {
				signalError.show("LINE:" + lexer.getCurrentLine()
						+ "=> Expected " + currentClass.getName()
						+ " to have a superclass with a public method "
						+ "called " + messageName);
			}

			lexer.nextToken();

			exprList = realParameters();
			method = superKraClass.getPublicMethod(messageName);
			paramListMethod = method.getParamList();

			// Checks if the number of parameters is equal to the number of
			// expressions
			if (paramListMethod.getSize() == exprList.getSize()) {
				Iterator<Expr> exprListIterator = exprList.getElements();
				Iterator<Parameter> paramListMethodIterador = paramListMethod
						.getElements();
				// Parameter Counter, only for better error messages
				int parametersCounter = 1;

				// Iterates over the parameters and expressions and checks if
				// they match on type or can be converted between them
				while (exprListIterator.hasNext()
						&& paramListMethodIterador.hasNext()) {
					Expr expr = exprListIterator.next();
					Parameter parameter = paramListMethodIterador.next();

					// Checks if the can convert the types
					if (!canConvertType(parameter.getType(), expr.getType())) {
						signalError
								.show("LINE:"
										+ lexer.getCurrentLine()
										+ "=> Expected expression type to be convertible to parameter "
										+ parametersCounter + " type."
										+ " The expression type is "
										+ expr.getType()
										+ " and the return type is "
										+ parameter.getType());
					}

					parametersCounter++;
				}
			} else {
				signalError
						.show("LINE:"
								+ lexer.getCurrentLine()
								+ "=> The number of parameters is different from the number of expressions.");
			}
			return new MessageSendToSuper(method, exprList, currentClass);

		case IDENT:
			String firstId = lexer.getStringValue();
			lexer.nextToken();
			
			String debug = lexer.getStringValue();
			if (lexer.token != Symbol.DOT) {
				
				boolean found = (currentClass.containsStaticPrivateMethod(firstId)
						|| currentClass.containsStaticPublicMethod(firstId));

				if (found)
					signalError.show("Wrong call of static method " + firstId);

				found = currentClass.containsPrivateMethod(firstId)
						|| currentClass.containsPublicMethod(firstId);
				if (found)
					signalError.show("Wrong call of method " + firstId
							+ " (need to use 'this'");
				Variable var = symbolTable.getInLocal(firstId);
				
				if (var == null && currentClass.containsInstanceVariable(firstId)) {
					signalError.show("Cant use instance variables without 'this'");
				} else if (var == null) {
					signalError.show("Identifier " + firstId + " was not declared");
				}
				
				if(var instanceof InstanceVariable && currentClass.containsInstanceVariable(firstId)) {
					signalError.show("Cant use instance variables without 'this'. Identifier '" + firstId + "' was not declared");
				}
				return new VariableExpr(var);
			} else {
				lexer.nextToken();
				if (lexer.token != Symbol.IDENT) {
					signalError.show("Identifier expected");
				} else {
					if (!isType(symbolTable.getInLocal(firstId).getType()
							.getName())) {
						signalError.show("Variable " + firstId
								+ " was not declared");
					}

					ident = lexer.getStringValue();
					lexer.nextToken();

					if (lexer.token == Symbol.DOT) {
						
						/*
						 * Se o compilador permite variáveis estáticas, é
						 * possível ter esta opçãoo, como
						 * Clock.currentDay.setDay(12); Contudo, se variáveis
						 * estáticas não estiver nas especificações, sinalize um
						 * erro neste ponto.
						 */

						signalError
								.show("'static' support to be fully developed");

					} else if (lexer.token == Symbol.LEFTPAR) {
						KraClass classOfIdent = symbolTable
								.getInGlobal(symbolTable.getInLocal(firstId).getType().getName());

						if (currentClass.getName() == classOfIdent.getName()) {
							if (classOfIdent.containsPrivateMethod(ident))
								method = classOfIdent.getPrivateMethod(ident);
							if (classOfIdent.containsPublicMethod(ident))
								method = classOfIdent.getPublicMethod(ident);

							if (method == null) {
								classOfIdent = classOfIdent.getSuperclass();
								while (classOfIdent != null) {
									if (classOfIdent
											.containsPublicMethod(ident)) {
										method = classOfIdent
												.getPublicMethod(ident);
										break;
									} else
										classOfIdent = classOfIdent
												.getSuperclass();
								}
							}

							if (method == null)
								signalError.show("No method " + ident
										+ " was found");
						} else {
							if (classOfIdent.containsPublicMethod(ident)) {
								method = classOfIdent.getPublicMethod(ident);
							}

							if (method == null) {
								classOfIdent = classOfIdent.getSuperclass();

								while (classOfIdent != null) {
									if (classOfIdent
											.containsPublicMethod(ident)) {
										method = classOfIdent
												.getPublicMethod(ident);
										break;
									}
									classOfIdent = classOfIdent.getSuperclass();
								}
							}

							if (method == null) {
								signalError.show("No public method "
										+ ident
										+ " was found for the class "
										+ symbolTable.getInLocal(firstId)
												.getType().getName());
							}
						}
						// method.getReturnType()
						exprList = realParameters();
						ParamList currentMethodPL = method.getParamList();
						
						if (exprList.getSize() != currentMethodPL.getSize()) {
							signalError.show("Wrong number of parameters");
						}

						Iterator<Expr> exprListIterator = exprList
								.getElements();
						Iterator<Parameter> paramListMethodIterador = currentMethodPL
								.getElements();
						// Parameter Counter, only for better error messages
						int parametersCounter = 1;

						// Iterates over the parameters and expressions and
						// checks if
						// they match on type or can be converted between them
						while (exprListIterator.hasNext()
								&& paramListMethodIterador.hasNext()) {
							Expr expr = exprListIterator.next();
							Parameter parameter = paramListMethodIterador
									.next();

							// Checks if the can convert the types
							if (!canConvertType(parameter.getType(),
									expr.getType())) {
								signalError
										.show("LINE:"
												+ lexer.getCurrentLine()
												+ "=> Expected expression type to be convertible to parameter "
												+ parametersCounter + " type."
												+ " The expression type is "
												+ expr.getType()
												+ " and the return type is "
												+ parameter.getType());
							}

							parametersCounter++;
						}

						return new MessageSendToVariable(
								symbolTable.getInLocal(firstId), method,
								exprList);
					} else {

						KraClass classOfIdent = symbolTable
								.getInGlobal(symbolTable.getInLocal(firstId)
										.getType().getName());

						if (classOfIdent.containsInstanceVariable(ident)) {
							return new MessageSendToInstance(
									symbolTable.getInLocal(firstId),
									classOfIdent.getInstanceVariable(ident));
						} else {
							signalError.show("Class " + classOfIdent.getName()
									+ " have no instance variable " + ident);
						}
					}
				}
			}

		case THIS:

			Method thisMethod = null;

			if (currentMethod.isStatic())
				signalError.show("Cant use 'this' in a 'static' method");

			lexer.nextToken();

			if (lexer.token != Symbol.DOT) {
				return new MessageSendToSelf(currentClass, null, null, null);
			} else {
				lexer.nextToken();

				if (lexer.token != Symbol.IDENT)
					signalError.show("Identifier expected");

				ident = lexer.getStringValue();
				lexer.nextToken();

				if (lexer.token == Symbol.LEFTPAR) {
					thisMethod = null;
					if (currentClass.containsPrivateMethod(ident))
						thisMethod = currentClass.getPrivateMethod(ident);
					else if (currentClass.containsPublicMethod(ident))
						thisMethod = currentClass.getPublicMethod(ident);
					else if (currentClass.containsStaticPrivateMethod(ident))
						thisMethod = currentClass.getStaticPrivateMethod(ident);
					else if (currentClass.containsStaticPublicMethod(ident))
						thisMethod = currentClass.getStaticPublicMethod(ident);
					else {
						if (currentClass.getSuperclass() != null) {
							thisMethod = currentClass.getSuperclass().getPublicMethod(ident);
						} else {
							thisMethod = null;
						}
					}
					if (thisMethod == null) {
						signalError
								.show("Method '" + ident + "' was not found in class '"+ currentClass.getName() +"' or any superclass");
					}

					exprList = this.realParameters();
					ParamList currentMethodPL = thisMethod.getParamList();

					if (exprList.getSize() != currentMethodPL.getSize()) {
						signalError.show("Wrong number of parameters");
					}

					Iterator<Expr> exprListIterator = exprList.getElements();
					Iterator<Parameter> paramListMethodIterador = currentMethodPL
							.getElements();
					// Parameter Counter, only for better error messages
					int parametersCounter = 1;

					// Iterates over the parameters and expressions and checks
					// if
					// they match on type or can be converted between them
					while (exprListIterator.hasNext()
							&& paramListMethodIterador.hasNext()) {
						Expr expr = exprListIterator.next();
						Parameter parameter = paramListMethodIterador.next();

						// Checks if the can convert the types
						if (!canConvertType(parameter.getType(), expr.getType())) {
							signalError
									.show("=> Expected expression type to be convertible to parameter "
											+ parametersCounter + " type."
											+ " The expression type is "
											+ expr.getType()
											+ " and the return type is "
											+ parameter.getType());
						}

						parametersCounter++;
					}

					return new MessageSendToSelf(currentClass, null,
							thisMethod, exprList);
				} else if (lexer.token == Symbol.DOT) {
					lexer.nextToken();

					if (lexer.token != Symbol.IDENT)
						signalError.show("Identifier expected");
					lexer.nextToken();

					String identIdent = lexer.getStringValue();
					lexer.nextToken();

					InstanceVariable instVar = (InstanceVariable) currentClass
							.getInstanceVariable(identIdent);

					if (!isType(instVar.getType().getName()))
						signalError
								.show("Variable is not an object of any class or basic type");

					KraClass thisClass = symbolTable.getInGlobal(instVar
							.getType().getName());

					if (!thisClass.containsPublicMethod(identIdent))
						signalError.show("Method not found");
					else
						thisMethod = thisClass.getPublicMethod(identIdent);

					exprList = this.realParameters();
					ParamList currentMethodPL = thisMethod.getParamList();

					if (exprList.getSize() != currentMethodPL.getSize()) {
						signalError.show("Wrong number of parameters");
					}

					Iterator<Expr> exprListIterator = exprList.getElements();
					Iterator<Parameter> paramListMethodIterador = currentMethodPL
							.getElements();
					// Parameter Counter, only for better error messages
					int parametersCounter = 1;

					// Iterates over the parameters and expressions and checks
					// if
					// they match on type or can be converted between them
					while (exprListIterator.hasNext()
							&& paramListMethodIterador.hasNext()) {
						Expr expr = exprListIterator.next();
						Parameter parameter = paramListMethodIterador.next();

						// Checks if the can convert the types
						if (!canConvertType(parameter.getType(), expr.getType())) {
							signalError
									.show("LINE:"
											+ lexer.getCurrentLine()
											+ "=> Expected expression type to be convertible to parameter "
											+ parametersCounter + " type."
											+ " The expression type is "
											+ expr.getType()
											+ " and the return type is "
											+ parameter.getType());
						}

						parametersCounter++;
					}

					return new MessageSendToSelf(currentClass, instVar,
							thisMethod, exprList);
				} else {
					String nameOfIdent = lexer.getStringValue();

					if (!currentClass.containsInstanceVariable(nameOfIdent))
						signalError
								.show("Class does not have the wanted variable");

					InstanceVariable lastVar = (InstanceVariable) currentClass
							.getInstanceVariable(nameOfIdent);

					return new MessageSendToSelf(currentClass, lastVar, null,
							null);
				}
			}
		default:
			signalError.show("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);

	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE
				|| token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.LITERALINT || token == Symbol.SUPER
				|| token == Symbol.LEFTPAR || token == Symbol.NULL
				|| token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}

	private SymbolTable symbolTable;
	private Lexer lexer;
	private SignalError signalError;
	private boolean hasReturnStmt;

	private KraClass currentClass;
	private Method currentMethod;
	private Stack whileStatements = new Stack();

}
