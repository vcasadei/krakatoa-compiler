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
      int _A_i;
   } _class_A;

_class_A *new_A(void);

void _A_put(_class_A *this, int _x, int _y, int _ok) {
   if ( ((_x) > (_y)) && (_ok) != false ) {
      this->_A_i = 0;
   }
}

int _A_get(_class_A *this) {
   return this->_A_i;
}

void _A_set(_class_A *this, int _i) {
   this->_A_i = _i;
}

Func VTclass_A[] = {
   (void (*) ()) _A_put,
   (void (*) ()) _A_get,
   (void (*) ()) _A_set
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
      int _A_i;
   } _class_B;

_class_B *new_B(void);

void _B_put(_class_B *this, int _a, int _b, int _c) {
   if ( (((_a) + (_b)) < 1) && !_c != false ) {
      printf("%d", 0);
   }
}

Func VTclass_B[] = {
   (void (*) ()) _B_put
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

void _Program_run(_class_Program *this) {
   _class_B *_b;
   _b = new_B();
   ((void (*) (_class_B *, int, intint)) _b->vt[0]) (_b, 1, 2, true);
}

Func VTclass_Program[] = {
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

