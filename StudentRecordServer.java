/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Mngomezulu kgotlelelo Allet
 * 222927380
 */
public class StudentRecordServer {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Object receivedObject;
    private List<Student> studentRecords = new ArrayList<>();
//In the constructor listen for incoming client connections 
    public StudentRecordServer() {
        try {
            serverSocket = new ServerSocket(12345, 1);
            System.out.println("Server listening on port 12345");

            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            getStreams();
            processClient();

        } catch (IOException ioe) {
            System.out.println("Exception: " + ioe.getMessage());
        }
    }//end constructor
//------------------------------------------------------------------------------
    //create the io streams
    public void getStreams() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }//end getStreams()
//------------------------------------------------------------------------------  
    //Declare arraylist and handle the communication between server and client
    public void processClient() {
        try {
            while (true) {
                receivedObject = in.readObject();
                if (receivedObject instanceof String) {
                    String request = (String) receivedObject;
                    if (request.equalsIgnoreCase("add record")) {
                        Student studentToAdd = (Student) in.readObject();
                        
                        boolean studentExists = false;
                        for (Student student : studentRecords) {
                            if (student.getStudentName().equals(studentToAdd.getStudentName())) {
                                studentExists = true;
                                break;
                            }
                        }
                        
                        if (!studentExists) {
                            studentRecords.add(studentToAdd);
                            out.writeObject("Add_Success");
                            out.flush();
                        }
                    } else if (request.equalsIgnoreCase("GET_RESULTS")) {
                        out.writeObject(studentRecords);
                        out.flush();
                    }
                    if (request.equalsIgnoreCase("SEARCH")) {
                        String searchName = (String) in.readObject();
                        Student foundStudent = null;

                        for (Student student : studentRecords) {
                            if (student.getStudentName().equalsIgnoreCase(searchName)) {
                                foundStudent = student;
                                break;
                            }
                        }

                        out.writeObject(foundStudent);
                        out.flush();
                    }if (request.equalsIgnoreCase("DELETE")) {
                        String deleteName = (String) in.readObject();
                        boolean deleted = false;
                        //// Iterate through the list of students and remove the matching student
                        Iterator<Student> iterator = studentRecords.iterator();
                        while (iterator.hasNext()) {
                            Student student = iterator.next();
                            if (student.getStudentName().equalsIgnoreCase(deleteName)) {
                                iterator.remove();
                                deleted = true;
                                break;
                            }
                        }
                        out.writeObject(deleted);
                        out.flush();
                    } else if (request.equalsIgnoreCase("UPDATE")) {
                        String updateName = (String) in.readObject();
                        double newScore = (double) in.readObject();
                        boolean updated = false;
                        for (Student student : studentRecords) {
                            if (student.getStudentName().equalsIgnoreCase(updateName)) {
                                student.setScore(newScore);
                                updated = true;
                                break;
                            }
                        }
                        out.writeObject(updated);
                        out.flush();
                    }

                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(StudentRecordServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//end processClient

    private void closeConnection() {
        try {
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
//execute the program and call all necessary methods
    public static void main(String[] args) {
        StudentRecordServer sr = new StudentRecordServer();
        sr.closeConnection();
    }
}
//end class

