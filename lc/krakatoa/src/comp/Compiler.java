
package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

	/**
	 * As funções compile() e program(), ambas do tipo Program, não serão
	 * alteradas, visto que já se encontram corretamente implementadas.
	 * De forma similar, não será tocada a função metaobjectCall().
	 */
	
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
			classDec();
			while ( lexer.token == Symbol.CLASS )
				classDec();
			if ( lexer.token != Symbol.EOF ) {
				signalError.show("End of file expected");
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

	/**
	 * A seguir encontram-se as funções que receberão ajustes de acordo com
	 * a gramática disponível em -- lc/texts/The\ Krakatoa\ Language.pdf --
	 */
	
	/** classDec()				- DEVE SER ALTERADA */	
	private void classDec() {
		// Note que os métodos desta classe não correspondem exatamente às
		// regras da gramática. Este método classDec, por exemplo, implementa
		// a produção KraClass (veja abaixo) e partes de outras produções.

		/*
		 * KraClass		::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
		 * MemberList	::= { Qualifier Member } 
		 * Member		::= InstVarDec | MethodDec
		 * InstVarDec	::= Type IdList ";" 
		 * MethodDec	::= Qualifier Type Id "("[ FormalParamDec ] ")"
		 * 						"{" StatementList "}" 
		 * Qualifier	::= [ "static" ]  ( "private" | "public" )
		 */
		
		/**
		 * ClassDec		::=	"class" Id [ "extends" Id ] "{" MemberList "}"
		 * MemberList	::=	{ Qualifier Member }
		 * Qualifier	::=	[ "final" ] [ "static" ] ( "private" | "public" )
		 * Member		::= InstVarDec | MethodDec
		 */
		
		if ( lexer.token != Symbol.CLASS ) signalError.show("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.IDENT )
			signalError.show(SignalError.ident_expected);
		String className = lexer.getStringValue();
		symbolTable.putInGlobal(className, new KraClass(className));
		lexer.nextToken();
		
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show(SignalError.ident_expected);
			
			String superclassName = lexer.getStringValue();
			
			/** NÃO VERIFICA SE EXISTE NA SYMBOLTABLE? */ 
			
			lexer.nextToken();
		}
		
		if ( lexer.token != Symbol.LEFTCURBRACKET )
			signalError.show("{ expected", true);
		lexer.nextToken();

		/** 
		 * E O TRATAMENTO PARA "FINAL" E "STATIC" ??
		 */
		
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
				signalError.show("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}
			
			Type t = type();
			if ( lexer.token != Symbol.IDENT )
				signalError.show("Identifier expected");
			
			String name = lexer.getStringValue();
			
			/** NÃO INSERE NA SYMBOLTABLE? */
			/** NÃO VERIFICA SE EXISTE NA SYMBOLTABLE? */ 
			
			lexer.nextToken();
			
			if ( lexer.token == Symbol.LEFTPAR )
				methodDec(t, name, qualifier);
			
			/**
			 * ONDE ESTÁ DIZENDO QUE NÃO PODE SER PÚBLICO?
			 */
			
			else if ( qualifier != Symbol.PRIVATE )
				signalError.show("Attempt to declare a public instance variable");
			else
				instanceVarDec(t, name);
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.show("public/private or \"}\" expected");
		lexer.nextToken();

	}

	/** instanceVarDec()		- DEVE SER ALTERADA */	
	private void instanceVarDec(Type type, String name) {
		
		// InstVarDec 	::= [ "static" ] "private" Type IdList ";"
		
		/**
		 * InstVarDec	::= Type IdList ";"
		 * Type			::= BasicType | Id
		 * BasicType	::= "void" | "int" | "boolean" | "String"
		 * Id			::= Letter { Letter | Digit | "_" }
		 * IdList		::= Id { "," Id } 
		 */

		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show("Identifier expected");
			String variableName = lexer.getStringValue();
			
			/** NÃO INSERE NA SYMBOLTABLE? */
			/** NÃO VERIFICA SE EXISTE NA SYMBOLTABLE? */ 
			
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
	}
	
	/** methodDec()				- OK */
	private void methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec		::=	Qualifier Return Id "("[ FormalParamDec ] ")"
		 * 							"{" StatementList "}"
		 */
		
		/**
		 * MethodDec		::=	Type Id "(" [ FormalParamDec ] ")" "{"
		 * 							StatementList "}"
		 * Type				::= BasicType | Id
		 * BasicType		::= "void" | "int" | "boolean" | "String"
		 * Id				::= Letter { Letter | Digit | "_" }
		 * FormalParamDec	::= ParamDec { "," ParamDec }
		 * ParamDec			::= Type Id
		 */

		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ) formalParamDec();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.show("{ expected");

		lexer.nextToken();
		statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.show("} expected");

		lexer.nextToken();

	}

	/** localDec()				- DEVE SER ALTERADA */
	private void localDec() {
		// LocalDec 	::= Type IdList ";"
		
		/**
		 * LocalDec		::= Type IdList ";"
		 */

		Type type = type();
		if ( lexer.token != Symbol.IDENT ) signalError.show("Identifier expected");
		Variable v = new Variable(lexer.getStringValue(), type);
		
		/** NÃO INSERE EM SYMBOLTABLE? */
		/** NÃO VERIFICA SE JÁ EXISTE? */
		
		lexer.nextToken();
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show("Identifier expected");
			v = new Variable(lexer.getStringValue(), type);
			
			/** NÃO INSERE EM SYMBOLTABLE? */
			/** NÃO VERIFICA SE JÁ EXISTE? */
			
			lexer.nextToken();
		}
	}

	/** formalParamDec()		- OK */
	private void formalParamDec() {
		// FormalParamDec 	::= ParamDec { "," ParamDec }
		
		/**
		 * FormalParamDec	::=	ParamDec { "," ParamDec }
		 */

		paramDec();
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			paramDec();
		}
	}

	/** paramDec()				- OK */
	private void paramDec() {
		// ParamDec		::= Type Id
		
		/**
		 * ParamDec		::= Type Id
		 */

		type();
		if ( lexer.token != Symbol.IDENT ) signalError.show("Identifier expected");
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
			
			// # corrija: faça uma busca na TS para buscar a classe
			// IDENT deve ser uma classe.
			
			/** DEVEMOS USAR A FUNÇÃO isType() DISPONÍVEL MAIS ABAIXO? */
			
			result = null;
			break;
		default:
			signalError.show("Type expected");
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

	/** localDec()				- OK */
	private void compositeStatement() {

		/**
		 * CompStatement	::= "{" { Statement } "}"
		 */
		
		lexer.nextToken();
		statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.show("} expected");
		else
			lexer.nextToken();
	}

	/** statementList()			- DEVE SER ALTERADA */
	private void statementList() {
		// CompStatement ::= "{" { Statement } "}"
		
		/**
		 * QUAL REGRA DEVERÁ SER USADA?
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
		
		/** RETORNARÁ NULO MESMO ? */
		
		return null;
	}

	/** realParameters()		- OK (MESMO EFEITO DA REGRA ExpressionList) */
	private ExprList realParameters() {
		ExprList anExprList = null;

		if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
		lexer.nextToken();
		if ( startExpr(lexer.token) ) anExprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		lexer.nextToken();
		return anExprList;
	}
	
	/** whileStatement()		- OK */
	private void whileStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
		lexer.nextToken();
		expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
		lexer.nextToken();
		statement();
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
			
			/** NÃO PRECISA VERIFICAR SE JÁ EXISTE NA SYMBOLTABLE? */
			
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
	private void breakStatement() {
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
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

}
