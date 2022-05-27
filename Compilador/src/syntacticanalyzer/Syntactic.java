package syntacticanalyzer;

import lexicalanalyzer.Lexical;
import lexicalanalyzer.Token;
import lexicalanalyzer.Type;

public class Syntactic {
	
	private Lexical lexical;
	private Token token;
	
	public Syntactic(Lexical lexical) {
		this.lexical = lexical;
	}

	public void program() {
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("int")) {
			throw new SyntacticException("Presta atenção no int do começo!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("main")) {
			throw new SyntacticException("Esquecesse do main tabacudo!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("(")) {
			throw new SyntacticException("Abre o parêntese do main direito boy!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals(")")) {
			throw new SyntacticException("Feche o parêntese do main direito boy!");
		}

		this.token = this.lexical.nextToken();
		this.block();

		this.token = this.lexical.nextToken();
		if (this.token.getType() == Type.END_CODE) {
			System.out.println("Ei boy, isso ta lindo");
		}else {
			throw new SyntacticException("Tá errado no fim do programa boy!");
		}
	}

	private void block() {
		if (!token.getContent().equals("{")) {
			throw new SyntacticException("Abre a chave, tabacudo!");
		}

		this.token = this.lexical.nextToken();
		this.variableDeclaration();

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("}")) {
			throw new SyntacticException("Fecha a chave, tabacudo!");
		}
	}

	private void variableDeclaration() {
		this.type();
		this.token = this.lexical.nextToken();
		if (token.getType() != Type.IDENTIFIER) {
			throw new SyntacticException("A declaração de variável tá errada!");
		}
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals(";")) {
			throw new SyntacticException("Faltou o ; da variável, donzelo!");
		}
	}

	private void type(){
		if (!(token.getContent().equals("int") || token.getContent().equals("float")
		|| token.getContent().equals("char") || token.getContent().equals("boolean"))) {
			throw new SyntacticException("Deu erro na declaração de variável boy");
		}
	}
}
