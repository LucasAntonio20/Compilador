package semanticanalysis;

import lexicalanalysis.Token;
import lexicalanalysis.Type;

public class Semantic {
    
    private String type;
    private String content;

    public Semantic(String type, String content){
        this.type = type;
        this.content = content;

    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public static Type verifyCalculation(Token[] tokens){
        if (tokens[0] != null) {
            int j = 0;
            Token[] auxList = tokens;
            for (int i = 0; i < auxList.length; i++) {
                if (tokens[i] == null) break;
                if (!tokens[i].getType().equals(auxList[j].getType())) {
                    throw new SemanticException("Não pode calcular "+ auxList[j].getType() +" com "+tokens[i].getType());
                }
            }
            return tokens[0].getType();
        }
        return null;
    }

    public static void verifyAllocation(Semantic semantic, Type type){
        if(semantic.getType().equals("char")){
            if (type != Type.CHAR) {
                throw new SemanticException("Meu irmão á variavel \""+ semantic.getContent() +"\" é do tipo char!");
            }
        }else if(semantic.getType().equals("boolean")){
            if (type != Type.BOOLEAN) {
                throw new SemanticException("Meu irmão á variavel \""+ semantic.getContent() +"\" é do tipo boolean!");
            }
        }else if(semantic.getType().equals("int")){
            if (type != Type.INT) {
                throw new SemanticException("Meu irmão á variavel \""+ semantic.getContent() +"\" é do tipo int!");
            }
        }else if(semantic.getType().equals("float")){
            if (type != Type.FLOAT) {
                throw new SemanticException("Meu irmão á variavel \""+ semantic.getContent() +"\" é do tipo float!");
            }
        }
    }

    public static Semantic exists(Semantic[] list, String variable){
        int i = 0;
            while(i < 1000){
                if (list[i] == null)
                return null;
                if(list[i].getContent().compareTo(variable) == 0){
                    return list[i];
                }
                i++;
            }
        return null;
    }
}
