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
   struct _St_Person {
      Func *vt;
      char * _Person_course;
      int _Person_number;
      int _Person_age;
      char * _Person_name;
   } _class_Person;

_class_Person *new_Person(void);

char * _Person_getCourse(_class_Person *this) {
   return this->_Person_course;
}

void _Person_setCourse(_class_Person *this, char * _course) {
   this->_Person_course = _course;
}

int _Person_getNumber(_class_Person *this) {
   return this->_Person_number;
}

void _Person_setNumber(_class_Person *this, int _number) {
   this->_Person_number = _number;
}

void _Person_init(_class_Person *this, char * _name, int _age) {
   this->_Person_name = _name;
   this->_Person_age = _age;
}

char * _Person_getName(_class_Person *this) {
   return this->_Person_name;
}

int _Person_getAge(_class_Person *this) {
   return this->_Person_age;
}

void _Person_print(_class_Person *this) {
   puts("Name = ");
   puts(this->_Person_name);
   puts("Age = ");
   printf("%d", this->_Person_age);
}

Func VTclass_Person[] = {
   (void (*) ()) _Person_getCourse,
   (void (*) ()) _Person_setCourse,
   (void (*) ()) _Person_getNumber,
   (void (*) ()) _Person_setNumber,
   (void (*) ()) _Person_init,
   (void (*) ()) _Person_getName,
   (void (*) ()) _Person_getAge,
   (void (*) ()) _Person_print
};

_class_Person *new_Person() {
   _class_Person *t;
   if ((t = malloc(sizeof(_class_Person))) != NULL) {
      t->vt = VTclass_Person;
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

