import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
//
//        HashSymbolTable hashSymbolTable=new HashSymbolTable(37);
//        hashSymbolTable.insertSymbol("a");
//        hashSymbolTable.insertSymbol("baba");
//        hashSymbolTable.insertSymbol("abba");
//        hashSymbolTable.insertSymbol("Andreea");
//        hashSymbolTable.insertSymbol("Korodi");
//        hashSymbolTable.insertSymbol("agree");
//        hashSymbolTable.insertSymbol("eager");
//        hashSymbolTable.insertSymbol("121");
//        hashSymbolTable.insertSymbol("211");
//        hashSymbolTable.insertSymbol("adverb");
//        hashSymbolTable.insertSymbol("braved");
//        hashSymbolTable.insertSymbol("braved");
//
//        System.out.println(hashSymbolTable.printTable());
//        System.out.println( hashSymbolTable.findBySymbol("braved").toString());
//        System.out.println( hashSymbolTable.findBySymbol("adverb").toString());
//        System.out.println(hashSymbolTable.findBySymbol("kk").toString());

        List<String> identifiersandC=new ArrayList<>();
        HashSymbolTable symbolTable=new HashSymbolTable(131);

        List<String> dataList=readFromFile("C:\\babes\\LFTC\\lab2FLCD\\FLCD\\p1.txt");
        List<String> tokenss=readFromFile("C:\\babes\\LFTC\\lab2FLCD\\FLCD\\tokne.in");

        String[] operators=new String[0];
        String[] separators = new String[0];
        String[] reservedWords = new String[0];
        for(String tok: tokenss){
            String[] sep=tok.split("@");
            if(sep[0].equals("Operators")) {
                operators = sep[1].split(";");

            }
            else if(sep[0].equals("Separators")){

                separators=sep[1].split(";");
            }
            else{
                reservedWords=sep[1].split(";");
            }

        }

        HashMap<String,Integer> piftable=new HashMap<>();

        //String split="\\+|=|\\*|\\?| ";
        List<String> separatorList=new ArrayList<>();
        separatorList.addAll(Arrays.asList(separators));
        List<String> operatorList=new ArrayList<>();
        operatorList.addAll(Arrays.asList(operators));
        List<String> reservedWordsList=new ArrayList<>();
        reservedWordsList.addAll(Arrays.asList(reservedWords));

        List<Pair<String,Pair<Integer,Integer>>> pifTable=new ArrayList<>();





        for(String line:dataList){
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(line);
            while (m.find()) {
                String quote = "\""+ m.group(1)+"\"";
                symbolTable.insertSymbol(quote);
                line=line.replace(quote,"");
            }
            ArrayList<String> linesplit=tokenize(line);
            for(String word: linesplit) {
                if(!(reservedWordsList.contains(word) || operatorList.contains(word) || separatorList.contains(word))){
                    symbolTable.insertSymbol(word);
                }
            }
        }


        int lineC=0;
        for (String line: dataList) {
            ArrayList<String> linesplit=tokenize(line);
            for(String word: linesplit){
                if(reservedWordsList.contains(word) || operatorList.contains(word) || separatorList.contains(word)){
                    if(!(word.equals(" ") || word.equals("")) ) {
                        Pair<Integer,Integer> p1=new Pair<>(-1,-1);
                        Pair<String, Pair<Integer,Integer>> p = new Pair<>(word,p1);
                        pifTable.add(p);
                    }


                }
                else {
                    if(symbolTable.exists(word)){
                        List<Integer> positions=symbolTable.findBySymbol(word);
                        Pair<Integer,Integer> p1=new Pair<>(positions.get(0),positions.get(1));
                        if((checkIfLexicallyCorrect(word) && checkIfNumber(word)) ) {
                            Pair<String, Pair<Integer, Integer>> p = new Pair<>("const",p1);
                            pifTable.add(p);
                        }
                        else if(checkIfLexicallyCorrect(word)){
                            Pair<String, Pair<Integer, Integer>> p = new Pair<>("id",p1);
                            pifTable.add(p);
                        }
                        else if(!checkIfLexicallyCorrect(word)){
                            System.out.println("error at line"+ lineC+"at the word"+ word);
                        }
                    }
                }

            }
            lineC+=1;


        }
        System.out.println(symbolTable.printTable());
        System.out.println(pifTable);


//        for(String dt: dataList) {
//
//            String[] tokens = dt.split(",|\\\\| |");
//
//
//            for (String token : tokens)
//            {
//                System.out.println(token);
//            }
//        }

//        createAndWrite("write.txt",dataList);
    }


    public static boolean checkIfNumber(String val){
        try {
            int d = Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean checkIfLexicallyCorrect(String val){
        if(!val.equals(""))
            if(val.charAt(0)>=48 && val.charAt(0)<=57){
                for(int i=1;i<val.length()-1;i++){
                    if(val.charAt(i)<48 || val.charAt(i)>57){
                        return false;
                    }
                }
            }
        return true;
    }

    public static ArrayList<String> tokenize(String x){
        ArrayList<String> all_tokens = new ArrayList<String>();
        char[] chaes = x.toCharArray();
        int where =0;
        String[] words = x.split("[+\\- /,{}()\\[\\]\\\\%*]");
        for(String s : words){
            where+=s.length();
            all_tokens.add(s);
            try {
                String n = String.valueOf(chaes[where]);
                all_tokens.add(n);
            }catch(Exception e){
                System.out.println(e);
            }
            where++;
        }

        return all_tokens;
    }

    private static void createAndWrite(String filename,List<String> data){
        try {
            FileWriter myWriter = new FileWriter(filename,true);
            for(String d: data) {
                myWriter.write(d);
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static List<String> readFromFile(String filename){
        List<String> dataList=new ArrayList<>();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                if(!(data.charAt(0)=='~' && data.charAt(1)=='~')) {
                    dataList.add(data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dataList;
    }
}
