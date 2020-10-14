public class Main {

    public static void main(String[] args) {

        HashSymbolTable hashSymbolTable=new HashSymbolTable(37);
        hashSymbolTable.insertSymbol("a");
        hashSymbolTable.insertSymbol("baba");
        hashSymbolTable.insertSymbol("abba");
        hashSymbolTable.insertSymbol("Andreea");
        hashSymbolTable.insertSymbol("Korodi");
        hashSymbolTable.insertSymbol("agree");
        hashSymbolTable.insertSymbol("eager");
        hashSymbolTable.insertSymbol("121");
        hashSymbolTable.insertSymbol("211");
        hashSymbolTable.insertSymbol("adverb");
        hashSymbolTable.insertSymbol("braved");

        System.out.println(hashSymbolTable.printTable());
        System.out.println( hashSymbolTable.findBySymbol("braved").toString());
        System.out.println( hashSymbolTable.findBySymbol("adverb").toString());
        System.out.println(hashSymbolTable.findBySymbol("kk").toString());


    }
}
