package lexicalanalys;

public enum Type {

	RESERVED_WORD("Palavra Reservada"),
	ARITHMETIC_OPERATOR("Operador Aritimetico"),
	ASSIGNMENT_OPERATOR("Operador de Atribuição"),
	RELATIONAL_OPERATOR("Operador Relacional"),
	SPECIAL_CHARACTER("Caracter Especial"),
	CHAR("Char"),
	FLOAT("Float"),
	INT("Inteiro"),
	BOOLEAN("Boolean"),
	END_CODE("Fim do código"),
	IDENTIFIER("Identificador"),
	CONDITION_INVERTER("Inversor de condição"),
	COMMENT("Comentário"),
	COMMENT_BLOCK("Comentário em bloco");

	private String type;
	
	Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}