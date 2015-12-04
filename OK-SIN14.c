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
   struct _St_Point {
      Func *vt;
      int _Point_x;
      int _Point_y;
   } _class_Point;

_class_Point *new_Point(void);

void _Point_set(_class_Point *this, int _x, int _y) {
   this->_Point_x = _x;
   this->_Point_y = _y;
}

int _Point_getX(_class_Point *this) {
   return this->_Point_x;
}

int _Point_getY(_class_Point *this) {
   return this->_Point_y;
}

Func VTclass_Point[] = {
   (void (*) ()) _Point_set,
   (void (*) ()) _Point_getX,
   (void (*) ()) _Point_getY
};

_class_Point *new_Point() {
   _class_Point *t;
   if ((t = malloc(sizeof(_class_Point))) != NULL) {
      t->vt = VTclass_Point;
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

