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
* OK-GER17 -- Static
* OK-GER18 -- Static
* OK-GER19 -- Static
* OK-GER20 -- Static
* OK-GER22 --> Only warning
* OK-SEM01 -- Static
* OK-SEM05 -- This
* OK-SEM09 -- This
* OK-SEM10 --> Only warning
* OK-SEM14 --> Only warning
* OK-SEM15  -- This
* OK-SEM17 -- Static
* OK-SEM18 -- Static
* OK-SEM19 -- Static
* OK-SIN07 -- This
* OK-SIN14 -- This

* 19/71 com erro e warning
* 14/71 com erro
