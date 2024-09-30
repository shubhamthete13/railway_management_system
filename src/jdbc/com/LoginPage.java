package jdbc.com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginPage extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnSIgnButton;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage frame = new LoginPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 200, 460, 367);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD, 36));
		lblNewLabel.setBounds(156, 11, 122, 48);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(63, 99, 122, 38);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(63, 174, 122, 38);
		contentPane.add(lblNewLabel_1_1);
		
		textField = new JTextField();
		textField.setBounds(195, 90, 219, 57);
		textField.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		passwordField.setBounds(195, 165, 219, 57);
		contentPane.add(passwordField);
		
		JButton btnSIgnButton = new JButton("Login");
		btnSIgnButton.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		btnSIgnButton.addActionListener(this);
		btnSIgnButton.setBounds(162, 257, 116, 48);
		contentPane.add(btnSIgnButton);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\df07cb4ccb697303462ad7a8b57b852f.jpg"));
		lblNewLabel_2.setBounds(0, 0, 459, 341);
		contentPane.add(lblNewLabel_2);
		
//		JLabel lblNewLabel_2 = new JLabel("");
//		lblNewLabel_2.setIcon(new ImageIcon("F:\\Images\\Login-page-character1.png"));
//		lblNewLabel_2.setBounds(-274, -21, 1406, 768);
//		contentPane.add(lblNewLabel_2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String usernameString=textField.getText();
		String passString=passwordField.getText();
		if(usernameString.contains("admin")&& passString.contains("admin123")) {
			textField.setText(null);
			passwordField.setText(null);
			AdminDashboard aDashboard=new AdminDashboard();
			aDashboard.setVisible(true);
			dispose();
			dispose();
			
		}else {
			JOptionPane.showMessageDialog(null, "Invalid Login");
			textField.setText(null);
			passwordField.setText(null);
		}
	}

	
}
