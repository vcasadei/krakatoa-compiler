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


