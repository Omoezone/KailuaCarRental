package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    public static void interactionMenu(){
        //loops while user inputs y
        boolean mainMenu = true;
        while(mainMenu == true) {
            //Choose which object to create 1,2 or 3
            System.out.println("Press 1 to create object \npress 2 to change an existing object \npress 3 to remove an existing object \npress 4 to print list of objects");
            int choice = console.nextInt();
            switch(choice){
                case 1:
                    //createObject(console, cars, renters, contracts);
                    break;
                case 2:
                    //changeObject(console, cars, renters, contracts);
                    break;
                case 3:
                    //removeObject(console, cars, renters, contracts);
                    break;
                case 4:
                    //printObjects(cars, renters, contracts);
                    break;
            }

            console.nextLine();
            System.out.println("Return to main menu? y/n");
            String answer = console.nextLine();
            if(answer.equals("n")) {
                mainMenu = false;
            }
        }
    }
}
