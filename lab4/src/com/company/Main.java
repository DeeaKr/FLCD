package com.company;

import javafx.util.Pair;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        FA fa=new FA();
        Scanner in = new Scanner( System.in );

        display_menu();
        while(true) {

        switch (in.nextInt()) {
            case 1:
                System.out.println(fa.getStates());
                break;
            case 2:
                System.out.println(fa.getAlphabet());
                break;
            case 3:
                for (Pair<String,String> key: fa.getTransitions().keySet()) {
                    System.out.println(key.getKey()+"->"+key.getValue()+"->"+fa.getTransitions().get(key));
                }

                break;
            case 4:
                System.out.println(fa.getFinalStates());
                break;
            case 5:
                System.out.println(fa.ifDeterministic());
                break;
            case 6:
                System.out.println(fa.getInitialState());
                break;
            case 7:
                System.out.println(fa.isSequenceAccepted("b"));
                break;
            case 8:
                fa.validations();
                break;
            case 0:
                return;
        }
        display_menu();
    }
    }

    private static void display_menu() {
        System.out.println("Choose");
        System.out.println("1.See all the states");
        System.out.println("2.See the alphabet");
        System.out.println("3.All the transitions");
        System.out.println("4.Final states");
        System.out.println("5.Check if deterministic");
        System.out.println("6.Initial State");
        System.out.println("7.Is Sequence Accepted?");
        System.out.println("8.Validate Inputs");
        System.out.println("0.Exit");
    }
}

