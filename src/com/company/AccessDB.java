package com.company;
import java.sql.Connection; //Interface
import java.sql.DriverManager; //Class
import java.sql.ResultSet; //Interface
import java.sql.SQLException; //Exception File (lyn market fil)
import java.sql.Statement; //Interface
/*
! Alle disse imports og deres tilhørende klasser, findes i library der hedder java.sql i library
! Tror at det man behøver at tilføje, er til mySQL, som vi allerede gjorde, da vi installede mySQL.
! Dette er bare ikke forklaret særlig godt i powerpoint filen.
*/
public class AccessDB{

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; // info: https://en.wikipedia.org/wiki/JDBC_driver
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/kailua";
    static Connection con; //Skaber en instance af den IKKE statiske interface Connection
    /**   we want to use JDBC protocol, mysql DBMS , the local host with
     the database ap */

    public static void main(String[] args) throws SQLException {
        try {

            //***  Establishing the connection
            con = null;
            Statement s = null;
            Class.forName (JDBC_DRIVER); //https://www.geeksforgeeks.org/class-forname-method-in-java-with-examples/
          /*Class and the forName() is a static method of the java. lang. Class .
          The JDBC Drivers (String) will be loaded into the class dynamically at run time and forName method contains static block
          which creates the Driver class object and register with the DriverManager Service automatically
           */

            // in the url we have to tell which account and password to use
            con =  DriverManager.getConnection(DATABASE_URL,"root","X7913bz1h11"); //DriverManager Class fra linje 186

            //*** now that the connection is established we do the query
            s = con.createStatement(); //Connection interface linje 105.
            //s.executeUpdate("INSERT INTO cars VALUES ('BC27381','Family','Renault','Laguna',1,0,150,'leather',5,1,2900,'Disel','2010-12-31',1000 )");
            ResultSet rs = s.executeQuery("SELECT * from cars");

            // ExceuteQuery er en metode i interfacet 'Statement' Se linje 69 i 'Statement'
            // Bliver initialiseret som "rs" der er en del af interfaces 'ResultSet'

            // if the resultset is not empty, we position to first row and display first field
            if (rs != null)
                while (rs.next()) {

                    System.out.printf("Registration: %-16s",rs.getString("car_reg_number"));
                    System.out.printf("Car model: %-16s",rs.getString("car_model"));
                    System.out.printf("Car Type: %-10s\n",rs.getString("car_type"));
                    System.out.printf("Car cruise-control: %-10s",rs.getString("car_cruise_control"));
                    System.out.printf("Car automatgear: %-10s",rs.getString("car_auto_gear"));
                    System.out.printf("Car hp: %-10s\n",rs.getString("car_hp"));
                    System.out.printf("Car seat material: %-11s",rs.getString("car_seat_material"));
                    System.out.printf("Car seat numbers: %-9s",rs.getString("car_seat_number"));
                    System.out.printf("Car ac: %-10s\n",rs.getString("car_ac"));
                    System.out.printf("Car ccm: %-21s",rs.getString("car_ccm"));
                    System.out.printf("Car fuel type: %-12s",rs.getString("car_fuel_type"));
                    System.out.printf("Car reg date: %-15s",rs.getString("car_reg_date"));
                    System.out.printf("Car odometer: %-10s\n\n",rs.getString("car_odometer"));

                }
            s.close();
            con.close();
        }
        /* Errorhandling */
        catch (SQLException sqlex) {
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
