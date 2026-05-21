JAVAC=javac
JAVA=java

SOURCES=TokenType.java Token.java Scanner.java CodeGenerator.java Parser.java Compiler.java

all:
	$(JAVAC) $(SOURCES)
	printf '#!/bin/sh\n$(JAVA) Compiler "$$@"\n' > yourcompiler
	chmod +x yourcompiler

clean:
	rm -f *.class *.s *.o yourcompiler
	rm -f test test.s test.o