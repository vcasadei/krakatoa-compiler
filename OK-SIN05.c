/* deve-se incluir alguns headers porque algumas funções
 * da biblioteca padrão de C são utilizadas na tradução. */
#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>

/* define o tipo boolean */
typedef int boolean;
#define true 1
#define false 0

/* define um tipo Func que é um ponteiro para função */
typedef
   void (*Func)();



int main() {
   _class_Program *program;

   /* crie objeto da classe Program e envie a mensagem run para ele.
    * Nem sempre o número de run no vetor é 0. */
   program = new_Program();
   ( ( void (*)(_class_Program *) ) program->vt[0] )(program);
   return 0
}
