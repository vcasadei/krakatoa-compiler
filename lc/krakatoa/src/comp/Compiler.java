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

	// TODO FINAL/STATIC SUPPORT
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
		
		if ( lexer.token != Symbol.CLASS ) signalError.show("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.IDENT ) signalError.show(SignalError.ident_expected);
		String className = lexer.getStringValue();
			
		KraClass alreadyDeclared = symbolTable.getInGlobal(className);
		if (alreadyDeclared != null) signalError.show("Class " + className + " already declared");
		currentClass = new KraClass(className);
		symbolTable.putInGlobal(className, currentClass);
		lexer.nextToken();
		
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT ) signalError.show(SignalError.ident_expected);
			String superclassName = lexer.getStringValue();
			
			if (className.equals(superclassName)) signalError.show("Can't inheritance from itself");
			alreadyDeclared = symbolTable.getInGlobal(superclassName);
			if (alreadyDeclared == null) signalError.show("Class " + superclassName + " does not exist");
			currentClass.setSuperclass(new KraClass(superclassName));
			
			lexer.nextToken();
		}
		
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.show("{ expected", true);
		lexer.nextToken();

		while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {
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
				signalError.show("private or public expected");
				qualifier = Symbol.PUBLIC;
			}
			
			Type t = type();
			
			if ( lexer.token != Symbol.IDENT ) signalError.show("Identifier expected");
			String name = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token == Symbol.LEFTPAR ) methodDec(t, name, qualifier);
			else {
				if ( qualifier != Symbol.PRIVATE ) signalError.show("Attempt to declare a public instance variable");
				else instanceVarDec(t, name);
			}
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.show("public/private or \"}\" expected");
		lexer.nextToken();

		/**
		 * "Every program must have a class named Program with a parameterless method
		 * called run. To start the execution of a program, the runtime system creates
		 * an object of class Program and sends to it a message run."
		 */
		if ( currentClass.getName().equals("Program") ) {
			boolean run = currentClass.containsPublicMethod("run");
			if ( !run ) signalError.show("Class Program without a run() method");
		}
		
		return currentClass;
	}
	
	// TODO FINAL/STATIC SUPPORT
	private void instanceVarDec(Type type, String name) {
		// InstVarDec 	::= [ "static" ] "private" Type IdList ";"

		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT ) signalError.show("Identifier expected");
			String variableName = lexer.getStringValue();
			
			boolean found;
			found = currentClass.containsInstanceVariable(variableName); 
			if (found) signalError.show("Static variable " + name + " already declared");
			found = currentClass.containsPrivateMethod(variableName) || currentClass.containsPublicMethod(variableName);
			if (found) signalError.show((name + " is a method and cannot be declared"));
			InstanceVariable instanceVariable = new InstanceVariable(name, type);
			currentClass.addInstanceVariable(instanceVariable);
			
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.SEMICOLON ) signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
	}
	
	// TODO FINAL/STATIC SUPPORT
	private void methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec		::=	Qualifier Return Id "("[ FormalParamDec ] ")"
		 * 							"{" StatementList "}"
		 */

		boolean found;
		found = currentClass.containsPrivateMethod(name) || currentClass.containsPublicMethod(name);
		if (found) signalError.equals("Method " + name + " already declared");
		found = currentClass.containsInstanceVariable(name);
		if (found) signalError.show((name + " is an instance variable and cannot be declared"));
		
		currentMethod = new Method(name, type);
		
		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ) formalParamDec();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		
		String className = currentClass.getName();
		String methodName = currentMethod.getName();
		
		if ( qualifier == Symbol.PUBLIC ) {
			currentClass.addPublicMethod(currentMethod);
			if ( className.equals("Program") && methodName.equals("run") ) {
				if ( currentMethod.getParamList().getSize() > 0 ) {
					signalError.show("Method 'run' must be parameterless");
				}
				else {
					if ( currentMethod.getReturnType() != Type.voidType ) {
						signalError.show("Method 'run' must return 'void'");
					}
				}
			}
		}
		else {
			currentClass.addPrivateMethod(currentMethod);		
			if ( className.equals("Program") && methodName.equals("run") ) {
				signalError.show("Method 'run' must be public");
			}
		}
		
		Method superclassMethod = null;
		
		KraClass superclass = currentClass.getSuperclass();
		while ( superclass != null ) {
			boolean exist = superclass.containsPublicMethod(name);
			if ( exist ) {
				superclassMethod = superclass.getPublicMethod(name);
				break;
			}
			else superclass = superclass.getSuperclass();
		}
		
		if ( superclassMethod != null ) {
			if ( type != superclassMethod.getReturnType() ) signalError.show("Wrong return type");
			if ( superclassMethod.getParamList().getSize() != currentMethod.getParamList().getSize() ) {
				signalError.show("Wrong number of parameters");
			}
			
			ParamList superMethodPL = superclassMethod.getParamList();
			ParamList currentMethodPL = currentMethod.getParamList();
			
			int i = 0;
			for (Variable param : superMethodPL.getList()) {
				if ( param.getType() != currentMethodPL.getList().get(i).getType() ) {
					signalError.show("Wrong parameters");
				}
				i++;
			}
		}

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.show("{ expected");

		lexer.nextToken();
		
		returnStatement = false;
		
		currentMethod.setStatementList(statementList());
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.show("} expected");
		
		if ( type == Type.voidType ) {
			if ( returnStatement ) {
				signalError.show("Method " + name + " should not have a return");
			}
		}
		
		symbolTable.removeLocalIdent();

		lexer.nextToken();

	}

	/** localDec()				- DEVE SER ALTERADA */
	private void localDec() {
		// LocalDec 	::= Type IdList ";"
		
		/**
		 * LocalDec		::= Type IdList ";"
		 */

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

	/** formalParamDec()		- OK */
	private void formalParamDec() {
		// FormalParamDec 	::= ParamDec { "," ParamDec }
		
		/**
		 * FormalParamDec	::=	ParamDec { "," ParamDec }
		 */

		paramDec();
		// If there is more than one parameter
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			paramDec();
		}
	}

	/** paramDec()				- OK */
	private void paramDec() {
		/**
		 * ParamDec		::= Type Id
		 */

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

	/** type()					- DEVE SER ALTERADA */	
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

		/**
		 * CompStatement	::= "{" { Statement } "}"
		 */
		
		lexer.nextToken();
		StatementList statementList = statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.show("} expected");
		else lexer.nextToken();
		
		return new CompositeStatement(statementList);
	}

	/** statementList()			- DEVE SER ALTERADA */
	private StatementList statementList() {
		// CompStatement ::= "{" { Statement } "}"
		
		/**
		 * QUAL REGRA DEVER� SER USADA?
		 */
		
		/**
		 * StatementList	::= { Statement }
		 * CompStatement	::= "{" { Statement } "}"
		 */
		
		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
				&& tk != Symbol.ELSE)
			statement();
		
		return null;
	}

	/** statement()				- OK */
	private void statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 *                ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 *               ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */
		
		/**
		 * Statement	::=	AssignExprLocalDec ";" | IfStat | WhileStat |
		 * 						ReturnStat  ";" | ReadStat ";" | WriteStat ";" |
		 * 						"break" ";" | ";" | CompStatement 
		 */

		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:
			assignExprLocalDec();
			break;
		case RETURN:
			returnStatement();
			break;
		case READ:
			readStatement();
			break;
		case WRITE:
			writeStatement();
			break;
		case WRITELN:
			writelnStatement();
			break;
		case IF:
			ifStatement();
			break;
		case BREAK:
			breakStatement();
			break;
		case WHILE:
			whileStatement();
			break;
		case SEMICOLON:
			nullStatement();
			break;
		case LEFTCURBRACKET:
			compositeStatement();
			break;
		default:
			signalError.show("Statement expected");
		}
	}
	
	/** isType()				- OK */
	/*
	 * retorne true se 'name' é uma classe declarada anteriormente. É necessário
	 * fazer uma busca na tabela de símbolos para isto.
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}
	
	// Verifies if some object is an instance of a class, returns true if it is
	private boolean isClass(Object obj) {
		return (obj instanceof KraClass);
	}

	/** assignExprLocalDec()	- OK (VERIFICAR APENAS RETORNO DA FUNÇÃO) */
	private Expr assignExprLocalDec() {
		/*
		 * AssignExprLocalDec 	::= Expression [ ``$=$'' Expression ] | LocalDec
		 */
		
		/**
		 * AssignExprLocalDec	::= Expression [ "=" Expression ] | LocalDec
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
		}
		else {
			
			/*
			 * AssignExprLocalDec 	::= Expression [ ``$=$'' Expression ]
			 */
			
			expr();
			if ( lexer.token == Symbol.ASSIGN ) {
				lexer.nextToken();
				expr();
				if ( lexer.token != Symbol.SEMICOLON )
					signalError.show("';' expected", true);
				else
					lexer.nextToken();
			}
		}
		
		/** RETORNAR� NULO MESMO ? */
		
		return null;
	}

	/** realParameters()		- OK (MESMO EFEITO DA REGRA ExpressionList) */
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
	
	/** whileStatement()		- OK */
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
		
		Statement statement = statement("while");
		// After the code, pops the item from the stack
		whileStatements.pop();
		return new WhileStatement(expr, statement);
	}

	/** ifStatement()			- OK */
	private void ifStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
		lexer.nextToken();
		expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		lexer.nextToken();
		statement();
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			statement();
		}
	}

	/** returnStatement()		- OK */
	private void returnStatement() {

		lexer.nextToken();
		expr();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
	}

	/** readStatement()			- DEVE SER ALTERADA */
	private void readStatement() {
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
		lexer.nextToken();
		while (true) {
			if ( lexer.token == Symbol.THIS ) {
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) signalError.show(". expected");
				lexer.nextToken();
			}
			if ( lexer.token != Symbol.IDENT )
				signalError.show(SignalError.ident_expected);

			String name = lexer.getStringValue();
			
			/** NÃO PRECISA VERIFICAR SE J� EXISTE NA SYMBOLTABLE? */
			
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
	}

	/** writeStatement()		- OK */
	private void writeStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
		lexer.nextToken();
		exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
	}

	/** writelnStatement()		- OK */
	private void writelnStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
		lexer.nextToken();
		exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
	}

	/** breakStatement()		- OK */
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

	/** nullStatement()			- OK */
	private void nullStatement() {
		lexer.nextToken();
	}

	/** exprList()				- OK */
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

	/** expr()					- OK */
	private Expr expr() {

		/**
		 * Expression	::=	SimpleExpression [Relation SimpleExpression]
		 */
		
		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			lexer.nextToken();
			Expr right = simpleExpr();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	/** simpleExpr()			- OK */
	private Expr simpleExpr() {
		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			lexer.nextToken();
			Expr right = term();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	/** term()					- OK */
	private Expr term() {
		Symbol op;

		Expr left = signalFactor();
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
			lexer.nextToken();
			Expr right = signalFactor();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	/** signalFactor()			- OK */
	private Expr signalFactor() {
		Symbol op;
		if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
			lexer.nextToken();
			return new SignalExpr(op, factor());
		}
		else
			return factor();
	}

	/** Factor()				- DEVE SER ALTERADA */
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

			String firstId = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// Id
				// retorne um objeto da ASA que representa um identificador
				return null;
			}
			else { // Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.show("Identifier expected");
				}
				else {
					// Id "." Id
					lexer.nextToken();
					ident = lexer.getStringValue();
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
						if ( lexer.token != Symbol.IDENT )
							signalError.show("Identifier expected");
						messageName = lexer.getStringValue();
						/**
						 * CREIO QUE SE REPETE A INDICAÇÃO ABAIXO:
						 * para fazer as conf. sem., procure por 'messageName'
						 * na superclasse/superclasse da superclasse, etc.
						 */
						lexer.nextToken();
						exprList = this.realParameters();

					}
					else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
						exprList = this.realParameters();
						/*
						 * para fazer as conferências semânticas, procure por
						 * método 'ident' na classe de 'firstId'
						 */
					}
					else {
						// retorne o objeto da ASA que representa Id "." Id
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

	/** literalInt()			- OK */
	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	/** startExpr()				- OK (USADA EM CONJUNTO COM realParameters()) */
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
	 */
	private KraClass		currentClass;
	private Method			currentMethod;
	private boolean 		returnStatement;
	private Stack			whileStatements = new Stack();

}
