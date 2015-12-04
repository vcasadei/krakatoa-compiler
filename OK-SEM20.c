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

int _A_m1(_class_A *this, int _ok) {
   return 0;
}

void _A_m2(_class_A *this) {
}

char * _A_m3(_class_A *this, char * _s, int _ok) {
   return "A";
}

char * _A_m4(_class_A *this, int _i, int _ok) {
   return "Am4";
}

Func VTclass_A[] = {
   (void (*) ()) _A_m1,
   (void (*) ()) _A_m2,
   (void (*) ()) _A_m3,
   (void (*) ()) _A_m4
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

int _B_m1(_class_B *this, int _ok) {
   return 1;
}

void _B_m2(_class_B *this) {
}

int _B_mB(_class_B *this) {
   return 1;
}

Func VTclass_B[] = {
   (void (*) ()) _B_m1,
   (void (*) ()) _B_m2,
   (void (*) ()) _B_mB
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

int _C_m1(_class_C *this, int _ok) {
   return 2;
}

char * _C_m4(_class_C *this, int _i, int _ok) {
   return "C";
}

char * _C_m5(_class_C *this) {
   return "finally";
}

Func VTclass_C[] = {
   (void (*) ()) _C_m1,
   (void (*) ()) _C_m4,
   (void (*) ()) _C_m5
};

_class_C *new_C() {
   _class_C *t;
   if ((t = malloc(sizeof(_class_C))) != NULL) {
      t->vt = VTclass_C;
   }
   return t;
}

typedef
   struct _St_D {
      Func *vt;
   } _class_D;

_class_D *new_D(void);

Func VTclass_D[] = {
};

_class_D *new_D() {
   _class_D *t;
   if ((t = malloc(sizeof(_class_D))) != NULL) {
      t->vt = VTclass_D;
   }
   return t;
}

typedef
   struct _St_E {
      Func *vt;
   } _class_E;

_class_E *new_E(void);

int _E_m1(_class_E *this, int _ok) {
   return 5;
}

void _E_m2(_class_E *this) {
}

char * _E_m4(_class_E *this, int _i, int _ok) {
   return "Em4";
}

Func VTclass_E[] = {
   (void (*) ()) _E_m1,
   (void (*) ()) _E_m2,
   (void (*) ()) _E_m4
};

_class_E *new_E() {
   _class_E *t;
   if ((t = malloc(sizeof(_class_E))) != NULL) {
      t->vt = VTclass_E;
   }
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run(_class_Program *this) {
   _class_C *_c;
   _class_B *_b;
   _class_A *_a;
   _class_D *_d;
   _class_E *_e;
   _c = new_C();
   printf("%d", ((int (*) (_class_C *, int)) _c->vt[0]) (_c, true));
   ((void (*) (_class_C *)) _c->vt[-1]) (_c);
   puts(((char * (*) (_class_C *, char *int)) _c->vt[-1]) (_c, "ok", false));
   puts(((char * (*) (_class_C *, intint)) _c->vt[1]) (_c, 0, false));
   puts(((char * (*) (_class_C *)) _c->vt[2]) (_c));
   printf("%d", ((int (*) (_class_C *)) _c->vt[-1]) (_c) + 1);
   _b = new_B();
   printf("%d", ((int (*) (_class_B *, int)) _b->vt[0]) (_b, true));
   ((void (*) (_class_B *)) _b->vt[1]) (_b);
   puts(((char * (*) (_class_B *, char *int)) _b->vt[-1]) (_b, "ok", false));
   puts(((char * (*) (_class_B *, intint)) _b->vt[-1]) (_b, 0, false));
   printf("%d", ((int (*) (_class_C *)) _c->vt[-1]) (_c) + 1);
   _a = new_A();
   printf("%d", ((int (*) (_class_A *, int)) _a->vt[0]) (_a, true));
   ((void (*) (_class_A *)) _a->vt[1]) (_a);
   puts(((char * (*) (_class_A *, char *int)) _a->vt[2]) (_a, "ok", false));
   puts(((char * (*) (_class_A *, intint)) _a->vt[3]) (_a, 0, false));
   _d = new_D();
   printf("%d", ((int (*) (_class_D *, int)) _d->vt[-1]) (_d, true));
   ((void (*) (_class_D *)) _d->vt[-1]) (_d);
   puts(((char * (*) (_class_D *, char *int)) _d->vt[-1]) (_d, "ok", false));
   puts(((char * (*) (_class_D *, intint)) _d->vt[-1]) (_d, 0, false));
   _e = new_E();
   printf("%d", ((int (*) (_class_E *, int)) _e->vt[0]) (_e, true));
   ((void (*) (_class_E *)) _e->vt[1]) (_e);
   puts(((char * (*) (_class_E *, char *int)) _e->vt[-1]) (_e, "ok", false));
   puts(((char * (*) (_class_E *, intint)) _e->vt[2]) (_e, 0, false));
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

