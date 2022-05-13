import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lexicalanalys.Lexical;

public class App {
    public static void main(String[] args) throws Exception {

		String path = "src\\in.txt";

		try {
            String conteudoStr = new String(Files.readAllBytes(Paths.get(path)));
            Lexical analyzer = new Lexical(conteudoStr.toCharArray());
            analyzer.printTokens();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
}