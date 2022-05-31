package syntacticanalyzer;

import lexicalanalysis.Lexical;
import lexicalanalysis.Token;
import lexicalanalysis.Type;

public class Parser {
	
	private Lexical lexical;
	private Token token;
	
	public Parser(Lexical lexical) {
		this.lexical = lexical;
	}

	public void program() {
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("int")) {
			throw new ParserException("Presta atenção no int do começo!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("main")) {
			throw new ParserException("Esquecesse do main tabacudo!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("(")) {
			throw new ParserException("Abre o parêntese do main direito boy!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals(")")) {
			throw new ParserException("Feche o parêntese do main direito boy!");
		}

		this.block();

		this.token = this.lexical.nextToken();
		if (this.token.getType() == Type.END_CODE) {
			System.out.println("Ei boy, isso ta lindo boy");
		}else {
			throw new ParserException("Tá errado no fim do programa boy!");
		}
	}

	private void block() {
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("{")) {
			throw new ParserException("Abre a chave, tabacudo!");
		}

		this.token = this.lexical.nextToken();
		//do {
			this.variableDeclaration();
			this.token = this.lexical.nextToken();
			//this.comment();
			//this.token = this.lexical.nextToken();
		//} while (!this.token.getContent().equals("}"));

		this.command();
		this.token = this.lexical.nextToken();

		if (!token.getContent().equals("}")) {
			throw new ParserException("Fecha a chave, tabacudo!");
		}
	}

	private void command() {
		this.basicCommand();
	}

	private void basicCommand() {
		this.allocation();
	}

	private void allocation() {
		if (token.getType() != Type.IDENTIFIER) {
			throw new ParserException("A declaração de variável tá errada!");
		}
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("=")) {
			throw new ParserException("Tá faltando um \"=\" boy!");
		}

		this.arithmeticExpression();

		if (!token.getContent().equals(";")) {
			throw new ParserException("Faltou o ; da atribuição, donzelo!");
		}
	}

	private void arithmeticExpression() {
		this.token = this.lexical.nextToken();
		if ((token.getContent().equals("true") || (token.getContent().equals("false")))) {
			this.token = this.lexical.nextToken();
			return;
		}
		this.term();
		if(token.getContent().equals("+") || token.getContent().equals("-")){
			this.token = this.lexical.nextToken();
			this.term();
		}
	}

	private void term() {	
		this.coefficient();
		this.token = this.lexical.nextToken();
		if(token.getContent().equals("*") || token.getContent().equals("/")){
			this.token = this.lexical.nextToken();
			this.coefficient();
			this.token = this.lexical.nextToken();
		}
	}

	private void coefficient() {
		if (token.getContent().equals("(")){
		    this.arithmeticExpression();
		    if (!token.getContent().equals(")")) {
			throw new ParserException("Feche o parêntese direito boy!");
		    }
		} else if (this.token.getType() != Type.IDENTIFIER && this.token.getType() != Type.FLOAT
		&& this.token.getType() != Type.INT && this.token.getType() != Type.CHAR) {
			throw new ParserException("Escreve a funcao aritimetica certo!");
		}
		
	}

	private void comment() {
		if (token.getType() != Type.COMMENT || token.getType() != Type.COMMENT_BLOCK) {
			throw new ParserException("Faz um comentario certo tabacudo!");
		}
	}

	private void variableDeclaration() {
		this.type();
		this.token = this.lexical.nextToken();
		if (token.getType() != Type.IDENTIFIER) {
			throw new ParserException("A declaração de variável tá errada!");
		}
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals(";")) {
			throw new ParserException("Faltou o ; da variável, donzelo!");
		}
	}

	private void type(){
		if (!(token.getContent().equals("int") || token.getContent().equals("float")
		|| token.getContent().equals("char") || token.getContent().equals("boolean"))) {
			throw new ParserException("Deu erro na declaração de variável boy");
		}
	}
}
