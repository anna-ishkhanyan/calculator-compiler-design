import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Scanner {
    private final String input;
    private int position = 0;

    public Scanner(String input) {
        this.input = input;
    }

    private char current() {
        if (position >= input.length()) {
            return '\0';
        }

        return input.charAt(position);
    }

    private char next() {
        if (position >= input.length()) {
            return '\0';
        }

        return input.charAt(position++);
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(current())) {
            next();
        }
    }

    private TokenType keywordOrIdent(String value) {
        if (value.equals("int")) {
            return TokenType.KW_INT;
        }

        return TokenType.IDENT;
    }

    public Token getToken() {
        skipWhitespace();

        char ch = current();

        if (ch == '\0') {
            return new Token(TokenType.EOF, "");
        }

        if (Character.isLetter(ch) || ch == '_') {
            StringBuilder value = new StringBuilder();

            while (Character.isLetterOrDigit(current()) || current() == '_') {
                value.append(next());
            }

            String text = value.toString();

            return new Token(keywordOrIdent(text), text);
        }

        if (Character.isDigit(ch)) {
            StringBuilder value = new StringBuilder();

            while (Character.isDigit(current())) {
                value.append(next());
            }

            return new Token(TokenType.NUMBER, value.toString());
        }

        if (ch == ':') {
            next();

            if (current() == '=') {
                next();
                return new Token(TokenType.COLON_ASSIGN, ":=");
            }

            throw new RuntimeException("Invalid character ':'");
        }

        next();

        switch (ch) {
            case '=':
                return new Token(TokenType.ASSIGN, "=");

            case ';':
                return new Token(TokenType.SEMICOLON, ";");

            case ',':
                return new Token(TokenType.COMMA, ",");

            case '(':
                return new Token(TokenType.LPAREN, "(");

            case ')':
                return new Token(TokenType.RPAREN, ")");

            case '+':
                return new Token(TokenType.PLUS, "+");

            case '-':
                return new Token(TokenType.MINUS, "-");

            case '*':
                return new Token(TokenType.STAR, "*");

            default:
                throw new RuntimeException("Invalid character '" + ch + "'");
        }
    }

    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}