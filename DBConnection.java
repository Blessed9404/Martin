/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ac.za.cput.mycityprotectss;

/**
 *
 * @author Olebaka
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:derby://localhost:1527/CityProtect";
    private static final String user = "adminstrator";
    private static final String password = "password";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        return con;
    }
}
