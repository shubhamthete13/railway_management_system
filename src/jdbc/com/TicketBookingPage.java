package jdbc.com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;

public class TicketBookingPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> trainComboBox;
    private JTextField seatsField;
    private JLabel totalPriceLabel;
    private JButton bookButton;
    private AdminReservationPage adminReservationPage; // Reference to AdminReservationPage
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminReservationPage adminReservationPage = new AdminReservationPage();
                TicketBookingPage frame = new TicketBookingPage(1, adminReservationPage); // Replace 1 with a valid user ID
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TicketBookingPage(int userId, AdminReservationPage adminReservationPage) {
        this.adminReservationPage = adminReservationPage; // Initialize the reference

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(430, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setLayout(null);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel lblSelectTrain = new JLabel("Select Train");
        lblSelectTrain.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblSelectTrain.setBounds(31, 70, 100, 30);
        contentPane.add(lblSelectTrain);

        trainComboBox = new JComboBox<>();
        trainComboBox.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        trainComboBox.setBounds(151, 70, 200, 30);
        contentPane.add(trainComboBox);

        JLabel lblSeats = new JLabel("Number of Seats");
        lblSeats.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        lblSeats.setBounds(31, 120, 120, 30);
        contentPane.add(lblSeats);

        seatsField = new JTextField();
        seatsField.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        seatsField.setBounds(151, 120, 200, 30);
        contentPane.add(seatsField);

        // Label to display the total price
        totalPriceLabel = new JLabel("Total Price: Rs. 0.00");
        totalPriceLabel.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        totalPriceLabel.setBounds(31, 170, 200, 30);
        contentPane.add(totalPriceLabel);

        // Book button
        bookButton = new JButton("Book Ticket");
        bookButton.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        bookButton.setBounds(111, 210, 120, 30);
        bookButton.addActionListener(e -> bookTicket(userId));
        contentPane.add(bookButton);
        
        btnNewButton = new JButton("Back ");
        btnNewButton.setFont(new Font("Sitka Text", Font.PLAIN, 15));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		UserDashboard urDashboard=new UserDashboard(userId);
        		urDashboard.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton.setBounds(251, 210, 100, 30);
        contentPane.add(btnNewButton);
        
        JLabel lblNewLabel = new JLabel("Ticket Book");
        lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD, 18));
        lblNewLabel.setBounds(125, 27, 132, 32);
        contentPane.add(lblNewLabel);

        // Add a listener to update total price when number of seats changes
        seatsField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTotalPrice();
            }
        });

        loadTrains(); // Load trains on frame creation
    }

    private void loadTrains() {
        Conn con = new Conn();
        try {
            String query = "SELECT train_id, train_name FROM trains";
            Statement stmt = con.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int trainId = rs.getInt("train_id");
                String trainName = rs.getString("train_name");
                trainComboBox.addItem(trainId + " - " + trainName);
            }

            rs.close();
            stmt.close();
            con.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalPrice() {
        try {
            String selectedTrain = (String) trainComboBox.getSelectedItem();
            int trainId = Integer.parseInt(selectedTrain.split(" - ")[0]);
            int seats = Integer.parseInt(seatsField.getText());

            Conn con = new Conn();
            String priceQuery = "SELECT train_ticket FROM trains WHERE train_id = ?";
            PreparedStatement pstmt = con.c.prepareStatement(priceQuery);
            pstmt.setInt(1, trainId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double ticketPrice = rs.getDouble("train_ticket");
                double totalPrice = ticketPrice * seats;
                totalPriceLabel.setText(String.format("Total Price: Rs. %.2f", totalPrice));
            }

            rs.close();
            pstmt.close();
            con.c.close();
        } catch (SQLException | NumberFormatException e) {
            totalPriceLabel.setText("Total Price: Rs. 0.00");
        }
    }

    private void bookTicket(int userId) {
        try {
            String selectedTrain = (String) trainComboBox.getSelectedItem();
            int trainId = Integer.parseInt(selectedTrain.split(" - ")[0]);
            int seats = Integer.parseInt(seatsField.getText());

            Conn con = new Conn();

            // Fetch the current available seats for the selected train
            String seatQuery = "SELECT train_seat_number, train_ticket FROM trains WHERE train_id = ?";
            PreparedStatement pstmtSeat = con.c.prepareStatement(seatQuery);
            pstmtSeat.setInt(1, trainId);
            ResultSet rsSeat = pstmtSeat.executeQuery();

            if (rsSeat.next()) {
                int availableSeats = rsSeat.getInt("train_seat_number");
                double ticketPrice = rsSeat.getDouble("train_ticket");
                double totalPrice = ticketPrice * seats;

                // Check if the user ID exists in the users table
                if (!userExists(userId, con)) {
                    JOptionPane.showMessageDialog(null, "User does not exist. Please register first.");
                    return;
                }

                if (availableSeats >= seats) {
                    // Proceed with booking
                    String bookingQuery = "INSERT INTO bookings (user_id, train_id, seats) VALUES (?, ?, ?)";
                    PreparedStatement pstmtBooking = con.c.prepareStatement(bookingQuery, Statement.RETURN_GENERATED_KEYS);
                    pstmtBooking.setInt(1, userId);
                    pstmtBooking.setInt(2, trainId);
                    pstmtBooking.setInt(3, seats);
                    pstmtBooking.executeUpdate();

                    // Retrieve the booking_id of the newly inserted booking
                    ResultSet generatedKeys = pstmtBooking.getGeneratedKeys();
                    int bookingId = 0;
                    if (generatedKeys.next()) {
                        bookingId = generatedKeys.getInt(1);
                    }

                    // Update available seats in the trains table
                    int newSeatCount = availableSeats - seats;
                    String updateQuery = "UPDATE trains SET train_seat_number = ? WHERE train_id = ?";
                    PreparedStatement pstmtUpdate = con.c.prepareStatement(updateQuery);
                    pstmtUpdate.setInt(1, newSeatCount);
                    pstmtUpdate.setInt(2, trainId);
                    pstmtUpdate.executeUpdate();

                    // Insert into admin_reservations table
                    String reservationQuery = "INSERT INTO admin_reservations (booking_id, status) VALUES (?, ?)";
                    PreparedStatement pstmtReservation = con.c.prepareStatement(reservationQuery);
                    pstmtReservation.setInt(1, bookingId);
                    pstmtReservation.setString(2, "Pending");  // Assuming new reservations start with a "Pending" status
                    pstmtReservation.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Ticket booked successfully! Total Price: Rs. " + totalPrice);

                    // Refresh the admin reservation page
                    if (adminReservationPage != null) {
                        adminReservationPage.refreshReservations();
                    }

                    // Close the resources
                    pstmtBooking.close();
                    pstmtUpdate.close();
                    pstmtReservation.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough available seats!");
                }
            }

            rsSeat.close();
            pstmtSeat.close();
            con.c.close();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Booking Failed: " + e.getMessage());
        }
    }

    private boolean userExists(int userId, Conn con) {
        try {
            String query = "SELECT id FROM users WHERE id = ?";
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
