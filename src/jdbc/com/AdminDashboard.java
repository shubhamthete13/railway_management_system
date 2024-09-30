package jdbc.com;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;

public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminDashboard frame = new AdminDashboard();
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
	public AdminDashboard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 240, 240));
		panel.setBounds(0, 0, 208, 700);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\DELL\\Downloads\\house-fotor-20240920232959.png"));
		lblNewLabel.setBounds(0, 11, 198, 59);
		panel.add(lblNewLabel);
		
		JButton btnTrainsButton = new JButton("Trains");
		btnTrainsButton.setBounds(10, 176, 188, 52);
		panel.add(btnTrainsButton);
		btnTrainsButton.setFont(new Font("Sitka Text", Font.BOLD, 18));
		
		JButton btnStationButton = new JButton("Station");
		btnStationButton.setBounds(10, 239, 188, 52);
		panel.add(btnStationButton);
		btnStationButton.setFont(new Font("Sitka Text", Font.BOLD, 18));
		
//		JButton btnEmployeeButton = new JButton("Employee");
//		btnEmployeeButton.setBounds(10, 239, 188, 52);
//		panel.add(btnEmployeeButton);
//		btnEmployeeButton.setFont(new Font("Sitka Text", Font.BOLD, 18));
		
		JButton btnTimeTableButton = new JButton("Timetable");
		btnTimeTableButton.setBounds(10, 302, 188, 52);
		panel.add(btnTimeTableButton);
		btnTimeTableButton.setFont(new Font("Sitka Text", Font.BOLD, 18));
		
		JButton btnReservationButton = new JButton("Reservation");
		btnReservationButton.setBounds(10, 365, 188, 52);
		panel.add(btnReservationButton);
		btnReservationButton.setFont(new Font("Sitka Text", Font.BOLD, 18));
		
		JButton btnNewButton = new JButton("Logout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Home home=new Home();
				home.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Sitka Text", Font.BOLD, 18));
		btnNewButton.setBounds(10, 428, 188, 44);
		panel.add(btnNewButton);
		btnReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminReservationPage adminReservationPage=new AdminReservationPage();
				adminReservationPage.setVisible(true);
				
			}
		});
		btnTimeTableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageTimetable mTimetable=new ManageTimetable();
				mTimetable.setVisible(true);
				
			}
		});
//		btnEmployeeButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		});
		btnStationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageStation manageStation=new ManageStation();
				manageStation.setVisible(true);
			}
		});
		btnTrainsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MangesTrains manageTrains=new MangesTrains();
				manageTrains.setVisible(true);
			}

		});
		
		JLabel lblNewLabel_1 = new JLabel("DashBoard");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Sitka Text", Font.BOLD, 25));
		lblNewLabel_1.setBounds(54, 38, 144, 32);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel1 = new JLabel("");
		lblNewLabel1.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\df07cb4ccb697303462ad7a8b57b852f.jpg"));
		lblNewLabel1.setBackground(new Color(0, 0, 0));
		lblNewLabel1.setBounds(0, -11, 209, 711);
		panel.add(lblNewLabel1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		panel_1.setBounds(206, 0, 1077, 50);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(1013, 0, 79, 50);
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\DELL\\Downloads\\person-fotor-20240920235050.png"));
		panel_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(0, 0, 1092, 50);
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\thumb-1920-66178312.jpg"));
		panel_1.add(lblNewLabel_3);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(206, 48, 1077, 652);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		CardLayout cardLayout = new CardLayout();
        panel_2.setLayout(cardLayout);

        JPanel defaultPanel = new JPanel();
        defaultPanel.setBackground(new Color(173, 216, 230)); // Optional background color for default view
        defaultPanel.setLayout(null);
        JLabel defaultLabel = new JLabel("Welcome to Admin Dashboard");
        defaultLabel.setForeground(new Color(255, 255, 255));
        defaultLabel.setFont(new Font("Sitka Text", Font.BOLD, 50));
        defaultLabel.setBounds(182, 265, 950, 85);
        defaultPanel.add(defaultLabel);
        panel_2.add(defaultPanel, "default");
        
        JLabel lblNewLabel_4 = new JLabel("");
        lblNewLabel_4.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\thumb-1920-661783.jpg"));
        lblNewLabel_4.setBounds(-20, 0, 1109, 652);
        defaultPanel.add(lblNewLabel_4);
        
        panel_2.add(new MangesTrains(), "ManageTrains");
        panel_2.add(new ManageStation(), "ManageStations");
//        panel_2.add(new ManageEmployee(), "ManageEmployee");
        panel_2.add(new ManageTimetable(), "ManageTimetable");
        panel_2.add(new AdminReservationPage(), "AdminReservationPage");
        
        
        btnTrainsButton.addActionListener(e -> cardLayout.show(panel_2, "ManageTrains"));
        btnStationButton.addActionListener(e -> cardLayout.show(panel_2, "ManageStations"));
//        btnEmployeeButton.addActionListener(e -> cardLayout.show(panel_2, "ManageEmployee"));
        btnTimeTableButton.addActionListener(e -> cardLayout.show(panel_2, "ManageTimetable"));
        btnReservationButton.addActionListener(e -> cardLayout.show(panel_2, "AdminReservationPage"));
        
        
        
		
	}
}
