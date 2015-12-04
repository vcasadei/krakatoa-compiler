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

void _A_m1(_class_A *this, int _n) {
   printf("%d", 1);
   printf("%d", _n);
}

void _A_m2(_class_A *this, int _n) {
   printf("%d", 2);
   printf("%d", _n);
}

void _A_m3(_class_A *this, int _n) {
   printf("%d", 3);
   printf("%d", _n);
}

Func VTclass_A[] = {
   (void (*) ()) _A_m1,
   (void (*) ()) _A_m2,
   (void (*) ()) _A_m3
};

_class_A *new_A() {
   _class_A *t;
   if ((t = malloc(sizeof(_class_A))) != NULL) {
      t->vt = VTclass_A;
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
   puts("");
   printf("\n");
   puts("Ok-ger08");
   printf("\n");
   puts("The output should be :");
   printf("\n");
   puts("1 1 2 2 3 3");
   printf("\n");
   _a = new_A();
   ((void (*) (_class_A *, int)) _a->vt[0]) (_a, 1);
   ((void (*) (_class_A *, int)) _a->vt[1]) (_a, 2);
   ((void (*) (_class_A *, int)) _a->vt[2]) (_a, 3);
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

