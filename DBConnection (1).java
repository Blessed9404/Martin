/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.racemarathon;

/**
 *
 * @author Mngomezulu kgotlelelo Allet
 */
import za.ac.cput.loginform.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:derby://localhost:1527/EventRegistration";
    private static final String password = "Allet247";
    private static final String user = "User1";

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}   


