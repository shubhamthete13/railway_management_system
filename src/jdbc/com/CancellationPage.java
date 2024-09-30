package jdbc.com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.sql.*;

public class CancellationPage extends JFrame {
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private AdminReservationPage adminReservationPage; // Reference to AdminReservationPage

    public CancellationPage(int userId, AdminReservationPage adminReservationPage) {
        this.adminReservationPage = adminReservationPage; // Assign reference to the constructor
        // Set JFrame properties
        setTitle("Cancel Ticket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1920, 1080);
        getContentPane().setLayout(new BorderLayout());

        // Create table model
        tableModel = new DefaultTableModel(new Object[]{"Booking ID", "Train ID", "Seats", "Status"}, 0);
        bookingsTable = new JTable(tableModel);

        // Set font for table data
        bookingsTable.setFont(new Font("Sitka Text", Font.PLAIN, 14)); // Font size 14 for table data
        bookingsTable.setRowHeight(25); // Adjust row height to accommodate the font

        // Set font for table header
        JTableHeader tableHeader = bookingsTable.getTableHeader();
        tableHeader.setFont(new Font("Sitka Text", Font.BOLD, 16)); // Font size 16 for table header

        // Add table to JScrollPane
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Cancel button
        JButton btnCancel = new JButton("Cancel Selected Ticket");
        btnCancel.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelTicket();
            }
        });
        getContentPane().add(btnCancel, BorderLayout.SOUTH);

        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDashboard urDashboard = new UserDashboard(userId);
                urDashboard.setVisible(true);
                dispose(); // Close this window and return to the previous one
            }
        });
        getContentPane().add(btnBack, BorderLayout.NORTH); // Adding Back button at the top

        // Load bookings into the table
        loadBookings(userId);
    }

    private void loadBookings(int userId) {
        try {
            Conn con = new Conn();
            String query = "SELECT booking_id, train_id, seats, status FROM bookings WHERE user_id = ? AND status = 'Active'";
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int trainId = rs.getInt("train_id");
                int seats = rs.getInt("seats");
                String status = rs.getString("status");
                tableModel.addRow(new Object[]{bookingId, trainId, seats, status});
            }

            rs.close();
            pstmt.close();
            con.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void cancelTicket() {
//        int selectedRow = bookingsTable.getSelectedRow();
//        if (selectedRow != -1) {
//            try {
//                int bookingId = (Integer) tableModel.getValueAt(selectedRow, 0); // Get booking ID
//                int trainId = (Integer) tableModel.getValueAt(selectedRow, 1); // Get Train ID
//                int seats = (Integer) tableModel.getValueAt(selectedRow, 2); // Get number of seats
//
//                Conn con = new Conn();
//
//                // Update the status of the booking to 'Canceled'
//                String cancelQuery = "UPDATE bookings SET status = 'Canceled' WHERE booking_id = ?";
//                PreparedStatement pstmt = con.c.prepareStatement(cancelQuery);
//                pstmt.setInt(1, bookingId);
//                int rowsUpdated = pstmt.executeUpdate();
//
//                if (rowsUpdated > 0) {
//                    // Update the available seats in the trains table
//                    String updateSeatsQuery = "UPDATE trains SET train_seat_number = train_seat_number + ? WHERE train_id = ?";
//                    PreparedStatement pstmtUpdate = con.c.prepareStatement(updateSeatsQuery);
//                    pstmtUpdate.setInt(1, seats); // Add back the canceled seats
//                    pstmtUpdate.setInt(2, trainId);
//                    pstmtUpdate.executeUpdate();
//
//                    // Remove the canceled row from the table
//                    tableModel.removeRow(selectedRow);
//
//                    JOptionPane.showMessageDialog(this, "Ticket canceled successfully! Seats updated.");
//
//                    // Check if the adminReservationPage reference is not null
//                    if (adminReservationPage != null) {
//                        adminReservationPage.refreshReservations(); // Refresh the admin reservations
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(this, "Cancellation failed!");
//                }
//
//                pstmt.close();
//                con.c.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Cancellation Failed: " + e.getMessage());
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
//        }
//    }
    private void cancelTicket() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                int bookingId = (Integer) tableModel.getValueAt(selectedRow, 0); // Get booking ID
                int trainId = (Integer) tableModel.getValueAt(selectedRow, 1); // Get Train ID
                int seats = (Integer) tableModel.getValueAt(selectedRow, 2); // Get number of seats

                Conn con = new Conn();

                // Update the status of the booking to 'Canceled'
                String cancelQuery = "UPDATE bookings SET status = 'Canceled' WHERE booking_id = ?";
                PreparedStatement pstmt = con.c.prepareStatement(cancelQuery);
                pstmt.setInt(1, bookingId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // Update the available seats in the trains table
                    String updateSeatsQuery = "UPDATE trains SET train_seat_number = train_seat_number + ? WHERE train_id = ?";
                    PreparedStatement pstmtUpdate = con.c.prepareStatement(updateSeatsQuery);
                    pstmtUpdate.setInt(1, seats); // Add back the canceled seats
                    pstmtUpdate.setInt(2, trainId);
                    pstmtUpdate.executeUpdate();

                    // Also update the status in the admin_reservations table
                    String adminUpdateQuery = "UPDATE admin_reservations SET status = 'Canceled' WHERE booking_id = ?";
                    PreparedStatement pstmtAdminUpdate = con.c.prepareStatement(adminUpdateQuery);
                    pstmtAdminUpdate.setInt(1, bookingId);
                    pstmtAdminUpdate.executeUpdate();

                    // Remove the canceled row from the table
                    tableModel.removeRow(selectedRow);

                    JOptionPane.showMessageDialog(this, "Ticket canceled successfully! Seats updated.");

                    // Refresh the admin reservations if the reference is not null
                    if (adminReservationPage != null) {
                        adminReservationPage.refreshReservations(); // Refresh the admin reservations
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cancellation failed!");
                }

                pstmt.close();
                con.c.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Cancellation Failed: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
        }
    }


    public static void main(String[] args) {
        // Example usage with a user ID
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminReservationPage adminPage = new AdminReservationPage(); // Ensure this is fully initialized
                    CancellationPage frame = new CancellationPage(1, adminPage); // Pass the admin page reference
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
