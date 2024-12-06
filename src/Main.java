import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
//        if (args.length > 0) {
//            runFile(args[0]);
//        } else {
//            runFromPrompt();
//        }
        runFile("src/test.txt");
    }

    private static void runFromPrompt() throws IOException {
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("> ");
            String input = br.readLine();
            if (input != null) {
                run(input);
            } else {
                break;
            }
        }
    }


    private static void runFile(String src) throws IOException {

        String fileSrc = new String(Files.readAllBytes(Path.of(src)), Charset.defaultCharset());
        run(fileSrc);
    }

    private static void run(String input) {
        System.out.println("Preparing to run");
        Lexer lexer=new Lexer(input);

    }
}