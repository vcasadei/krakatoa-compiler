# krakatoa-compiler
A Compiler for the Krakatoa Language written in Java

Tarefas Completas: ✔

================================
Fase 2 - Geração de código em C
================================

* AssignStatement ✔ 
* BreakStatement ✔
* CompositeExpr  ✔ * 
* CompositeStatement ✔
* Expr ✔ *
* ExprList ✔ * 
* IfStatement ✔
* InstanceVariable ✔
* InstanceVariableList ✔
* KraClass ✔
* LiteralBoolean ✔ *
* LiteralInt ✔ *
* LiteralString ✔ *
* LocalVariableList ✔
* MessageSend ✔
* MessageSendStatement ✔
* MessageSendToInstance ✔
* MessageSendToSelf ✔
* MessageSendToSuper  ✔
* MessageSendToVariable	 ✔
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
* Variable ✔
* VariableExpr ✔ *
* WhileStatement ✔
* Write ✔
* WriteLn ✔

================================
Dúvidas
================================
  
Dúvida 1:  
O código  
	if ( expr ) statement;  
em Krakatoa, onde o tipo de 'expr' é boolean, deve ser traduzido para   
	if ( (expr) != false ) statement;  
em C.   
E o código   
	if ( ! expr ) statement;  
deve ser traduzido para  
	if ( (expr) == false ) statement;  
  
Dúvida 2:  
Como varrer as superclasses em busca de determinado método, na função  
getPosition()?

================================
Lista de problemas
================================

* OK-CHK07
* OK-GER14 --> Only warning
* OK-GER16 --> Only warning
* OK-GER17
* OK-GER18
* OK-GER19
* OK-GER20
* OK-GER22 --> Only warning
* OK-SEM01 
* OK-SEM05
* OK-SEM09
* OK-SEM10 --> Only warning
* OK-SEM14 --> Only warning
* OK-SEM15 
* OK-SEM17
* OK-SEM18
* OK-SEM19
* OK-SIN07
* OK-SIN14

* 19/71 com erro e warning
* 14/71 com erro
