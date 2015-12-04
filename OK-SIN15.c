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
   struct _St_Test {
      Func *vt;
   } _class_Test;

_class_Test *new_Test(void);

char * _Test_fakeClone(_class_Test *this, char * _s) {
   return _s;
}

Func VTclass_Test[] = {
   (void (*) ()) _Test_fakeClone
};

_class_Test *new_Test() {
   _class_Test *t;
   if ((t = malloc(sizeof(_class_Test))) != NULL) {
      t->vt = VTclass_Test;
   }
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run(_class_Program *this) {
   _class_String _s;
   _class_Test *_t;
   _s = "Ola !!!";
   puts(_s);
   {
      char __s[512];
      gets(__s);
      _s = malloc(strlen(__s) + 1);
strcpy(_s, __s);
   }
   _t = new_Test();
   puts(((char * (*) (_class_Test *, char *)) _t->vt[0]) (_t, "Dolly Parton"));
   puts("barra   \\");
   puts("barra n  \n");
   puts("barra a  \a");
   puts("barra barra \\\\");
   puts("barra no final  \ ");
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

