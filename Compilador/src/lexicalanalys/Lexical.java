package lexicalanalys;

import java.util.ArrayList;

public class Lexical {
    
    ArrayList<Token> tokens = new ArrayList<>();

	private char[] content;
	private int index;

	public Lexical(char[] content) {
		this.content = content;
		this.index = 0;
		this.analyzing();
	}

	public boolean isLetter(char c) {
		return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
	}

	public boolean isDigit(char c) {
		return (c >= '0') && (c <= '9');
	}

	private void backChar() {
		this.index--;
	}

	private char nextChar() {
		return this.content[this.index++];
	}

	private boolean hasNextChar() {
		return index < this.content.length;
	}

	public void analyzing() {
		char c;
		int state = 0;

		StringBuffer str = new StringBuffer();
		while (hasNextChar()) {
			c = nextChar();
			switch (state) {
			case 0:
				if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
					state = 0;
				} else if (this.isLetter(c) || c == '_') {
					str.append(c);
					state = 1;
				} else if (this.isDigit(c)) {
					str.append(c);
					state = 3;
				} else if (c == '\'') {
					str.append(c);
					state = 6;
				} else if (c == '(' || c == ')' || c == '{' || c == '}' || c == ',' || c == ';') {
					str.append(c);
					state = 9;
				} else if (c == '+' || c == '-' || c == '*' || c == '/') {
					str.append(c);
					state = 10;
				} else if (c == '>' || c == '<' || c == '!') {
					str.append(c);
					state = 11;
				} else if (c == '=') {
					str.append(c);
					state = 13;
				} else if (c == '\\') {
					str.append(c);
					state = 14;
				} else if (c == '$') {
					state = 777;
				} else {
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				}
				break;
			case 1:
				if (this.isDigit(c) || this.isLetter(c) || c == '_') {
					str.append(c);
					state = 1;
				} else {
					if (str.toString().compareTo("main") == 0 || str.toString().compareTo("if") == 0
							|| str.toString().compareTo("else") == 0 || str.toString().compareTo("while") == 0
							|| str.toString().compareTo("do") == 0 || str.toString().compareTo("for") == 0
							|| str.toString().compareTo("int") == 0 || str.toString().compareTo("float") == 0
							|| str.toString().compareTo("char") == 0 || str.toString().compareTo("boolean") == 0) {
						this.backChar();
						tokens.add(new Token(str.toString(), Type.RESERVED_WORD));
					} else if(str.toString().compareTo("true") == 0|| str.toString().compareTo("false") == 0){
						this.backChar();
						tokens.add(new Token(str.toString(), Type.BOOLEAN));
					}else {
						this.backChar();
						tokens.add(new Token(str.toString(), Type.IDENTIFIER));
					}
					str = new StringBuffer();
					state = 0;
				}
				break;
			case 3:
				if (this.isDigit(c)) {
					str.append(c);
					state = 3;
				} else if (c == '.') {
					str.append(c);
					state = 4;
				} else {
					this.backChar();
					tokens.add(new Token(str.toString(), Type.INT));
					str = new StringBuffer();
					state = 0;
				}
				break;
			case 4:
				if (this.isDigit(c)) {
					str.append(c);
					state = 5;
				} else {
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				}
				break;
			case 5:
				if (this.isDigit(c)) {
					str.append(c);
					state = 5;
				} else {
					this.backChar();
					tokens.add(new Token(str.toString(), Type.FLOAT));
					str = new StringBuffer();
					state = 0;
				}
				break;
			case 6:
				if (this.isDigit(c) || this.isLetter(c)) {
					str.append(c);
					state = 7;
				} else {
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				}
				break;
			case 7:
				if (c == '\'') {
					str.append(c);
					state = 8;
				} else {
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				}
				break;
			case 8:
				this.backChar();
				tokens.add(new Token(str.toString(), Type.CHAR));
				str = new StringBuffer();
				state = 0;
				break;
			case 9:
				this.backChar();
				tokens.add(new Token(str.toString(), Type.SPECIAL_CHARACTER));
				str = new StringBuffer();
				state = 0;
				break;
			case 10:
				this.backChar();
				tokens.add(new Token(str.toString(), Type.ARITHMETIC_OPERATOR));
				str = new StringBuffer();
				state = 0;
				break;
			case 11:
				if (c == '=') {
					str.append(c);
					state = 12;
				} else {
					if (str.toString().compareTo("!") == 0) {
						this.backChar();
						tokens.add(new Token(str.toString(), Type.CONDITION_INVERTER));
						str = new StringBuffer();
						state = 0;
					} else {
						this.backChar();
						tokens.add(new Token(str.toString(), Type.RELATIONAL_OPERATOR));
						str = new StringBuffer();
						state = 0;
					}
				}
				break;
			case 12:
				this.backChar();
				tokens.add(new Token(str.toString(), Type.RELATIONAL_OPERATOR));
				str = new StringBuffer();
				state = 0;
				break;
			case 13:
				if (c == '=') {
					str.append(c);
					state = 12;
				} else {
					this.backChar();
					tokens.add(new Token(str.toString(), Type.ASSIGNMENT_OPERATOR));
					str = new StringBuffer();
					state = 0;
				}
				break;
			case 14:
				if (c == '\\') {
					str.append(c);
					state = 15;
				} else if (c == '*') {
					str.append(c);
					state = 16;
				} else {
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				}
				break;
			case 15:
				if (c == '\r' || c == '$') {
					this.backChar();
					tokens.add(new Token(str.toString(), Type.COMMENT));
					str = new StringBuffer();
					state = 0;
				} else {
					str.append(c);
					state = 15;
				}
				break;
			case 16:
				if (c == '*') {
					str.append(c);
					state = 17;
				} else if(c == '$'){
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				} else {
					str.append(c);
					state = 16;
				}
				break;
			case 17:
				if (c == '\\') {
					str.append(c);
					state = 18;
				}else {
					throw new RuntimeException("Error: invalid term \"" + str.toString() + "\"");
				}
				break;
			case 18:
				this.backChar();
				tokens.add(new Token(str.toString(), Type.COMMENT_BLOCK));
				str = new StringBuffer();
				state = 0;
				break;
			case 777:
				tokens.add(new Token(str.toString(), Type.END_CODE));
				break;
			default:
				break;
			}
		}

	}

	public void printTokens() {
		for (Token t : tokens) {
			System.out.println(t);
		}
	}
}