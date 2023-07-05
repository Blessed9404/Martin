/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package za.ac.cput.racemarathon;

import javax.swing.SwingUtilities;

/**
 *
 * @author Mngomezulu kgotlelelo Allet
 */
public class RunRaceRegistration {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RaceRegistrationGUI().setVisible(true);
            }
        });
    }
    
}
