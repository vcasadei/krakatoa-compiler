# krakatoa-compiler
A Compiler for the Krakatoa Language written in Java

Tarefas Completas: ✔

Métodos:
================

*Maurízio*
* Program ✔
* ClassDec ✔ [exceto final]
* InstaceVarDec ✔
* MethodDed ✔
* Statement ✔ [exceto number]
* CompositeStatment ✔
* StatementList ✔
* AssignExprLocalDec
* ReadStament 
* Write/WritelnStatment ✔
* ExprList ✔
* Expr 
* SimpleExpr ✔
* Factor
    * Ident
    * this

*Vitor*
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
    * new
    * super

Classes:
================

*Maurízio*
* Assign
* CompositeExpression
* CompositeStatment ✔ [exceto genKra]
* Expr ✔
* ExprList ✔ [com genKra]
* InstanceVar ✔ [exceto genKra]
* InstanceVarList ✔ [exceto genKra]
* KraClass ✔ [exceto genKra]
* MethodList ✔ [exceto genKra]
* Method ✔ [exceto genKra]
* Read
* Statement ✔
* StatementList ✔ [exceto genKra]
* UnaryExpr ✔ [exceto genKra]
* Variable ✔
* VariableExpr ✔ [exceto genKra]
* Write/Writeln
* MessageSendToClass
* MessageSend ✔
* MessageSendStatement
* MessageSendToInstance
* MessageSendToSuper
* MessageSendToVariable

*Vitor*
* BreakStatement ✔ [exceto genKra]
* IfStatement ✔ [exceto genKra]
* LiteralInt
* LiteralString
* LiteralBoolean
* LocalVarList ✔ [exceto genKra]
* NullStatement ✔ [com genKra]
* NullExpr ✔ [com genKra]
* ObjectCreation
* Parameter
* ParamList ✔ [exceto genkra]
* ParenthesisExpr
* Program
* ReturnStatement ✔ [com genKra]
* SignalExpr ✔ [com genKra]
* Types ✔
* WhileStatement ✔ [exceto genKra]
* MessageSendToSelf
