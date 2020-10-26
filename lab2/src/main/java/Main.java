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


        HashSymbolTable symbolTable = new HashSymbolTable(131);

        List<String> dataList = readFromFile("C:\\babes\\LFTC\\lab2FLCD\\FLCD\\p1err.txt");
        List<String> tokenss = readFromFile("C:\\babes\\LFTC\\lab2FLCD\\FLCD\\tokne.in");

        String[] operators = new String[0];
        String[] separators = new String[0];
        String[] reservedWords = new String[0];
        for (String tok : tokenss) {
            String[] sep = tok.split("@");
            if (sep[0].equals("Operators")) {
                operators = sep[1].split(";");

            } else if (sep[0].equals("Separators")) {

                separators = sep[1].split(";");
            } else {
                reservedWords = sep[1].split(";");
            }

        }

        List<String> separatorList = new ArrayList<>();
        separatorList.addAll(Arrays.asList(separators));
        List<String> operatorList = new ArrayList<>();
        operatorList.addAll(Arrays.asList(operators));
        List<String> reservedWordsList = new ArrayList<>();
        reservedWordsList.addAll(Arrays.asList(reservedWords));

        List<Pair<String, Pair<Integer, Integer>>> pifTable = new ArrayList<>();
        List<String> errors=new ArrayList<>();


        addInST(symbolTable, dataList, separatorList, operatorList, reservedWordsList);


        addInPif(symbolTable, dataList, separatorList, operatorList, reservedWordsList, pifTable,errors);


        System.out.println(symbolTable.printTable());
        System.out.println(pifTable);
        createAndWriteST("st.out",symbolTable);
        createAndWritePif("pif.out",pifTable);
        createAndWriteMessage("message.out",errors);
    }

    private static void addInST(HashSymbolTable symbolTable, List<String> dataList, List<String> separatorList, List<String> operatorList, List<String> reservedWordsList) {
        for (int i=0;i<dataList.size();i++) {
            String line=dataList.get(i);
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(line);
            while (m.find()) {
                String quote = "\"" + m.group(1) + "\"";
                symbolTable.insertSymbol(quote);
                line = line.replace(quote, "");
            }
            ArrayList<String> linesplit = tokenize(line);
            for (String word : linesplit) {

                if (!(reservedWordsList.contains(word) || operatorList.contains(word) || separatorList.contains(word) || !checkIfLexicallyCorrect(word) || word.contains("\""))) {
                    symbolTable.insertSymbol(word);
                }
            }
        }
    }

    private static void addInPif(HashSymbolTable symbolTable, List<String> dataList, List<String> separatorList, List<String> operatorList, List<String> reservedWordsList, List<Pair<String, Pair<Integer, Integer>>> pifTable,List<String> errors) {
        int lineC = 0;
        for (String line : dataList) {
            ArrayList<String> linesplit = tokenize(line);
            for (int i = 0; i <= linesplit.size() - 1; i++) {
                String word = linesplit.get(i);
                if (reservedWordsList.contains(word) || operatorList.contains(word) || separatorList.contains(word)) {
                    if (!(word.equals(" ") || word.equals(""))) {
                        Pair<Integer, Integer> p1 = new Pair<>(-1, -1);
                        Pair<String, Pair<Integer, Integer>> p = new Pair<>(word, p1);
                        pifTable.add(p);
                    }


                } else {
                    if (symbolTable.exists(word)) {
                        List<Integer> positions = symbolTable.findBySymbol(word);
                        Pair<Integer, Integer> p1 = new Pair<>(positions.get(0), positions.get(1));
                        if (!word.isEmpty()) {
                            if ((checkIfLexicallyCorrect(word) && checkIfNumber(word))) {
                                Pair<String, Pair<Integer, Integer>> p = new Pair<>("const", p1);
                                pifTable.add(p);
                            } else if (checkIfLexicallyCorrect(word)) {
                                Pair<String, Pair<Integer, Integer>> p = new Pair<>("id", p1);
                                pifTable.add(p);
                            } else if (!checkIfLexicallyCorrect(word)) {
                                String error="error at line " + lineC + " at the word " + word;
                                errors.add(error);
                            }
                        }
                    } else if (checkIfLexicallyCorrect(word) && word.charAt(0) == '"') {
                        String constant = word;
//                        i++;
//                        word=linesplit.get(i);
                        while (word.charAt(word.length() - 1) != '"') {
                            i++;
                            word = linesplit.get(i);
                            constant = constant.concat(word);
                            if(word.contains("\"") && word.charAt(word.length() - 1) != '"'){
                                String error="error at line " + lineC + " at the word " + word;
                                errors.add(error);
                                break;
                            }
                            if(i>=linesplit.size()-1){
                                String error="error at line " + lineC + " because you didn't close the string" ;
                                errors.add(error);
                                break;
                            }




                        }
                        List<Integer> positions = symbolTable.findBySymbol(constant);
                        Pair<Integer, Integer> p1 = new Pair<>(positions.get(0), positions.get(1));
                        Pair<String, Pair<Integer, Integer>> p = new Pair<>("const", p1);
                        pifTable.add(p);
                    } else if (!checkIfLexicallyCorrect(word)) {
                        String error="error at line " + lineC + " at the word " + word;
                        errors.add(error);
                    }
                }

            }
            lineC += 1;


        }
    }


    public static boolean checkIfNumber(String val) {
        try {
            int d = Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean checkIfLexicallyCorrect(String val) {
        if (!val.equals("")) {
            if (Character.isDigit(val.charAt(0))) {
                for (int i = 1; i < val.length(); i++) {
                    if (Character.isLetter(val.charAt(i))) {
                        return false;
                    }
                }
            } else {
                for (int i = 0; i < val.length(); i++) {
                    if (!(Character.isLetter(val.charAt(i)) || Character.isDigit(val.charAt(i)) || val.charAt(i) == '"')) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<String> tokenize(String x) {
        ArrayList<String> all_tokens = new ArrayList<String>();
        char[] chaes = x.toCharArray();
        int where = 0;
        String[] words = x.split("[+\\- /,{}()\\[\\]\\\\%*]");
        for (String s : words) {
            where += s.length();
            all_tokens.add(s);
            try {
                String n = String.valueOf(chaes[where]);
                all_tokens.add(n);
            } catch (Exception e) {

            }
            where++;
        }

        return all_tokens;
    }

    private static void createAndWritePif(String filename, List<Pair<String, Pair<Integer, Integer>>> data ) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            for ( int i=0;i<data.size();i++) {
                myWriter.write(data.get(i).toString());
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static void createAndWriteMessage(String filename, List<String> data) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            if(data.size()==0){
                myWriter.write("lexically correct");
                myWriter.write("\n");
            }
            else{
                myWriter.write("lexically incorrect");
                myWriter.write("\n");
            }
            for ( int i=0;i<data.size();i++) {
                myWriter.write(data.get(i));
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void createAndWriteST(String filename,  HashSymbolTable data ) {
        try {
            FileWriter myWriter = new FileWriter(filename);

                myWriter.write(data.printTable().toString());
                myWriter.write("\n");

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static List<String> readFromFile(String filename) {
        List<String> dataList = new ArrayList<>();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                if (!(data.charAt(0) == '~' && data.charAt(1) == '~')) {
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
