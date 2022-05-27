import lexicalanalyzer.Lexical;
import syntacticanalyzer.Syntactic;

public class App {
    public static void main(String[] args) throws Exception {

		String path = "Compilador\\src\\in.txt";
        Lexical lexical = new Lexical(path);
        Syntactic syntactic = new Syntactic(lexical);
        syntactic.program();
	}
}