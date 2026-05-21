import java.util.LinkedHashSet;
import java.util.Set;

public class Parser {
    private final Scanner scanner;
    private Token current;
    private final CodeGenerator generator = new CodeGenerator();
    private final Set<String> variables = new LinkedHashSet<>();

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.current = scanner.getToken();
    }

    private void advance() {
        current = scanner.getToken();
    }

    private void expect(TokenType type) {
        if (current.type != type) {
            throw new RuntimeException("Expected " + type + " but got " + current.type);
        }

        advance();
    }

    public String parseProgram() {
        parseDeclarations();

        generator.generateHeader(variables);

        boolean hasStatement = false;

        while (current.type != TokenType.EOF) {
            parseAssignment();
            expect(TokenType.SEMICOLON);
            hasStatement = true;
        }

        if (!hasStatement) {
            generator.generateNumber("0");
        }

        generator.generateExitWithRax();

        return generator.getCode();
    }

    private void parseDeclarations() {
        expect(TokenType.KW_INT);

        parseIdentifierDeclaration();

        while (current.type == TokenType.COMMA) {
            advance();
            parseIdentifierDeclaration();
        }

        expect(TokenType.SEMICOLON);
    }

    private void parseIdentifierDeclaration() {
        if (current.type != TokenType.IDENT) {
            throw new RuntimeException("Identifier expected");
        }

        if (variables.contains(current.value)) {
            throw new RuntimeException("Duplicate variable '" + current.value + "'");
        }

        variables.add(current.value);

        advance();
    }

    private void parseAssignment() {
        if (current.type != TokenType.IDENT) {
            throw new RuntimeException("Assignment must start with identifier");
        }

        String name = current.value;

        if (!variables.contains(name)) {
            throw new RuntimeException("Undeclared variable '" + name + "'");
        }

        advance();

        if (current.type == TokenType.ASSIGN || current.type == TokenType.COLON_ASSIGN) {
            advance();
        } else {
            throw new RuntimeException("Expected = or :=");
        }

        parseExpression();

        generator.generateStoreVariable(name);
    }

    private void parseExpression() {
        parseTerm();

        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS) {
            TokenType op = current.type;

            advance();

            generator.pushRax();

            parseTerm();

            generator.popRbx();

            if (op == TokenType.PLUS) {
                generator.add();
            } else {
                generator.subtract();
            }
        }
    }

    private void parseTerm() {
        parseFactor();

        while (current.type == TokenType.STAR) {
            advance();

            generator.pushRax();

            parseFactor();

            generator.popRbx();

            generator.multiply();
        }
    }

    private void parseFactor() {
        if (current.type == TokenType.NUMBER) {
            generator.generateNumber(current.value);
            advance();
            return;
        }

        if (current.type == TokenType.IDENT) {
            if (!variables.contains(current.value)) {
                throw new RuntimeException("Undeclared variable '" + current.value + "'");
            }

            generator.generateLoadVariable(current.value);
            advance();
            return;
        }

        if (current.type == TokenType.LPAREN) {
            advance();

            parseExpression();

            expect(TokenType.RPAREN);
            return;
        }

        throw new RuntimeException("Number, identifier, or parenthesized expression expected");
    }
}