public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    public Token(TokenType type, String lexeme, Object literal, int line ){
        this.type=type;
        this.lexeme=lexeme;
        this.line=line;
        this.literal=literal;
    }
}
