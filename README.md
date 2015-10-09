# krakatoa-compiler
A Compiler for the Krakatoa Language written in Java

Tarefas Completas: ✔

Métodos:
================

*Maurízio*
* Program ✔ [exceto final/static]
* ClassDec ✔ [exceto final/static]
* InstaceVarDec ✔ [exceto final/static]
* MethodDed ✔ [exceto final/static]
* Statement
* CompositeStatment ✔
* StatementList
* AssignExprLocalDec
* ReadStament
* Write/WritelnStatment
* ExprList
* Expr
* SimpleExpr
* Factor
    * Ident
    * this

*Vitor*
* LocalDec ✔
* FormalParamDec ✔
* ParamDec ✔
* type ✔
* WhileStatement ✔
* RealParameters ✔
* IfStatement
* ReturnStatement
* BreakStatement ✔
* NullStatement
* Term
* SignalFactor
* Factor
    * new
    * super

Classes:
================

*Maurízio*
* Assign
* CompositeExpression
* CompositeStatment ✔ [exceto genKra()]
* Expr
* ExprList
* InstanceVar ✔ [exceto genKra()]
* InstanceVarList ✔ [exceto genKra()]
* KraClass ✔ [exceto genKra()]
* MethodList ✔ [exceto genKra()]
* Method ✔ [exceto genKra()]
* Read
* Statement ✔
* StatementList ✔ [exceto genKra()]
* UnaryExpr ✔ [exceto genKra()]
* Variable ✔
* VariableExpr ✔ [exceto genKra()]
* Write/Writeln
* MessageSendToClass
* MessageSend ✔
* MessageSendStatement
* MessageSendToInstance
* MessageSendToSuper
* MessageSendToVariable

*Vitor*
* BreakStatement ✔ [exceto genKra]
* IfStatement
* LiteralInt
* LiteralString
* LiteralBoolean
* LocalVarList ✔ [exceto genKra]
* NullStatement
* NullExpr
* ObjectCreation
* Parameter
* ParamList ✔ [exceto genkra]
* ParenthesisExpr
* Program
* ReturnStatement
* SignalExpr
* Types
* WhileStatement ✔ [exceto genKra]
* MessageSendToSelf
