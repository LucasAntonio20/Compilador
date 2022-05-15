import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lexicalanalyzer.Lexical;

public class App {
    public static void main(String[] args) throws Exception {

		String path = "Compilador\\src\\in.txt";

		try {
            String conteudoStr = new String(Files.readAllBytes(Paths.get(path)));
            Lexical analyzer = new Lexical(conteudoStr.toCharArray());
            analyzer.printTokens();
        } catch (IOException e) {
            System.out.println("Error: Arquivo " + e.getMessage() + " não encontrado");
        }
	}
}