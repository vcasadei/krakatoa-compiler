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
ER-SEM83.KRA	(não deveria ser gerado)
OK-SIN09.KRA2	(erro desconhecido)  
OK-SIN10.KRA2	(ausência de 'this' em ReadStatement)  
OK-SEM19.KRA	(arquivo não é criado)  
OK-SEM18.KRA	(arquivo em branco, provável exception)  
OK-SEM17.KRA	(arquivo em branco, provável exception)  
OK-SEM01.KRA	(arquivo não é criado)  
OK-CHK06.KRA	(está ausente parte do código, provável exception)  
...  
