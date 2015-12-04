# krakatoa-compiler
A Compiler for the Krakatoa Language written in Java

Tarefas Completas: ✔

================================
Fase 1
================================

Métodos:
================
** Maurízio **

* Program ✔
* ClassDec ✔
* InstaceVarDec ✔
* MethodDed ✔
* Statement ✔
* CompositeStatment ✔
* StatementList ✔
* AssignExprLocalDec ✔
* ReadStament  ✔
* Write/WritelnStatment ✔
* ExprList ✔
* Expr ✔
* SimpleExpr ✔
* Factor
    * Ident ✔
    * this ✔

** Vitor **

* LocalDec ✔
* FormalParamDec ✔
* ParamDec ✔
* Type ✔
* WhileStatement ✔
* RealParameters ✔
* IfStatement ✔
* ReturnStatement ✔
* BreakStatement ✔
* NullStatement ✔
* Term ✔
* SignalFactor ✔
* Factor
    * new ✔
    * super ✔

Classes:
================

** Maurízio **

* Assign ✔
* CompositeExpression ✔
* CompositeStatment ✔
* Expr ✔
* ExprList ✔
* InstanceVar ✔
* InstanceVarList ✔
* KraClass ✔
* MethodList ✔
* Method ✔
* Read ✔
* Statement ✔
* StatementList ✔
* UnaryExpr ✔
* Variable ✔
* VariableExpr ✔
* Write/Writeln ✔
* MessageSendToClass ✔
* MessageSend ✔
* MessageSendStatement ✔
* MessageSendToInstance ✔
* MessageSendToSuper ✔
* MessageSendToVariable ✔

** Vitor **

* BreakStatement ✔
* IfStatement ✔
* LiteralInt ✔
* LiteralString ✔
* LiteralBoolean ✔
* LocalVarList ✔
* NullStatement ✔
* NullExpr ✔
* ObjectBuilder
* Parameter ✔
* ParamList ✔
* ParenthesisExpr ✔
* Program ✔
* ReturnStatement ✔
* SignalExpr ✔
* Types ✔
* WhileStatement ✔
* MessageSendToSelf ✔

Arquivos gerados com falha:
================
OK-SIN09.KRA2	(erro desconhecido)  
OK-SIN10.KRA2	(ausência de 'this' em ReadStatement)  
OK-SEM17-19.KRA	(arquivos em branco)  
OK-SEM01.KRA	(arquivo em branco)    
OK-GER18-20.KRA	(arquivos em branco)  
OK-CHK06.KRA	(está ausente a classe Program)  

================================
Fase 2 - Geração de código em C
================================

* AssignStatement ✔
* BreakStatement ✔
* CompositeExpr  ✔ *
* CompositeStatement ✔ ??
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
* MessageSend
* MessageSendStatement
* MessageSendToInstance
* MessageSendToSelf
* MessageSendToSuper
* MessageSendToVariable
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

?? Not sure

=====
Dúvidas
=====
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