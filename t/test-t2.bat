REM Segundo trabalho. 
REM testa a geração de código 

del ok-*.txt
del er-*.txt
del *.c
del z.txt
del r.txt
del ok-*.exe
del er-*.exe

rem del ..\..\t\ok-*.txt
rem del ..\..\t\er-*.txt
rem del ..\..\t\ok-*.exe
rem del ..\..\t\er-*.exe
rem del ..\..\ktests\ok-*.txt
rem del ..\..\ktests\er-*.txtcd
rem del ..\..\ktests\ok-*.exe
rem del ..\..\ktests\er-*.exe
rem del ..\..\ktests\ok-*.c




java -cp bin comp.Comp ..\..\ktests\OK-GER01.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER02.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER03.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER04.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER05.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER06.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER07.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER08.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER09.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER10.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER11.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER12.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER14.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER15.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER16.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER17.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER18.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER19.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER20.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER21.KRA
java -cp bin comp.Comp ..\..\ktests\OK-GER22.KRA


rem move ..\..\t\ok-*.txt .
rem move ..\..\t\er-*.txt .
rem move ..\..\t\ok-*.exe .
rem move ..\..\ktests\ok-*.txt .
rem move ..\..\ktests\er-*.txt .
rem move ..\..\ktests\ok-*.c .
rem move ..\..\ktests\ok-*.exe .



set path="C:\MinGW\bin";%path%

del z.txt

gcc  -IC:\MinGW\include  -o OK-GER01.exe OK-GER01.KRA.KRA.c
OK-GER01  < ..\..\t\30-enters.txt > OK-Out01.txt
gcc  -IC:\MinGW\include  -o OK-GER02.exe OK-GER02.KRA.c
OK-GER02  < ..\..\t\30-enters.txt > OK-Out02.txt
gcc  -IC:\MinGW\include  -o OK-GER03.exe OK-GER03.KRA.c
OK-GER03  < ..\..\t\30-enters.txt > OK-Out03.txt
gcc  -IC:\MinGW\include  -o OK-GER04.exe OK-GER04.KRA.c
OK-GER04  < ..\..\t\30-enters.txt > OK-Out04.txt
gcc  -IC:\MinGW\include  -o OK-GER06.exe OK-GER06.KRA.c
OK-GER06  < ..\..\t\30-enters.txt > OK-Out06.txt
gcc  -IC:\MinGW\include  -o OK-GER07.exe OK-GER07.KRA.c
OK-GER07  < ..\..\t\30-enters.txt > OK-Out07.txt
gcc  -IC:\MinGW\include  -o OK-GER08.exe OK-GER08.KRA.c
OK-GER08  < ..\..\t\30-enters.txt > OK-Out08.txt
gcc  -IC:\MinGW\include  -o OK-GER09.exe OK-GER09.KRA.c
OK-GER09  < ..\..\t\30-enters.txt > OK-Out09.txt
gcc  -IC:\MinGW\include  -o OK-GER10.exe OK-GER10.KRA.c
OK-GER10  < ..\..\t\30-enters.txt > OK-Out10.txt
gcc  -IC:\MinGW\include  -o OK-GER12.exe OK-GER12.KRA.c
OK-GER12  < ..\..\t\30-enters.txt > OK-Out12.txt
gcc  -IC:\MinGW\include  -o OK-GER14.exe OK-GER14.KRA.c
OK-GER14  < ..\..\t\30-enters.txt > OK-Out14.txt
gcc  -IC:\MinGW\include  -o OK-GER15.exe OK-GER15.KRA.c
OK-GER15  < ..\..\t\30-enters.txt > OK-Out15.txt
gcc  -IC:\MinGW\include  -o OK-GER16.exe OK-GER16.KRA.c
OK-GER16  < ..\..\t\30-enters.txt > OK-Out16.txt
gcc  -IC:\MinGW\include  -o OK-GER17.exe OK-GER17.KRA.c
OK-GER17  < ..\..\t\30-enters.txt > OK-Out17.txt
gcc  -IC:\MinGW\include  -o OK-GER18.exe OK-GER18.KRA.c
OK-GER18  < ..\..\t\30-enters.txt > OK-Out18.txt
gcc  -IC:\MinGW\include  -o OK-GER19.exe OK-GER19.KRA.c
OK-GER19  < ..\..\t\30-enters.txt > OK-Out19.txt

gcc  -IC:\MinGW\include  -o OK-GER20.exe OK-GER20.KRA.c
OK-GER20  < ..\..\t\30-enters.txt > OK-Out20.txt
gcc  -IC:\MinGW\include  -o OK-GER21.exe OK-GER21.KRA.c
OK-GER21  < ..\..\t\30-enters.txt > OK-Out21.txt
gcc  -IC:\MinGW\include  -o OK-GER22.exe OK-GER22.KRA.c
OK-GER22  < ..\..\t\30-enters.txt > OK-Out22.txt


copy OK-Out*.txt z.txt

gcc  -IC:\MinGW\include  -o OK-GER05.exe OK-GER05.KRA.c
OK-GER05  < ..\..\t\sixnum.txt  > OK-Out05.txt

gcc  -IC:\MinGW\include  -o OK-GER11.exe OK-GER11.KRA.c
OK-GER11  < ..\..\t\30-enters.txt > OK-Out11.txt


type OK-Out05.txt >> z.txt
type OK-Out11.txt >> z.txt


del *.tds
del *.obj
del *.bak

