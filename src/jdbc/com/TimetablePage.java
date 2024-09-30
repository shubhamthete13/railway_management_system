package jdbc.com;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.sql.*;

public class TimetablePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private int userId; 

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TimetablePage frame = new TimetablePage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TimetablePage() {
        // Set up the JFrame
        setTitle("Train Timetable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // Set up the table and its model
        String[] columnNames = {"Train Name", "Train Number", "From Station", "To Station", "Arrival Time", "Departure Time", "Date", "Seats Available"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBounds(0, 446, 1056, 186);
        scrollPane.setPreferredSize(new Dimension(1056, 303));
        add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        table.setFont(new Font("Sitka Text", Font.PLAIN, 14)); // Font size 14 for table data
        table.setRowHeight(25); // Adjust row height to accommodate the font

        // Set font for table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Sitka Text", Font.BOLD, 16)); 

        // Back button to return to UserDashboard
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	UserDashboard urDashboard = new UserDashboard(userId);
            	urDashboard.setVisible(true);
                dispose(); 
            }
        });
        contentPane.add(backButton, BorderLayout.SOUTH); // Add the Back button at the bottom

        // Load timetable data from the database
        loadTimetable(model);
        table.setFont(new Font("Sitka Text", Font.PLAIN, 14)); // Font size 14 for table data
        table.setRowHeight(25); // Adjust row height if needed

        // Set font for table header
        JTableHeader tableHeader1 = table.getTableHeader();
        tableHeader1.setFont(new Font("Sitka Text", Font.BOLD, 16));
    }

    private void loadTimetable(DefaultTableModel model) {
        // Create a database connection
        Conn con = new Conn();  // Ensure this class is implemented to establish DB connection
        try {
            // SQL query to fetch timetable data
            String query = "SELECT t.train_name, t.train_number, " +
                           "s1.station_name AS station_from, " +
                           "s2.station_name AS station_to, " +
                           "tt.arrival_time, tt.departure_time, " +
                           "tt.timetable_date, " + 
                           "t.train_seat_number " + // Add seats available from trains table
                           "FROM timetable tt " +
                           "JOIN trains t ON tt.train_id = t.train_id " +
                           "JOIN stations s1 ON tt.departure_station_id = s1.station_id " +
                           "JOIN stations s2 ON tt.arrival_station_id = s2.station_id";

            PreparedStatement pstmt = con.c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Populate the table model with data from the ResultSet
            while (rs.next()) {
                String trainName = rs.getString("train_name");
                String trainNumber = rs.getString("train_number");
                String fromStation = rs.getString("station_from");
                String toStation = rs.getString("station_to");
                String arrivalTime = rs.getString("arrival_time");
                String departureTime = rs.getString("departure_time");
                String date = rs.getString("timetable_date");
                int seatsAvailable = rs.getInt("train_seat_number"); // Get seats available
                model.addRow(new Object[]{trainName, trainNumber, fromStation, toStation, arrivalTime, departureTime, date, seatsAvailable}); // Add to model
            }
            rs.close();
            pstmt.close();
            con.c.close();  // Close the connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
