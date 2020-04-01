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
            con =  DriverManager.getConnection(DATABASE_URL,"root","hoPPElort97!#"); //DriverManager Class fra linje 186

            //*** now that the connection is established we do the query
            s = con.createStatement(); //Connection interface linje 105.
            s.executeUpdate("INSERT INTO cars VALUES ('BC27389','Family','Renault','Laguna',1,0,150,'leather',5,1,2900,'Disel','2010-12-31',1000 )");
            ResultSet rs = s.executeQuery("SELECT car_reg_number, car_model from cars");
            // ExceuteQuery er en metode i interfacet 'Statement' Se linje 69 i 'Statement'
            // Bliver initialiseret som "rs" der er en del af interfaces 'ResultSet'

            // if the resultset is not empty, we position to first row and display first field
            if (rs != null)
                while (rs.next()) {
                    //System.out.println("Data from name: " + rs.getString("vendor_name") +
                    //      "        " + rs.getString("vendor_city"));
                    System.out.printf("Data from name: %-10s ",rs.getString("car_reg_number"));
                    System.out.printf("%s\n ",rs.getString("car_model"));
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
