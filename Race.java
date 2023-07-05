/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.racemarathon;

import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Mngomezulu kgotlelelo Allet
 */
public class Race {
    private String raceCode;
    private String firstName;
    private String lastName;
    private String raceType;
    private boolean belongToRace;
    
    private JTextField raceCodeField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> raceTypeComboBox;
    private JRadioButton belongToClubYesRadioButton,belongToClubNoRadioButton ;

    public Race(String raceCode, String firstName, String lastName, String raceType, boolean belongToRace) {
        this.raceCode = raceCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.raceType = raceType;
        this.belongToRace = belongToRace;
    }

    

    public String getRaceCode() {
        return raceCode;
    }

    public void setRaceCode(String raceCode) {
        this.raceCode = raceCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRaceType() {
        return raceType;
    }

    public void setRaceType(String raceType) {
        this.raceType = raceType;
    }

    public boolean isBelongToRace() {
        return belongToRace;
    }

    public void setBelongToRace(boolean belongToRace) {
        this.belongToRace = belongToRace;
    }
    /*private void saveRecord() {
        String raceCode = raceCodeField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String raceType = (String) raceTypeComboBox.getSelectedItem();
        boolean belongToClub = belongToClubYesRadioButton.isSelected();

        try {
            if (RaceDAO.isRaceCodeDuplicate(raceCode)) {
                JOptionPane.showMessageDialog(null, "Duplicate race code. Please enter a unique race code.");
            } else {
                Race race = new Race(raceCode, firstName, lastName, raceType, belongToClub);
                if (RaceDAO.saveRaceRecord(race)) {
                    JOptionPane.showMessageDialog(null, "Record saved successfully.");
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to save the record.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error accessing the database.");
        }
    }
    private void resetForm() {
        raceCodeField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        raceTypeComboBox.setSelectedIndex(0);
        belongToClubYesRadioButton.setSelected(false);
        belongToClubNoRadioButton.setSelected(false);
    }*/

   
}
