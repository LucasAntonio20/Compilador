package semanticanalysis;

import lexicalanalysis.Token;
import lexicalanalysis.Type;

public class Semantic {
    
    private String type;
    private String content;
    private int quantity;

    public Semantic(String type, String content){
        this.type = type;
        this.content = content;
        quantity = 1;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void increase(){
        this.quantity++;
    }

    public static void verifyAllocation(Semantic semantic, Token token){
        if(semantic.getType().equals("char")){
            if (token.getType() != Type.CHAR) {
                throw new SemanticException("Meu irmão á variavel \""+ semantic.getContent() +"\" é do tipo char!");
            }
        }else if(semantic.getType().equals("boolean")){
            if (token.getType() != Type.BOOLEAN) {
                throw new SemanticException("Meu irmão á variavel \""+ semantic.getContent() +"\" é do tipo boolean!");
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
