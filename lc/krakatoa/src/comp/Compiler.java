/**
 * Laborat�rio de Compiladores 2015/2
 * Universidade Federal de S�o Carlos
 * Orienta��o: Prof. Dr. Jos� de O. Guimar�es
 * 
 * @author Maur�cio Spinardi 408174
 * @author Vitor Casadei 408301
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


	private Program program(ArrayList<CompilationError> compilationErrorList) {
		// Program ::= KraClass { KraClass }
		
		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		Program program = new Program(kraClassList, metaobjectCallList, compilationErrorList);
		try {
			while ( lexer.token == Symbol.MOCall ) {
				metaobjectCallList.add(metaobjectCall());
			}
			kraClassList.add(classDec());
			while ( lexer.token == Symbol.CLASS )
				kraClassList.add(classDec());
			if ( lexer.token != Symbol.EOF ) {
				signalError.show("End of file expected");
			}
			
			/**
			* "Every class can only refer to the classes that appear textually before it."
			* Considerando que Program � a classe principal, ent�o Program deve ser a �ltima
			* classe na lista de classes.
			*/
			int lastClass = kraClassList.size() - 1;
			if ( !kraClassList.get(lastClass).getName().equals("Program") ) {
				signalError.show("The last class must be the Program class");
			}
			
		}
		catch( RuntimeException e) {
			// if there was an exception, there is a compilation signalError
		}
		return program;
		
	}

	/**  parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     *     public void run() { } <br>
     * end <br>
     * </code>
     * 
	   
	 */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		
		String name = lexer.getMetaobjectName();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		if ( lexer.token == Symbol.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING ||
					lexer.token == Symbol.IDENT ) {
				switch ( lexer.token ) {
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
				if ( lexer.token == Symbol.COMMA ) 
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Symbol.RIGHTPAR ) 
				signalError.show("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if ( name.equals("nce") ) {
			if ( metaobjectParamList.size() != 0 )
				signalError.show("Metaobject 'nce' does not take parameters");
		}
		else if ( name.equals("ce") ) {
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				signalError.show("Metaobject 'ce' take three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  )
				signalError.show("The first parameter of metaobject 'ce' should be an integer number");
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				signalError.show("The second and third parameters of metaobject 'ce' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )  
				signalError.show("The fourth parameter of metaobject 'ce' should be a literal string");
			
		}
			
		return new MetaobjectCall(name, metaobjectParamList);
		
	}

	// TODO FINAL
	private KraClass classDec() {
		// Note que os m�todos desta classe n�o correspondem exatamente �s
		// regras da gram�tica. Este m�todo classDec, por exemplo, implementa
		// a produ��o KraClass (veja abaixo) e partes de outras produ��es.

		/*
		 * KraClass		::= "class" Id [ "extends" Id ] "{" MemberList "}"
		 * MemberList	::= { Qualifier Member } 
		 * Member		::= InstVarDec | MethodDec
		 * InstVarDec	::= Type IdList ";" 
		 * MethodDec	::= Qualifier Type Id "("[ FormalParamDec ] ")"
		 * 						"{" StatementList "}" 
		 * Qualifier	::= [ "static" ]  ( "private" | "public" )
		 */
		
		if ( lexer.token != Symbol.CLASS ) {
			signalError.show("'class' expected");
		}
		
		lexer.nextToken();
		
		if ( lexer.token != Symbol.IDENT ) {
			signalError.show(SignalError.ident_expected);
		}
		
		String className = lexer.getStringValue();
		
		/**
		 * Verifica se a classe foi declarada anteriormente.
		 */
		KraClass alreadyDeclared = symbolTable.getInGlobal(className);
		if ( alreadyDeclared != null ) {
			signalError.show("Class " + className + " already declared");
		}
		currentClass = new KraClass(className);
		symbolTable.putInGlobal(className, currentClass);
		lexer.nextToken();
		
		/**
		 * Verifica se está sendo feito uso de herança.
		 */
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT ) {
				signalError.show(SignalError.ident_expected);
			}
			String superclassName = lexer.getStringValue();
			
			if ( className.equals(superclassName) ) {
				signalError.show("Cant inheritance from the class itself");
			}
			alreadyDeclared = symbolTable.getInGlobal(superclassName);
			if ( alreadyDeclared == null ) {
				signalError.show("Class " + superclassName + " was not declared");
			}
			currentClass.setSuperclass(new KraClass(superclassName));
			
			lexer.nextToken();
		}
		/**
		 * Caso não esteja sendo feito uso de herança.
		 */
		else currentClass.setSuperclass(null);
		
		if ( lexer.token != Symbol.LEFTCURBRACKET ) {
			signalError.show("{ expected", true);
		}
		lexer.nextToken();

		/**
		 * Verifica os tipos dos métodos/variáveis.
		 */
		while ( lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC || lexer.token == Symbol.STATIC ) {
			Symbol qualifier;
			isStatic = false;
			
			if ( lexer.token == Symbol.STATIC ) {
				isStatic = true;
			}
			
			switch ( lexer.token ) {
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
			
			if ( lexer.token != Symbol.IDENT ) {
				signalError.show("Identifier expected");
			}
			String name = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token == Symbol.LEFTPAR ) {
				methodDec(t, name, qualifier);
			}
			else {
				if ( qualifier != Symbol.PRIVATE ) {
					signalError.show("Attempt to declare a public instance variable");
				}
				else instanceVarDec(t, name);
			}
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			signalError.show("public/private or \"}\" expected");
		}
		lexer.nextToken();

		/**
		 * "Every program must have a class named Program with a parameterless method
		 * called run. To start the execution of a program, the runtime system creates
		 * an object of class Program and sends to it a message run."
		 */
		if ( currentClass.getName().equals("Program") ) {
			boolean run = currentClass.containsPublicMethod("run");
			if ( !run ) signalError.show("Class Program without a method 'run'");
		}
		
		return currentClass;
		
	} 
	
	
	private void instanceVarDec(Type type, String name) {
		// InstVarDec 	::= [ "static" ] "private" Type IdList ";"

		boolean found;

		while ( lexer.token == Symbol.COMMA ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT ) signalError.show("Identifier expected");
			String variableName = lexer.getStringValue();
			
			int check = 0;
			if ( isStatic ) check = 1;
			else check = 2;
			
			/**
			 * Verifica se a variável é estática.
			 */
			switch ( check ) {
			case 1:
				/**
				 * Verifica se a variável já foi declarada.
				 */
				found = currentClass.containsStaticVariable(name);
				if ( found ) {
					signalError.show("Static variable " + name + "already declared");
				}
				else {
					/**
					 * Verifica se já existe algum método de mesmo nome.
					 */
					found = currentClass.containsStaticPrivateMethod(name);
					if ( !found ) found = currentClass.containsStaticPublicMethod(name);
					if ( !found ) found = currentClass.containsPrivateMethod(name);
					if ( !found ) found = currentClass.containsPublicMethod(name);
					if ( found ) {
						signalError.show(name + " already declared as a method");
					}
				}
				/**
				 * Declara, na verdade, uma variável estática.
				 */
				currentClass.addStaticVariable(new InstanceVariable(name, type, true));
				break;
			case 2:
				/**
				 * Verifica se a variável já foi declarada.
				 */
				found = currentClass.containsInstanceVariable(variableName);
				if ( found ) {
					signalError.show("Instance variable " + name + " already declared");
				}
				else {
					/**
					 * Verifica se já existe algum método de mesmo nome.
					 */
					found = currentClass.containsStaticPrivateMethod(name);
					if ( !found ) found = currentClass.containsStaticPublicMethod(name);
					if ( !found ) found = currentClass.containsPrivateMethod(name);
					if ( !found ) found = currentClass.containsPublicMethod(name);
					if ( found ) {
						signalError.show(name + " already declared as a method");
					}
				}
				/**
				 * Declara uma variável de instância.
				 */
				currentClass.addInstanceVariable(new InstanceVariable(name, type));
				break;
			default:
				// não deverá chegar a esse caso
			}
			lexer.nextToken();
		}
		
		if ( lexer.token != Symbol.SEMICOLON ) {
			signalError.show(SignalError.semicolon_expected);
		}
		lexer.nextToken();
		
	}
	
	
	private void methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec		::=	Qualifier Return Id "("[ FormalParamDec ] ")"
		 * 							"{" StatementList "}"
		 */

		boolean found;

		/**
		 * Verifica se o método é estático.
		 */
		int check = 0;
		if ( isStatic ) check = 1;
		else check = 2;
		
		switch ( check ) {
		case 1:
			/**
			 * Verifica se o método já foi declarado.
			 * Vale lembrar que podem existir dois métodos com a mesma assinatura,
			 * desde que um seja estático.
			 */
			found = currentClass.containsStaticPrivateMethod(name) || currentClass.containsStaticPublicMethod(name);
			if ( found ) {
				signalError.show("Static method " + name + " already declared");
			}
			break;
		case 2:
			found = currentClass.containsPrivateMethod(name) || currentClass.containsPublicMethod(name);
			if ( found ) {
				signalError.show("Method " + name + " already declared");
			}
			break;
		default:
			// não deverá chegar a esse caso
		}
		
		/**
		 * Verifica se já existe alguma variável de mesmo nome.
		 */
		found = currentClass.containsStaticVariable(name) || currentClass.containsInstanceVariable(name);
		if ( found ) {
			signalError.show(name + " already declared as an instance variable");
		}
		
		if ( isStatic ) currentMethod = new Method(name, type, true);
		else currentMethod = new Method(name, type);
		
		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ) formalParamDec();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		
		String className = currentClass.getName();
		String methodName = currentMethod.getName();
		
		if ( qualifier == Symbol.PUBLIC ) {
			switch ( check ) {
			case 1:
				currentClass.addStaticPublicMethod(currentMethod);
				if ( className.equals("Program") && methodName.equals("run") ) {
					signalError.show("Method 'run' of class Program cannot be static");
				}
				break;
			case 2:
				currentClass.addPublicMethod(currentMethod);
				if ( className.equals("Program") && methodName.equals("run") ) {
					if ( currentMethod.getParamList().getSize() > 0 ) {
						signalError.show("Method 'run' of class Program must be parameterless");
					}
					if ( currentMethod.getReturnType() != Type.voidType ) {
						signalError.show("Method 'run' of class Program must return 'void'");
					}
				}
				break;
			default:
				// não deverá chegar a esse caso
			}
		}
		else {
			switch ( check ) {
			case 1:
				if ( isStatic ) {
					currentClass.addStaticPrivateMethod(currentMethod);
					if ( className.equals("Program") && methodName.equals("run") ) {
						signalError.show("Method 'run' of class Program cannot be static");
					}
				}
				break;
			case 2:
				currentClass.addPrivateMethod(currentMethod);
				if ( className.equals("Program") && methodName.equals("run") ) {
					signalError.show("Method 'run' of class Program must be public");
				}
				break;
			default:
				// não deverá chegar a esse caso
			}
		}

		Method superclassMethod = null;
		
		KraClass superclass = currentClass.getSuperclass();
		while ( superclass != null ) {
			found = superclass.containsPublicMethod(name);
			if ( found ) {
				superclassMethod = superclass.getPublicMethod(name);
				break;
			}
			else superclass = superclass.getSuperclass();
		}
		
		/**
		 * Verifica se está ocorrendo uma sobrecarga de método.
		 */
		if ( superclassMethod != null ) {
			if ( type != superclassMethod.getReturnType() ) {
				signalError.show("Attempt to override a method changing its signature (return type)");
			}
			if ( superclassMethod.getParamList().getSize() != currentMethod.getParamList().getSize() ) {
				signalError.show("Attempt to override a method changing its signature (wrong number of parameters");
			}
			
			ParamList superMethodPL = superclassMethod.getParamList();
			ParamList currentMethodPL = currentMethod.getParamList();
			
			int i = 0;
			for ( Variable param : superMethodPL.getList() ) {
				if ( param.getType() != currentMethodPL.getList().get(i).getType() ) {
					signalError.show("Attempt to override a method changing its signature (parameter " + i + " doesnt match types");
				}
				i++;
			}
		}

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) {
			signalError.show("{ expected");
		}

		lexer.nextToken();
		
		returnStatement = false;
		
		currentMethod.setStatementList(statementList());
		
		if ( type != Type.voidType ) {
			if ( !returnStatement ) {
				signalError.show("Method " + name + " must have a return");
			}
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			signalError.show("} expected");
		}
		
		symbolTable.removeLocalIdent();

		lexer.nextToken();

	}

	
	private void localDec() {
		// LocalDec 	::= Type IdList ";"

		Type type = type();
		// Get current token string value
		String currentTokenValue = lexer.getStringValue();
		// Expect to be IDENT
		if ( lexer.token != Symbol.IDENT ) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected Identifier, but got " + currentTokenValue + "instead.");
		}
		// Creates variable
		Variable v = new Variable(currentTokenValue, type);
		
		// Checks if it was previously declared
		if (isInLocal(currentTokenValue)) {
			symbolTable.putInLocal(currentTokenValue, v);
			currentMethod.setLocalVariable(v);
		} else {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Duplicated declariation of variable " + currentTokenValue);
		}
		
		lexer.nextToken();
		// Verifies if there is more variables declared (followed by commas) and repeats
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			currentTokenValue = lexer.getStringValue();
			if ( lexer.token != Symbol.IDENT ) {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected Identifier, but got " + currentTokenValue + "instead.");
			}
			v = new Variable(currentTokenValue, type);
			
			if (isInLocal(currentTokenValue)) {
				symbolTable.putInLocal(currentTokenValue, v);
				currentMethod.setLocalVariable(v);
			} else {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Duplicated declariation of variable " + currentTokenValue);
			}
			
			lexer.nextToken();
		}
		
	}
	
	// Verifies if a key (variable) is already in the local symbol table
	private boolean isInLocal(String key) {
		
		if (symbolTable.getInLocal(key) == null) {
			return false;
		} else {
			return true;
		}
		
	}

	
	private void formalParamDec() {
		// FormalParamDec 	::= ParamDec { "," ParamDec }

		paramDec();
		// If there is more than one parameter
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			paramDec();
		}
		
	}

	
	private void paramDec() {
		
		Type t = type();
		// Expects the token to be an identifier
		String currentTokenValue = lexer.getStringValue();
		if ( lexer.token != Symbol.IDENT ) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected Identifier, but got " + currentTokenValue + "instead.");
		}
		// Verifies if there already exists another parameter of the same type and name on local
		if (isInLocal(currentTokenValue)) {
			// Creates the param and sets in the method's params and local symbol table
			Parameter param = new Parameter(currentTokenValue, t);
			currentMethod.addParam(param);
			symbolTable.putInLocal(currentTokenValue, param);
		} else {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Duplicated declariation of variable " + currentTokenValue);
		}
		
		lexer.nextToken();
		
	}
	
	
	private Type type() {
		// Type		::= BasicType | Id
		
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
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Class " + currentTokenValue + " was not declared before use.");
				result = null;
			}
			
			if (o == null) {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Class " + currentTokenValue + " was not declared before use.");
				result = null;
			}
			
			// Expect current token to be a class instance
			if (!isClass(o)) {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected identifier " + currentTokenValue + " to be a class.");
				result = null;
			}
			// Finally casts the object to a class instance
			result = (KraClass) o;
			break;
		default:
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected type, but got " + lexer.getStringValue());
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
		
	}

	
	private CompositeStatement compositeStatement() {
		
		lexer.nextToken();
		StatementList statementList = statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) {
			signalError.show("} expected");
		}
		else lexer.nextToken();
		
		return new CompositeStatement(statementList);
		
	}

	
	private StatementList statementList() {
		// CompStatement ::= "{" { Statement } "}"
		
		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...
		
		StatementList stmtList = new StatementList();
		Statement stmt;
		
		while ( (tk = lexer.token) != Symbol.RIGHTCURBRACKET && tk != Symbol.ELSE ) {
			if ( (stmt = statement()) != null ) {
				stmtList.addElement(stmt);
			}
		}
		
		return stmtList;
		
	}

	// TODO NUMBER
	private Statement statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 *                ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 *               ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */

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
	
	/*
	 * Retorna true se 'name' é uma classe declarada anteriormente.
	 */
	private boolean isType(String name) {
		
		return this.symbolTable.getInGlobal(name) != null;
		
	}

	// Verifies if some object is an instance of a class, returns true if it is
	private boolean isClass(Object obj) {
		
		return (obj instanceof KraClass);
		
	}

	
	// TODO NUMBER AND CASTING
	private Statement assignExprLocalDec() {
		/*
		 * AssignExprLocalDec 	::= Expression [ ``$=$'' Expression ] | LocalDec
		 */
		
		if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
				|| lexer.token == Symbol.STRING ||
				// token � uma classe declarada textualmente antes desta
				// instru��o
				(lexer.token == Symbol.IDENT && isType(lexer.getStringValue())) ) {
			/*
			 * uma declara��o de vari�vel. 'lexer.token' � o tipo da vari�vel
			 * 
			 * AssignExprLocalDec 	::= Expression [ ``$=$'' Expression ] |
			 * 								LocalDec 
			 * LocalDec 			::= Type IdList ``;''
			 */
			localDec();
			
			/**
			 * Verifica, uma vez que se trata de uma declaração de variável,
			 * se não ocorreu erro léxico por ausência de ';'
			 */
			if (lexer.token != Symbol.SEMICOLON) {
				signalError.show("; expected");
			}
		}
		else {
			
			/*
			 * AssignExprLocalDec 	::= Expression [ ``$=$'' Expression ]
			 */
			
			/**
			 * Se trata de uma atribuição, de fato.
			 */
			Expr esq;
			Expr dir;
			
			esq = expr();
			if ( lexer.token == Symbol.ASSIGN ) {
				lexer.nextToken();
				dir = expr();
				
				/**
				 * Verifica a compatibilidade dos tipos da atribuição.
				 */
				if ( dir.getType() == Type.voidType ) {
					signalError.show("Cant assignment a void call to a variable");
				}
				if ( !canConvertType(esq.getType(), dir.getType()) ) {
					signalError.show("Uncompatible types in assignment");
				}
				else {
					//TODO CASTING
				}
				
				if ( lexer.token != Symbol.SEMICOLON )
					signalError.show("';' expected", true);
				else
					lexer.nextToken();
				return new AssignmentStatement(esq, dir);
			}
			else {
				/**
				 * "A method that does not return a value can be used as a statement."
				 * Otherwise, "must be called only within an expression".
				 */
				if ( esq.getType() == Type.voidType ) {
					if ( lexer.token != Symbol.SEMICOLON )
						signalError.show("';' expected", true);
					else {
						lexer.nextToken();
						return new MessageSendStatement(esq);
					}
				}
				else {
					signalError.show("Call is an expression, not a statement");
				}
			}
		}
		
		return null;
		
	}

	
	private ExprList realParameters() {
		
		ExprList anExprList = new ExprList();
		String currentTokenValue = lexer.getStringValue();
		// Expects to find leftpar
		if (lexer.token != Symbol.LEFTPAR) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find '(' but got " + currentTokenValue + " instead.");
		}
		lexer.nextToken();
		
		// Initiates the exprList
		if (startExpr(lexer.token)) {
			anExprList = exprList();
		}
		currentTokenValue = lexer.getStringValue();
		// Expects to find rightpar
		if (lexer.token != Symbol.RIGHTPAR) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find ')' but got " + currentTokenValue + " instead.");
		}
		lexer.nextToken();
		// returns the exprList instance
		return anExprList;
		
	}
	
	
	private WhileStatement whileStatement() {
		
		// If a while statement is found, it's added an item to the while stack
		whileStatements.push(new Integer(1));
		lexer.nextToken();
		String currentTokenValue = lexer.getStringValue();
		// Expects to find leftpar
		if (lexer.token != Symbol.LEFTPAR) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find '(' but got " + currentTokenValue + " instead.");
		}
		lexer.nextToken();
		Expr expr = expr();
		if (expr.getType() != Type.booleanType) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected expression to be boolean, but got " + expr.getType().getName() + " instead.");
		}
		currentTokenValue = lexer.getStringValue();
		// Expects to find rightpar
		if (lexer.token != Symbol.RIGHTPAR) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find ')' but got " + currentTokenValue + " instead.");
		}
		
		lexer.nextToken();
		
		Statement statement = statement();
		// After the code, pops the item from the stack
		whileStatements.pop();
		return new WhileStatement(expr, statement);
		
	}

	
	private IfStatement ifStatement() {
	
		lexer.nextToken();
		// Expect left par
		if ( lexer.token != Symbol.LEFTPAR ) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find '(' but got " + lexer.getStringValue() + " instead.");
		}
		
		lexer.nextToken();
		Expr expr = expr();
		// Expect the expression to be a boolean
		if (expr.getType() != Type.booleanType) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected expression to be boolean, but got " + expr.getType().getName() + " instead.");
		}
		// Expect a right par
		if ( lexer.token != Symbol.RIGHTPAR ) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find ')' but got " + lexer.getStringValue() + " instead.");
		}
		
		lexer.nextToken();
		// Create a ifStatement and elseStatement
		Statement statementIf = statement();
		Statement statementElse = null;
		// If there is an else
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			statementElse = statement();
		}
		// Returns the instance of statement
		return new IfStatement(expr, statementIf, statementElse);
		
	}

	
	private ReturnStatement returnStatement() {

		lexer.nextToken();
		// Gets the expression
		Expr returnExpr = expr();
		// Expect to find ;
		if ( lexer.token != Symbol.SEMICOLON ) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find ';' but got " + lexer.getStringValue() + " instead.");
		}
		// The type of the method with return must not be void
		if (currentMethod.getReturnType() == Type.voidType) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Return statement found on void method. Cannot return a void method");
		}
		// Or at least be convertible between one and another
		if (!canConvertType(currentMethod.getReturnType(), returnExpr.getType())) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Method type not convertible to return type." +
					" The method type is " + currentMethod.getReturnType() + " and the return type is " + returnExpr.getType());
		}

		lexer.nextToken();
		// Flags that a return statement was found
		returnStatement = true;
		
		return new ReturnStatement(returnExpr);
		
	}
		
	// Method that verifies if a type can be converted to another type
	private boolean canConvertType(Type type1, Type type2) {
		
		// If both types are the same
		if (type1 == type2) {
			// And are basic types
			if (type1 == Type.intType || type1 == Type.stringType || type1 == Type.booleanType) {
				return true;
			} else {
				// If are classes in hierarchy
				if (isType(type1.getName()) && isType(type2.getName())) {
					return true;
				} else {
					// Gets the supperclass of type2
					KraClass secondClass = symbolTable.getInGlobal(type2.getName());
					secondClass = secondClass.getSuperclass();
					// Runs on all hierarchy looking for a superclass compatible with type1
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
		}
		
		// If the first type is a class and the second type is undefined
		if (isType(type1.getName()) && type2 == Type.undefinedType) {
			return true;
		}
		
		return false;
		
	}

	
	private Statement readStatement() {
		
		ArrayList<Variable> ReadStatement = new ArrayList<Variable>();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) {
			signalError.show("( expected");
		}
		lexer.nextToken();
		while ( true ) {
				
			Symbol classVariable = null;
			if ( lexer.token == Symbol.THIS ) {
				classVariable = Symbol.THIS;
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) {
					signalError.show(". expected");
				}
				lexer.nextToken();
			}
			if ( lexer.token != Symbol.IDENT ) {
				signalError.show(SignalError.ident_expected);
			}

			String name = lexer.getStringValue();
			
			if ( classVariable == Symbol.THIS ) {
				boolean found = currentClass.containsInstanceVariable(name);
				if ( !found ) {
					signalError.show("Class " + currentClass.getName() + " have no instance variable '" + name + "'. Wrong usage of 'this'");
				}
			
				Variable classVar = currentClass.getInstanceVariable(name);
				
				/**
				 * De acordo com a documentação, só é possível ler tipo int e String.
				 * Não é possível utilizar o modelo switch-case em valores do tipo Type.
				 */
				if ( classVar.getType() != Type.intType && classVar.getType() != Type.stringType ) {
					signalError.show("Can only read int and String types");
				}
				
				ReadStatement.add(classVar);
			}
			else {
				Variable local = symbolTable.getInLocal(name);
				if ( local == null ) {
					signalError.show("Variable " + name + " was not declared");
				}
				if ( local.getType() != Type.intType && local.getType() != Type.stringType ) {
					signalError.show("Can only read int and String types");
				}
				
				ReadStatement.add(local);
			}
				
			lexer.nextToken();
			if ( lexer.token == Symbol.COMMA )
				lexer.nextToken();
			else
				break;
		}

		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		
		return new ReadStatement(ReadStatement);
		
	}
	
	

	private Statement writeStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) {
			signalError.show("( expected");
		}
		lexer.nextToken();
		
		ArrayList<Expr> exprList = exprList().getExprList();
		for ( Expr expr: exprList ) {
			if (expr.getType() != Type.intType && expr.getType() != Type.stringType ) {
				signalError.show("Can only write int and String types");
			}
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) {
			signalError.show(") expected");
		}
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON ) {
			signalError.show(SignalError.semicolon_expected);
		}
		lexer.nextToken();
		
		return new WriteStatement(exprList);
	}

	

	private Statement writelnStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) {
			signalError.show("( expected");
		}
		lexer.nextToken();
		ArrayList<Expr> exprList = exprList().getExprList();
		for ( Expr expr: exprList ) {
			if (expr.getType() != Type.intType && expr.getType() != Type.stringType ) {
				signalError.show("Can only write int and String types");
			}
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) {
			signalError.show(") expected");
		}
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON ) {
			signalError.show(SignalError.semicolon_expected);
		}
		lexer.nextToken();
		
		return new WriteLnStatement(exprList);
	}

	

	private BreakStatement breakStatement() {
		
		// whileStatements
		lexer.nextToken();
		
		// If there is no while statements on the stack
		// It means that the break statement is outside a while
		if (whileStatements.empty()) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Break statement called outside while statement.");
		}
		// Expected to have ; after break
		if ( lexer.token != Symbol.SEMICOLON ) {
			signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected to find ';' but got " + lexer.getStringValue() + " instead.");
		}
		lexer.nextToken();
		return new BreakStatement();
		
	}

	

	private NullStatement nullStatement() {
		
		lexer.nextToken();
		return new NullStatement();
		
	}

	

	private ExprList exprList() {
		// ExpressionList ::= Expression { "," Expression }

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
		
	}

	private Expr expr() {
		
		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			lexer.nextToken();
			Expr right = simpleExpr();
			
			/**
			 * Verifica a combinação de tipos para os tipos básicos.
			 */
			switch ( op ) {
			case LE:
			case LT:
			case GE:
			case GT:
				if ( left.getType() != Type.intType || right.getType() != Type.intType ) {
					signalError.show("Invalid operation (>, >=, <, <= are only valid to int variables)");
				}
				break;
			case EQ:
			case NEQ:
				/**
				 * Verifica se não são o tipo básico que suporta os operadores.
				 * Verifica, também, se são convertíveis e/ou diferentes.
				 */
				if ( left.getType() != Type.stringType && right.getType() != Type.stringType ) {
					if ( !canConvertType(left.getType(), right.getType()) && !canConvertType(right.getType(), left.getType()) ) {
						signalError.show("Invalid operation (incompatible types in comparison)");
					}
				}
				else {
					if(	left.getType() != Type.undefinedType && right.getType() != Type.undefinedType && left.getType() != right.getType() ) {
						signalError.show("Invalid operation (incompatible types in comparison)");
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

	private Expr simpleExpr() {
		
		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS || op == Symbol.OR) {
			lexer.nextToken();
			Expr right = term();
			
			/**
			 * Verifica a combinação de tipos para os tipos básicos.
			 */
			switch ( op ) {
			case MINUS:
			case PLUS:
				if ( left.getType() != Type.intType || right.getType() != Type.intType ) {
					signalError.show("Invalid operation (+ and - accept only int variables)");
				}
				break;
			case OR:
				if ( left.getType() != Type.booleanType || right.getType() != Type.booleanType ) {
					signalError.show("Invalid operation (|| accept only boolean variables)");
				}
				break;
			default:
				// não deverá chegar a esse caso
			}
			
			left = new CompositeExpr(left, op, right);
		}
		
		return left;
		
	}

	private Expr term() {
		
		Symbol op;

		Expr leftExpr = signalFactor();
		// If there is a div, mult or add operator
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT || op == Symbol.AND) {
			lexer.nextToken();
			Expr rightExpr = signalFactor();
			// If the operator is arithmetic (div or mult)
			if (op == Symbol.DIV || op == Symbol.MULT) {
				// The type of both expressions must be int
				if (leftExpr.getType() != Type.intType || rightExpr.getType() != Type.intType) {
					signalError.show("LINE:" + lexer.getCurrentLine() +
							"=> Exprected int type, but got " + leftExpr.getType() + " and " + rightExpr.getType() + " instead." +
							" Mult(*) and Div(/) operations only accept variables of type int");
				}
			} else {
				// If the operator is not div or mult, it is add
				// It only accepts boolean types
				if (leftExpr.getType() != Type.booleanType || rightExpr.getType() != Type.booleanType) {
					signalError.show("LINE:" + lexer.getCurrentLine() +
						"=> Exprected boolean type, but got " + leftExpr.getType() + " and " + rightExpr.getType() + " instead." +
						" And(&&) operation only accept variables of type boolean");
				}
			}
			// Composes continuously the expr on the left
			leftExpr = new CompositeExpr(leftExpr, op, rightExpr);
		}
		return leftExpr;
		
	}

	private Expr signalFactor() {
		
		Symbol op;
		// If there is a signal
		if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
			lexer.nextToken();
			Expr expr = factor();
			// Signals only work with int type
			if (expr.getType() != Type.intType) {
				signalError.show("LINE:" + lexer.getCurrentLine() + "=> Expected int type, but got " + expr.getType() + " instead.");
			}
			
			return new SignalExpr(op, expr);
		} else {
			// If there is no signal
			return factor();
		}
		
	}

	
	private Expr factor() {

		/*
		 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
		 *      ObjectCreation | PrimaryExpr
		 * 
		 * BasicValue ::= IntValue | BooleanValue | StringValue 
		 * BooleanValue ::=  "true" | "false" 
		 * ObjectCreation ::= "new" Id "(" ")" 
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
		
		Expr e;
		ExprList exprList;
		String messageName, ident;

		switch (lexer.token) {
			// IntValue
		case LITERALINT:
			return literalInt();
			
			// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
			
			// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
			
			// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
			
			// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			e = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(e);
			
			// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
			
			// "!" Factor
		case NOT:
			lexer.nextToken();
			e = expr();
			return new UnaryExpr(e, Symbol.NOT);
			
			// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show("Identifier expected");

			String className = lexer.getStringValue();
			/*
			 * // encontre a classe className in symbol table KraClass 
			 *      aClass = symbolTable.getInGlobal(className); 
			 *      if ( aClass == null ) ...
			 */

			lexer.nextToken();
			if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
			lexer.nextToken();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
			lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			return null;
			
			/*
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
		case SUPER:
			// "super" "." Id "(" [ ExpressionList ] ")"
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				signalError.show("'.' expected");
			}
			else
				lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show("Identifier expected");
			messageName = lexer.getStringValue();
			/*
			 * para fazer as conferências semânticas, procure por 'messageName'
			 * na superclasse/superclasse da superclasse etc
			 */
			lexer.nextToken();
			exprList = realParameters();
			break;
		
		case IDENT:
			/*
          	 * PrimaryExpr ::=  
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
			 */
			
			Method method = null;

			String firstId = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// Id
				// retorne um objeto da ASA que representa um identificador
				
				boolean found = currentClass.containsStaticPrivateMethod(firstId) || currentClass.containsStaticPublicMethod(firstId);
				if ( found ) {
					signalError.show("Wrong call of static method " + firstId);
				}
				
				found = currentClass.containsPrivateMethod(firstId) || currentClass.containsPublicMethod(firstId);
				if ( found ) {
					signalError.show("Wrong call of method " + firstId + " (need to use 'this'");
				}
				
				if ( symbolTable.getInLocal(firstId) == null && currentClass.containsInstanceVariable(firstId) ) {
					signalError.show("Cant use instance variables without 'this'");
				}
				else {
					if ( symbolTable.getInLocal(firstId) == null ) {
						signalError.show("Variable " + firstId + " was not declared");
					}
				}
				
				return new VariableExpr(symbolTable.getInLocal(firstId));
			}
			else { // Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.show("Identifier expected");
				}
				else {
					// Id "." Id
					if ( symbolTable.getInLocal(firstId) == null ) {
						signalError.show("Variable " + firstId + "was not declared");
					}
					
					if ( !isType(symbolTable.getInLocal(firstId).getType().getName()) ) {
						signalError.show("Variable " + firstId + " was not declared");
					}
					
					ident = lexer.getStringValue();
					lexer.nextToken();
					
					if ( lexer.token == Symbol.DOT ) {
						// Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite variáveis estáticas, é possível
						 * ter esta opçãoo, como
						 *     Clock.currentDay.setDay(12);
						 * Contudo, se variáveis estáticas não estiver nas especificações,
						 * sinalize um erro neste ponto.
						 */
												
						lexer.nextToken();
						if ( lexer.token != Symbol.IDENT ) {
							signalError.show("Identifier expected");
						}
						messageName = lexer.getStringValue();
						
						KraClass classOfIdent = symbolTable.getInGlobal(symbolTable.getInLocal(ident).getType().getName());
						while ( classOfIdent != null ) {
							if ( !(classOfIdent.containsStaticPrivateMethod(messageName) ||
									classOfIdent.containsStaticPublicMethod(messageName) ||
									classOfIdent.containsPrivateMethod(messageName) ||
									classOfIdent.containsPublicMethod(messageName)) ) {
								signalError.show("No method " + messageName + " was found");
							}
							if ( classOfIdent.containsStaticPrivateMethod(messageName) ) {
								method = classOfIdent.getStaticPrivateMethod(messageName); 
								break;
							}
							if ( classOfIdent.containsStaticPublicMethod(messageName) ) {
								method = classOfIdent.getStaticPublicMethod(messageName); 
								break;
							}
							if ( classOfIdent.containsPublicMethod(messageName) ) {
								method = classOfIdent.getPublicMethod(messageName); 
								break;
							}
							if ( classOfIdent.containsPrivateMethod(messageName) ) {
								method = classOfIdent.getPrivateMethod(messageName); 
								break;
							}
							classOfIdent = classOfIdent.getSuperclass();
						}
						lexer.nextToken();
						exprList = this.realParameters();
						
						ParamList currentMethodPL = method.getParamList();
						
						if (exprList.getSize() == currentMethodPL.getSize()) {
							signalError.show("Wrong number of parameters");
						}
						
						// TODO PARAMETERS CHECK
						
						return new MessageSendToVariable(symbolTable.getInLocal(firstId), method, exprList);

					}
					else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
						
						KraClass classOfIdent = symbolTable.getInGlobal(symbolTable.getInLocal(ident).getType().getName());
						
						if ( currentClass.getName() == classOfIdent.getName() ) {
							if ( classOfIdent.containsPrivateMethod(ident) ) {
								method = classOfIdent.getPrivateMethod(ident);
							}
							if ( classOfIdent.containsPublicMethod(ident) ) {
								method = classOfIdent.getPublicMethod(ident);
							}
							if ( method == null ) {
								classOfIdent = classOfIdent.getSuperclass();
								while ( classOfIdent != null ) {
									if ( classOfIdent.containsPublicMethod(ident) ) {
										method = classOfIdent.getPublicMethod(ident);
										break;
									}
									classOfIdent = classOfIdent.getSuperclass();
								}
							}
							if ( method == null ) {
								signalError.show("No method " + ident + " was found");
							}
						}
						else {
							if ( classOfIdent.containsPublicMethod(ident) ) {
								method = classOfIdent.getPublicMethod(ident);
							}
							if ( method == null ) {
								classOfIdent = classOfIdent.getSuperclass();
								while ( classOfIdent != null ) {
									if ( classOfIdent.containsPublicMethod(ident) ) {
										method = classOfIdent.getPublicMethod(ident);
										break;
									}
									classOfIdent = classOfIdent.getSuperclass();
								}
							}
							if ( method == null ) {
								signalError.show("No public method " + ident + " was found for the class " + symbolTable.getInLocal(firstId).getType().getName());
							}
						}
						exprList = this.realParameters();
						
						ParamList currentMethodPL = method.getParamList();
						
						if (exprList.getSize() == currentMethodPL.getSize()) {
							signalError.show("Wrong number of parameters");
						}
						
						// TODO PARAMETERS CHECK
						
						return new MessageSendToVariable(symbolTable.getInLocal(firstId), method, exprList);
					}
					else {
						// retorne o objeto da ASA que representa Id "." Id
						
						KraClass classOfIdent = symbolTable.getInGlobal(symbolTable.getInLocal(firstId).getType().getName());
						
						if ( classOfIdent.containsInstanceVariable(ident) ) {
							return new MessageSendToInstance(symbolTable.getInLocal(firstId), classOfIdent.getInstanceVariable(ident));
						}
						else {
							signalError.show("Class " + classOfIdent.getName() + " have no instance variable " + ident);
						}
					}
				}
			}
			break;
		
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: 
          	 * PrimaryExpr ::= 
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// only 'this'
				// retorne um objeto da ASA que representa 'this'
				// confira se não estamos em um método estático
				return null;
			}
			else {
				lexer.nextToken();
				if ( lexer.token != Symbol.IDENT )
					signalError.show("Identifier expected");
				ident = lexer.getStringValue();
				lexer.nextToken();
				// já analisou "this" "." Id
				if ( lexer.token == Symbol.LEFTPAR ) {
					// "this" "." Id "(" [ ExpressionList ] ")"
					/*
					 * Confira se a classe corrente possui um método cujo nome é
					 * 'ident' e que pode tomar os parâmetros de ExpressionList
					 */
					exprList = this.realParameters();
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					lexer.nextToken();
					if ( lexer.token != Symbol.IDENT )
						signalError.show("Identifier expected");
					lexer.nextToken();
					exprList = this.realParameters();
				}
				else {
					// retorne o objeto da ASA que representa "this" "." Id
					/*
					 * confira se a classe corrente realmente possui uma
					 * variável de instância 'ident'
					 */
					return null;
				}
			}
			break;
			
		default:
			signalError.show("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

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

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private SignalError		signalError;
	
	/**
	 * Vari�veis globais adicionais.
	 * 
	 * @currentClass Permite declara��o/uso de mais de uma classe
	 * @currentMethod Permite declara��o/uso de mais de um m�todo por classe
	 * @returnStatement Indica se um Statement possui retorno
	 * @whileStatements
	 * @isStatic Permite identificar se se trata de um tipo estático
	 */
	private KraClass		currentClass;
	private Method			currentMethod;
	private boolean 		returnStatement;
	private Stack			whileStatements = new Stack();
	private boolean			isStatic;

}
