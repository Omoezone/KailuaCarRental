package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; // drivers lokation i Intellij - connecter java fil med MySQL
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/kailua"; // url for vores DB på localhost
    static Connection con; // Connection object som sørger for forbindelse

    public static void interactionMenu()throws SQLException{
        Scanner console = new Scanner(System.in);
         try {
             // DB connection statements
             con = null;
             Statement s = null;
             Statement s2 = null;
             Class.forName(JDBC_DRIVER);

             // Connection statement der bruger vores final string URL og vores password til DB.
             con = DriverManager.getConnection(DATABASE_URL, "root", "X7913bz1h11");

             // vi skaber to Statements, fordi vi i vores contract creation skal bruge to Resultsets åbne på samme tid.
             s = con.createStatement();
             s2 = con.createStatement();

             // loops while user inputs y
             boolean mainMenu = true;
             while (mainMenu) {
                 //Choose which action to do with 1,2, 3 or 4.
                 System.out.println("1. Create new DB entry \n2. Change an existing entry \n3. Remove an existing entry \n4. Print list of entries\n5. Exit program");
                 int choice = InputValidation.intRange(console, 1,5); // validere at int værdi er mellem 1 og 4 inkluderet 1 = min 4 = max
                 switch (choice) {
                     case 1: // calls method that creates any of the entries
                         createEntry(console, s, s2);
                         break;
                     case 2: // calls method that changes any given row in DB
                         updateChoiceMethod(s,console);
                         break;
                     case 3: // calls method that removes any entry in DB
                         removeEntry(s, console);
                         break;
                     case 4: // calls print method from Print class
                         Print.entryMenu(s,console);
                         break;
                     case 5:
                         System.out.println("You have chosen to exit.\nGoodbye");
                         System.exit(1);
                 }
                 System.out.println("Return to main menu? yes/no");
                 String answer = InputValidation.chooseYesNo(console);
                 if (answer.equalsIgnoreCase("no")) {
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

    private static void createEntry(Scanner console, Statement s, Statement s2) throws SQLException {
        // ArrayLists til at indeholde midlertidlig data, der bliver brugt under kreation delen.
        ArrayList<String> cusList = new ArrayList<>();
        ArrayList<String> carList = new ArrayList<>();
        ArrayList<String> conList = new ArrayList<>();
        ArrayList<String> zipList = new ArrayList<>();

        System.out.println("1. New customer \n2. New contract \n3. New car \n4. New city\n5. Return to menu");
        int choiceCreate = InputValidation.intRange(console,1,5);
        switch (choiceCreate) {
            /*
            case 1: Customer Creation
            Bruger bliver promptet for at indtaste information i for loop (fra String array på linie 49) og løbende bliver
            Informationen om customers zip code og city bliver først indsat i DB med try/catch statement der sørger for at
            det ikke allerede er information der er i zip code table. Derefter sættes resten informationen ind i DB.
            */
            case 1:
                String[] customerPrompts = {"Input the following information: ","First name?", "Last name?", "Address?", "license Number", "Mobile number?", "Phone?", "Email?", "When did the driver start driving?","zip code","City"};
                for (int i = 1; i < 11; i++) {
                    System.out.println(customerPrompts[0]);
                    System.out.println(customerPrompts[i]);
                    cusList.add(console.nextLine());
                }
                // Creates zips rows med try catch, for at sikre at zip ikke allerede findes.
                try {
                    s.executeUpdate("INSERT INTO zips VALUES('" + Integer.parseInt(cusList.get(8)) + "','" + cusList.get(9) + "')");
                }catch(SQLException zipExist){
                    System.out.println("Zip code does already exist in the zip table");
                }
                // Creates customers table data
                s.executeUpdate("INSERT INTO customers(customer_first_name,customer_last_name,customer_address,customer_license_number,customer_mobile_phone,customer_phone,customer_email,customer_driver_since_date,zip_code) VALUES ('"+ cusList.get(0)+"','"+cusList.get(1)+"','"+cusList.get(2)+"','"+cusList.get(3)+"','"+cusList.get(4)+"','"+cusList.get(5)+"','"+cusList.get(6)+"','"+cusList.get(7)+"','"+Integer.parseInt(cusList.get(8))+"')");
                cusList.clear();
                break;

            /*
            Case 2: Contract Creation
            først bliver der lavet to resultsets, som bliver tjekket med subqueries for om der der nogen "frie" biler og kunder.
            Hvis der er, så bliver man promptet til at vælge én af hver og ellers er der en række med else ifs, som, ud fra
            om det er en car og/eler en customer, der mangler, skriver 'fejlmeddelselse' og beder brugeren om at vænne tilbage
            til hovedmenuen.
            */

            case 2:
                /* Nødvendige FRIE data vi behøver for at kunne lave en contract er:
                   1. customer_id fra customer
                   2. car_reg_number fra cars
                   Altså skal der både være lavet et customers og cars data sæt, der ikke er koblet op på noget, for at vi vil kunne lave en kontrakt.
                */
                ResultSet rsCars = s.executeQuery("SELECT car_reg_number,car_model FROM cars c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.car_reg_number = con.car_reg_number)");
                ResultSet rsCust = s2.executeQuery("SELECT customer_id,customer_first_name, customer_last_name FROM   customers c WHERE  NOT EXISTS (SELECT * FROM   contracts con WHERE  c.customer_id = con.customer_id)");
                // Hvis der findes useable (altså uden contract) entries i begge Resultsets, så skal bruger vælge en af hver for at skabe en ny contract.
                if(rsCars.next() && rsCust.next()){
                    String[] contractPrompt = {"Input the following information","choose customer by id number","contract start date (YYYY-MM-DD)","contract end date (YYYY-MM-DD)","Max km by contract","choose car by registration number"};
                    for (int h = 1; h < contractPrompt.length; h++) {
                        System.out.println(contractPrompt[0]);
                        System.out.println(contractPrompt[h]+"\n");
                        if(h == 1){
                            rsCust.beforeFirst();
                            while(rsCust.next()) {
                                System.out.printf("Customer id: %s\n", rsCust.getString("customer_id"));
                                System.out.printf("Customer Name: %-4s %s\n\n",rsCust.getString("customer_first_name"),rsCust.getString("customer_last_name"));
                            }
                        }
                        if(h == 5){
                            rsCars.beforeFirst();
                            while(rsCars.next()){
                                System.out.printf("Car registration number: %-4s \nModel: %s\n\n", rsCars.getString("car_reg_number"),rsCars.getString("car_model"));
                            }
                        }
                        conList.add(console.next());
                    }
                    // Tænker at vi kan tage customer_id fra cusList og car_reg_number fra carList
                    s.executeUpdate("INSERT INTO contracts (customer_id,contract_to_date,contract_from_date,contract_max_km,car_reg_number) VALUES('"+conList.get(0)+"','"+conList.get(1)+"','"+conList.get(2)+"','"+conList.get(3)+"','"+conList.get(4)+"')");
                    conList.clear();
                // Hvis der mangler en bil og/eller kunde får man én af de følgende fejl.
                }else if(!(rsCars.next()) && !(rsCust.next())){
                    System.out.println("There is not a cars and customers available to create a contract.\nPlease return to menu.");
                }else if(!(rsCars.next())){
                    System.out.println("There are no cars available to create a contract\nPlease return to menu.");
                }else if(!(rsCust.next())){
                    System.out.println("There are no customers available to create a contract\nPlease return to menu.");
                }
                break;

            /*case 3: Car creation
            bruger bliver promptet til at indtaste information om bilen, som bliver added til en temp arraylist som derefter
            bliver brugt som 'parametre' til SQL statement, som skaber den nye bil i vores database*/
            case 3:
                String[] carPrompts = {"Input the following information for the car: ","Registration number","Type","Brand","Model","cruise control?","automatic gear?","Horse Power","Seat Material","Number of seats","Air Condition?","CCM","Fuel Type","Registration date (YYYY-MM-DD)","Odometer"};
                for (int j = 1; j < carPrompts.length; j++) {
                    System.out.println(carPrompts[0]);
                    System.out.println(carPrompts[j]);
                    if(j == 5 || j == 6 || j == 10){
                        System.out.println("Please enter yes/no");
                        if(InputValidation.chooseYesNo(console).equalsIgnoreCase("yes")){
                            carList.add("1");
                        }else{
                            carList.add("0");
                        }
                    }else {
                        carList.add(console.next());
                    }
                }
                // Det næste statement tager information der er blivet besvaret i sout prompt i forloopet foroven,
                // og sætter det ind i et sql statement, der matcher de forventede værdier.
                s.executeUpdate("INSERT INTO cars VALUES ('"+carList.get(0)+"','"+carList.get(1)+"','"+carList.get(2)+"','"+carList.get(3)+"','"+Integer.parseInt(carList.get(4))+"','"+Integer.parseInt(carList.get(5))+"','"+
                        Integer.parseInt(carList.get(6))+"','"+carList.get(7)+"','"+Integer.parseInt(carList.get(8))+"','"+Integer.parseInt(carList.get(9))+"','"+Integer.parseInt(carList.get(10))+"','"+carList.get(11)+"','"+carList.get(12)+"','"+Integer.parseInt(carList.get(13))+"')");
                carList.clear();
                break;

            /*
            case 4: Zip Code/ City Creation
            Til at lave en separat zip kode uden en customer. De bliver lavet sammen med customer i case 1
            */
            case 4:
                String[] zipPrompts = {"Input the following information for the car: ","zip code","city"};
                for (int k = 1; k < zipPrompts.length; k++) {
                    System.out.println(zipPrompts[0]);
                    System.out.println(zipPrompts[k]);
                    zipList.add(console.next());
                }
                s.executeUpdate("INSERT INTO zips VALUES ('"+zipList.get(0)+"','"+zipList.get(1)+"')");
                zipList.clear();
                break;
            default:
                interactionMenu();
                break;
        }
    }

    private static void removeEntry(Statement s, Scanner console) throws SQLException {
        System.out.println("What information do you want to remove?\n1.Customer\n2.Car\n3.Contract\n4.Return to menu");
        int removeChoice = InputValidation.intRange(console,1,4);
        String areYouSure;
        switch (removeChoice){
            case 1: // Customer
                Print.customers(s);
                System.out.println("Which customer do you want to remove? Please enter ID nr");
                int removeChoiceCus = console.nextInt();
                System.out.println("Are you sure? yes/no");
                areYouSure = InputValidation.chooseYesNo(console);
                if(areYouSure.equalsIgnoreCase("no")){
                    removeEntry(s, console);
                }else{
                    if((s.executeQuery("SELECT customer_id FROM contracts WHERE customer_id = "+removeChoiceCus))!= null) {
                        s.executeUpdate("DELETE FROM contracts WHERE customer_id = " + removeChoiceCus);
                        s.executeUpdate("DELETE FROM customers WHERE customer_id = " + removeChoiceCus);
                    }else{
                        s.executeUpdate("DELETE FROM customers WHERE customer_id = " + removeChoiceCus);
                    }
                }
                break;

            case 2: // Car
                Print.cars(s);
                System.out.println("Which car do you want to remove? Please enter registration number");
                String removeChoiceCar = console.next().toUpperCase();
                System.out.println("Are you sure? yes/no");
                areYouSure = InputValidation.chooseYesNo(console);
                if(areYouSure.equalsIgnoreCase("no")){
                    removeEntry(s, console);
                }else{
                    if((s.executeQuery("SELECT car_reg_number FROM contracts WHERE car_reg_number = '"+removeChoiceCar+"'"))!= null) {
                        s.executeUpdate("DELETE FROM contracts WHERE car_reg_number = '" + removeChoiceCar+"'");
                        s.executeUpdate("DELETE FROM cars WHERE car_reg_number = '" + removeChoiceCar+"'");
                    }else{
                        s.executeUpdate("DELETE FROM cars WHERE car_reg_number = '" + removeChoiceCar+"'");
                    }
                }
                break;
            case 3: // Contract
                Print.contracts(s);
                System.out.println("Which contract do you want to remove? Please enter ID nr");
                int removeChoiceCon = console.nextInt();
                System.out.println("Are you sure? yes/no");
                areYouSure = InputValidation.chooseYesNo(console);
                if(areYouSure.equalsIgnoreCase("no")){
                    removeEntry(s, console);
                }else{
                    s.executeUpdate("DELETE FROM contracts WHERE contract_id = " + removeChoiceCon);
                }
                break;
            default:
                interactionMenu();
                break;
        }
    }

    public static void updateChoiceMethod(Statement s,Scanner console) throws SQLException{ //updaterer data ud fra valgte tables data
        System.out.println("What information do you wish to change?\n1.Customer\n2.Cars\n3.Contract\n4.Return to menu");
        int updateChoice = InputValidation.intRange(console, 1,4);
        switch(updateChoice){
            case 1: //Customers
                System.out.println("Who do you want to change?");
                ResultSet cuSet = s.executeQuery("SELECT customer_id,customer_first_name,customer_last_name FROM customers ORDER BY customer_first_name");
                if(cuSet != null){
                    while(cuSet.next()){
                        System.out.printf("Customer id: %-10s Customer name: %-4s %s\n", cuSet.getString("customer_id"),cuSet.getString("customer_first_name"),cuSet.getString("customer_last_name"));
                    }
                }
                int cuInt = console.nextInt();
                System.out.println("What information do you wish to change in customer? Input number");
                System.out.println("1# customer first name\n2# customer last name\n3# customer address\n4# customer license number\n" +
                        "5# customer mobile number\n6# customer phone number\n7# customer email\n8# Customer driver since date\n9# zip code");
                String[] custTemp = {"customer_first_name","customer_last_name","customer_address","customer_license_number",
                        "customer_mobile_phone","customer_phone","customer_email","customer_drive_since_date","zip_code"};
                int cu = InputValidation.intRange(console,1,9)-1;
                System.out.println("What should the new info be?");
                console.nextLine();
                String cuNew = console.nextLine();
                if(cu == 8) {
                    try {
                        System.out.println("What is the city that belongs to the zip code");
                        String newCity = console.nextLine();
                        s.executeUpdate("INSERT INTO zips VALUES('" + Integer.parseInt(cuNew) + "','" + newCity + "')");
                    } catch (SQLException zipExist) {
                        System.out.println("Zip code does already exist in the zip table");
                    }
                    s.executeUpdate("UPDATE customers SET "+custTemp[cu]+" = "+Integer.parseInt(cuNew)+" WHERE customer_id = "+cuInt+"");
                }else
                    s.executeUpdate("UPDATE customers SET "+custTemp[cu]+" = '"+cuNew+"' WHERE customer_id = "+cuInt+"");
                break;

            case 2: //cars
                System.out.println("Which car do you want to change?");
                ResultSet carSet = s.executeQuery("SELECT car_reg_number,car_type,car_brand, car_model FROM cars ORDER BY car_reg_number");
                if(carSet != null){
                    while(carSet.next()){
                        System.out.printf("Car registration number: %-10s Car type: %-10s\n", carSet.getString("car_reg_number"),carSet.getString("car_type"));
                        System.out.printf("Car brand: %-24s Car model : %-10s\n\n", carSet.getString("car_brand"),carSet.getString("car_model"));
                    }
                }
                console.nextLine();
                String carReg = console.nextLine().toUpperCase();
                System.out.println("What information do you wish to change in car? Input number");
                System.out.println("1# car registration number \n2# car type\n3# car brand\n4# car model\n" +
                        "5# car cruise control \n6# automatic \n7# horsepower \n8# seat material \n9# number of seats \n10# air condition\n"
                        + "11# ccm \n12# fuel type \n13# registration date \n14# odometer");
                String[] carTemp = {"car_reg_number","car_type","car_brand","car_model",
                        "car_cruise_control","car_auto_gear","car_hp","car_seat_material","car_seat_number","car_ac","car_ccm","car_fuel_type"
                ,"car_reg_number","car_odometer"};
                int car = InputValidation.intRange(console, 1,10)-1;
                System.out.println("What should the new info be?");
                console.nextLine();
                String carNew = console.nextLine();

                if(car == 0 || car == 1 || car == 2|| car == 3 || car == 7 || car == 11){
                    s.executeUpdate("UPDATE cars SET " + carTemp[car] + " = '" + carNew + "' WHERE car_reg_number = '" + carReg + "'");
                }else
                    s.executeUpdate("UPDATE cars SET " + carTemp[car] + " = '" + Integer.parseInt(carNew) + "' WHERE car_reg_number = '" + carReg + "'");
                break;

            case 3: //contracts
                System.out.println("Which contract do you want to change by contract id?");
                ResultSet conSet = s.executeQuery("SELECT contract_id, customers.customer_id, customer_first_name, customer_last_name, car_reg_number FROM contracts JOIN customers ON contracts.customer_id = customers.customer_id ORDER BY contract_id");
                if(conSet != null){
                    while(conSet.next()){
                        System.out.printf("Contract id: %-19s Customer id: %-10s\n", conSet.getString("contract_id"),conSet.getString("customer_id"));
                        System.out.printf("Customer name: %-17s Car registration number : %s \n\n", conSet.getString("customer_first_name")+" "+conSet.getString("customer_last_name"),conSet.getString("car_reg_number"));
                    }
                }
                console.nextLine();
                int conReg = console.nextInt();
                System.out.println("What information do you wish to change in the contract? Input number");
                System.out.println("1# contract end date\n2# max km");
                String[] conTemp = {"contract_to_date","contract_max_km"};
                int conCount = InputValidation.intRange(console,1,2)-1;
                System.out.println("What should the new info be?");
                console.nextLine();
                String conNew = console.nextLine();
                if(conCount == 0){
                    s.executeUpdate("UPDATE contracts SET "+conTemp[0]+" = '"+conNew+"' WHERE contract_id = "+conReg+"");
                }else
                    s.executeUpdate("UPDATE contracts SET "+conTemp[1]+" = '"+Integer.parseInt(conNew)+"' WHERE contract_id = "+conReg+"");
                break;
            default:
                interactionMenu();
                break;
        }
    }
}
