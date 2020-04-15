package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Print { // Klasse der står for al udprintning af data til console
    public static void entryMenu(Statement s, Scanner console) throws SQLException {
        ResultSet printSet; //initialisere et resultSet instance
        System.out.println("Which information do you want to print?\n1. Customers\n2. Cars\n3. Contracts\n4. Cities\n5. Return to menu");
        int printChoice = InputValidation.intRange(console,1, 5); //Laver et kald til inputValidation der validere et input værdi
        System.out.println("What do you wish to print?\n1. All info\n2. Specific info");
        int printChoiceSpec = InputValidation.intRange(console, 1,2);
        if (printChoiceSpec == 1) { //Hvis man vælger "All info" i println statement
            switch (printChoice) { //Laver kald til forskellige metoder, der printer data for de forskellige grupper
                case 1: // Udprintning af customers table
                    customers(s);
                    break;
                case 2: // Udprintning af cars table
                    cars(s);
                    break;
                case 3: //Prints the contracts table
                    contracts(s);
                    break;
                case 4: //Prints the zips table
                    cities(s);
                    break;
                default:
                    Menu.interactionMenu();
                    break;
            }
        }else{
            specific(s, console, printChoice); //Metode kald til en metode, der printer specifikke data fra en given table
        }
    }
    // Metode der printer specifik data fra en given table
    private static void specific(Statement s, Scanner console, int printChoice) throws SQLException {
        ResultSet printSet;
        //Et Array der indeholder navnet på de forskellige table i DB
        String[] table = {"customers","cars","contracts","zips"};
        //Et MultiArray, der indeholder de forskellige table fields
        String[][] column = {{"customer_id","customer_first_name","customer_last_name","customer_address","customer_license_number",
                "customer_mobile_phone","customer_phone","customer_email","customer_driver_since_date","zip_code"},{"car_reg_number","car_type","car_brand","car_model","car_cruise_control","car_auto_gear","car_hp",
                "car_seat_material","car_seat_number","car_ac","car_ccm","car_fuel_type","car_reg_date","car_odometer"},{"contract_id","customer_id","contract_to_date","contract_from_date","contract_max_km","car_reg_number"},{"zip_code","zip_city"}};

        System.out.println("Select which criteria to use:");
        //For loop der iterere, antal gange af længden af "printChoice", der er blevet parameter overført (for at vælge hvilke table der skal udprintes)
        for(int i = 0; i < column[printChoice-1].length;i++){
            //Bruger replace, til at fjerne "_" for indekstal "i", i column multiArrayet (for et bedre udprint til console)
            System.out.println(i+1+". "+column[printChoice-1][i].replace("_"," "));
        }

        int inputI = InputValidation.intRange(console,1,column[printChoice-1].length)-1;
        System.out.println("Input search word");
        // Hvis det givende data i multiArray[printchoice-1][x] er en int værdi
        if((printChoice-1 == 0 && inputI == 0)||(printChoice-1 == 0 && inputI == 9)||(printChoice-1 == 1 && inputI == 4)||(printChoice-1 == 1 && inputI == 5)||(printChoice-1 == 1 && inputI == 6)||(printChoice-1 == 1 && inputI == 8)||(printChoice-1 == 1 && inputI == 9)||(printChoice-1 == 1 && inputI == 10)||
                (printChoice-1 == 1 && inputI == 13)||(printChoice-1 == 2 && inputI == 0)||(printChoice-1 == 2 && inputI == 1)||(printChoice-1 == 2 && inputI == 4)||(printChoice-1 == 3 && inputI == 0)){
            int searchCriteriaI = console.nextInt(); //laver det givende seach criteria man ønsker at søge efter i en given table
            //Et SQL statement der tager info fra table Array, column multiArray og det søge criteria
            printSet = s.executeQuery("SELECT * FROM "+table[printChoice-1]+" WHERE "+column[printChoice-1][inputI]+" = "+searchCriteriaI+"");
        }else { //Hvis det ikke er en int værdi
            String searchCriteriaS = console.nextLine();
            printSet = s.executeQuery("SELECT * FROM "+table[printChoice-1]+" WHERE "+column[printChoice-1][inputI]+" = '"+searchCriteriaS+"'");
        }
        //Udprinter data til console ud fra ovenstående SELECT statement
        if(printSet != null) { //Hvis ikke at printSet (SELECT statement) ikke er tomt, kør program
            boolean check = true;
            String tableMinusS;
            //Så længe at det næste plads i SELECT statement ikke er null, iterer igen
            while (printSet.next()) {
                for (int i = 0; i < column[printChoice - 1].length; i++) {
                    //Hvis der er valgt table, fjerner man det sidste s ved at sætte det til en ny sætning uden s, da der er flere end 1 s i customers
                    if(table[printChoice-1].equalsIgnoreCase("customers")) {
                        tableMinusS = table[printChoice - 1].replace("customers", "customer");
                    }else{
                        //Alle andre tables end customers, har kun 1 s, så der siger vi bare at den skal fjerne s, der er det sidste i tablenavnet
                        tableMinusS = table[printChoice - 1].replace("s", "");
                    }
                    String colNew = column[printChoice - 1][i].replace(tableMinusS+"_", "");
                    //Udprinter data til console, ud fra søge kriterierne der blev givet tidligere
                    System.out.printf("%-20s: %s\n", colNew.replace("_"," "), printSet.getString("" + column[printChoice - 1][i] + ""));
                }
                System.out.println();
                //Hvis man kommer ind i dette while loop, betyder det at der er info i SELECT statement til udprintning,
                // og vi sætter derfor check til false, så vi ikke går ind i nedenstående if statement
                check = false;
            }if(check){ //Hvis der ikke er data i SELECT statementet, udprint println statement
                System.out.println("No info found.\nPlease try searching for something else");
            }
        }
    }

    public static void customers(Statement s) throws SQLException { // printer info om ALT data der befinder sig i customers table
        ResultSet rsCu = s.executeQuery("SELECT customer_id,customer_first_name,customer_last_name,customer_address,customer_license_number, " +
                "customer_mobile_phone,customer_phone,customer_email,customer_driver_since_date,zip_code FROM customers");
        if(rsCu != null) { //Checker at der er data i customers table, ud fra ovenstående SELECT statement
            while (rsCu.next()) { //Går customer tables ResultSet igennem, et 'row' af gangen af ResultSet for ovenstående SELECT statement
                System.out.printf("customer id: %-32s", rsCu.getString("customer_id"));
                System.out.printf("customer name: %-28s", rsCu.getString("customer_first_name")+" "+rsCu.getString("customer_last_name"));
                System.out.printf("customer address: %-10s\n", rsCu.getString("customer_address"));
                System.out.printf("customer license number: %-20s", rsCu.getString("customer_license_number"));
                System.out.printf("customer mobile phone: %-20s", rsCu.getString("customer_mobile_phone"));
                System.out.printf("customer phone: %-10s\n", rsCu.getString("customer_phone"));
                System.out.printf("customer email: %-29s", rsCu.getString("customer_email"));
                System.out.printf("customer driver since: %-20s", rsCu.getString("customer_driver_since_date"));
                System.out.printf("customer zipcode: %-10s\n\n", rsCu.getString("zip_code"));
            }
        }
    }

    public static void cars(Statement s) throws SQLException { // Printer info om ALT data der befinder sig i cars table
        ResultSet rsCar = s.executeQuery("SELECT car_reg_number,car_type,car_brand,car_model,car_cruise_control,car_auto_gear,car_hp," +
                "car_seat_material,car_seat_number,car_ac,car_ccm,car_fuel_type,car_reg_date,car_odometer FROM cars");
        if(rsCar != null){ //Checker at der er data i cars table, ud fra ovenstående SELECT statement
            while(rsCar.next()){ //Går cars tables ResultSet igennem, et 'row' af gangen af ResultSet for ovenstående SELECT statement
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
    }

    public static void contracts(Statement s) throws SQLException { //printer info om data der befinder sig i contracts table
        ResultSet rsCon = s.executeQuery("SELECT contract_id,customer_id,contract_to_date,contract_from_date,contract_max_km,car_reg_number FROM contracts");
        if(rsCon != null){//Checker at der er data i contracts table, ud fra ovenstående SELECT statement
            while(rsCon.next()){ //Går contracts tables ResultSet igennem, et 'row' af gangen af ResultSet for ovenstående SELECT statement
                System.out.printf("Contract id: %-16s",rsCon.getString("contract_id"));
                System.out.printf("Customer id: %-17s",rsCon.getString("customer_id"));
                System.out.printf("Contract period: From '%s' to '%-10s'\n",rsCon.getString("contract_from_date"),rsCon.getString("contract_to_date"));
                System.out.printf("Contract max km: %-12s",rsCon.getString("contract_max_km"));
                System.out.printf("Car number: %-10s\n\n",rsCon.getString("car_reg_number"));
            }
        }
    }

    public static void cities(Statement s) throws SQLException { // printer info om data der befinder sig i zips table
        ResultSet rsZip = s.executeQuery("SELECT zip_code,zip_city FROM zips");
        if(rsZip != null){//Checker at der er data i zips table, ud fra ovenstående SELECT statement
            while(rsZip.next()){//Går zips tables ResultSet igennem, et 'row' af gangen af ResultSet for ovenstående SELECT statement
                System.out.printf("Zip code: %-12s",rsZip.getString("zip_code"));
                System.out.printf("City: %s\n\n",rsZip.getString("zip_city"));
            }
        }
    }
}
