import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Compiler {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: ./yourcompiler source.calc");
            System.exit(1);
        }

        try {
            String source = Scanner.readFile(args[0]);

            Scanner scanner = new Scanner(source);
            Parser parser = new Parser(scanner);

            String asmCode = parser.parseProgram();

            Path path = Paths.get(args[0]);
            String base = removeExtension(path.toString());

            String asmFile = base + ".s";
            String objFile = base + ".o";
            String exeFile = base;

            Files.write(Paths.get(asmFile), asmCode.getBytes());

            run("as", asmFile, "-o", objFile);
            run("ld", "-o", exeFile, objFile);

            System.out.println("Compilation successful.");
            System.out.println("Generated:");
            System.out.println("  " + asmFile);
            System.out.println("  " + objFile);
            System.out.println("  " + exeFile);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void run(String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();

        Process process = processBuilder.start();
        int code = process.waitFor();

        if (code != 0) {
            throw new RuntimeException("Command failed: " + String.join(" ", command));
        }
    }

    private static String removeExtension(String file) {
        int dot = file.lastIndexOf('.');

        if (dot == -1) {
            return file;
        }

        return file.substring(0, dot);
    }
}