/*****************************************************************************
 *                     Laboratório de Compiladores                           *
 *																			 *
 * Autor: Alexandre Braga Saldanha											 *
 * R.A.: 408484                                                              *
 ****************************************************************************/

package lexer;

import java.util.*;

import comp.*;

public class Lexer {

    public Lexer( char []input, CompilerError error ) {
        this.input = input;
          // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
          // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        lastTokenPos = 0;
        beforeLastTokenPos = 0;
        this.error = error;
    }


    private static final int MaxValueInteger = 32767;
      // contains the keywords
    static private Hashtable<String, Symbol> keywordsTable;

     // this code will be executed only once for each program execution
	static {
		keywordsTable = new Hashtable<String, Symbol>();

		keywordsTable.put( "true", Symbol.TRUE );
		keywordsTable.put( "false", Symbol.FALSE );
		keywordsTable.put( "final", Symbol.FINAL );
		keywordsTable.put( "void", Symbol.VOID );
		keywordsTable.put( "null", Symbol.NULL );
		keywordsTable.put( "if", Symbol.IF );
		keywordsTable.put( "else", Symbol.ELSE );
		keywordsTable.put( "while", Symbol.WHILE );
		keywordsTable.put( "read", Symbol.READ );
		keywordsTable.put( "write", Symbol.WRITE );
		keywordsTable.put( "writeln", Symbol.WRITELN );

		keywordsTable.put( "break", Symbol.BREAK );
		keywordsTable.put( "int", Symbol.INT );
		keywordsTable.put( "boolean", Symbol.BOOLEAN );
		keywordsTable.put( "return", Symbol.RETURN );
		keywordsTable.put( "class", Symbol.CLASS );
		keywordsTable.put( "super", Symbol.SUPER );
		keywordsTable.put( "this", Symbol.THIS );
		keywordsTable.put( "new", Symbol.NEW );
		keywordsTable.put( "public", Symbol.PUBLIC );
		keywordsTable.put( "private", Symbol.PRIVATE );
		keywordsTable.put( "static", Symbol.STATIC);
		keywordsTable.put( "String", Symbol.STRING );
		keywordsTable.put( "extends", Symbol.EXTENDS );

	}




    public void nextToken() {
        char ch;

        lastTokenPos = tokenPos;
        while (  (ch = input[tokenPos]) == ' ' || ch == '\r' ||
                 ch == '\t' || ch == '\n')  {
            // count the number of lines
          if ( ch == '\n')
            lineNumber++;
          tokenPos++;
          }
        if ( ch == '\0')
          token = Symbol.EOF;
        else
          if ( input[tokenPos] == '/' && input[tokenPos + 1] == '/' ) {
                // comment found
               while ( input[tokenPos] != '\0'&& input[tokenPos] != '\n' )
                 tokenPos++;
               nextToken();
          }
          else if ( input[tokenPos] == '/' && input[tokenPos + 1] == '*' ) {
             int posStartComment = tokenPos;
             int lineNumberStartComment = lineNumber;
             tokenPos += 2;
             while ( (ch = input[tokenPos]) != '\0' &&
                 (ch != '*' || input[tokenPos + 1] != '/') ) {
                if ( ch == '\n' )
                   lineNumber++;
                tokenPos++;
             }
             if ( ch == '\0' )
                error.show( "Comment opened and not closed",
                      getLine(posStartComment), lineNumberStartComment);
             else
                tokenPos += 2;
             nextToken();
          }
          else {
            if ( Character.isLetter( ch ) ) {
                // get an identifier or keyword
                StringBuffer ident = new StringBuffer();
                while ( Character.isLetter( ch = input[tokenPos] ) ||
                        Character.isDigit(ch) ||
                        ch == '_' ) {
                    ident.append(input[tokenPos]);
                    tokenPos++;
                }
                stringValue = ident.toString();
                  // if identStr is in the list of keywords, it is a keyword !
                Symbol value = keywordsTable.get(stringValue);
                if ( value == null )
                  token = Symbol.IDENT;
                else
                  token = value;
            }
            else if ( Character.isDigit( ch ) ) {
                // get a number
                StringBuffer number = new StringBuffer();
                while ( Character.isDigit( input[tokenPos] ) ) {
                    number.append(input[tokenPos]);
                    tokenPos++;
                }
                token = Symbol.NUMBER;
                try {
                   numberValue = Integer.valueOf(number.toString()).intValue();
                } catch ( NumberFormatException e ) {
                   error.show("Number out of limits");
                }
                if ( numberValue > MaxValueInteger )
                   error.show("Number out of limits");
            }
            else {
                tokenPos++;
                switch ( ch ) {
                    case '+' :
                      token = Symbol.PLUS;
                      break;
                    case '-' :
                      token = Symbol.MINUS;
                      break;
                    case '*' :
                      token = Symbol.MULT;
                      break;
                    case '/' :
                      token = Symbol.DIV;
                      break;
                    case '<' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Symbol.LE;
                      }
                      else
                        token = Symbol.LT;
                      break;
                    case '>' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Symbol.GE;
                      }
                      else
                        token = Symbol.GT;
                      break;
                    case '=' :
                      if ( input[tokenPos] == '=' ) {
                        tokenPos++;
                        token = Symbol.EQ;
                      }
                      else
                        token = Symbol.ASSIGN;
                      break;
                    case '!' :
                      if ( input[tokenPos] == '=' ) {
                         tokenPos++;
                         token = Symbol.NEQ;
                      }
                      else
                         token = Symbol.NOT;
                      break;
                    case '(' :
                      token = Symbol.LEFTPAR;
                      break;
                    case ')' :
                      token = Symbol.RIGHTPAR;
                      break;
                    case ',' :
                      token = Symbol.COMMA;
                      break;
                    case ';' :
                      token = Symbol.SEMICOLON;
                      break;
                    case '.' :
                      token = Symbol.DOT;
                      break;
                    case '&' :
                      if ( input[tokenPos] == '&' ) {
                         tokenPos++;
                         token = Symbol.AND;
                      }
                      else
                        error.show("& expected");
                      break;
                    case '|' :
                      if ( input[tokenPos] == '|' ) {
                         tokenPos++;
                         token = Symbol.OR;
                      }
                      else
                        error.show("| expected");
                      break;
                    case '{' :
                      token = Symbol.LEFTCURBRACKET;
                      break;
                    case '}' :
                      token = Symbol.RIGHTCURBRACKET;
                      break;
                    case '_' :
                      error.show("_ cannot start an indentifier");
                      break;
                    case '"' :
                       StringBuffer s = new StringBuffer();
                       while ( input[tokenPos] != '\0' && input[tokenPos] != '\n' )
                          if ( input[tokenPos] == '"' )
                             break;
                          else
                             if ( input[tokenPos] == '\\' ) {
                                if ( input[tokenPos+1] != '\n' && input[tokenPos+1] != '\0' ) {
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                }
                                else {
                                   s.append(input[tokenPos]);
                                   tokenPos++;
                                }
                             }
                             else {
                                s.append(input[tokenPos]);
                                tokenPos++;
                             }

                       if ( input[tokenPos] == '\0' || input[tokenPos] == '\n' ) {
                          error.show("Nonterminated string");
                          literalStringValue = "";
                       }
                       else {
                          tokenPos++;
                          literalStringValue = s.toString();
                       }
                       token = Symbol.LITERALSTRING;
                       break;
                    default :
                      error.show("Invalid Character: '" + ch + "'", false);
                }
            }
          }
        beforeLastTokenPos = lastTokenPos;
    }

      // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineNumberBeforeLastToken() {
        return getLineNumber( lastTokenPos );
    }

    private int getLineNumber( int index ) {
        // return the line number in which the character input[index] is
        int i, n, size;
        n = 1;
        i = 0;
        size = input.length;
        while ( i < size && i < index ) {
          if ( input[i] == '\n' )
            n++;
          i++;
        }
        return n;
    }


    public String getCurrentLine() {
        //return getLine(lastTokenPos);
        return getLine(tokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }

    private String getLine( int index ) {
        // get the line that contains input[index]. Assume input[index] is at a token, not
        // a white space or newline

        int i;
        if ( input.length <= 1 )
           return "";
        i = index;
        if ( i <= 0 )
          i = 1;
        else
          if ( i >= input.length )
            i = input.length;

        while ( input[i] == '\n' || input[i] == '\r' )
           i--;

        StringBuffer line = new StringBuffer();
          // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
          i--;
        if ( input[i] == '\n' )
          i++;
          // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
       return stringValue;
    }

    public int getNumberValue() {
       return numberValue;
    }

    public String getLiteralStringValue() {
       return literalStringValue;
    }
    
    public Symbol lookAhead() {
    	int tokenBefore = this.tokenPos;
    	Symbol before = this.token;
    	
    	this.nextToken();
    	
    	Symbol next = this.token;
    	
    	tokenPos = tokenBefore;
    	this.token = before;
    	
    	return next;
    }

          // current token
    public Symbol token;
    private String stringValue, literalStringValue;
    private int numberValue;

    private int  tokenPos;
      //  input[lastTokenPos] is the last character of the last token found
    private int lastTokenPos;
      //  input[beforeLastTokenPos] is the last character of the token before the last
      // token found
    private int beforeLastTokenPos;

    private char []input;

    // number of current line. Starts with 1
    private int lineNumber;

    private CompilerError error;
}
