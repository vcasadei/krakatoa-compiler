# krakatoa-compiler
A Compiler for the Krakatoa Language written in Java

Tarefas Completas: ✔

Métodos:
================
**Maurízio**
* Program ✔
* ClassDec ✔ [exceto final]
* InstaceVarDec ✔
* MethodDed ✔
* Statement ✔ [exceto number]
* CompositeStatment ✔
* StatementList ✔
* AssignExprLocalDec ✔ [exceto number e casting]
* ReadStament  ✔
* Write/WritelnStatment ✔
* ExprList ✔
* Expr ✔
* SimpleExpr ✔
* Factor
    * Ident ✔ [exceto checagem de parâmetros]
    * this

**Vitor**
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
* Factor ✔
    * new ✔
    * super ✔

Classes:
================

**Maurízio**
* Assign
* CompositeExpression ✔ [exceto genKra]
* CompositeStatment ✔ [exceto genKra]
* Expr ✔
* ExprList ✔
* InstanceVar ✔ [exceto genKra]
* InstanceVarList ✔ [exceto genKra]
* KraClass ✔ [exceto genKra]
* MethodList ✔ [exceto genKra]
* Method ✔ [exceto genKra]
* Read ✔
* Statement ✔
* StatementList ✔ [exceto genKra]
* UnaryExpr ✔ [exceto genKra]
* Variable ✔
* VariableExpr ✔ [exceto genKra]
* Write/Writeln ✔ [exceto genKra]
* MessageSendToClass
* MessageSend ✔
* MessageSendStatement
* MessageSendToInstance
* MessageSendToSuper ✔ [Verifica o genKra por favor. Não tenho certeza se está certo]
* MessageSendToVariable

**Vitor**

*Including genKra*
* BreakStatement ✔
* IfStatement ✔
* LiteralInt ✔
* LiteralString ✔
* LiteralBoolean ✔
* LocalVarList ✔ [Verifica o genKra por favor. Não tenho certeza se está certo]
* NullStatement ✔
* NullExpr ✔
* ObjectBuilder
* Parameter ✔
* ParamList ✔
* ParenthesisExpr ✔
* Program ✔ [Verifica o genKra por favor. Não tenho certeza se está certo]
* ReturnStatement ✔
* SignalExpr ✔
* Types ✔
* WhileStatement ✔
* MessageSendToSelf ✔ [Verifica o genKra por favor. Não tenho certeza se está certo]
