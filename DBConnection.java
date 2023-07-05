/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.loginform;

/**
 *
 * @author Mngomezulu kgotlelelo Allet
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:derby://localhost:1527/Login";
        String user = "User1";
        String password = "Allet247";

        return DriverManager.getConnection(url, user, password);
    }
}
