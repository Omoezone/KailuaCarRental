package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet; //Interface
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.*;

public class Menu {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; // info: https://en.wikipedia.org/wiki/JDBC_driver
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/kailua";
    static Connection con;

    public static void interactionMenu()throws SQLException{
        Scanner console = new Scanner(System.in);
         try {
             con = null;
             Statement s = null;
             Class.forName(JDBC_DRIVER);
             con = DriverManager.getConnection(DATABASE_URL, "root", "Omoezone12");
             s = con.createStatement();


             //loops while user inputs y
             boolean mainMenu = true;
             while (mainMenu == true) {
                 //Choose which object to create 1,2 or 3
                 System.out.println("Press 1 to create new DB entry \npress 2 to change an existing entry \npress 3 to remove an existing entry \npress 4 to print list of entries");
                 int choice = console.nextInt();
                 switch (choice) {
                     case 1:
                         System.out.println("Press 1 for new customer \nPress 2 for new contract \nPress 3 for new car \nPress 4 for new city");
                         int choiceCreate = console.nextInt();
                         ArrayList<String> objectCreation = new ArrayList<>();
                         switch (choiceCreate) {
                             case 1:

                                 String[] customerPrompts = {"Input the following information: ","First name?", "Last name?", "Address?", "license Number", "Mobile number?", "Phone?", "Email?", "When did the driver start driving?","zip code","City"};
                                 for (int i = 1; i < 11; i++) {
                                     System.out.println(customerPrompts[0]);
                                     System.out.println(customerPrompts[i]);
                                     objectCreation.add(console.next());
                                 }

                                 //Creates zips table
                                 s.executeUpdate("INSERT INTO zips VALUES('"+Integer.parseInt(objectCreation.get(8))+"','"+objectCreation.get(9)+"')");
                                 int i = 4;
                                 // Creates customers table data
                                 s.executeUpdate("INSERT INTO customers VALUES ('"+i+"','"+ objectCreation.get(0)+"','"+objectCreation.get(1)+"','"+objectCreation.get(2)+"','"+objectCreation.get(3)+"','"+objectCreation.get(4)+"','"+objectCreation.get(5)+"','"+objectCreation.get(6)+"','"+objectCreation.get(7)+"','"+Integer.parseInt(objectCreation.get(8))+"')");

                                 break;
                             case 2:

                                 break;
                             case 3:

                                 break;
                             case 4:

                                 break;

                         }
                         break;
                     case 2:
                         //changeObject(console, cars, renters, contracts);
                         break;
                     case 3:
                         //removeObject(console, cars, renters, contracts);
                         break;
                     case 4:
                         break;
                 }

                 console.nextLine();
                 System.out.println("Return to main menu? y/n");
                 String answer = console.nextLine();
                 if (answer.equals("n")) {
                     mainMenu = false;
                 }
             }
             /* Errorhandling */
         }
         catch(SQLException sqlex) {
            try{
                System.out.println(sqlex.getMessage());
                con.close();
                System.exit(1);  // terminate program
            }
            catch(SQLException sql){}
        }
        catch (ClassNotFoundException noClass) {
            System.err.println("Driver Class not found");
            System.out.println(noClass.getMessage());
            System.exit(1);  // terminate program
        }
    }
}
