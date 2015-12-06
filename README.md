# krakatoa-compiler
A Compiler for the Krakatoa Language written in Java

Tarefas Completas: ✔

================================
Fase 2 - Geração de código em C
================================

* AssignStatement ✔ 
* BreakStatement ✔
* CompositeExpr  ✔ * 
* CompositeStatement ✔ ?? **
* Expr ✔ *
* ExprList ✔ * 
* IfStatement ✔ **
* InstanceVariable ✔
* InstanceVariableList ✔
* KraClass ✔
* LiteralBoolean ✔ *
* LiteralInt ✔ *
* LiteralString ✔ *
* LocalVariableList ✔
* MessageSend ✔
* MessageSendStatement ✔ **
* MessageSendToInstance ✔ **
* MessageSendToSelf ✔
* MessageSendToSuper  ✔ **
* MessageSendToVariable	 ✔ **
* Method ✔
* MethodList ✔
* NullExpr ✔ *
* NullStatement ✔
* ObjectBuilder ✔
* Parameter ✔
* ParamList ✔
* ParenthesisExpr ✔
* Program ✔
* ReadStatement ✔
* ReturnStatement ✔
* SignalExpr ✔
* Statement ✔ *
* StatementList ✔
* Type ✔ *
* TypeBoolean ✔ *
* TypeInt ✔ *
* TypeString ✔
* TypeUndefined ✔ *
* TypeVoid ✔ *
* UnaryExpr ✔ *
* Variable ✔ **
* VariableExpr ✔ *
* WhileStatement ✔
* Write ✔
* WriteLn ✔

* by Zé
?? Not sure
** To be reviewed

================================
Dúvidas
================================
  
Dúvida 1:
Não sei como fazer isso (abaixo), tem ideia?  
O código  
	if ( expr ) statement;  
em Krakatoa, onde o tipo de expr  é boolean, deve ser traduzido para   
	if ( (expr) != false ) statement;  
em C.   
E o código   
	if ( ! expr ) statement;  
deve ser traduzido para  
	if ( (expr) == false ) statement;  
          
Apostila (Geração de Krakatoa para C) pág 15, no final da página.
  
Dúvida 2:
Precisa varrer as superclasses em busca do método, na função
getPosition(). Como fazer?

================================
Lista de problemas
================================

* OK-CHK04 ReadStatement: v.getType().getCname não retorna classe
* OK-CHK06
* OK-CHK07
* OK-GER05 Warning: the 'gets' function should not be used.
* OK-GER14 Warning: assignment from incompatible pointer type
* OK-GER16 Warning: assignment from incompatible pointer type
* OK-GER17
* OK-GER18
* OK-GER19
* OK-GER20
* OK-GER21
* OK-GER22 Warning: assignment from incompatible pointer type
* OK-SEM01 
* OK-SEM05 Various erros related to pointers
* OK-SEM09 Error: incompatible type for one or more arguments
* OK-SEM10 Warning: assignment from incompatible pointer type
* OK-SEM14 Warning: comparison of distinct pointer types lacks a cast
* OK-SEM15 
* OK-SEM17
* OK-SEM18
* OK-SEM19
* OK-SIN02 Warning: the 'gets' function should not be used
* OK-SIN05 Warning: the 'gets' function should not be used
* OK-SIN07 Various erros related to pointers
* OK-SIN10 ReadStatement: v.getType().getCname não retorna classe
* OK-SIN14
* OK-SIN15 Missing header for string functions?
* 27/71 com erros ou 24/71 se desconsiderar avisos sobre 'gets'
