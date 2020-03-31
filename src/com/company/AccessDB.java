package com.company;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccessDB{

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/ap";
    static Connection con;
    /**   we want to use JDBC protocol, mysql DBMS , the local host with
     the database ap */

    public static void main(String[] args) throws SQLException {
        try {

            //***  Establishing the connection
            con = null;
            Statement s = null;
            Class.forName (JDBC_DRIVER);
          /*Class and the forName() is a static method of the java. lang. Class .
          The JDBC Drivers (String) will be loaded into the class dynamically at run time and forName method contains static block
          which creates the Driver class object and register with the DriverManager Service automatically
           */

            // in the url we have to tell which account and password to use
            con =  DriverManager.getConnection(DATABASE_URL,"root","Sabumnim22");

            //*** now that the connection is established we do the query
            s = con.createStatement();

            ResultSet rs = s.executeQuery("SELECT vendor_name,  vendor_city  from vendors where default_account_number > '500'");

            // if the resultset is not empty, we position to first row and display first field
            if (rs != null)
                while (rs.next()) {
                    //System.out.println("Data from name: " + rs.getString("vendor_name") +
                    //      "        " + rs.getString("vendor_city"));
                    System.out.printf("Data from name: %-34s ",rs.getString("vendor_name"));
                    System.out.printf("%s\n ",rs.getString("vendor_city"));
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
