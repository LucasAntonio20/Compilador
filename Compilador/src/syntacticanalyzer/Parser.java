package syntacticanalyzer;

import lexicalanalysis.Lexical;
import lexicalanalysis.Token;
import lexicalanalysis.Type;
import semanticanalysis.Semantic;
import semanticanalysis.SemanticException;

public class Parser {
	
	private Lexical lexical;
	private Token token;
	private Semantic[] variableList;
	private Token[] auxListTokens;
	private int auxListTokensSize;
	private int semanticSize;
	
	public Parser(Lexical lexical) {
		this.lexical = lexical;
		this.variableList = new Semantic[999];
		this.auxListTokens = new Token[999];
		this.auxListTokensSize = 0;
		this.semanticSize = 0;
	}

	public void program() {

		this.token = this.lexical.nextToken();
		if (this.token.getType() == Type.COMMENT || this.token.getType() == Type.COMMENT_BLOCK) {
			this.token = this.lexical.nextToken();
		}

		if (!token.getContent().equals("int")) {
			throw new ParserException("Presta atenção no int do começo!");
		}

		token = lexical.nextToken();
		if (!token.getContent().equals("main")) {
			throw new ParserException("Esquecesse do main tabacudo!");
		}

		token = lexical.nextToken();
		if (!token.getContent().equals("(")) {
			throw new SemanticException("Abre o parêntese do main direito boy!");
		}

		token = lexical.nextToken();
		if (!token.getContent().equals(")")) {
			throw new SemanticException("Feche o parêntese do main direito boy!");
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
			throw new SemanticException("Abre a chave, tabacudo!");
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
			throw new SemanticException("Fecha a chave, tabacudo!");
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
			throw new SemanticException("Abre o parêntese do if direito boy!");
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
				if (!token.getContent().equals(")")) {
					throw new SemanticException("Depois de um booleano tem que fechar o parêntese!");
				}
			}else{
				this.relationalExpression();
				Semantic.verifyCalculation(auxListTokens);
				dellList(auxListTokens);
			    auxListTokensSize = 0;
			}

		    if (!token.getContent().equals(")")) {
				throw new SemanticException("Fecha o parêntese do if direito boy!");
			}

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("{")) {
				throw new SemanticException("O { do if tabacudo!");
			}

			this.token = this.lexical.nextToken();
			this.command();

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("}")) {
				throw new SemanticException("O } do if tabacudo!");
			}

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("else")) {
				throw new ParserException("Tu esqueceu o else meu irmão!");
			}

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("{")) {
				throw new SemanticException("O { do else tabacudo!");
			}

			this.token = this.lexical.nextToken();
			this.command();

			this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("}")) {
				throw new SemanticException("O } do else tabacudo!");
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
			throw new SemanticException("Abre o parêntese do while direito boy!");
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
			if (!token.getContent().equals(")")) {
				throw new SemanticException("Depois de um booleano tem que fechar o parêntese!");
			}
		}else{
			this.relationalExpression();
			Semantic.verifyCalculation(auxListTokens);
				dellList(auxListTokens);
			    auxListTokensSize = 0;
		}

		if (!token.getContent().equals(")")) {
			throw new SemanticException("Fecha o parêntese do while direito boy!");
		}

		this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("{")) {
				throw new SemanticException("O { do while tabacudo!");
		}

		this.token = this.lexical.nextToken();
		this.command();

		this.token = this.lexical.nextToken();
			if (!this.token.getContent().equals("}")) {
				throw new SemanticException("O } do while tabacudo!");
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
		Semantic aux = Semantic.exists(variableList, token.getContent());
		if(aux == null){
			throw new SemanticException("A variavel \'"+ this.token.getContent() +"\' não existe tabacudo!");
		}

		this.token = this.lexical.nextToken();
		if (!token.getContent().equals("=")) {
			throw new ParserException("Tá faltando um \"=\" boy!");
		}

		this.token = this.lexical.nextToken();
		String auxTokenName = token.getContent();
		if (aux.getType().equals("boolean") || aux.getType().equals("char")) {
			Semantic.verifyAllocation(aux, token.getType());
			this.token = this.lexical.nextToken();
			if (!token.getContent().equals(";")) {
				throw new ParserException("Depois de um \"" + auxTokenName + "\" tem que ter o ponto e virgula!");
			}
		}else{
			this.arithmeticExpression();
			Type auxType = Semantic.verifyCalculation(auxListTokens);
			if (auxType == Type.INT || auxType == Type.FLOAT) {
				Semantic.verifyAllocation(aux, auxType);
			}else {
				throw new SemanticException(aux.getType() + " Só pode receber " + aux.getType());
			}
			
			if (!token.getContent().equals(";")) {
				throw new ParserException("Faltou o ; da atribuição, donzelo!");
			}
			dellList(auxListTokens);
			auxListTokensSize = 0;
		}
	}

	private void arithmeticExpression() {
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
			throw new SemanticException("Feche o parêntese direito boy!");
		    }
		} else if (this.token.getType() == Type.IDENTIFIER) {
			Semantic aux = Semantic.exists(variableList, token.getContent());
		    if(aux == null){
				throw new SemanticException("A variavel não existe tabacudo!");
		    } 
		}else if (this.token.getType() != Type.IDENTIFIER && this.token.getType() != Type.FLOAT
		&& this.token.getType() != Type.INT && this.token.getType() != Type.CHAR) {
			throw new ParserException("Escreve a funcao aritimetica certo!");
		}else if (this.token.getType() == Type.FLOAT || this.token.getType() == Type.INT){
			auxListTokens[auxListTokensSize] = this.token;
			auxListTokensSize++;
		}
		
	}

	private void variableDeclaration() {
		this.type();
		Token auxToken = token;
		this.token = this.lexical.nextToken();
		if (token.getType() != Type.IDENTIFIER) {
			throw new ParserException("A declaração de variável tá errada!");
		}
		Semantic aux = Semantic.exists(variableList, token.getContent());
		if(aux != null){
			throw new SemanticException("E tu quer ter 2 variaveis iguais é bonitinho!");
		}
		variableList[semanticSize] = new Semantic(auxToken.getContent(), token.getContent());
		semanticSize++;
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

	public void dellList(Token[] list){
		int i = 0;
		while (list[i] != null) {
			list[i] = null;
			i++;
		}
	}
}