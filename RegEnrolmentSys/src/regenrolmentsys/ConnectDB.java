/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package regenrolmentsys;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    public static Connection connect() {
        Connection con = null;
        try {
            
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/enrolsys", "root", "pass");
            System.out.println("Connection Established successfully");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}

