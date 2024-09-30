package jdbc.com;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;

public class ManageTimetable extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> trainComboBox;
    private JComboBox<String> departureStationComboBox;
    private JComboBox<String> arrivalStationComboBox;
    private JTable table;
    private DefaultTableModel model;
    private JDateChooser dateChooser;
    private int selectedTimetableId = -1;
    private JSpinner arrivalTimeSpinner; 
    private JSpinner departureTimeSpinner; 

    public ManageTimetable() {
        setLayout(null);
        model = new DefaultTableModel(new Object[][]{}, new String[]{"Timetable ID", "Train", "Departure Station", "Arrival Station", "Date", "Arrival Time", "Departure Time"});

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(220, 220, 220) : Color.WHITE);
                } else {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        };
        table.setFont(new Font("Sitka Text", Font.PLAIN, 14)); // Font size 14 for table data
        table.setRowHeight(25); // Adjust row height if needed

        // Set font for table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Sitka Text", Font.BOLD, 16));

        dateChooser = new JDateChooser();
        dateChooser.setBounds(303, 145, 200, 30);
        add(dateChooser);

        JLabel lblTrain = new JLabel("Select Train");
        lblTrain.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblTrain.setBounds(50, 30, 120, 25);
        add(lblTrain);

        trainComboBox = new JComboBox<>();
        trainComboBox.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        trainComboBox.setBounds(303, 30, 200, 25);
        add(trainComboBox);

        JLabel lblDepartureStation = new JLabel("Departure Station");
        lblDepartureStation.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblDepartureStation.setBounds(50, 70, 150, 25);
        add(lblDepartureStation);

        departureStationComboBox = new JComboBox<>();
        departureStationComboBox.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        departureStationComboBox.setBounds(303, 70, 200, 25);
        add(departureStationComboBox);

        JLabel lblArrivalStation = new JLabel("Arrival Station");
        lblArrivalStation.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblArrivalStation.setBounds(50, 110, 150, 25);
        add(lblArrivalStation);

        arrivalStationComboBox = new JComboBox<>();
        arrivalStationComboBox.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        arrivalStationComboBox.setBounds(303, 110, 200, 25);
        add(arrivalStationComboBox);

        JLabel lblDate = new JLabel("Schedule Date");
        lblDate.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblDate.setBounds(50, 150, 120, 25);
        add(lblDate);

        JLabel lblArrivalTime = new JLabel("Arrival Time");
        lblArrivalTime.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblArrivalTime.setBounds(50, 190, 200, 25);
        add(lblArrivalTime);

        arrivalTimeSpinner = new JSpinner(new SpinnerDateModel());
        arrivalTimeSpinner.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        JSpinner.DateEditor arrivalTimeEditor = new JSpinner.DateEditor(arrivalTimeSpinner, "HH:mm");
        arrivalTimeSpinner.setEditor(arrivalTimeEditor);
        arrivalTimeSpinner.setValue(Calendar.getInstance().getTime());
        arrivalTimeSpinner.setBounds(303, 190, 200, 25);
        add(arrivalTimeSpinner);

        JLabel lblDepartureTime = new JLabel("Departure Time");
        lblDepartureTime.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblDepartureTime.setBounds(50, 230, 200, 25);
        add(lblDepartureTime);

        departureTimeSpinner = new JSpinner(new SpinnerDateModel());
        departureTimeSpinner.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        JSpinner.DateEditor departureTimeEditor = new JSpinner.DateEditor(departureTimeSpinner, "HH:mm");
        departureTimeSpinner.setEditor(departureTimeEditor);
        departureTimeSpinner.setValue(Calendar.getInstance().getTime());
        departureTimeSpinner.setBounds(303, 230, 200, 25);
        add(departureTimeSpinner);

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnAdd.setBounds(150, 279, 100, 30);
        add(btnAdd);
        btnAdd.addActionListener(e -> {
            addTimetableEntry();
            clearFields();  // Clear fields after adding
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnUpdate.setBounds(303, 279, 100, 30);
        add(btnUpdate);
        btnUpdate.addActionListener(e -> {
            updateTimetableEntry();
            clearFields();  // Clear fields after updating
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnDelete.setBounds(454, 279, 100, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> {
            deleteTimetableEntry();
            clearFields();  // Clear fields after deleting
        });

        

        // Adding a Refresh Button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnRefresh.setBounds(739, 279, 100, 30);
        add(btnRefresh);
        btnRefresh.addActionListener(e -> {
            populateTrainComboBox();
            populateStationComboBox();
            JOptionPane.showMessageDialog(this, "ComboBoxes Refreshed!");
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 341, 1057, 292);
        scrollPane.setPreferredSize(new Dimension(1056, 303));
        add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\008 Rainy Ashville.png"));
        lblNewLabel.setBounds(0, 0, 1095, 342);
        add(lblNewLabel);
        populateTrainComboBox();
        populateStationComboBox();

        // Add an item listener to handle the disabling of the arrival station combo box
        departureStationComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedDepartureStation = (String) e.getItem();
                String selectedArrivalStation = (String) arrivalStationComboBox.getSelectedItem();

                if (selectedDepartureStation != null && selectedDepartureStation.equals(selectedArrivalStation)) {
                    // Disable the arrival combo box and clear the selection
                    arrivalStationComboBox.setSelectedIndex(-1); // Deselect any item
                    arrivalStationComboBox.setEnabled(false); // Disable if same as departure
                } else {
                    arrivalStationComboBox.setEnabled(true); // Re-enable if different
                }
            }
        });

        arrivalStationComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedArrivalStation = (String) e.getItem();
                String selectedDepartureStation = (String) departureStationComboBox.getSelectedItem();

                if (selectedArrivalStation != null && selectedArrivalStation.equals(selectedDepartureStation)) {
                    // Disable the arrival combo box and clear the selection
                    arrivalStationComboBox.setSelectedIndex(-1); // Deselect the same station
                    arrivalStationComboBox.setEnabled(false); // Disable if same as departure
                }
            }
        });

//        departureStationComboBox.addItemListener(e -> {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                String selectedDepartureStation = (String) e.getItem();
//                String selectedArrivalStation = (String) arrivalStationComboBox.getSelectedItem();
//
//                if (selectedDepartureStation != null && selectedDepartureStation.equals(selectedArrivalStation)) {
//                    arrivalStationComboBox.setEnabled(false);
//                    arrivalStationComboBox.setSelectedIndex(-1); // Deselect any selected item
//                } else {
//                    arrivalStationComboBox.setEnabled(true);
//                }
//            }
//        });


        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedTimetableId = (int) table.getValueAt(selectedRow, 0);
                    trainComboBox.setSelectedItem(table.getValueAt(selectedRow, 1).toString());
                    departureStationComboBox.setSelectedItem(table.getValueAt(selectedRow, 2).toString());
                    arrivalStationComboBox.setSelectedItem(table.getValueAt(selectedRow, 3).toString());
                    dateChooser.setDate((java.util.Date) table.getValueAt(selectedRow, 4));
                    arrivalTimeSpinner.setValue(table.getValueAt(selectedRow, 5));
                    departureTimeSpinner.setValue(table.getValueAt(selectedRow, 6));
                    
                   
                }
            }
        });

        fetchTimetableData();
    }

    private void fetchTimetableData() {
        Conn con = new Conn();
        try {
            String query = "SELECT t.timetable_id, tr.train_name, ds.station_name AS departure_station, " +
                           "arrival_stations.station_name AS arrival_station, t.timetable_date, t.arrival_time, t.departure_time " +
                           "FROM timetable t " +
                           "JOIN trains tr ON t.train_id = tr.train_id " +
                           "JOIN stations ds ON t.departure_station_id = ds.station_id " +
                           "JOIN stations arrival_stations ON t.arrival_station_id = arrival_stations.station_id"; 
            ResultSet rs = con.s.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("timetable_id"),
                    rs.getString("train_name"),
                    rs.getString("departure_station"),
                    rs.getString("arrival_station"),
                    rs.getDate("timetable_date"),
                    rs.getTime("arrival_time"),
                    rs.getTime("departure_time")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTimetableEntry() {
        Conn con = new Conn();
        try {
            String query = "INSERT INTO timetable (train_id, departure_station_id, arrival_station_id, timetable_date, arrival_time, departure_time) " +
                           "VALUES ((SELECT train_id FROM trains WHERE train_name = ?), " +
                           "(SELECT station_id FROM stations WHERE station_name = ?), " +
                           "(SELECT station_id FROM stations WHERE station_name = ?), ?, ?, ?)";
            PreparedStatement ps = con.c.prepareStatement(query);
            ps.setString(1, (String) trainComboBox.getSelectedItem());
            ps.setString(2, (String) departureStationComboBox.getSelectedItem());
            ps.setString(3, (String) arrivalStationComboBox.getSelectedItem());
            ps.setDate(4, new java.sql.Date(dateChooser.getDate().getTime()));
            ps.setTime(5, new java.sql.Time(((java.util.Date) arrivalTimeSpinner.getValue()).getTime()));
            ps.setTime(6, new java.sql.Time(((java.util.Date) departureTimeSpinner.getValue()).getTime()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Timetable Entry Added Successfully!");
            model.setRowCount(0); // Clear the existing table data
            fetchTimetableData(); // Refresh the table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTimetableEntry() {
        Conn con = new Conn();
        try {
            String query = "UPDATE timetable SET train_id = (SELECT train_id FROM trains WHERE train_name = ?), " +
                           "departure_station_id = (SELECT station_id FROM stations WHERE station_name = ?), " +
                           "arrival_station_id = (SELECT station_id FROM stations WHERE station_name = ?), " +
                           "timetable_date = ?, arrival_time = ?, departure_time = ? WHERE timetable_id = ?";
            PreparedStatement ps = con.c.prepareStatement(query);
            ps.setString(1, (String) trainComboBox.getSelectedItem());
            ps.setString(2, (String) departureStationComboBox.getSelectedItem());
            ps.setString(3, (String) arrivalStationComboBox.getSelectedItem());
            ps.setDate(4, new java.sql.Date(dateChooser.getDate().getTime()));
            ps.setTime(5, new java.sql.Time(((java.util.Date) arrivalTimeSpinner.getValue()).getTime()));
            ps.setTime(6, new java.sql.Time(((java.util.Date) departureTimeSpinner.getValue()).getTime()));
            ps.setInt(7, selectedTimetableId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Timetable Entry Updated Successfully!");
            model.setRowCount(0); // Clear the existing table data
            fetchTimetableData(); // Refresh the table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTimetableEntry() {
        Conn con = new Conn();
        try {
            String query = "DELETE FROM timetable WHERE timetable_id = ?";
            PreparedStatement ps = con.c.prepareStatement(query);
            ps.setInt(1, selectedTimetableId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Timetable Entry Deleted Successfully!");
            model.setRowCount(0); // Clear the existing table data
            fetchTimetableData(); // Refresh the table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void populateTrainComboBox() {
        trainComboBox.removeAllItems();
        Conn con = new Conn();
        try {
            String query = "SELECT train_name FROM trains";
            ResultSet rs = con.s.executeQuery(query);
            while (rs.next()) {
                trainComboBox.addItem(rs.getString("train_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateStationComboBox() {
        departureStationComboBox.removeAllItems();
        arrivalStationComboBox.removeAllItems();
        Conn con = new Conn();
        try {
            String query = "SELECT station_name FROM stations";
            ResultSet rs = con.s.executeQuery(query);
            while (rs.next()) {
                departureStationComboBox.addItem(rs.getString("station_name"));
                arrivalStationComboBox.addItem(rs.getString("station_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        trainComboBox.setSelectedIndex(-1);
        departureStationComboBox.setSelectedIndex(-1);
        arrivalStationComboBox.setSelectedIndex(-1);
        dateChooser.setDate(null);
        arrivalTimeSpinner.setValue(Calendar.getInstance().getTime());
        departureTimeSpinner.setValue(Calendar.getInstance().getTime());
        selectedTimetableId = -1; // Reset selectedTimetableId
    }
}
