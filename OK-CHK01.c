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
   } _class_A;

_class_A *new_A(void);

void _A_m1(_class_A *this) {
}

void _A_m2(_class_A *this) {
}

Func VTclass_A[] = {
   (void (*) ()) _A_m1,
   (void (*) ()) _A_m2
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
   } _class_B;

_class_B *new_B(void);

void _B_m3(_class_B *this) {
}

void _B_m4(_class_B *this) {
}

Func VTclass_B[] = {
   (void (*) ()) _B_m3,
   (void (*) ()) _B_m4
};

_class_B *new_B() {
   _class_B *t;
   if ((t = malloc(sizeof(_class_B))) != NULL) {
      t->vt = VTclass_B;
   }
   return t;
}

typedef
   struct _St_C {
      Func *vt;
   } _class_C;

_class_C *new_C(void);

void _C_m5(_class_C *this) {
}

void _C_m6(_class_C *this) {
}

void _C_m7(_class_C *this) {
}

Func VTclass_C[] = {
   (void (*) ()) _C_m5,
   (void (*) ()) _C_m6,
   (void (*) ()) _C_m7
};

_class_C *new_C() {
   _class_C *t;
   if ((t = malloc(sizeof(_class_C))) != NULL) {
      t->vt = VTclass_C;
   }
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run(_class_Program *this) {
   _class_A *_a1;
   _class_A *_a2;
   _class_B *_b1;
   _class_B *_b2;
   _class_C *_c1;
   _class_C *_c2;
   _a1 = new_A();
   _a2 = new_A();
   _b1 = new_B();
   _b2 = new_B();
   _c1 = new_C();
   _c2 = new_C();
   ((void (*) (_class_A *)) _a1->vt[0]) (_a1);
   ((void (*) (_class_A *)) _a1->vt[1]) (_a1);
   ((void (*) (_class_A *)) _a2->vt[1]) (_a2);
   ((void (*) (_class_B *)) _b1->vt[0]) (_b1);
   ((void (*) (_class_B *)) _b1->vt[0]) (_b1);
   ((void (*) (_class_B *)) _b2->vt[0]) (_b2);
   ((void (*) (_class_B *)) _b1->vt[1]) (_b1);
   ((void (*) (_class_B *)) _b2->vt[1]) (_b2);
   ((void (*) (_class_B *)) _b1->vt[1]) (_b1);
   ((void (*) (_class_B *)) _b2->vt[1]) (_b2);
   ((void (*) (_class_C *)) _c1->vt[0]) (_c1);
   ((void (*) (_class_C *)) _c2->vt[0]) (_c2);
   ((void (*) (_class_C *)) _c1->vt[0]) (_c1);
   ((void (*) (_class_C *)) _c2->vt[0]) (_c2);
   ((void (*) (_class_C *)) _c1->vt[0]) (_c1);
   ((void (*) (_class_C *)) _c1->vt[1]) (_c1);
   ((void (*) (_class_C *)) _c1->vt[1]) (_c1);
   ((void (*) (_class_C *)) _c1->vt[1]) (_c1);
   ((void (*) (_class_C *)) _c2->vt[1]) (_c2);
   ((void (*) (_class_C *)) _c2->vt[1]) (_c2);
   ((void (*) (_class_C *)) _c2->vt[1]) (_c2);
   ((void (*) (_class_C *)) _c1->vt[2]) (_c1);
   ((void (*) (_class_C *)) _c1->vt[2]) (_c1);
   ((void (*) (_class_C *)) _c1->vt[2]) (_c1);
   ((void (*) (_class_C *)) _c2->vt[2]) (_c2);
   ((void (*) (_class_C *)) _c2->vt[2]) (_c2);
   ((void (*) (_class_C *)) _c2->vt[2]) (_c2);
   ((void (*) (_class_C *)) _c2->vt[2]) (_c2);
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

