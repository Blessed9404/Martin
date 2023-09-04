/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Mngomezulu kgotlelelo Allet 
 * 222927380
 */

public class StudentRecordClient implements ActionListener{
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static JFrame frame;
    private static JTextField nameField;
    private static JTextField idField;
    private static JTextField scoreField;
    private static JTextArea recordsTextArea;
    private static JButton addButton;
    private static JButton retrieveButton;
    private JButton searchButton,deleteButton;
    private static JButton exitButton, updateButton;
    private static Socket socket;

// Connect to the server, create io streams, and call the method that defines the gui
    public StudentRecordClient() {
        try {
            socket = new Socket("127.0.0.1", 12345);
            System.out.println("Connected to server on " + socket.getInetAddress().getHostAddress());

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());
            createAndShowGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end constructor

//------------------------------------------------------------------------------    

//Create the swing-based gui
    private void createAndShowGUI() {
    frame = new JFrame("Student Record Management");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 400);
    frame.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel(new GridLayout(3, 2));
    inputPanel.add(new JLabel("Name:"));
    nameField = new JTextField();
    inputPanel.add(nameField);
    inputPanel.add(new JLabel("ID:"));
    idField = new JTextField();
    inputPanel.add(idField);
    inputPanel.add(new JLabel("Score:"));
    scoreField = new JTextField();
    inputPanel.add(scoreField);

    frame.add(inputPanel, BorderLayout.NORTH);

    addButton = new JButton("Add Record");
    addButton.addActionListener(this);
    retrieveButton = new JButton("Retrieve Records");
    retrieveButton.addActionListener(this);
    searchButton = new JButton("Search");   
    searchButton.addActionListener(this);
    deleteButton = new JButton("Delete");
    deleteButton.addActionListener(this);
    updateButton = new JButton("Update");
    updateButton.addActionListener(this);
    exitButton = new JButton("Exit");
    exitButton.addActionListener(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addButton);
    buttonPanel.add(retrieveButton);
    buttonPanel.add(searchButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(updateButton);
    buttonPanel.add(exitButton);
    frame.add(buttonPanel, BorderLayout.CENTER);

    recordsTextArea = new JTextArea(10,10);
    recordsTextArea.setEditable(false);
    frame.add(new JScrollPane(recordsTextArea), BorderLayout.SOUTH);
    frame.setVisible(true);

    }//end createAndShowGUI()

//------------------------------------------------------------------------------    
    
// In this method, construct a Student object that is initialized with the values entered by the user on the gui.   
// Send the object to the server.
// Clear the textfields and place the cursor in the name textfield    
    private static void addStudentRecord(String studentName, String studentId, double score) {
    try {
        Student student = new Student(studentName, studentId, score);
        out.writeObject("add record");
        out.flush();
        out.writeObject(student);
        out.flush();

        String response = (String) in.readObject();
        if (response.equalsIgnoreCase("Add_Success")) {
            System.out.println("Record added for " + studentName + " successfully");
        }
    } catch (IOException | ClassNotFoundException ex) {
        Logger.getLogger(StudentRecordClient.class.getName()).log(Level.SEVERE, null, ex);
    }
}

//------------------------------------------------------------------------------    

// In this method, send a string to the server that indicates a retrieve request.
// Read the Arraylist Object sent from the server, and call the method to display the student records.
    private static void retrieveStudentRecords() {
        try {
            out.writeObject("GET_RESULTS");
            out.flush();
                       
            ArrayList<Student> students = (ArrayList<Student>) in.readObject();
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                String studentName = student.getStudentName();
                String studentId = student.getStudentId();
                double score = student.getScore();
                recordsTextArea.append(studentName + " " + studentId + " " + score + "\n");
            }
//            Object receivedObject = in.readObject();
//
//            if (receivedObject instanceof List<?>) {
//                List<Student> studentList = (List<Student>) receivedObject;
                //displayStudentRecords(studentList);
            } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(StudentRecordClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void searchStudentRecord(String studentName) {
        try {
            out.writeObject("SEARCH");
            out.flush();

            out.writeObject(studentName);
            out.flush();

            Student foundStudent = (Student) in.readObject();

            if (foundStudent != null) {
                String studentId = foundStudent.getStudentId();
                double score = foundStudent.getScore();
                recordsTextArea.append("Found Student: " + studentName + " " + studentId + " " + score + "\n");
            } else {
                recordsTextArea.append("Student not found: " + studentName + "\n");
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(StudentRecordClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //------------------------------------------------------------------------------
    private static boolean deleteStudentRecord(String studentName) {
        try {
        out.writeObject("DELETE");
        out.flush();
        
        out.writeObject(studentName);
        out.flush();
        
        boolean deleted = (boolean) in.readObject();
        
        if (deleted) {
            return true;
        } else {
            return false; 
        }
    } catch (IOException | ClassNotFoundException ex) {
        Logger.getLogger(StudentRecordClient.class.getName()).log(Level.SEVERE, null, ex);
        return false; // Handle exceptions gracefully
    }
    }

//------------------------------------------------------------------------------
    // New method to update a student's score    
    private static boolean updateStudentScore(String studentName, double newScore) {
        try {
            out.writeObject("UPDATE");
            out.flush();

            out.writeObject(studentName);
            out.flush();

            out.writeObject(newScore);
            out.flush();

            boolean updated = (boolean) in.readObject();

            if (updated) {
                return true; 
            } else {
                return false; 
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(StudentRecordClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
// In this method, you must append the records in the arraylist (sent as a parameter) in the textarea.
//    private static void displayStudentRecords(List<Student> studentList) {
//        recordsTextArea.setText("");
//        for (Student student : studentList) {
//            recordsTextArea.append(student.toString() + "\n");
//        }
//
//   }//end displayStudentRecords()

//------------------------------------------------------------------------------    

// Send a string value to the server indicating an exit request. 
// Read the returning string from the server
// Close all connections and exit the application    
    private static void closeConnection() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        
    }//end closeConnection()

//------------------------------------------------------------------------------    

// Handle all action events generated by the user-interaction with the buttons
    public void actionPerformed(ActionEvent e) {  
        if (e.getSource() == addButton) {
        String studentName = nameField.getText();
        String studentId =  idField.getText();     
        double score = Double.parseDouble(scoreField.getText());
        addStudentRecord(studentName, studentId, score);
        } else if (e.getSource() == retrieveButton) {
            retrieveStudentRecords();
        } else if (e.getSource() == searchButton) {
        String studentName = nameField.getText();
        searchStudentRecord(studentName);
        } else if (e.getSource() == exitButton) {
            closeConnection();
        }else if (e.getSource() == deleteButton) {
            String studentName = nameField.getText();
            boolean deleted = deleteStudentRecord(studentName);
            
            if (deleted) {
                recordsTextArea.append("Deleted Student: " + studentName + "\n");
            } else {
                recordsTextArea.append("Student not found or not deleted: " + studentName + "\n");
            }
        }else if (e.getSource() == updateButton) { 
            String studentName = nameField.getText();
            double newScore = Double.parseDouble(scoreField.getText());
            boolean updated = updateStudentScore(studentName, newScore);

            if (updated) {
                recordsTextArea.append("Updated score for " + studentName + " to " + newScore + "\n");
            } else {
                recordsTextArea.append("Student not found or score not updated for " + studentName + "\n");
            }
        }
        else if (e.getSource() == updateButton) { 
            String studentName = nameField.getText();
            double newScore = Double.parseDouble(scoreField.getText());
            boolean updated = updateStudentScore(studentName, newScore);

            if (updated) {
                recordsTextArea.append("Updated score for " + studentName + " to " + newScore + "\n");
            } else {
                recordsTextArea.append("Student not found or score not updated for " + studentName + "\n");
            }
        } else if (e.getSource() == exitButton) {
            closeConnection();
        }
    }

    //end actionPerformed

//------------------------------------------------------------------------------    

// Execute the application by calling the necessary methods   
   public static void main(String[] args) {
       StudentRecordClient sr = new StudentRecordClient();
    }//end mai
}//end class

