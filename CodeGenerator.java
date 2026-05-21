import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CodeGenerator {
    private final List<String> instructions = new ArrayList<>();

    public void generateHeader(Set<String> variables) {
        instructions.add(".section .data");

        for (String variable : variables) {
            instructions.add(variable + ": .quad 0");
        }

        instructions.add(".section .text");
        instructions.add(".globl _start");
        instructions.add("_start:");
    }

    public void generateNumber(String number) {
        instructions.add("mov $" + number + ", %rax");
    }

    public void generateLoadVariable(String name) {
        instructions.add("mov " + name + ", %rax");
    }

    public void generateStoreVariable(String name) {
        instructions.add("mov %rax, " + name);
    }

    public void pushRax() {
        instructions.add("push %rax");
    }

    public void popRbx() {
        instructions.add("pop %rbx");
    }

    public void add() {
        instructions.add("add %rbx, %rax");
    }

    public void subtract() {
        instructions.add("sub %rax, %rbx");
        instructions.add("mov %rbx, %rax");
    }

    public void multiply() {
        instructions.add("imul %rbx, %rax");
    }

    public void generateExitWithRax() {
        instructions.add("mov %rax, %rdi");
        instructions.add("mov $60, %rax");
        instructions.add("syscall");
    }

    public String getCode() {
        return String.join("\n", instructions) + "\n";
    }
}