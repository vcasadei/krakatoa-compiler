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

void _A_put(_class_A *this, int _pn) {
   this->_A_n = _pn;
}

int _A_get(_class_A *this) {
   return this->_A_n;
}

Func VTclass_A[] = {
   (void (*) ()) _A_put,
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
   struct _St_No {
      Func *vt;
      No _No_next;
      A _No_a;
   } _class_No;

_class_No *new_No(void);

void _No_setNext(_class_No *this, No _p_next) {
   this->_No_next = _p_next;
}

No _No_getNext(_class_No *this) {
   return this->_No_next;
}

void _No_set(_class_No *this, A _pa) {
   this->_No_a = _pa;
}

A _No_get(_class_No *this) {
   return this->_No_a;
}

Func VTclass_No[] = {
   (void (*) ()) _No_setNext,
   (void (*) ()) _No_getNext,
   (void (*) ()) _No_set,
   (void (*) ()) _No_get
};

_class_No *new_No() {
   _class_No *t;
   if ((t = malloc(sizeof(_class_No))) != NULL) {
      t->vt = VTclass_No;
   }
   return t;
}

typedef
   struct _St_B {
      Func *vt;
      int _A_n;
      int _B_k;
      No _B_first;
   } _class_B;

_class_B *new_B(void);

int _B_get(_class_B *this) {
   printf("%d", this->_B_k);
   return _A_get((_class_A * ) this)
;
}

void _B_init(_class_B *this, int _pk) {
   this->_B_k = _pk;
}

void _B_buildList(_class_B *this) {
   _class_int _i;
   _class_No *_w;
   _class_No *_newNo;
   _class_A *_a;
   _i = ((int (*) (_class_B *)) this->vt[0]) ((_class_B *) this);
   this->_B_first = new_No();
   _w = this->_B_first;
   ((void (*) (_class_No *, int)) _w->vt[0]) (_w,    NULL);
   _a = new_A();
   ((void (*) (_class_A *, int)) _a->vt[0]) (_a, _i);
   ((void (*) (_class_No *, A)) _w->vt[2]) (_w, _a);
   while (true) {
      _i = (_i) - 1;
      if ( (_i) <= 0 != false ) {
         break;
      }
      _newNo = new_No();
      ((void (*) (_class_No *, No)) _newNo->vt[0]) (_newNo, this->_B_first);
      _a = new_A();
      ((void (*) (_class_A *, int)) _a->vt[0]) (_a, _i);
      ((void (*) (_class_No *, A)) _newNo->vt[2]) (_newNo, _a);
      this->_B_first = _newNo;
}
}

void _B_list(_class_B *this) {
   _class_No *_w;
   _class_A *_a;
   _w = this->_B_first;
   while ((_w) !=    NULL) {
      _a = ((A (*) (_class_No *)) _w->vt[3]) (_w);
      printf("%d", ((int (*) (_class_A *)) _a->vt[1]) (_a));
      _w = ((No (*) (_class_No *)) _w->vt[1]) (_w);
}
}

Func VTclass_B[] = {
   (void (*) ()) _B_get,
   (void (*) ()) _B_init,
   (void (*) ()) _B_buildList,
   (void (*) ()) _B_list
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
   ((void (*) (_class_B *, int)) _b->vt[-1]) (_b, 10);
   ((void (*) (_class_B *)) _b->vt[2]) (_b);
   ((void (*) (_class_B *)) _b->vt[3]) (_b);
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

