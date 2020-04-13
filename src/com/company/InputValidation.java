package com.company;

import java.util.Scanner;

public class InputValidation {

    public static int intRange(Scanner console, int min, int max) {
        int value = console.nextInt(); //Takes input from user via console
        while(value > max || value < min){ // checks that the input is in the required range. If not it enter loop
            System.out.println("Invalid input, please try again");
            value = console.nextInt();
        }
        return value;
    }

    public static String chooseYesNo(Scanner console) {
        String value = console.next(); //Takes input from user via console
        while(!(value.equalsIgnoreCase("yes")) && !(value.equalsIgnoreCase("no"))){ // checks that the input is in the required range. If not it enter loop
            System.out.println("Invalid input, please try again");
            value = console.next();
        }
        return value;
    }
}
