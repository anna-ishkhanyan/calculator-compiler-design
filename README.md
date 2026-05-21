# Calculator Compiler

This project is a small compiler written in Java that compiles a simple calculator-like language into x86_64 Linux assembly. The generated assembly is then assembled and linked into an executable binary.

The resulting executable computes arithmetic expressions and returns the final result as the program exit code.

---

# Supported Features

- Integer variable declarations
- Assignments using `=` and `:=`
- Arithmetic expressions
- Operator precedence
- Parentheses
- Variable usage inside expressions
- Compilation to x86_64 assembly
- Automatic assembling and linking

---

# Example Input

```text
int i, j, k;

i = 0;
j = i + 5;
k = j - 2;
k := i + 2 - (i * 2) + k;
```

# Example Workflow

Compile the source file:
```
./yourcompiler test.calc
```
This generates:

- test.s
- test.o
- test

Run the generated executable:
```
./test
```
Check the exit code:
```
echo $?
```
Expected result for the example above:
```
5
```
# Building

Build the compiler:
```
make
```
Clean generated files:
```
make clean
```
# Implementation Details

The compiler includes:

- Scanner 
- Recursive descent parser
- Simple code generator
- x86_64 AT&T syntax assembly generation

The parser supports recursive arithmetic expression parsing using:

- parseExpression()
- parseTerm()
- parseFactor()

which correctly handles operator precedence and parentheses.

# Platform Notes

The generated assembly targets:
```
x86_64 Linux
```
The project was tested on an ARM-based MacBook using Docker with an amd64 Linux container.

Example container:
```
docker run --platform linux/amd64 --rm -it \
-v "$PWD":/work \
-w /work \
ubuntu:22.04 bash
```
Inside the container:
```
apt update
apt install -y build-essential default-jdk
```

# AI Usage

AI tools were used during development for assistance with implementation details, debugging, code organization, and assembly-related issues.

The overall project structure, testing, integration, and final adjustments were performed manually.