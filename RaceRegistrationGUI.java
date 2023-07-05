/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.racemarathon;

/**
 *
 * @author Mngomezulu kgotlelelo Allet
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RaceRegistrationGUI extends JFrame {
    private JTextField raceCodeField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JLabel logoname, chefLabel;
    private JComboBox<String> raceTypeComboBox;
    private JRadioButton belongToClubYesRadioButton,belongToClubNoRadioButton ;
    private Font ft1;

    public RaceRegistrationGUI() {
        setTitle("Marathon Race Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon("path/to/emoji/image/file");
        label.setIcon(icon);
        
        ft1 = new Font("Arial", Font.BOLD | Font.ITALIC, 16);
        
        JPanel northPanel = new JPanel();
        /* chefLabel = new JLabel("\uD83C\uDF54");
        chefLabel.setFont(new Font("Arial", Font.BOLD, 20));
        northPanel.add(chefLabel);*/
        
        logoname = new JLabel("Event Marathon Registration");
        northPanel.add(logoname);
        northPanel.setPreferredSize(new Dimension(20,30));
        logoname.setForeground(Color.black);
        logoname.setFont(ft1);
        northPanel.setBackground(Color.blue);

        JPanel centerPanel = new JPanel(new GridLayout(5, 2));
        centerPanel.add(new JLabel("Race Code:"));
        raceCodeField = new JTextField();
        centerPanel.add(raceCodeField);

        centerPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        centerPanel.add(firstNameField);

        centerPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        centerPanel.add(lastNameField);

        centerPanel.add(new JLabel("Race Type:"));
        raceTypeComboBox = new JComboBox<>(new String[]{"Ultra Marathon (56km)", "Full Marathon (42km)", "Half Marathon (21km)"});
        centerPanel.add(raceTypeComboBox);

        centerPanel.add(new JLabel("Belong to Race Club:"));
        JPanel radioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        belongToClubYesRadioButton = new JRadioButton("Yes");
        radioButtonPanel.add(belongToClubYesRadioButton);
        belongToClubNoRadioButton = new JRadioButton("No");
        radioButtonPanel.add(belongToClubNoRadioButton);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(belongToClubYesRadioButton);
        buttonGroup.add(belongToClubNoRadioButton);
        centerPanel.add(radioButtonPanel);
        
        //centerPanel.setBackground(Color.pink);
        //radioButtonPanel.setBackground(Color.PINK);
        mainPanel.setBackground(Color.blue);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(northPanel,BorderLayout.NORTH);

        JPanel southPanel = new JPanel(new GridLayout(1, 3));

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Race racers = new Race();
                saveRecord();
            }
        });
        southPanel.add(saveBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        southPanel.add(resetBtn);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        southPanel.add(cancelButton);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

        private void saveRecord() {
        String raceCode = raceCodeField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String raceType = (String) raceTypeComboBox.getSelectedItem();
        boolean belongToClub = belongToClubYesRadioButton.isSelected();

        try {
            if (RaceDAO.isRaceCodeDuplicate(raceCode)) {
                JOptionPane.showMessageDialog(this, "Duplicate race code. Please enter a unique race code.");
            } else {
                Race race = new Race(raceCode, firstName, lastName, raceType, belongToClub);
                if (RaceDAO.saveRaceRecord(race)) {
                    JOptionPane.showMessageDialog(this, "Record saved successfully.");
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save the record.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error accessing the database.");
        }
    }

    private void resetForm() {
        raceCodeField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        raceTypeComboBox.setSelectedIndex(0);
        belongToClubYesRadioButton.setSelected(false);
        belongToClubNoRadioButton.setSelected(false);
    }
}



