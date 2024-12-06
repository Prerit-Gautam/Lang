import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    static int start = 0;
    static int current = 0;
    static int line = 1;
    static String src;

    static ArrayList<Token> tokenList = new ArrayList<>();

    static HashMap<String, TokenType> typeMapper = new HashMap<>();


    public Lexer(String src) {
        this.src = src;

        typeMapper.put("int", TokenType.INT);
        typeMapper.put("string", TokenType.STRING);
        typeMapper.put("char", TokenType.CHAR);
        typeMapper.put("boolean", TokenType.BOOLEAN);
        typeMapper.put("and", TokenType.AND);
        typeMapper.put("or", TokenType.OR);
        typeMapper.put("while", TokenType.WHILE);
        typeMapper.put("for", TokenType.FOR);
        typeMapper.put("if", TokenType.IF);
        typeMapper.put("else", TokenType.ELSE);
        typeMapper.put("elseif", TokenType.ELSEIF);
        typeMapper.put("def", TokenType.DEF);
        typeMapper.put("class", TokenType.CLASS);
        typeMapper.put("return", TokenType.RETURN);
        typeMapper.put("break", TokenType.BREAK);
        typeMapper.put("continue", TokenType.CONTINUE);

        scan();
    }

    public static void scan() {
        while (!hasReachedEnd()) {
            char c = getNext();
            switch (c) {
                case '\n':
                    line++;
                    start = current;
                    break;

                case '=':
                    pushToken(check('=') ? TokenType.EQUALS_EQUALS : TokenType.EQUALS, null, null, line);
                    break;

                case '!':
                    pushToken(check('=') ? TokenType.NOT_EQUALS : TokenType.NOT, null, null, line);
                    break;

                case '+':
                    pushToken(TokenType.PLUS, null, null, line);
                    break;

                case '-':
                    pushToken(TokenType.MINUS, null, null, line);
                    break;

                case '*':
                    pushToken(TokenType.MULTIPLY, null, null, line);
                    break;

                case '/':
                    if (check('/')) {
                        while (peek() != '\n' && !hasReachedEnd()) {
                            getNext();
                        }
                    } else {
                        pushToken(TokenType.DIVIDE, null, null, line);
                    }
                    break;

                case ' ', '\0', '\r':
                    break;

                case '(':
                    pushToken(TokenType.LEFT_PAREN, null, null, line);
                    break;

                case ')':
                    pushToken(TokenType.RIGHT_PAREN, null, null, line);
                    break;

                case '{':
                    pushToken(TokenType.LEFT_CURLY, null, null, line);
                    break;

                case '}':
                    pushToken(TokenType.RIGHT_CURLY, null, null, line);
                    break;

                case '[':
                    pushToken(TokenType.LEFT_BRACKET, null, null, line);
                    break;

                case ']':
                    pushToken(TokenType.RIGHT_BRACKET, null, null, line);
                    break;

                case ',':
                    pushToken(TokenType.COMMA, null, null, line);
                    break;

                case ';':
                    pushToken(TokenType.SEMICOLON, null, null, line);
                    break;

                case '|':
                    if (check('|')) {
                        pushToken(TokenType.OR, null, null, line);
                    } else {
                        pushError("Invalid token '|' ", line);
                    }
                    break;

                case '&':
                    if (check('&')) {
                        pushToken(TokenType.AND, null, null, line);
                    } else {
                        pushError("Invalid token '&'", line);
                    }
                    break;

                case '"':
                    string();

                default:
                    if (isNumber(c)){
                        number();
                    }else if (isCharacter(c) || c=='_') {
                        identifier();
                    }else{
                        pushError("Invalid character", line);
                    }

            }
        }
    }

    private static void identifier() {
        while ((isCharacter(peek()) || isNumber(peek()) || peek()=='_') && !hasReachedEnd()) {
            getNext();
        }
        String lexeme = src.substring(start, current);
        TokenType type = typeMapper.get(lexeme);
        if (type == null) {
            pushToken(TokenType.IDENTIFIER, lexeme, null, line);
        } else {
            pushToken(type, lexeme, null, line);
        }
    }

    private static void number() {
        while (isNumber(peek()) && !hasReachedEnd()) {
            getNext();
        }
        if (peek() == '.' && isNumber(peek())) {
            getNext();
            while (isNumber(peek())) {
                getNext();
            }
        }
        pushToken(TokenType.INT, src.substring(start, current), null, line);
    }

    private static boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private static boolean isCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static void string() {
        while (!hasReachedEnd() && peek() != '"') {
            getNext();
        }
        if (hasReachedEnd()) {
            pushError("Unterminated string", line);
            return;
        }
        getNext(); // Consume the closing quotation mark
        pushToken(TokenType.STRING, src.substring(start + 1, current - 1), null, line);
    }


    // peek returns the next character in the string without consuming it
    public static char peek() {
        if (hasReachedEnd()){
            return '\0';
        } else {
            return src.charAt(current);
        }
    }

    // getnext returns the next character in the string by consuming it
    public static char getNext() {
        if (hasReachedEnd()){
            return '\0';
        } else {
            current++;
            return src.charAt(current-1);
        }
    }

    public static boolean hasReachedEnd() {
        return current == src.length();
    }

    public static boolean check(char c) {
        if (hasReachedEnd()) {
            return false;
        }
        if (peek() == c) {
            current++;
            return true;
        } else {
            return false;
        }
    }

    public static void pushToken(TokenType type, String lexeme, Object literal, int line) {
        tokenList.add(new Token(type, lexeme, literal, line));
        start=current;
        System.out.println(type + " " + lexeme + " " + literal + " " + line);
    }


    public static void pushError(String message, int line) {
        System.out.println("Error: " + message + " at line " + line);
    }
}
