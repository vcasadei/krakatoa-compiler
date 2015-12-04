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

* OK-CHK04
* OK-CHK06
* OK-CHK07
* OK-GER05
* OK-GER09
* OK-GER10
* OK-GER11
* OK-GER12
* OK-GER13
* OK-GER14
* OK-GER15
* OK-GER16
* OK-GER17
* OK-GER18
* OK-GER19
* OK-GER20
* OK-GER21
* OK-SEM01
* OK-SEM03
* OK-SEM05
* OK-SEM08
* OK-SEM09
* OK-SEM10
* OK-SEM14
* OK-SEM15
* OK-SEM17
* OK-SEM18
* OK-SEM19
* OK-SEM20
* OK-SIN02
* OK-SIN05
* OK-SIN07
* OK-SIN08
* OK-SIN09
* OK-SIN10
* OK-SIN14
* OK-SIN15
* 38/71 arquivo(s) com erros.
