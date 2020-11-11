package com.company;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FA {
    private Set<String> states=new HashSet<>();
    private Set<String> alphabet=new HashSet<>();
    private String initialState;
    private Set<String> finalStates=new HashSet<>();
    private HashMap<Pair<String,String>,List<String>> transitions=new HashMap<>();

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<String> finalStates) {
        this.finalStates = finalStates;
    }

    public HashMap<Pair<String, String>,List< String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(HashMap<Pair<String, String>, List<String>> transitions) {
        this.transitions = transitions;
    }

    public FA() {
        readFromFile("C:\\babes\\LFTC\\lab2FLCD\\FLCD\\lab4\\src\\com\\company\\FA.in");



    }
    public boolean ifDeterministic(){
        for (Pair<String,String> key: transitions.keySet()) {
            if(transitions.get(key).size()>1){
                return false;
            }
        }

        return true;
    }
    public void validations(){
        boolean finalStateAmongStates=true;
        boolean initialStateAmongStates=true;
        boolean transitionsAmongStates=true;
        for(String finalS: finalStates){
            if(!states.contains(finalS)){
                finalStateAmongStates=false;
            }
        }
        if(!states.contains(initialState)){
            initialStateAmongStates=false;
        }
        for(Pair<String,String> keys: transitions.keySet()){
            for(String val:transitions.get(keys)){
            if(!states.contains(keys.getKey()) || !states.contains(val)){
                transitionsAmongStates=false;
            }
        }}

        if(finalStateAmongStates){
            System.out.println("The final states are among all states");
        }
        else{
            System.out.println("The final states are new states. We don't know them");
        }

        if(initialStateAmongStates){
            System.out.println("The initial state is among all states");
        }
        else{
            System.out.println("The initial state is a new state. We don't know this state");
        }
        if(transitionsAmongStates){
            System.out.println("The states from transition are among all states");
        }
        else{
            System.out.println(
                    "The states from transitions are some new states. We don't know them"
            );
        }


    }

    public boolean isSequenceAccepted(String sequence){
        if(ifDeterministic()) {
            String current = initialState;
            int i = 0;
            boolean found = true;
            while (i < sequence.length() && found) {
                found = false;
                for (Pair<String, String> keys : transitions.keySet()) {
                    if (keys.getKey().equals(current) && keys.getValue().equals(String.valueOf(sequence.charAt(i)))) {
                        current = transitions.get(keys).get(0);
                        found = true;
                    }
                }
                i++;
            }
            return finalStates.contains(current) && i == sequence.length();
        }
        else{
            System.out.println("Is not deterministic");
            return false;
        }



    }



    private void readFromFile(String filename){

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            String[] data = myReader.nextLine().split(" ");
            states.addAll(Arrays.asList(data));
            String[] alp=myReader.nextLine().split(" ");
            alphabet.addAll(Arrays.asList(alp));
            initialState=myReader.nextLine();
            String[] finalS=myReader.nextLine().split(" ");
            finalStates.addAll(Arrays.asList(finalS));
            while (myReader.hasNextLine()){
                String[] trans=myReader.nextLine().split(" ");
                Pair<String,String> pair=new Pair<>(trans[0],trans[1]);
                if(transitions.containsKey(pair)) {
                    transitions.get(pair).add(trans[2]);
                }
                else{
                    List<String> val=new ArrayList<>();
                    val.add(trans[2]);
                    transitions.put(pair,val);
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }
}
