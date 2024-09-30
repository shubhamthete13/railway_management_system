package jdbc.com;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;

public class UserDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private int userId; // Declare userId
    private String userName; // Declare userName

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserDashboard frame = new UserDashboard(1); // Example userId
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
    public UserDashboard(int userId) {
        this.userId = userId; // Initialize userId
        this.userName = fetchUserName(userId); // Fetch the user name from the database

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 0, 1122, 658);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Welcome label with user's name
        JLabel lblWelcome = new JLabel("Hello " + userName + ", Welcome to the Dashboard");
        lblWelcome.setFont(new Font("Sitka Text", Font.PLAIN, 24));
        lblWelcome.setBounds(380, 77, 600, 50); // Adjust size and position as needed
        contentPane.add(lblWelcome);

        // Button for booking a ticket
        JButton btnBook = new JButton("Book");
        btnBook.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Pass userId to TicketBookingPage
                TicketBookingPage ticketBookingPage = new TicketBookingPage(userId, null);
                ticketBookingPage.setVisible(true);
                dispose(); // Close the UserDashboard window if needed
            }
        });
        btnBook.setBounds(281, 164, 184, 44);
        contentPane.add(btnBook);

        // Button for viewing tickets
        JButton btnViewTickets = new JButton("View Ticket");
        btnViewTickets.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnViewTickets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewTicketsPage vePage = new ViewTicketsPage(userId); // Pass userId to view tickets
                vePage.setVisible(true);
                dispose();
            }
        });
        btnViewTickets.setBounds(281, 301, 184, 44);
        contentPane.add(btnViewTickets);

        // Button for canceling tickets
        JButton btnCancelTicket = new JButton("Cancel Ticket");
        btnCancelTicket.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnCancelTicket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancellationPage cPage = new CancellationPage(userId, null); // Pass userId to cancel tickets
                cPage.setVisible(true);
            }
        });
        btnCancelTicket.setBounds(622, 301, 184, 44);
        contentPane.add(btnCancelTicket);

        JButton btnNewButton = new JButton("Timetable");
        btnNewButton.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TimetablePage ttPage = new TimetablePage();
                ttPage.setVisible(true);
                dispose();
            }
        });
        btnNewButton.setBounds(622, 164, 184, 44);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Logout");
        btnNewButton_1.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Home h1 = new Home();
                h1.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setBounds(470, 436, 184, 52);
        contentPane.add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\1565769552920.jpg"));
        lblNewLabel.setBounds(0, 0, 1127, 633);
        contentPane.add(lblNewLabel);
    }

    /**
     * Fetch the user's name based on their userId
     */
    private String fetchUserName(int userId) {
        String name = "";
        try {
            Conn con = new Conn(); // Establish connection using Conn class
            String query = "SELECT name FROM users WHERE id = ?";
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name"); // Get the name from the result set
            }

            rs.close();
            pstmt.close();
            con.c.close(); // Close the connection
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch user details");
        }
        return name;
    }
}
