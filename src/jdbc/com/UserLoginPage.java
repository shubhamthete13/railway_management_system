package jdbc.com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class UserLoginPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textFieldEmail;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserLoginPage frame = new UserLoginPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserLoginPage() {
    	getContentPane().setBackground(new Color(192, 192, 192));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(430, 100, 495, 431);
        getContentPane().setLayout(null);

        JLabel lblEmail = new JLabel("Email (Username)");
        lblEmail.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblEmail.setBounds(30, 124, 175, 30);
        getContentPane().add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textFieldEmail.setBounds(215, 123, 200, 30);
        getContentPane().add(textFieldEmail);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblPassword.setBounds(30, 202, 100, 30);
        getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        passwordField.setBounds(215, 202, 200, 30);
        getContentPane().add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnLogin.setBounds(78, 279, 100, 30);
        btnLogin.addActionListener(e -> loginUser());
        getContentPane().add(btnLogin);
        
        JButton btnNewButton = new JButton("Register");
        btnNewButton.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		UserRegistrationPage uRegistrationPage=new UserRegistrationPage();
        		uRegistrationPage.setVisible(true);
        	}
        });
        btnNewButton.setBounds(234, 279, 125, 30);
        getContentPane().add(btnNewButton);
        
        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 25));
        lblNewLabel.setBounds(183, 54, 100, 34);
        getContentPane().add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("If alreday have an account then login otherwise do registration");
        lblNewLabel_1.setBounds(96, 254, 310, 14);
        getContentPane().add(lblNewLabel_1);
        
        JButton btnNewButton_1 = new JButton("Back");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Home home=new Home();
        		home.setVisible(true);
        		}
        });
        btnNewButton_1.setFont(new Font("Sitka Text", Font.BOLD, 18));
        btnNewButton_1.setBounds(382, 360, 89, 23);
        getContentPane().add(btnNewButton_1);
    }

    private void loginUser() {
        String email = textFieldEmail.getText();
        String password = new String(passwordField.getPassword());

        try {
            Conn con = new Conn();
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Login successful!");

                int userId = rs.getInt("id"); // Get the user's ID from the database
                UserDashboard userDashboard = new UserDashboard(userId); // Pass the userId to the dashboard
                userDashboard.setVisible(true);
                dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email or password. Please try again.");
            }

            rs.close();
            pstmt.close();
            con.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
        }
    }
}
