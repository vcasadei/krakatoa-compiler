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

int _A_f(_class_A *this) {
   return 0;
}

void _A_m(_class_A *this) {
   printf("%d", ((int (*) (_class_A *)) this->vt[0]) ((_class_A *) this));
}

Func VTclass_A[] = {
   (void (*) ()) _A_f,
   (void (*) ()) _A_m
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

int _B_g(_class_B *this) {
   return 1;
}

void _B_p(_class_B *this) {
   printf("%d", ((int (*) (_class_B *)) this->vt[0]) ((_class_B *) this) + ((int (*) (_class_B *)) this->vt[3]) ((_class_B *) this));
}

void _B_r(_class_B *this) {
   printf("%d", 2);
}

int _B_f(_class_B *this) {
   return 10;
}

Func VTclass_B[] = {
   (void (*) ()) _B_g,
   (void (*) ()) _B_p,
   (void (*) ()) _B_r,
   (void (*) ()) _B_f
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

int _C_f(_class_C *this) {
   return 20;
}

int _C_g(_class_C *this) {
   return 101;
}

void _C_r(_class_C *this) {
   printf("%d", 200);
}

Func VTclass_C[] = {
   (void (*) ()) _C_f,
   (void (*) ()) _C_g,
   (void (*) ()) _C_r
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
   struct _St_F {
      Func *vt;
   } _class_F;

_class_F *new_F(void);

int _F_f(_class_F *this) {
   return 3;
}

void _F_m(_class_F *this) {
   printf("%d", ((int (*) (_class_F *)) this->vt[0]) ((_class_F *) this));
}

Func VTclass_F[] = {
   (void (*) ()) _F_f,
   (void (*) ()) _F_m
};

_class_F *new_F() {
   _class_F *t;
   if ((t = malloc(sizeof(_class_F))) != NULL) {
      t->vt = VTclass_F;
   }
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run(_class_Program *this) {
   _class_A *_a;
   _class_B *_b;
   _class_C *_c;
   _class_D *_d;
   _class_F *_f;
   puts("");
   printf("\n");
   puts("Ok-ger22");
   printf("\n");
   puts("The output should be: ");
   printf("\n");
   puts("0 10 11 2 20 121 200 20 121 200 0 0 3");
   printf("\n");
   _a = new_A();
   ((void (*) (_class_A *)) _a->vt[1]) (_a);
   _b = new_B();
   ((void (*) (_class_B *)) _b->vt[-1]) (_b);
   ((void (*) (_class_B *)) _b->vt[1]) (_b);
   ((void (*) (_class_B *)) _b->vt[2]) (_b);
   _c = new_C();
   ((void (*) (_class_C *)) _c->vt[-1]) (_c);
   ((void (*) (_class_C *)) _c->vt[-1]) (_c);
   ((void (*) (_class_C *)) _c->vt[2]) (_c);
   _b = _c;
   ((void (*) (_class_B *)) _b->vt[-1]) (_b);
   ((void (*) (_class_B *)) _b->vt[1]) (_b);
   ((void (*) (_class_B *)) _b->vt[2]) (_b);
   _d = new_D();
   ((void (*) (_class_D *)) _d->vt[-1]) (_d);
   _a = _d;
   ((void (*) (_class_A *)) _a->vt[1]) (_a);
   _f = new_F();
   ((void (*) (_class_F *)) _f->vt[1]) (_f);
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

