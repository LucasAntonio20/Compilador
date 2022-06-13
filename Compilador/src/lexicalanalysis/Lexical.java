package lexicalanalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import syntacticanalyzer.Parser;

public class Lexical {
    
	private char[] content;
	private int index;

	public Lexical(String path){
		try {
            String conteudoStr = new String(Files.readAllBytes(Paths.get(path)));
            this.content = conteudoStr.toCharArray();
			this.index = 0;
			Parser syntactic = new Parser(this);
			syntactic.program();
        } catch (IOException e) {
            System.out.println("Error: Arquivo " + e.getMessage() + " não encontrado");
        }
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

	public Token nextToken() {
		Token token = null;
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
					state = 2;
				} else if (c == '\'') {
					str.append(c);
					state = 5;
				} else if (c == '(' || c == ')' || c == '{' || c == '}' || c == ',' || c == ';') {
					str.append(c);
					state = 8;
				} else if (c == '+' || c == '-' || c == '*' || c == '/') {
					str.append(c);
					state = 9;
				} else if (c == '>' || c == '<' || c == '!') {
					str.append(c);
					state = 10;
				} else if (c == '=') {
					str.append(c);
					state = 12;
				} else if (c == '\\') {
					str.append(c);
					state = 13;
				} else if (c == '$') {
					str.append(c);
					state = 777;
					this.backChar();
				} else {
					str.append(c);
					throw new LexicalException("Que ideia é essa? \"" + str.toString() + "\"");
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
						return new Token(str.toString(), Type.RESERVED_WORD);
					} else if(str.toString().compareTo("true") == 0|| str.toString().compareTo("false") == 0){
						this.backChar();
						return new Token(str.toString(), Type.BOOLEAN);
					}else {
						this.backChar();
						return new Token(str.toString(), Type.IDENTIFIER);
					}
				}
				break;
			case 2:
				if (this.isDigit(c)) {
					str.append(c);
					state = 2;
				} else if (c == '.') {
					str.append(c);
					state = 3;
				} else {
					this.backChar();
					return new Token(str.toString(), Type.INT);
				}
				break;
			case 3:
				if (this.isDigit(c)) {
					str.append(c);
					state = 4;
				} else {
					throw new LexicalException("Ei boy, esse float \"" + str.toString() + "\" ta embaçado");
				}
				break;
			case 4:
				if (this.isDigit(c)) {
					str.append(c);
					state = 4;
				} else {
					this.backChar();
					return new Token(str.toString(), Type.FLOAT);
				}
				break;
			case 5:
				if (this.isDigit(c) || this.isLetter(c)) {
					str.append(c);
					state = 6;
				} else {
					throw new LexicalException("Ei boy, esse char \"" + str.toString() + "\" ta inválido, tá ligado?!");
				}
				break;
			case 6:
				if (c == '\'') {
					str.append(c);
					state = 7;
				} else {
					throw new LexicalException("Ei boy, esse char \"" + str.toString() + "\" é inválido, tá ligado?!");
				}
				break;
			case 7:
				this.backChar();
				return new Token(str.toString(), Type.CHAR);
			case 8:
				this.backChar();
				return new Token(str.toString(), Type.SPECIAL_CHARACTER);
			case 9:
				this.backChar();
				return new Token(str.toString(), Type.ARITHMETIC_OPERATOR);
			case 10:
				if (c == '=') {
					str.append(c);
					state = 11;
				} else {
					if (str.toString().compareTo("!") == 0) {
						this.backChar();
						return new Token(str.toString(), Type.CONDITION_INVERTER);
					} else {
						this.backChar();
						return new Token(str.toString(), Type.RELATIONAL_OPERATOR);
					}
				}
				break;
			case 11:
				this.backChar();
				return new Token(str.toString(), Type.RELATIONAL_OPERATOR);
			case 12:
				if (c == '=') {
					str.append(c);
					state = 11;
				} else {
					this.backChar();
					return new Token(str.toString(), Type.ASSIGNMENT_OPERATOR);
				}
				break;
			case 13:
				if (c == '\\') {
					str.append(c);
					state = 14;
				} else if (c == '*') {
					str.append(c);
					state = 15;
				} else {
					throw new LexicalException("Se isso \"" + str.toString() + "\" foi uma tentativa de comentario, ta tudo errado");
				}
				break;
			case 14:
				if (c == '\r' || c == '$') {
					this.backChar();
					return new Token(str.toString(), Type.COMMENT);
				} else {
					str.append(c);
					state = 14;
				}
				break;
			case 15:
				if (c == '*') {
					str.append(c);
					state = 16;
				} else if(c == '$'){
					throw new LexicalException("Se isso \"" + str.toString() + "\" foi uma tentativa de comentario, ta tudo errado");
				} else {
					str.append(c);
					state = 15;
				}
				break;
			case 16:
				if (c == '\\') {
					str.append(c);
					state = 17;
				}else {
					throw new LexicalException("Se isso \"" + str.toString() + "\" foi uma tentativa de comentario, ta tudo errado");
				}
				break;
			case 17:
				this.backChar();
				return new Token(str.toString(), Type.COMMENT_BLOCK);
			case 777:
				return new Token(str.toString(), Type.END_CODE);
			default:
				break;
			}
		}
return token;
}

	public void printTokens() {
		Token t = null;
        while((t = this.nextToken()) != null){
            System.out.println(t.toString());
        }
	}
}