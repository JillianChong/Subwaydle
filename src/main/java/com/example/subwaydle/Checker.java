package com.example.subwaydle;

import java.util.Scanner;

public class Checker {
    // Correct trains
    private char[] trains;

    private int attempts;

    public Checker(String t1, String t2, String t3) {
        trains = new char[]{t1.charAt(0), t2.charAt(0), t3.charAt(0)};
        attempts = 1;
    }

    private char[] askAnswer(Scanner scan) {
        System.out.println("ATTEMPT " + attempts);
        System.out.print("Enter guess for TRAIN 1: ");
        char guess1 = scan.next().toUpperCase().charAt(0);

        System.out.print("Enter guess for TRAIN 2: ");
        char guess2 = scan.next().toUpperCase().charAt(0);

        System.out.print("Enter guess for TRAIN 3: ");
        char guess3 = scan.next().toUpperCase().charAt(0);

        return new char[]{guess1, guess2, guess3};
    }

    private int checkSingleTrain(int correctTrainIndex, char guess) {
        /*
         * 0: green, correct
         * 1: blue, same routing in that spot
         * 2: yellow, part of the trip, wrong spot
         * 3: gray, not part of the trip
         */

        char[] otherTrains = new char[2];
        char correctTrain = trains[correctTrainIndex];

        int indx = 0;
        for(int i = 0; i < trains.length; i++) {
            if(i != correctTrainIndex) {
                otherTrains[indx] = trains[i];
                indx++;
            }
        }

        boolean temp = false; // FIX WHEN DETERMINED SAME ROUTING

        if(correctTrain == guess) {
            return 0;
        } else if(temp) { // determine same routing
            // TODO: IMPLEMENT SAME ROUTING
            return 1; 
        } else if(otherTrains[0] == guess || otherTrains[1] == guess) {
            return 2;
        } else {
            return 3;
        }
    }

    public int[] checkAnswer(char[] guesses) {
        int[] answers = new int[3];
        for(int i = 0; i < guesses.length; i++) {
            int output = checkSingleTrain(i, guesses[i]);
            answers[i] = output;
        }

        return answers;
    }

    private boolean endGame(int[] outputs) {
        return outputs[0] == 0 && outputs[1] == 0 && outputs[2] == 0;
    }

    public boolean playGame() {
        Scanner scan = new Scanner(System.in);
        char[] guesses = new char[3];
        int[] output = new int[]{-1, -1, -1};

        do{
            if(output[0] != -1) {
                System.out.println("GUESS 1: " + output[0]);
                System.out.println("GUESS 2: " + output[1]);
                System.out.println("GUESS 3: " + output[2]);
            }

            guesses = askAnswer(scan);
            attempts++;
            output = checkAnswer(guesses);
        }while(!endGame(output));

        return true;
    }
}
