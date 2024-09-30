package jdbc.com;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class UserRegistrationPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textFieldEmail; // Email as Username
    private JPasswordField passwordField; // Password field
    private JTextField textFieldName;
    private JTextField textFieldMobile;
    private JTextField textFieldNationality;
    private JTextField textFieldOccupation;
    private JTextField textFieldState;
    private JTextField textFieldCity;
    private JTextField textFieldLocality;
    private JTextField textFieldPin;
    private JDateChooser dateChooser;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserRegistrationPage frame = new UserRegistrationPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserRegistrationPage() {
        getContentPane().setBackground(new Color(192, 192, 192));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(430, 0, 503, 700);
        getContentPane().setLayout(null);

        // Name
        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblName.setBounds(89, 74, 100, 30);
        getContentPane().add(lblName);
        textFieldName = new JTextField();
        textFieldName.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldName.setBounds(209, 74, 200, 30);
        getContentPane().add(textFieldName);

        // Email (as Username)
        JLabel lblEmail = new JLabel("Email (Username)");
        lblEmail.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblEmail.setBounds(77, 124, 150, 30);
        getContentPane().add(lblEmail);
        textFieldEmail = new JTextField();
        textFieldEmail.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldEmail.setBounds(209, 122, 200, 30);
        getContentPane().add(textFieldEmail);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblPassword.setBounds(89, 174, 100, 30);
        getContentPane().add(lblPassword);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        passwordField.setBounds(209, 174, 200, 30);
        getContentPane().add(passwordField);

        // Mobile
        JLabel lblMobile = new JLabel("Mobile");
        lblMobile.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblMobile.setBounds(89, 224, 100, 30);
        getContentPane().add(lblMobile);
        textFieldMobile = new JTextField();
        textFieldMobile.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldMobile.setBounds(209, 224, 200, 30);
        textFieldMobile.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                // Restrict input to numbers only
                if (!Character.isDigit(e.getKeyChar()) || textFieldMobile.getText().length() >= 10) {
                    e.consume(); // Ignore the event if the character is not a digit or if length is 10
                }
            }
        });
        getContentPane().add(textFieldMobile);

        // Date of Birth
        JLabel lblDob = new JLabel("Date of Birth");
        lblDob.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblDob.setBounds(89, 274, 100, 30);
        getContentPane().add(lblDob);
        dateChooser = new JDateChooser();
        dateChooser.setBounds(209, 274, 200, 30);
        getContentPane().add(dateChooser);

        // Nationality
        JLabel lblNationality = new JLabel("Nationality");
        lblNationality.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblNationality.setBounds(89, 324, 100, 30);
        getContentPane().add(lblNationality);
        textFieldNationality = new JTextField();
        textFieldNationality.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldNationality.setBounds(209, 324, 200, 30);
        getContentPane().add(textFieldNationality);

        // Occupation
        JLabel lblOccupation = new JLabel("Occupation");
        lblOccupation.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblOccupation.setBounds(89, 374, 100, 30);
        getContentPane().add(lblOccupation);
        textFieldOccupation = new JTextField();
        textFieldOccupation.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldOccupation.setBounds(209, 374, 200, 30);
        getContentPane().add(textFieldOccupation);

        // State
        JLabel lblState = new JLabel("State");
        lblState.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblState.setBounds(89, 424, 100, 30);
        getContentPane().add(lblState);
        textFieldState = new JTextField();
        textFieldState.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldState.setBounds(209, 424, 200, 30);
        getContentPane().add(textFieldState);

        // City
        JLabel lblCity = new JLabel("City");
        lblCity.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblCity.setBounds(89, 474, 100, 30);
        getContentPane().add(lblCity);
        textFieldCity = new JTextField();
        textFieldCity.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldCity.setBounds(209, 474, 200, 30);
        getContentPane().add(textFieldCity);

        // Locality
        JLabel lblLocality = new JLabel("Locality");
        lblLocality.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblLocality.setBounds(89, 524, 100, 30);
        getContentPane().add(lblLocality);
        textFieldLocality = new JTextField();
        textFieldLocality.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldLocality.setBounds(209, 524, 200, 30);
        getContentPane().add(textFieldLocality);

        // Pin
        JLabel lblPin = new JLabel("Pin");
        lblPin.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblPin.setBounds(89, 574, 100, 30);
        getContentPane().add(lblPin);
        textFieldPin = new JTextField();
        textFieldPin.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        textFieldPin.setBounds(209, 574, 200, 30);
        getContentPane().add(textFieldPin);

        // Register Button
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnRegister.setBounds(89, 615, 117, 30);
        btnRegister.addActionListener(e -> registerUser());
        getContentPane().add(btnRegister);
        
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserLoginPage ulp = new UserLoginPage();
                ulp.setVisible(true);
            }
        });
        btnLogin.setBounds(292, 615, 117, 30);
        getContentPane().add(btnLogin);
    }

    private void registerUser() {
        // Your registration logic (validation, database insertion) goes here
        // You can add validation for mobile number and other fields here if needed

        String name = textFieldName.getText();
        String email = textFieldEmail.getText();
        String password = new String(passwordField.getPassword());
        String mobile = textFieldMobile.getText();
        String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
        String nationality = textFieldNationality.getText();
        String occupation = textFieldOccupation.getText();
        String state = textFieldState.getText();
        String city = textFieldCity.getText();
        String locality = textFieldLocality.getText();
        String pin = textFieldPin.getText();

        // Example validation check
        if (mobile.length() != 10) {
            JOptionPane.showMessageDialog(this, "Mobile number must be 10 digits.");
            return;
        }

        // Database connection and insertion logic goes here
        try {
            // Assume you have a method to get a database connection
            Connection conn = DriverManager.getConnection("your_database_url", "username", "password");
            String query = "INSERT INTO users (name, email, password, mobile, dob, nationality, occupation, state, city, locality, pin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, mobile);
            pstmt.setString(5, dob);
            pstmt.setString(6, nationality);
            pstmt.setString(7, occupation);
            pstmt.setString(8, state);
            pstmt.setString(9, city);
            pstmt.setString(10, locality);
            pstmt.setString(11, pin);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
            UserLoginPage ulp = new UserLoginPage();
            ulp.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
