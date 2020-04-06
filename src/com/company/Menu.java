package com.company;

import java.sql.*;
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
             Statement s2 = null;
             Class.forName(JDBC_DRIVER);
             con = DriverManager.getConnection(DATABASE_URL, "root", "hoPPElort97!#");
             s = con.createStatement();

             //ArrayLists til at indeholde midlertidlig data
             ArrayList<String> cusList = new ArrayList<>();
             ArrayList<String> carList = new ArrayList<>();
             ArrayList<String> conList = new ArrayList<>();
             ArrayList<String> zipList = new ArrayList<>();
             //loops while user inputs y
             boolean mainMenu = true;
             while (mainMenu) {
                 //Choose which object to create 1,2 or 3
                 System.out.println("Press 1 to create new DB entry \npress 2 to change an existing entry \npress 3 to remove an existing entry \npress 4 to print list of entries");
                 int choice = console.nextInt();
                 switch (choice) {
                     case 1:
                         System.out.println("Press 1 for new customer \nPress 2 for new contract \nPress 3 for new car \nPress 4 for new city");
                         int choiceCreate = console.nextInt();
                         // ArrayList<String> objectCreation = new ArrayList<>(); //Temp ArrayList der indeholder de informationer vi bruger til at sende excecuteUpdate statements til DB
                         switch (choiceCreate) {
                             case 1:
                                 String[] customerPrompts = {"Input the following information: ","First name?", "Last name?", "Address?", "license Number", "Mobile number?", "Phone?", "Email?", "When did the driver start driving?","zip code","City"};
                                 for (int i = 1; i < 11; i++) {
                                     System.out.println(customerPrompts[0]);
                                     System.out.println(customerPrompts[i]);
                                     cusList.add(console.next());
                                 }
                                 //Creates zips rows
                                 try {
                                     s.executeUpdate("INSERT INTO zips VALUES('" + Integer.parseInt(cusList.get(8)) + "','" + cusList.get(9) + "')");
                                 }catch(SQLException zipExist){
                                     System.out.println("Zip code does already exist in the zip table");
                                 }
                                 // Creates customers table data
                                 s.executeUpdate("INSERT INTO customers(customer_first_name,customer_last_name,customer_address,customer_license_number,customer_mobile_phone,customer_phone,customer_email,customer_driver_since_date,zip_code) VALUES ('"+ cusList.get(0)+"','"+cusList.get(1)+"','"+cusList.get(2)+"','"+cusList.get(3)+"','"+cusList.get(4)+"','"+cusList.get(5)+"','"+cusList.get(6)+"','"+cusList.get(7)+"','"+Integer.parseInt(cusList.get(8))+"')");
                                 cusList.clear();
                                 break;
                             case 2:
                                 /* Nødvendige FRIE data vi behøver for at kunne lave en contract er:
                                    1. customer_id fra customer
                                    2. car_reg_number fra cars
                                    Altså skal der både være lavet et customers og cars data sæt, der ikke er koblet op på noget, for at vi vil kunne lave en kontrakt.
                                 *  */
                                 if(s.executeQuery("SELECT car_reg_number FROM cars c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.car_reg_number = con.car_reg_number)") != null && s.executeQuery("SELECT customer_first_name, customer_last_name FROM   customers c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.customer_id = con.customer_id)") != null){
                                     String[] contractPrompt = {"Input the following information","contract_id","customer_id","contract_to_date","contract_from_date","contract_max_km","car_reg_number"};
                                     for (int h = 1; h < contractPrompt.length; h++) {
                                         System.out.println(0);
                                         System.out.println(h);
                                         conList.add(console.next());
                                     }
                                     // Tænker at vi kan tage customer_id fra cusList og car_reg_number fra carList
                                     s.executeUpdate("INSERT INTO contracts VALUES('"+conList.get(0)+"','"+conList.get(1)+"','"+conList.get(2)+"','"+conList.get(3)+"','"+conList.get(4)+"','"+conList.get(5)+"')");
                                     conList.clear();
                                     /**/
                                 }else if(s.executeQuery("SELECT car_reg_number FROM cars c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.car_reg_number = con.car_reg_number)") == null && s2.executeQuery("SELECT customer_first_name, customer_last_name FROM   customers c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.customer_id = con.customer_id)") == null){
                                     System.out.println("There is not a cars and customers available to create a contract.\nReturning to menu.");
                                 }else if(s.executeQuery("SELECT car_reg_number FROM cars c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.car_reg_number = con.car_reg_number)") == null){
                                     System.out.println("There are no cars available to create a contract\nReturning to menu.");
                                 }else if(s2.executeQuery("SELECT customer_first_name, customer_last_name FROM   customers c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.customer_id = con.customer_id)") == null){
                                     System.out.println("There are no customers available to create a contract\nReturning to menu.");
                                 }
                                 break;
                             case 3:
                                 String[] carPrompts = {"Input the following information for the car: ","Registration number","Type","Brand","Model","cruise control","automatic gear?","Horse Power","Seat Material","Number of seats","Air Condition?","CCM","Fuel Type","Registration date","Odometer"};
                                 for (int j = 1; j < carPrompts.length; j++) {
                                     System.out.println(carPrompts[0]);
                                     System.out.println(carPrompts[j]);
                                     carList.add(console.next());
                                 }
                                 // Det næste statement tager information der er blivet besvaret i sout prompt i forloopet foroven, og sætter det ind i et sql statement, der matcher de forventede værdier.
                                 s.executeUpdate("INSERT INTO cars VALUES ('"+carList.get(0)+"','"+carList.get(1)+"','"+carList.get(2)+"','"+carList.get(3)+"','"+Integer.parseInt(carList.get(4))+"','"+Integer.parseInt(carList.get(5))+"','"+
                                         Integer.parseInt(carList.get(6))+"','"+carList.get(7)+"','"+Integer.parseInt(carList.get(8))+"','"+Integer.parseInt(carList.get(9))+"','"+Integer.parseInt(carList.get(10))+"','"+carList.get(11)+"','"+carList.get(12)+"','"+Integer.parseInt(carList.get(13))+"')");
                                 carList.clear();
                                 break;
                             case 4: //Til at lave en seperat zip kode uden en customer. De bliver lavet sammen med customer i case 1
                                 String[] zipPrompts = {"Input the following information for the car: ","zip code","city"};
                                 for (int k = 1; k < zipPrompts.length; k++) {
                                     System.out.println(zipPrompts[0]);
                                     System.out.println(zipPrompts[k]);
                                     zipList.add(console.next());
                                 }
                                 s.executeUpdate("INSERT INTO zips VALUES ('"+zipList.get(0)+"','"+zipList.get(1)+"')");
                                 zipList.clear();
                                 break;
                         }
                         break;
                     case 2:
                         //TODO updateObject;
                         System.out.println("What information do you wish to change?\n1.Customer\n2.Contract\n3.Car\n4.zips");
                         break;
                     case 3:
                         // TODO removeObject; (Behøver vi dette, eller kan vi ikke bare nøjes med at fokusere på update?)
                         System.out.println("What information do you want to remove?\n1.Customer\n2.Contract\n3.Car\n4.zips");
                         int removeChoice = console.nextInt();
                         switch (removeChoice){
                             case 1:

                                 break;
                             case 2:
                                 break;
                             case 3:
                                 break;
                             case 4:
                                 /*System.out.println("Which zip do you want to delete? (Input the zip code)");
                                 ResultSet rsCu = s.executeQuery("SELECT customer_id,customer_first_name,customer_last_name,customer_address,customer_license_number, " +
                                         "customer_mobile_phone,customer_phone,customer_email,customer_driver_since_date,zip_code FROM customers");
                                 if(rsCu != null)
                                     while(rsCu.next()){
                                         System.out.printf("zipcode: %s\n",rsCu.getString("zip_code"));
                                     }
                                 int zipDelete = console.nextInt();
                                     s.executeUpdate("SET FOREIGN_KEY_CHECKS=0; ");
                                 s.executeUpdate("DELETE FROM zips WHERE zip_code='"+zipDelete+"'");
                                 s.executeUpdate("UPDATE customers SET zip_code = '"+(zipDelete-1)+"' WHERE zip_code = '"+zipDelete+"'");
                                 s.executeUpdate("SET FOREIGN_KEY_CHECKS=1; "); */
                                 break;
                         }
                         break;
                     case 4: // Hver case i denne del, består af et excecuteQuery der ved hjælp af printF statements, udprinter en given table
                         System.out.println("Which information do you want to print?\n1.Customers\n2.Cars\n3.Contracts\n4.Cities");
                         int printChoise = console.nextInt();
                         switch (printChoise){
                             case 1: // Udprintning af customers table
                                 ResultSet rsCu = s.executeQuery("SELECT customer_id,customer_first_name,customer_last_name,customer_address,customer_license_number, " +
                                         "customer_mobile_phone,customer_phone,customer_email,customer_driver_since_date,zip_code FROM customers");
                                 if(rsCu != null) {
                                     while (rsCu.next()) {
                                         System.out.printf("customer id: %-32s", rsCu.getString("customer_id"));
                                         System.out.printf("customer name: %s %-23s", rsCu.getString("customer_first_name"), rsCu.getString("customer_last_name"));
                                         System.out.printf("customer address: %-10s\n", rsCu.getString("customer_address"));
                                         System.out.printf("customer license number: %-20s", rsCu.getString("customer_license_number"));
                                         System.out.printf("customer mobile phone: %-20s", rsCu.getString("customer_mobile_phone"));
                                         System.out.printf("customer phone: %-10s\n", rsCu.getString("customer_phone"));
                                         System.out.printf("customer email: %-29s", rsCu.getString("customer_email"));
                                         System.out.printf("customer driver since: %-20s", rsCu.getString("customer_driver_since_date"));
                                         System.out.printf("customer zipcode: %-10s\n\n", rsCu.getString("zip_code"));
                                     }
                                 }
                                 break;
                             case 2: // Udprintning af cars table
                                 ResultSet rsCar = s.executeQuery("SELECT car_reg_number,car_type,car_brand,car_model,car_cruise_control,car_auto_gear,car_hp," +
                                         "car_seat_material,car_seat_number,car_ac,car_ccm,car_fuel_type,car_reg_date,car_odometer FROM cars");
                                 if(rsCar != null){
                                     while(rsCar.next()){
                                         System.out.printf("Car reg number: %-14s",rsCar.getString("car_reg_number"));
                                         System.out.printf("Car type: %-17s",rsCar.getString("car_type"));
                                         System.out.printf("Car brand & model: %s %-10s\n",rsCar.getString("car_brand"),rsCar.getString("car_model"));
                                         System.out.printf("Car cruise-control: %-10s",rsCar.getString("car_cruise_control"));
                                         System.out.printf("Car auto-gear: %-12s",rsCar.getString("car_auto_gear"));
                                         System.out.printf("Car hp: %-10s\n",rsCar.getString("car_hp"));
                                         System.out.printf("Car seat material: %-11s",rsCar.getString("car_seat_material"));
                                         System.out.printf("Car seat numbers: %-9s",rsCar.getString("car_seat_number"));
                                         System.out.printf("Car ac: %-10s\n",rsCar.getString("car_ac"));
                                         System.out.printf("Car ccm: %-21s",rsCar.getString("car_ccm"));
                                         System.out.printf("Car fuel type: %-12s",rsCar.getString("car_fuel_type"));
                                         System.out.printf("Car reg date: %-15s",rsCar.getString("car_reg_date"));
                                         System.out.printf("Car odometer: %-10s\n\n",rsCar.getString("car_odometer"));
                                     }
                                 }
                                 break;
                             case 3: //Prints the contracts table
                                 ResultSet rsCon = s.executeQuery("SELECT contract_id,customer_id,contract_to_date,contract_from_date,contract_max_km,car_reg_number FROM contracts");
                                 if(rsCon != null){
                                     while(rsCon.next()){
                                         System.out.printf("Contract id: %-16s",rsCon.getString("contract_id"));
                                         System.out.printf("Customer id: %-17s",rsCon.getString("customer_id"));
                                         System.out.printf("Contract period: From '%s' to '%-10s'\n",rsCon.getString("contract_from_date"),rsCon.getString("contract_to_date"));
                                         System.out.printf("Contract max km: %-12s",rsCon.getString("contract_max_km"));
                                         System.out.printf("Car number: %-10s\n\n",rsCon.getString("car_reg_number"));
                                     }
                                 }
                                 break;
                             case 4: //Prints the zips table
                                 ResultSet rsZip = s.executeQuery("SELECT zip_code,zip_city FROM zips");
                                 if(rsZip != null){
                                     while(rsZip.next()){
                                         System.out.printf("Zip code: %-12s",rsZip.getString("zip_code"));
                                         System.out.printf("City for zip code: %s\n\n",rsZip.getString("zip_city"));
                                     }
                                 }
                                 break;
                         }
                         break;
                 }

                 console.nextLine(); //Æder escapeSequence
                 System.out.println("Return to main menu? y/n");
                 String answer = console.nextLine();
                 if (answer.equalsIgnoreCase("n")) {
                     System.exit(1);
                     mainMenu = false;
                 }
             }
             /* Errorhandling */
             s.close(); //Lukker statement objektet
             con.close(); //Lukker DriverManger (Forbindelsen til DB)
         }
         catch(SQLException sqlex) {
            try{
                System.out.println(sqlex.getMessage()); //TODO Ved vi hvad getMessage() findes ?
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
