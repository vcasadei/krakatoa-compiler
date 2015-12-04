/*
 * Deve-se incluir alguns headers porque algumas fun��es
 * da biblioteca padr�o de C s�o utilizadas na tradu��o.
*/
#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

/* Define o tipo boolean */
typedef int boolean;
#define true 1
#define false 0

/* Define um tipo Func, um ponteiro para fun��o */
typedef
   void (*Func)();

typedef
   struct _St_A {
      Func *vt;
      int _A_n;
   } _class_A;

_class_A *new_A(void);

void _A_set(_class_A *this, int _pn) {
   this->_A_n = _pn;
}

int _A_get(_class_A *this) {
   return this->_A_n;
}

Func VTclass_A[] = {
   (void (*) ()) _A_set,
   (void (*) ()) _A_get
};

_class_A *new_A() {
   _class_A *t;
   if ((t = malloc(sizeof(_class_A))) != NULL) {
      t->vt = VTclass_A;
   }
   return t;
}

typedef
   struct _St_B {
      Func *vt;
      int _A_n;
   } _class_B;

_class_B *new_B(void);

void _B_set(_class_B *this, int _pn) {
   printf("%d", _pn);
   _A_set((_class_A * ) this, _pn)
;
}

Func VTclass_B[] = {
   (void (*) ()) _B_set
};

_class_B *new_B() {
   _class_B *t;
   if ((t = malloc(sizeof(_class_B))) != NULL) {
      t->vt = VTclass_B;
   }
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

B _Program_m(_class_Program *this, A _a) {
   ((void (*) (_class_A *, int)) _a->vt[0]) (_a, 0);
   return new_B();
}

A _Program_p(_class_Program *this, int _i) {
   if ( (_i) > 0 != false ) {
      return new_A();
   } else {
      return new_B();
   }
}

void _Program_run(_class_Program *this) {
   _class_A *_a;
   _class_B *_b;
   _a = new_A();
   _b = new_B();
   _a = _b;
   ((void (*) (_class_A *, int)) _a->vt[0]) (_a, 0);
   _a = ((B (*) (_class_Program *, A)) this->vt[0]) ((_class_Program *) this, _a);
   _b = ((B (*) (_class_Program *, B)) this->vt[0]) ((_class_Program *) this, _b);
   _a = ((A (*) (_class_Program *, int)) this->vt[1]) ((_class_Program *) this, 0);
}

Func VTclass_Program[] = {
   (void (*) ()) _Program_m,
   (void (*) ()) _Program_p,
   (void (*) ()) _Program_run
};

_class_Program *new_Program() {
   _class_Program *t;
   if ((t = malloc(sizeof(_class_Program))) != NULL) {
      t->vt = VTclass_Program;
   }
   return t;
}

int main() {
   _class_Program *program;

   /*
    * Crie objeto da classe Program e envie a mensagem run para ele.
    * Nem sempre o n�mero de run no vetor � 0.
   */
   program = new_Program();
   ( ( void (*)(_class_Program *) ) program->vt[0] )(program);
   return 0;
}

