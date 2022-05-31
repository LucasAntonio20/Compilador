import lexicalanalysis.Lexical;
import syntacticanalyzer.Parser;

public class App {
    public static void main(String[] args) throws Exception {

		String path = "Compilador\\src\\in.txt";
        Lexical lexical = new Lexical(path);
        Parser syntactic = new Parser(lexical);
        syntactic.program();
	}
}