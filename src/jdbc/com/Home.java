package jdbc.com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
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
	public Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("RAILWAY MANAGEMENT  SYSTEM");
		lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 47));
		lblNewLabel.setBounds(232, 129, 877, 93);
		contentPane.add(lblNewLabel);
			
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage lPage=new LoginPage();
				lPage.setVisible(true);
				dispose();
				
			}
		});
		btnAdmin.setFont(new Font("Sitka Text", Font.BOLD, 17));
		btnAdmin.setBounds(986, 11, 118, 40);
		contentPane.add(btnAdmin);
		
		JButton btnUser = new JButton("User");
		btnUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserLoginPage urLoginPage=new UserLoginPage();
				urLoginPage.setVisible(true);
//				UserLogin userLogin=new UserLogin();
//				userLogin.setVisible(true);
				}
		});
		btnUser.setFont(new Font("Sitka Text", Font.BOLD, 17));
		btnUser.setBounds(1114, 11, 118, 40);
		contentPane.add(btnUser);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\thumb-1920-661783.jpg"));
		lblNewLabel_1.setBounds(-211, -364, 1516, 1080);
		contentPane.add(lblNewLabel_1);
		
		
	}
}
