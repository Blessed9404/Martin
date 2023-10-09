/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ac.za.cput.mycityprotectss;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.mail.Session;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.mindrot.jbcrypt.BCrypt;


/**
 *
 * @author Olebaka
 */
public class MyCityProtectss {
    
    private static final int BCRYPT_WORKLOAD = 12;

     private static final List<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static JFrame rootFrame;
    private static JPanel loginFrame;
    private static JPanel signupFrame;
    private static JPanel forgotPasswordFrame;
    private static JPanel incidentFrame;
    private static JPanel communityFrame;
    private static JPanel profileFrame;
    private static JPanel navigationFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        rootFrame = new JFrame("CITY PROTECT");
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and show login frame
        loginFrame = createLoginFrame();
        rootFrame.add(loginFrame);
        
        
        rootFrame.setSize(1900, 1600);
        rootFrame.pack();
        rootFrame.setVisible(true);
    }

    private static JPanel createLoginFrame() {
        JPanel loginPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        //welcomeLabel.setBackground(Color.blue);
        loginPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel loginInnerPanel = new JPanel(new GridLayout(5, 1));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        JButton signupButton = new JButton("Don't have an account? Sign Up now");
        
        loginInnerPanel.setBackground(Color.white);

        loginInnerPanel.add(emailLabel);
        loginInnerPanel.add(emailField);
        loginInnerPanel.add(passwordLabel);
        loginInnerPanel.add(passwordField);
        loginInnerPanel.add(loginButton);
        loginInnerPanel.add(forgotPasswordButton);
        loginInnerPanel.add(signupButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                try (Connection connection = DBConnection.getConnection()) {
                    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, email);
                        statement.setString(2, password);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                currentUser = new User(
                                    resultSet.getString("first_name"),
                                    resultSet.getString("last_name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password")
                                );
                                showNavigationWindow();
                                 JOptionPane.showMessageDialog(rootFrame, "Login successful! Welcome, " + currentUser.getFirstName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            }else {
                        JOptionPane.showMessageDialog(rootFrame, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(rootFrame, "An error occurred while logging in.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                for (User user : users) {
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                        currentUser = user;
                        showNavigationWindow();
                        return;
                    }
                }

            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showForgotPasswordFrame();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignupFrame();
            }
        });

        loginPanel.add(loginInnerPanel, BorderLayout.CENTER);

        return loginPanel;
    }

    private static void showSignupFrame() {
        signupFrame = createSignupFrame();
        rootFrame.getContentPane().removeAll();
        rootFrame.add(signupFrame);
        rootFrame.revalidate();
        rootFrame.repaint();
    }

    private static JPanel createSignupFrame() {
        JPanel signupPanel = new JPanel(new BorderLayout());

        JLabel signupLabel = new JLabel("Signup Form", SwingConstants.CENTER);
        signupLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        signupPanel.add(signupLabel, BorderLayout.NORTH);

        JPanel signupInnerPanel = new JPanel(new GridLayout(0, 2));
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton signupSubmitButton = new JButton("Sign up");
        JButton backButton = new JButton("Back to Login");

        signupInnerPanel.add(firstNameLabel);
        signupInnerPanel.add(firstNameField);
        signupInnerPanel.add(lastNameLabel);
        signupInnerPanel.add(lastNameField);
        signupInnerPanel.add(emailLabel);
        signupInnerPanel.add(emailField);
        signupInnerPanel.add(passwordLabel);
        signupInnerPanel.add(passwordField);
        signupInnerPanel.add(confirmPasswordLabel);
        signupInnerPanel.add(confirmPasswordField);
        signupInnerPanel.add(signupSubmitButton);
        signupInnerPanel.add(backButton);

   signupSubmitButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Implement signup submit logic
        
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Check if the email already exists
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                JOptionPane.showMessageDialog(rootFrame, "Email already exists. Please choose a different email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
//
//        if (!password.matches("^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$")) {
//            JOptionPane.showMessageDialog(rootFrame, "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 digit, 1 special character, and be at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(rootFrame, "Passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User(firstName, lastName, email, password);
        users.add(newUser);
        JOptionPane.showMessageDialog(rootFrame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        
        try (Connection connection = DBConnection.getConnection()) {
        //String sql = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        String sql = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.executeUpdate();
        }
        JOptionPane.showMessageDialog(rootFrame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        JOptionPane.showMessageDialog(rootFrame, "An error occurred while creating the account.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
});



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootFrame.getContentPane().removeAll();
                rootFrame.add(loginFrame);
                rootFrame.revalidate();
                rootFrame.repaint();
            }
        });

        signupPanel.add(signupInnerPanel, BorderLayout.CENTER);

        return signupPanel;
    }


     private static void showForgotPasswordFrame() {
        forgotPasswordFrame = createForgotPasswordFrame();
        rootFrame.getContentPane().removeAll();
        rootFrame.add(forgotPasswordFrame);
        rootFrame.revalidate();
        rootFrame.repaint();
    }

    private static JPanel createForgotPasswordFrame() {
    JPanel forgotPasswordPanel = new JPanel(new BorderLayout());

    JLabel forgotPasswordLabel = new JLabel("Forgot Password", SwingConstants.CENTER);
    forgotPasswordLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    forgotPasswordPanel.add(forgotPasswordLabel, BorderLayout.NORTH);

    JPanel forgotPasswordInnerPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    JLabel emailLabel = new JLabel("Email:");
    JTextField emailField = new JTextField();
    JButton resetPasswordButton = new JButton("Reset Password");
    JButton backButton = new JButton("Back to Login");

    forgotPasswordInnerPanel.add(emailLabel);
    forgotPasswordInnerPanel.add(emailField);
    forgotPasswordInnerPanel.add(resetPasswordButton);
    forgotPasswordInnerPanel.add(backButton);

resetPasswordButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();

        // Check if the email exists in the database (you need to implement this)
        if (emailExistsInDatabase(email)) {
            // Generate a reset token
            String resetToken = generateResetToken();

            // Store the token and expiration timestamp in the database
            boolean tokenStored = storeResetTokenInDatabase(email, resetToken);

            if (tokenStored) {
                // Send the password reset email with the reset link
                boolean emailSent = sendPasswordResetEmail(email, resetToken);

                if (emailSent) {
                    JOptionPane.showMessageDialog(rootFrame, "A password reset link has been sent to " + email, "Password Reset", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(rootFrame, "Failed to send the password reset email. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(rootFrame, "Failed to initiate the password reset. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(rootFrame, "Email not found. Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        private static boolean emailExistsInDatabase(String email) {
    try (Connection connection = DBConnection.getConnection()) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // If count > 0, email exists in the database
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false; // Default to false if there's an error or email not found
}

    });


    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            rootFrame.getContentPane().removeAll();
            rootFrame.add(loginFrame);
            rootFrame.revalidate();
            rootFrame.repaint();
        }
    });

    forgotPasswordPanel.add(forgotPasswordInnerPanel, BorderLayout.CENTER);

    return forgotPasswordPanel;
}

 private static String generateResetToken() {
        // Generate a unique reset token (e.g., a combination of random characters and a timestamp)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 20; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return token.toString();
    
}

private static boolean storeResetTokenInDatabase(String email, String resetToken) {
    try (Connection connection = DBConnection.getConnection()) {
        // Implement code to store the reset token and expiration timestamp in the database
        String sql = "INSERT INTO reset_tokens (email, token, expiration_timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, resetToken);
            // Set an expiration timestamp (e.g., 24 hours from now)
            long expirationTimestamp = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
            statement.setTimestamp(3, new Timestamp(expirationTimestamp)); // Corrected


            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

private static boolean sendPasswordResetEmail(String email, String resetToken) {
    // Determine the base URL of your application
    String baseUrl = "https://example.com"; // Replace with your actual base URL

    // Construct the reset password URL with the token
    String resetUrl = baseUrl + "/reset-password?token=" + resetToken;

    // The email body should contain this URL
    String emailBody = "To reset your password, click the following link:\n" + resetUrl;

    final String smtpUsername = "ses-smtp-user.20230919-002617";
    final String smtpPassword = "BDay9+bepAMzcBGz4ZmAqCNZb+3kJiY1ktUYy173SkT";

    Properties prop = new Properties();
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.host", "email-smtp.eu-west-1.amazonaws.com"); 
    prop.put("mail.smtp.port", "587");

    Session session;
    session = Session.getInstance(prop, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUsername, new String(smtpPassword));




        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUsername)); 
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Password Reset");
        message.setText(emailBody);

        Transport.send(message);

        return true;
    } catch (MessagingException ex) {
        ex.printStackTrace();
        return false;
    }
}

private static JPanel createPasswordResetFrame(String resetToken) {
    JPanel resetPasswordPanel = new JPanel(new BorderLayout());

    JLabel resetPasswordLabel = new JLabel("Password Reset", SwingConstants.CENTER);
    resetPasswordLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    resetPasswordPanel.add(resetPasswordLabel, BorderLayout.NORTH);

    JPanel resetPasswordInnerPanel = new JPanel(new GridLayout(0, 2));
    JLabel newPasswordLabel = new JLabel("New Password:");
    JPasswordField newPasswordField = new JPasswordField();
    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    JPasswordField confirmPasswordField = new JPasswordField();
    JButton resetPasswordButton = new JButton("Reset Password");

    resetPasswordInnerPanel.add(newPasswordLabel);
    resetPasswordInnerPanel.add(newPasswordField);
    resetPasswordInnerPanel.add(confirmPasswordLabel);
    resetPasswordInnerPanel.add(confirmPasswordField);
    resetPasswordInnerPanel.add(resetPasswordButton);

    resetPasswordButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            char[] newPassword = newPasswordField.getPassword();
            char[] confirmPassword = confirmPasswordField.getPassword();

            if (isValidPassword(newPassword, confirmPassword)) {
                // Validate the reset token
                if (isValidResetToken(resetToken)) {
                    boolean passwordUpdated = updatePasswordInDatabase(newPassword);

                    if (passwordUpdated) {
                        JOptionPane.showMessageDialog(rootFrame, "Password reset successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        showLoginFrame();
                    } else {
                        JOptionPane.showMessageDialog(rootFrame, "Failed to reset the password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Handle invalid or expired token
                    JOptionPane.showMessageDialog(rootFrame, "Invalid or expired reset token. Please request a new one.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(rootFrame, "Invalid input. Please check your password and confirmation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

private static boolean isValidResetToken(String resetToken) {
    try (Connection connection = DBConnection.getConnection()) {
        // Query the database to check if the reset token exists and is not expired
        String sql = "SELECT COUNT(*) FROM reset_tokens WHERE token = ? AND expiration_timestamp > ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, resetToken);

            // Set the current timestamp to compare with the expiration timestamp
            long currentTimestamp = System.currentTimeMillis();
            statement.setTimestamp(2, new Timestamp(currentTimestamp));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the token is valid
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false; // Token is considered invalid in case of any database error
}

        private boolean updatePasswordInDatabase(char[] newPassword) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

    });

    resetPasswordPanel.add(resetPasswordInnerPanel, BorderLayout.CENTER);

    return resetPasswordPanel;
}

private static boolean isValidPassword(char[] newPassword, char[] confirmPassword) {
    // Check if the password and confirm password match
    String newPasswordStr = new String(newPassword);
    String confirmPasswordStr = new String(confirmPassword);
    
    if (!newPasswordStr.equals(confirmPasswordStr)) {
        return false;
    }

    // Check if the password meets complexity requirements
    if (newPasswordStr.length() < 8) {
        return false;
    }

    // Check for at least one uppercase letter
    if (!newPasswordStr.matches(".[A-Z].")) {
        return false;
    }

    // Check for at least one lowercase letter
    if (!newPasswordStr.matches(".[a-z].")) {
        return false;
    }

    // Check for at least one digit
    if (!newPasswordStr.matches(".\\d.")) {
        return false;
    }

    // Check for at least one special character (you can customize the character set)
    if (!newPasswordStr.matches(".[@#$%^&+=].")) {
        return false;
    }

    // If all checks pass, return true
    return true;
}


private static boolean updatePasswordInDatabase(User currentUser, char[] newPassword) {
    try (Connection connection = DBConnection.getConnection()) {
        // Update the password in the database for the user with the provided email
        String sql = "UPDATE users SET password = ? WHERE email = ?";

        // Hash and salt the new password before updating
        String hashedPassword = hashAndSaltPassword(new String(newPassword));

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hashedPassword);
            statement.setString(2, currentUser.getEmail()); // Use the email property of the current user
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}







    private static JPanel createNavigationFrame() {
        JPanel navigationPanel = new JPanel(new BorderLayout());

        JLabel navLabel = new JLabel("User Navigation", SwingConstants.CENTER);
        navLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        navigationPanel.add(navLabel, BorderLayout.NORTH);

        JPanel navButtonPanel = new JPanel(new GridLayout(6, 1));
        JButton incidentButton = new JButton("Incident Reporting");
        JButton communityButton = new JButton("Community Forum");
        JButton crimeButton = new JButton("Crime Statistics");
        JButton safetyButton = new JButton("Safety Tips");
        JButton profileButton = new JButton("User Profile");
        JButton logoutButton = new JButton("Log Out");

        navButtonPanel.add(incidentButton);
        navButtonPanel.add(communityButton);
        navButtonPanel.add(crimeButton);
        navButtonPanel.add(safetyButton);
        navButtonPanel.add(profileButton);
        navButtonPanel.add(logoutButton);

        incidentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showIncidentReportingFrame();
            }
        });

        communityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCommunityForumFrame();
            }
        });
            
         crimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCrimeStatisticsFrame();
            }

          private void showCrimeStatisticsFrame() {
    JPanel crimeStatisticsFrame = createCrimeStatisticsFrame();
    rootFrame.getContentPane().removeAll();
    rootFrame.add(crimeStatisticsFrame);
    rootFrame.revalidate();
    rootFrame.repaint();
}

        });
            
        
         safetyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSafetyTipsFrame();
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserProfileFrame();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUser = null;
                rootFrame.getContentPane().removeAll();
                rootFrame.add(loginFrame);
                rootFrame.revalidate();
                rootFrame.repaint();
            }
        });

        navigationPanel.add(navButtonPanel, BorderLayout.CENTER);

        return navigationPanel;
    }

    private static void showIncidentReportingFrame() {
        incidentFrame = createIncidentReportingFrame();
        rootFrame.getContentPane().removeAll();
        rootFrame.add(incidentFrame);
        rootFrame.revalidate();
        rootFrame.repaint();
    }

    private static JPanel createIncidentReportingFrame() {
    JPanel incidentPanel = new JPanel(new BorderLayout());

    JLabel incidentLabel = new JLabel("Incident Reporting", SwingConstants.CENTER);
    incidentLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    incidentPanel.add(incidentLabel, BorderLayout.NORTH);

    JPanel incidentInnerPanel = new JPanel(new GridLayout(6, 2));
    JLabel incidentTypeLabel = new JLabel("Incident Type:");
    JTextField incidentTypeField = new JTextField();
    JLabel locationLabel = new JLabel("Location:");
    JTextField locationField = new JTextField();
    JLabel timeLabel = new JLabel("Time:");
    JTextField timeField = new JTextField();
    JButton uploadImageButton = new JButton("Upload Image");
    JButton submitReportButton = new JButton("Submit Report");
    JButton backButton = new JButton("Back to Navigation");

    

    incidentInnerPanel.add(incidentTypeLabel);
    incidentInnerPanel.add(incidentTypeField);
    incidentInnerPanel.add(locationLabel);
    incidentInnerPanel.add(locationField);
    incidentInnerPanel.add(timeLabel);
    incidentInnerPanel.add(timeField);
    incidentInnerPanel.add(uploadImageButton);
    incidentInnerPanel.add(submitReportButton);
    incidentInnerPanel.add(backButton);

    // A JLabel to display the uploaded image
    JLabel uploadedImageLabel = new JLabel();
    incidentInnerPanel.add(uploadedImageLabel);

    uploadImageButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(rootFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Display the selected image
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                uploadedImageLabel.setIcon(imageIcon);
            }
        }
    });

    submitReportButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement incident reporting logic
            // For simplicity, we'll show a message dialog
            JOptionPane.showMessageDialog(rootFrame, "Incident report submitted successfully!", "Report Submitted", JOptionPane.INFORMATION_MESSAGE);
        }
    });

    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNavigationWindow();
        }
    });

    incidentPanel.add(incidentInnerPanel, BorderLayout.CENTER);

    return incidentPanel;
}


private static void showCommunityForumFrame() {
        Community currentCommunity = null;
        JPanel currentPanel = createCommunityForumFrame(currentCommunity);
        rootFrame.getContentPane().removeAll();
        rootFrame.add(currentPanel);
        rootFrame.revalidate();
        rootFrame.repaint();
    }

    private static JPanel createCommunityForumFrame(Community community) {
        JPanel communityPanel = new JPanel(new BorderLayout());

        JLabel communityLabel = new JLabel("Community Forum: " + community.getName(), SwingConstants.CENTER);
        communityLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        communityPanel.add(communityLabel, BorderLayout.NORTH);

        JTextArea forumTextArea = new JTextArea();
        forumTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(forumTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send Message");
        JButton backButton = new JButton("Back to Navigation");
        JButton createRoomButton = new JButton("Create Room");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement sending message logic
                String message = messageField.getText();
                forumTextArea.append("You: " + message + "\n");
                messageField.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNavigationWindow();
            }
        });

        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement creating a new room
                showCreateRoomDialog(community);
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(inputPanel, BorderLayout.SOUTH);

        communityPanel.add(contentPanel, BorderLayout.CENTER);
        communityPanel.add(backButton, BorderLayout.SOUTH);
        communityPanel.add(createRoomButton, BorderLayout.WEST);

        return communityPanel;
    }

   private static void showCreateRoomDialog(Community community) {
    // Create a dialog for creating a new room
    JPanel dialogPanel = new JPanel(new BorderLayout());
    JTextField roomNameField = new JTextField(20);
    JPasswordField passwordField = new JPasswordField(20);

    dialogPanel.add(new JLabel("Room Name:"));
    dialogPanel.add(roomNameField, BorderLayout.NORTH);
    dialogPanel.add(new JLabel("Room Password:"));
    dialogPanel.add(passwordField, BorderLayout.CENTER);

    int result = JOptionPane.showConfirmDialog(rootFrame, dialogPanel, "Create New Room", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        // User clicked "OK," so create the room and store it in your database
        String roomName = roomNameField.getText();
        char[] password = passwordField.getPassword();
        String hashedPassword = hashAndSaltPassword(new String(password)); // Hash and salt the password

        // Store the room in your database and associate it with the current community
        Room newRoom = new Room(roomName, hashedPassword);
        // Save 'newRoom' to your database and associate it with 'community'

        // Optionally, you can refresh the UI or update the room list
        // based on the changes made in your database
    }
}

    private static void showNavigationWindow() {
        // Implement your navigation window
        // ...
    }

    
    private static JPanel createCrimeStatisticsFrame() {
    JPanel crimeStatisticsPanel = new JPanel(new BorderLayout());

    JLabel crimeStatisticsLabel = new JLabel("Crime Statistics", SwingConstants.CENTER);
    crimeStatisticsLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    crimeStatisticsPanel.add(crimeStatisticsLabel, BorderLayout.NORTH);

    JTextArea statisticsTextArea = new JTextArea();
    statisticsTextArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(statisticsTextArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // Populate the crime statistics text area with information
    statisticsTextArea.append("Crime Statistics:\n");
    statisticsTextArea.append("- Total reported incidents: 100\n");
    statisticsTextArea.append("- Robbery cases: 30\n");
    statisticsTextArea.append("- Burglary cases: 20\n");
    statisticsTextArea.append("- Assault cases: 50\n");

    // Create a dataset and add data
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(30, "Cases", "Robbery");
    dataset.addValue(20, "Cases", "Burglary");
    dataset.addValue(50, "Cases", "Assault");

    // Create a bar chart
    JFreeChart chart = ChartFactory.createBarChart(
        "Crime Statistics", // Chart title
        "Crime Type",      // X-axis label
        "Number of Cases", // Y-axis label
        dataset,
        PlotOrientation.VERTICAL,
        true, true, false);

    // Create a ChartPanel to display the chart
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));

    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    JButton backButton = new JButton("Back to Navigation");

    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNavigationWindow();
        }
    });

    buttonPanel.add(backButton);

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.add(chartPanel, BorderLayout.CENTER);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);

    crimeStatisticsPanel.add(contentPanel, BorderLayout.CENTER);

    return crimeStatisticsPanel;
}

    private static void showSafetyTipsFrame() {
        JPanel safetyFrame = createSafetyTipsFrame();
        rootFrame.getContentPane().removeAll();
        rootFrame.add(safetyFrame);
        rootFrame.revalidate();
        rootFrame.repaint();
        
    }
 private static JPanel createSafetyTipsFrame() {
    JPanel safetyTipsPanel = new JPanel(new BorderLayout());

    JLabel safetyTipsLabel = new JLabel("Safety Tips", SwingConstants.CENTER);
    safetyTipsLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    safetyTipsPanel.add(safetyTipsLabel, BorderLayout.NORTH);

    JTextArea tipsTextArea = new JTextArea();
    tipsTextArea.setEditable(false);
    tipsTextArea.setWrapStyleWord(true);
    tipsTextArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(tipsTextArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // Add safety tips content to the text area
    tipsTextArea.append("1. Always lock your doors and windows when leaving your home.\n");
    tipsTextArea.append("2. Do not share personal information with strangers online.\n");
    tipsTextArea.append("3. In case of a fire, use the nearest fire exit and call the fire department.\n");

    JButton emergencyContactsButton = new JButton("Emergency Contacts");
    JButton backButton = new JButton("Back to Navigation");

    emergencyContactsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showEmergencyContactsFrame();
        }

        private static void showEmergencyContactsFrame() {
            JPanel emergencyContactsFrame = createEmergencyContactsFrame();
            rootFrame.getContentPane().removeAll();
            rootFrame.add(emergencyContactsFrame);
            rootFrame.revalidate();
            rootFrame.repaint();
}
private static JPanel createEmergencyContactsFrame() {
    JPanel emergencyContactsPanel = new JPanel(new BorderLayout());

    JLabel emergencyContactsLabel = new JLabel("Emergency Contacts", SwingConstants.CENTER);
    emergencyContactsLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    emergencyContactsPanel.add(emergencyContactsLabel, BorderLayout.NORTH);

    JTextPane contactsTextPane = new JTextPane();
    contactsTextPane.setEditable(false);
    contactsTextPane.setContentType("text/html"); // Use HTML content
    contactsTextPane.setText("<html>"
            + "<b>Emergency Services:</b> <a href=\"tel:10111\">10111</a><br>"
            + "<b>Police (SAPS):</b> <a href=\"tel:0860010111\">08600 10111</a><br>"
            + "<b>Ambulance:</b> <a href=\"tel:10177\">10177</a><br>"
            + "<b>Fire Department:</b> <a href=\"tel:10177\">10177</a><br>"
            + "<b>Women Abuse Helpline:</b> <a href=\"tel:0800150150\">0800 150 150</a><br>"
            + "<b>Childline:</b> <a href=\"tel:0800055555\">0800 055 555</a><br>"
            + "<b>Suicide Crisis Helpline:</b> <a href=\"tel:0800567567\">0800 567 567</a><br>"
            + "</html>"
    );

    contactsTextPane.addHyperlinkListener(e -> {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        String urlString = e.getDescription(); // Get the URL as a string

        if (urlString != null && urlString.startsWith("tel:")) {
            // Handle phone number link
            String phoneNumber = urlString.substring(4); // Remove "tel:" prefix
            JOptionPane.showMessageDialog(null, "Calling " + phoneNumber, "Calling", JOptionPane.INFORMATION_MESSAGE);
            // You can add code here to initiate a call using the phone number.
        } else {
            try {
                URL url = new URL(urlString); // Convert the string URL to a URL object

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    URI uri = url.toURI(); // Convert URL to URI
                    // Open the URI in the default web browser
                    Desktop.getDesktop().browse(uri);
                } else {
                    // Handle the case where the system doesn't support browsing
                    JOptionPane.showMessageDialog(null, "URL opening is not supported on this system.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // Handle any exceptions that might occur when opening the URL
            }
        }
    }
});


    JButton backButton = new JButton("Back to Safety Tips");

    backButton.addActionListener(e -> showSafetyTipsFrame());

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.add(contactsTextPane, BorderLayout.CENTER);
    contentPanel.add(backButton, BorderLayout.SOUTH);

    emergencyContactsPanel.add(contentPanel, BorderLayout.CENTER);

    return emergencyContactsPanel;
}


    });

    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNavigationWindow(); // Call to show navigation window
        }
    });

    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    buttonPanel.add(emergencyContactsButton);
    buttonPanel.add(backButton);

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.add(scrollPane, BorderLayout.CENTER);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);

    safetyTipsPanel.add(contentPanel, BorderLayout.CENTER);

    return safetyTipsPanel;
}


    private static void showUserProfileFrame() {
        profileFrame = createUserProfileFrame();
        rootFrame.getContentPane().removeAll();
        rootFrame.add(profileFrame);
        rootFrame.revalidate();
        rootFrame.repaint();
    }

    private static JPanel createUserProfileFrame() {
        JPanel profilePanel = new JPanel(new BorderLayout());

        JLabel profileLabel = new JLabel("User Profile", SwingConstants.CENTER);
        profileLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        profilePanel.add(profileLabel, BorderLayout.NORTH);

        JTextArea profileInfoTextArea = new JTextArea();
        profileInfoTextArea.setEditable(false);
        profileInfoTextArea.setWrapStyleWord(true);
        profileInfoTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(profileInfoTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton deleteAccountButton = new JButton("Delete Account");
        JButton backButton = new JButton("Back to Navigation");

        profileInfoTextArea.append("Name: " + currentUser.getFirstName() + " " + currentUser.getLastName() + "\n");
        profileInfoTextArea.append("Email: " + currentUser.getEmail() + "\n");

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmResult = JOptionPane.showConfirmDialog(rootFrame, "Are you sure you want to delete your account?", "Confirm Account Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    users.remove(currentUser);
                    currentUser = null;
                    showLoginFrame();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNavigationWindow();
            }
        });

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(deleteAccountButton, BorderLayout.SOUTH);

        profilePanel.add(contentPanel, BorderLayout.CENTER);
        profilePanel.add(backButton, BorderLayout.SOUTH);

        return profilePanel;
    }

    private static void showLoginFrame() {
        loginFrame = createLoginFrame();
        rootFrame.getContentPane().removeAll();
        rootFrame.add(loginFrame);
        rootFrame.revalidate();
        rootFrame.repaint();
    }
    
        // Method to hash and salt the password
    private static String hashAndSaltPassword(String password) {
        // Generate a random salt
        String salt = BCrypt.gensalt(BCRYPT_WORKLOAD);

        // Hash the password with the salt
        return BCrypt.hashpw(password, salt);
    }

     private static class Community {
        private String name;

        public Community(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
     
     private static class Room {
        private String name;
        private String password; // Hash and salt the password for security

        public Room(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }
    }
}
