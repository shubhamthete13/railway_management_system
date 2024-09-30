
package jdbc.com;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.sql.*;

public class AdminReservationPage extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton approveButton;

    public AdminReservationPage() {
        setLayout(new BorderLayout());

        // Define column names
        String[] columnNames = {
            "Reservation ID", "Booking ID", "User Name", "User Email", "Train Name", "Seats", "Status", "Total Price"
        };
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons for Approve and Reject actions
        JPanel buttonPanel = new JPanel();
       approveButton = new JButton("Approve");
        approveButton.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        
       
        buttonPanel.add(approveButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Load reservations when the panel is created
        loadReservations();
        table.setFont(new Font("Sitka Text", Font.PLAIN, 14));
        table.setRowHeight(25);

        // Set font for table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Sitka Text", Font.BOLD, 16));

        // Add action listeners to buttons
        approveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateReservationStatus("Approved");
            }
        });

        
    }

    // Method to load reservations from the database
    public void loadReservations() {
        Conn con = new Conn();
        try {
            String query = "SELECT ar.reservation_id, b.booking_id, u.name, u.email, t.train_name, b.seats, ar.status, (t.train_ticket * b.seats) AS total_price " +
                           "FROM admin_reservations ar " +
                           "JOIN bookings b ON ar.booking_id = b.booking_id " +
                           "JOIN users u ON b.user_id = u.id " +
                           "JOIN trains t ON b.train_id = t.train_id";
            Statement stmt = con.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            model.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                int bookingId = rs.getInt("booking_id");
                String userName = rs.getString("name");
                String userEmail = rs.getString("email");
                String trainName = rs.getString("train_name");
                int seats = rs.getInt("seats");
                String status = rs.getString("status");
					double totalPrice = rs.getDouble("total_price");
					model.addRow(new Object[]{reservationId, bookingId, userName, userEmail, trainName, seats, status, totalPrice});
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
       }
    }

    // Method to update the status of a selected reservation
    public void updateReservationStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int bookingId = (Integer) model.getValueAt(selectedRow, 1);
           Conn con = new Conn();
            try {
                String query = "UPDATE admin_reservations SET status = 'confirmed' WHERE booking_id = ?";
                PreparedStatement pstmt = con.c.prepareStatement(query);
                pstmt.setInt(1, bookingId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Reservation " + status.toLowerCase() + " successfully.");
                loadReservations(); // Refresh the table
                pstmt.close();
                con.c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to update.");
        }
    }

    // Method to refresh the reservations
    public void refreshReservations() {
        loadReservations();
    }
}
