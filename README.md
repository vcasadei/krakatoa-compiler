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

* * by Zé
* ?? Not sure
* ** To be reviewed

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

* OK-CHK06
* OK-CHK07
* OK-GER14
* OK-GER16
* OK-GER17
* OK-GER18
* OK-GER19
* OK-GER20
* OK-GER21
* OK-GER22
* OK-SEM01 
* OK-SEM05
* OK-SEM09
* OK-SEM10
* OK-SEM14
* OK-SEM15 
* OK-SEM17
* OK-SEM18
* OK-SEM19
* OK-SIN07
* OK-SIN14
* 21/71 erros
