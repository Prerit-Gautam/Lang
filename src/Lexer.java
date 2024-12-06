import java.util.ArrayList;

public class Lexer {
    static int start=0;
    static int current=0;
    static int line=1;
    static String src;

    static ArrayList<Token> tokenList=new ArrayList<>();

    public Lexer(String src){
        this.src=src;
        scan();
    }

    public static void scan(){
        while (!hasReachedEnd()){
            char c=getNext();
            switch(c){
                case '\n':
                    line++;
                    start=current;
                    break;

                case '=':
                    pushToken(check('=')?TokenType.EQUALS_EQUALS:TokenType.EQUALS, null, null, line);
                    break;

                case '!':
                    pushToken(check('=')?TokenType.NOT_EQUALS:TokenType.NOT, null, null, line);
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
                    if (check('/')){
                        while (peek()!='\n' && !hasReachedEnd()){
                            getNext();
                        }
                    } else {
                        pushToken(TokenType.DIVIDE, null, null, line);
                    }
                    break;

                case ' ':
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

                case '|':
                    if (check('|')){
                        pushToken(TokenType.OR, null, null, line);
                    }else{
                        pushError("Invalid token '|' ", line);
                    }
                    break;

                case '&':
                    if (check('&')) {
                        pushToken(TokenType.AND, null, null, line);
                    }else{
                        pushError("Invalid token '&'", line);
                    }
                    break;




            }
        }
    }


    // peek returns the next character in the string without consuming it
    public static char peek(){
        return src.charAt(current);
    }

    // getnext returns the next character in the string by consuming it
    public static char getNext(){
        current++;
        return src.charAt(current-1);
    }

    public static boolean hasReachedEnd(){
        return current==src.length();
    }

    public static boolean check(char c){
        if (hasReachedEnd()){
            return false;
        }
        if(peek()==c){
            current++;
            return true;
        }else{
            return false;
        }
    }

    public static void pushToken(TokenType type, String lexeme, Object literal, int line){
        tokenList.add(new Token(type, lexeme, literal, line));
        System.out.println(type+" "+lexeme+" "+literal+" "+line);
    }


    public static void pushError(String message, int line){
        System.out.println("Error: "+message+" at line "+line);
    }
}
