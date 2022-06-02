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
		token = lexical.nextToken();
		if (!token.getContent().equals("int")) {
			throw new ParserException("Presta atenção no int do começo!");
		}

		token = lexical.nextToken();
		if (!token.getContent().equals("main")) {
			throw new ParserException("Esquecesse do main tabacudo!");
		}

		token = lexical.nextToken();
		if (!token.getContent().equals("(")) {
			throw new ParserException("Abre o parêntese do main direito boy!");
		}

		token = lexical.nextToken();
		if (!token.getContent().equals(")")) {
			throw new ParserException("Feche o parêntese do main direito boy!");
		}

		this.token = this.lexical.nextToken();
		this.block();

		this.token = this.lexical.nextToken();
		if (this.token.getType() == Type.END_CODE) {
			System.out.println("Ei boy, isso ta lindo boy");
		}else {
			throw new ParserException("Tá errado no fim do programa boy!");
		}
	}

	private void block() {
		if (!token.getContent().equals("{")) {
			throw new ParserException("Abre a chave, tabacudo!");
		}

		this.token = this.lexical.nextToken();
		do {
			this.variableDeclaration();
			this.token = this.lexical.nextToken();
		} while (isType());
		do {
			this.command();
			this.token = this.lexical.nextToken();
		} while (token.getContent().equals("if") || token.getContent().equals("while")
		         || isType() || token.getContent().equals("{"));
		

		if (!token.getContent().equals("}")) {
			throw new ParserException("Fecha a chave, tabacudo!");
		}
	}

	private void command() {
		if(this.token.getContent().equals("{") || this.token.getType() == Type.IDENTIFIER){
			this.basicCommand();
		}else if (this.token.getContent().equals("while")){
			this.iteration();
		}else if (this.token.getContent().equals("if")) {
			this.token = this.lexical.nextToken();
		    if (!token.getContent().equals("(")) {
			throw new ParserException("Abre o parêntese do if direito boy!");
		    }

			this.token = this.lexical.nextToken();
		    if (token.getType() == Type.CONDITION_INVERTER) {
				this.token = this.lexical.nextToken();
			    if (token.getType() != Type.IDENTIFIER) {
					throw new ParserException("Tem que ter uma variavel depois do inversor do if, abestado!");
				}
			this.token = this.lexical.nextToken();
		    }else if(token.getType() == Type.BOOLEAN){
				this.token = this.lexical.nextToken();
			}else{
				this.relationalExpression();
			}

		    if (!token.getContent().equals(")")) {
			throw new ParserException("Fecha o parêntese do if direito boy!");
		    }

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("{")) {
				throw new ParserException("O { do if tabacudo!");
			}

			this.token = this.lexical.nextToken();
			this.command();

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("}")) {
				throw new ParserException("O } do if tabacudo!");
			}

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("else")) {
				throw new ParserException("Tu esqueceu o else meu irmão!");
			}

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("{")) {
				throw new ParserException("O { do else tabacudo!");
			}

			this.token = this.lexical.nextToken();
			this.command();

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("}")) {
				throw new ParserException("O } do else tabacudo!");
			}
		}else{
			throw new ParserException("Que comando é esse boy?");
		}
		
	}

	private void iteration() {
		if (!this.token.getContent().equals("while")){
			throw new ParserException("Barril, tá faltando o while!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("(")) {
			throw new ParserException("Abre o parêntese do while direito boy!");
		}

		this.token = this.lexical.nextToken();
		if (token.getType() == Type.CONDITION_INVERTER) {
			this.token = this.lexical.nextToken();
			if (token.getType() != Type.IDENTIFIER) {
				throw new ParserException("Tem que ter uma variavel depois do inversor do while, abestado!");
			}
			this.token = this.lexical.nextToken();
		}else if(token.getType() == Type.BOOLEAN){
			this.token = this.lexical.nextToken();
		}else{
			this.relationalExpression();
		}

		if (!token.getContent().equals(")")) {
			throw new ParserException("Fecha o parêntese do while direito boy!");
		}

		this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("{")) {
				throw new ParserException("O { do while tabacudo!");
		}

		this.token = this.lexical.nextToken();
		this.command();

		this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("}")) {
				throw new ParserException("O } do while tabacudo!");
		}
	}

	private void relationalExpression() {
		this.arithmeticExpression();

		if (token.getType() != Type.RELATIONAL_OPERATOR) {
			throw new ParserException("Operador relacional errado!");
		}

		this.token = this.lexical.nextToken();
		this.arithmeticExpression();
	}

	private void basicCommand() {
		if (token.getContent().equals("{")) {
			this.block();
		}else if(token.getType() == Type.IDENTIFIER){
			this.allocation();
		}else{
			throw new ParserException("Ei tabacudo tá faltando uma atribuição ou um abertura de bloco!");
		}
	}

	private void allocation() {
		if (token.getType() != Type.IDENTIFIER) {
			throw new ParserException("A declaração de variável tá errada!");
		}
		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("=")) {
			throw new ParserException("Tá faltando um \"=\" boy!");
		}

		this.token = this.lexical.nextToken();
		this.arithmeticExpression();


		if (!token.getContent().equals(";")) {
			throw new ParserException("Faltou o ; da atribuição, donzelo!");
		}
	}

	private void arithmeticExpression() {
		if (this.token.getType() == Type.BOOLEAN) {
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
			this.token = this.lexical.nextToken();
		    this.arithmeticExpression();

		    if (!token.getContent().equals(")")) {
			throw new ParserException("Feche o parêntese direito boy!");
		    }
		} else if (this.token.getType() != Type.IDENTIFIER && this.token.getType() != Type.FLOAT
		&& this.token.getType() != Type.INT && this.token.getType() != Type.CHAR) {
			throw new ParserException("Escreve a funcao aritimetica certo!");
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
		if (!isType()) {
			throw new ParserException("Deu erro na declaração de variável boy");
		}
	}

	private boolean isType(){
		if (this.token.getContent().equals("int") || this.token.getContent().equals("float")
		|| this.token.getContent().equals("char") || this.token.getContent().equals("boolean"))
		return true;
		return false;
	}
}