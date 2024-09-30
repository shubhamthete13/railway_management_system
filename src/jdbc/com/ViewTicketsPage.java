package jdbc.com;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewTicketsPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private int userId; // Instance variable to store user ID
    private JPanel ticketContainer; // Instance variable for ticket container

    public static void main(String[] args) {
        int userId = 1; // Replace with actual user ID from login session
        EventQueue.invokeLater(() -> {
            try {
                ViewTicketsPage frame = new ViewTicketsPage(userId);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ViewTicketsPage(int userId) {
        this.userId = userId; // Initialize instance variable
        setTitle("View Tickets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 531);
        getContentPane().setLayout(new BorderLayout());

        // Panel to hold ticket information
        ticketContainer = new JPanel();
        ticketContainer.setLayout(new BoxLayout(ticketContainer, BoxLayout.Y_AXIS));
        ticketContainer.setBackground(new Color(128, 128, 255)); // Background color for the container
        JScrollPane scrollPane = new JScrollPane(ticketContainer);
        getContentPane().add(scrollPane, BorderLayout.CENTER); // Add scrollable ticket container to the center

        // Load tickets for the user
        loadTickets(userId, ticketContainer);

        // Create Back button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDashboard urDashboard = new UserDashboard(userId);
                urDashboard.setVisible(true);
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel(); // Panel to hold the button
        buttonPanel.add(btnBack);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom
    }

    private void loadTickets(int userId, JPanel ticketContainer) {
        Conn con = new Conn();
        try {
            // Updated SQL query to fetch user-specific ticket details including status
            String query = "SELECT b.booking_id, t.train_name, s1.station_name AS departure_station, "
                         + "s2.station_name AS arrival_station, b.seats, "
                         + "(t.train_ticket * b.seats) AS total_price, b.booking_date, b.status "
                         + "FROM bookings b "
                         + "JOIN trains t ON b.train_id = t.train_id "
                         + "JOIN timetable tm ON t.train_id = tm.train_id "
                         + "JOIN stations s1 ON tm.departure_station_id = s1.station_id "
                         + "JOIN stations s2 ON tm.arrival_station_id = s2.station_id "
                         + "WHERE b.user_id = ?"; // Removed status filter to show all bookings
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            ticketContainer.removeAll(); // Clear existing tickets before loading new ones
            boolean hasTickets = false; // Flag to check if tickets are found
            while (rs.next()) {
                hasTickets = true; // Ticket exists for user

                // Extract ticket details
                int bookingId = rs.getInt("booking_id");
                String trainName = rs.getString("train_name");
                String departureStation = rs.getString("departure_station");
                String arrivalStation = rs.getString("arrival_station");
                int seats = rs.getInt("seats");
                double totalPrice = rs.getDouble("total_price");
                String bookingDate = rs.getString("booking_date");
                String status = rs.getString("status"); // Get status

                // Create and style a ticket panel
                JPanel ticketPanel = new JPanel();
                ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
                ticketPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                ticketPanel.setBackground(Color.WHITE);
                ticketPanel.setPreferredSize(new Dimension(600, 250));

                // Ticket header
                JLabel headerLabel = new JLabel("JOURNEY CUM RESERVATION TICKET");
                headerLabel.setFont(new Font("Sitka Text", Font.BOLD, 16));
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                ticketPanel.add(headerLabel);

                ticketPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add space

                // Booking ID
                JLabel bookingIdLabel = new JLabel("Booking ID: " + bookingId);
                bookingIdLabel.setFont(new Font("Sitka Text", Font.PLAIN, 14));
                ticketPanel.add(bookingIdLabel);

                // Train Name
                JLabel trainNameLabel = new JLabel("Train: " + trainName);
                trainNameLabel.setFont(new Font("Sitka Text", Font.BOLD, 14));
                ticketPanel.add(trainNameLabel);

                // Journey information (From, To)
                JLabel journeyLabel = new JLabel("From: " + departureStation + "   To: " + arrivalStation);
                journeyLabel.setFont(new Font("Sitka Text", Font.PLAIN, 14));
                ticketPanel.add(journeyLabel);

                // Seats information
                JLabel seatsLabel = new JLabel("Seats: " + seats);
                seatsLabel.setFont(new Font("Sitka Text", Font.PLAIN, 14));
                ticketPanel.add(seatsLabel);

                // Total Price information
                JLabel totalPriceLabel = new JLabel("Total Price: ₹" + totalPrice);
                totalPriceLabel.setFont(new Font("Sitka Text", Font.PLAIN, 14));
                ticketPanel.add(totalPriceLabel);

                // Booking Date
                JLabel bookingDateLabel = new JLabel("Booking Date: " + bookingDate);
                bookingDateLabel.setFont(new Font("Sitka Text", Font.PLAIN, 14));
                ticketPanel.add(bookingDateLabel);

                // Status
                JLabel statusLabel = new JLabel("Status: " + status);
                statusLabel.setFont(new Font("Sitka Text", Font.PLAIN, 14));
                ticketPanel.add(statusLabel);

                ticketPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add space

                // Footer
                JLabel footerLabel = new JLabel("I-TICKET / NO CASH REFUNDS");
                footerLabel.setFont(new Font(status, Font.ITALIC, 12));
                footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                ticketPanel.add(footerLabel);

                // Add the ticket panel to the container
                ticketContainer.add(ticketPanel);
            }

            if (!hasTickets) {
                // If no tickets found, display a message
                JLabel noTicketsLabel = new JLabel("No tickets found for your account.");
                noTicketsLabel.setForeground(Color.RED);
                noTicketsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                ticketContainer.add(noTicketsLabel);
            }

            ticketContainer.revalidate(); // Refresh the container to display updated content
            ticketContainer.repaint(); // Repaint the container to reflect changes

            rs.close();
            pstmt.close();
            con.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}

