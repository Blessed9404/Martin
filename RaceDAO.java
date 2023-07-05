/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.racemarathon;

/**
 *
 * @author Mngomezulu kgotlelelo Allet
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {
    public static boolean isRaceCodeDuplicate(String raceCode) throws SQLException {
        try (Connection connection = DBConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT raceCode FROM RaceTable WHERE raceCode = ?")) {
            statement.setString(1, raceCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public static boolean saveRaceRecord(Race race) throws SQLException {
        try (Connection connection = DBConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO RaceTable (raceCode, firstName, lastName, raceType, belongToRace) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, race.getRaceCode());
            statement.setString(2, race.getFirstName());
            statement.setString(3, race.getLastName());
            statement.setString(4, race.getRaceType());
            statement.setBoolean(5, race.isBelongToRace());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static List<String> getRaceTypes() throws SQLException {
        List<String> raceTypes = new ArrayList<>();
        try (Connection connection = DBConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT marathonType FROM RaceTypeTable")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    raceTypes.add(resultSet.getString("marathonType"));
                }
            }
        }
        return raceTypes;
    }
}


