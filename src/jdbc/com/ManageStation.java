package jdbc.com;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class ManageStation extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldstationName;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> comboBoxType;
    private JTextArea textArea;
    private int selectedStationId = -1; // Holds the ID of the selected station

    public ManageStation() {
        setLayout(null);
        model = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Name", "Type", "Description", "Created", "Updated"});

        // Initialize JTable
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

        JLabel lblName = new JLabel("Station Name");
        lblName.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblName.setBounds(224, 40, 149, 25);
        add(lblName);

        JLabel lblType = new JLabel("Station Type");
        lblType.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblType.setBounds(224, 91, 149, 25);
        add(lblType);

        JLabel lbldescription = new JLabel("Station Description");
        lbldescription.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lbldescription.setBounds(224, 141, 149, 25);
        add(lbldescription);

        textFieldstationName = new JTextField();
        textFieldstationName.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textFieldstationName.setBounds(396, 33, 227, 40);
        add(textFieldstationName);
        textFieldstationName.setColumns(10);

        String[] stationType = {"Main", "Substation", "Junction"};
        comboBoxType = new JComboBox<>(stationType);
        comboBoxType.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        comboBoxType.setBounds(396, 83, 227, 41);
        add(comboBoxType);

        textArea = new JTextArea();
        textArea.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textArea.setBounds(396, 143, 227, 40);
        add(textArea);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Conn conn = new Conn();
                try {
                    if (textFieldstationName.getText().isEmpty() || comboBoxType.getSelectedItem() == null || textArea.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String query = "INSERT INTO stations (station_name, station_type, station_description) VALUES (?, ?, ?)";
                    PreparedStatement psmt = conn.c.prepareStatement(query);
                    psmt.setString(1, textFieldstationName.getText());
                    psmt.setString(2, comboBoxType.getSelectedItem().toString());
                    psmt.setString(3, textArea.getText());
                    int result = psmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Station Added Successfully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error while adding Station");
                    }
                    psmt.close();
                    conn.c.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                textFieldstationName.setText("");
                textArea.setText("");
            }
        });
        btnAdd.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnAdd.setBounds(127, 379, 132, 46);
        add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedStationId == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a station from the table to update.");
                    return;
                }

                Conn conn = new Conn();
                try {
                    String query = "UPDATE stations SET station_name = ?, station_type = ?, station_description = ? WHERE station_id = ?";
                    PreparedStatement psmt = conn.c.prepareStatement(query);
                    psmt.setString(1, textFieldstationName.getText());
                    psmt.setString(2, comboBoxType.getSelectedItem().toString());
                    psmt.setString(3, textArea.getText());
                    psmt.setInt(4, selectedStationId);

                    int result = psmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Station Updated Successfully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error while updating Station");
                    }
                    psmt.close();
                    conn.c.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                textFieldstationName.setText("");
                textArea.setText("");
                selectedStationId = -1;
            }
        });
        btnUpdate.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnUpdate.setBounds(316, 379, 132, 46);
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedStationId == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a station from the table to delete.");
                    return;
                }

                Conn conn = new Conn();
                try {
                    String query = "DELETE FROM stations WHERE station_id = ?";
                    PreparedStatement psmt = conn.c.prepareStatement(query);
                    psmt.setInt(1, selectedStationId);

                    int result = psmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Station Deleted Successfully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error while deleting Station");
                    }
                    psmt.close();
                    conn.c.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                textFieldstationName.setText("");
                textArea.setText("");
                selectedStationId = -1;
            }
        });
        btnDelete.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnDelete.setBounds(501, 379, 135, 46);
        add(btnDelete);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Conn con = new Conn();
                try {
                    String stationName = textFieldstationName.getText();
                    if (stationName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter the Station Name to search", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String searchQuery = "SELECT * FROM stations WHERE station_name = ?";
                    PreparedStatement pstmt = con.c.prepareStatement(searchQuery);
                    pstmt.setString(1, stationName);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        selectedStationId = rs.getInt("station_id");
                        textFieldstationName.setText(rs.getString("station_name"));
                        comboBoxType.setSelectedItem(rs.getString("station_type"));
                        textArea.setText(rs.getString("station_description"));
                    } else {
                        JOptionPane.showMessageDialog(null, "Station not found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    }

                    rs.close();
                    pstmt.close();
                    con.c.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnSearch.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnSearch.setBounds(677, 379, 132, 46);
        add(btnSearch);

        JScrollPane scrollPane1 = new JScrollPane(table);
        scrollPane1.setBounds(0, 446, 1056, 186);
        scrollPane1.setPreferredSize(new Dimension(1056, 303));
        add(scrollPane1);
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\008 Rainy Ashville.png"));
        lblNewLabel.setBounds(0, 0, 1124, 461);
        add(lblNewLabel);

        // Add table selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        selectedStationId = (int) table.getValueAt(selectedRow, 0);
                        textFieldstationName.setText(table.getValueAt(selectedRow, 1).toString());
                        comboBoxType.setSelectedItem(table.getValueAt(selectedRow, 2).toString());
                        textArea.setText(table.getValueAt(selectedRow, 3).toString());
                    }
                }
            }
        });

        // Fetch initial data
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0); // Clear existing rows
        Conn con = new Conn();
        try {
            String query = "SELECT station_id, station_name, station_type, station_description, created_at, updated_at FROM stations";
            ResultSet rs = con.s.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                String type = rs.getString("station_type");
                String description = rs.getString("station_description");
                String createdAt = rs.getString("created_at");
                String updatedAt = rs.getString("updated_at");
                model.addRow(new Object[]{id, name, type, description,createdAt,updatedAt});
            }
            rs.close();
            con.c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
