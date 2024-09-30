package jdbc.com;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MangesTrains extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldName;
    private JTextField textFieldNumber;
    private JTable table;
    private JTextField textFieldSeat;
    private JTextField textFieldTicket;
    private int selectedTrainId = -1;

    public MangesTrains() {
        setLayout(null);

        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"ID", "Name", "Number", "Seat", "Ticket", "Type", "Description", "Created", "Updated"});
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
        
        JLabel lblName = new JLabel("Train Name");
        lblName.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblName.setBounds(224, 40, 149, 25);
        add(lblName);

        JLabel lblNumber = new JLabel("Number");
        lblNumber.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblNumber.setBounds(224, 80, 149, 25);
        add(lblNumber);

        JLabel lblType = new JLabel("Type");
        lblType.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblType.setBounds(224, 200, 149, 25);
        add(lblType);

        JLabel lblDescription = new JLabel("Description");
        lblDescription.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblDescription.setBounds(224, 240, 149, 25);
        add(lblDescription);

        textFieldName = new JTextField();
        textFieldName.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textFieldName.setBounds(396, 33, 227, 32);
        add(textFieldName);
        textFieldName.setColumns(10);

        textFieldNumber = new JTextField();
        textFieldNumber.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textFieldNumber.setBounds(396, 77, 227, 32);
        add(textFieldNumber);
        textFieldNumber.setColumns(10);

        String[] trainTypes = {"Express", "Local", "Passenger", "Superfast"};
        JComboBox<String> comboBoxType = new JComboBox<>(trainTypes);
        comboBoxType.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        comboBoxType.setBounds(396, 200, 227, 32);
        add(comboBoxType);

        JTextArea textAreaDescription = new JTextArea();
        textAreaDescription.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textAreaDescription.setBounds(396, 248, 227, 32);
        add(textAreaDescription);
        

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Conn con = new Conn();
                try {
                    if (textFieldName.getText().isEmpty() || textFieldNumber.getText().isEmpty() ||
                        textFieldSeat.getText().isEmpty() || textFieldTicket.getText().isEmpty() ||
                        textAreaDescription.getText().isEmpty() || comboBoxType.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int seatCount = Integer.parseInt(textFieldSeat.getText());
                    double ticketPrice = Double.parseDouble(textFieldTicket.getText());
                    String query = "INSERT INTO trains (train_name, train_number, train_seat_number, train_ticket, train_type, train_description) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = con.c.prepareStatement(query);
                    pstmt.setString(1, textFieldName.getText());
                    pstmt.setString(2, textFieldNumber.getText());
                    pstmt.setInt(3, seatCount);
                    pstmt.setDouble(4, ticketPrice);
                    pstmt.setString(5, comboBoxType.getSelectedItem().toString());
                    pstmt.setString(6, textAreaDescription.getText());

                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Train added successfully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error while adding train");
                    }

                    pstmt.close();
                    con.c.close();

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numeric values for seat count and ticket price", "Validation Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                textFieldName.setText("");
                textFieldNumber.setText("");
                textFieldSeat.setText("");
                textFieldTicket.setText("");
                textAreaDescription.setText("");
            }
        });
        btnAdd.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnAdd.setBounds(127, 379, 132, 46);
        add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Conn con = new Conn();

                try {
                    if (textFieldName.getText().isEmpty() || textFieldNumber.getText().isEmpty() ||
                        textFieldSeat.getText().isEmpty() || textFieldTicket.getText().isEmpty() ||
                        textAreaDescription.getText().isEmpty() || comboBoxType.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int seatCount = Integer.parseInt(textFieldSeat.getText());
                    double ticketPrice = Double.parseDouble(textFieldTicket.getText());

                    String updateQuery = "UPDATE trains SET train_name= ?, train_number = ?, train_seat_number = ?, train_ticket = ?, train_type = ?, train_description = ? WHERE train_number = ?";
                    PreparedStatement pstmt = con.c.prepareStatement(updateQuery);

                    pstmt.setString(1, textFieldName.getText());
                    pstmt.setString(2, textFieldNumber.getText());
                    pstmt.setInt(3, seatCount);
                    pstmt.setDouble(4, ticketPrice);
                    pstmt.setString(5, comboBoxType.getSelectedItem().toString());
                    pstmt.setString(6, textAreaDescription.getText());
                    pstmt.setString(7, textFieldNumber.getText());

                    int result = pstmt.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Train updated successfully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error while updating train. Train number may not exist.", "Update Error", JOptionPane.ERROR_MESSAGE);
                    }

                    pstmt.close();
                    con.c.close();

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numeric values for seat count and ticket price", "Validation Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                textFieldName.setText("");
                textFieldNumber.setText("");
                textFieldSeat.setText("");
                textFieldTicket.setText("");
                textAreaDescription.setText("");
            }
        });
        btnUpdate.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnUpdate.setBounds(316, 379, 132, 46);
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Conn con = new Conn();
                try {
                    String trainNumber = textFieldNumber.getText();
                    if (trainNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter the train number to delete", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String deleteQuery = "DELETE FROM trains WHERE train_number = ?";
                    PreparedStatement pstmt = con.c.prepareStatement(deleteQuery);
                    pstmt.setString(1, trainNumber);

                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Train deleted successfully");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error while deleting train. Train number may not exist.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    }

                    pstmt.close();
                    con.c.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                textFieldNumber.setText("");
            }
        });
        btnDelete.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        btnDelete.setBounds(505, 379, 132, 46);
        add(btnDelete);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Conn con = new Conn();
                try {
                    String trainNumber = textFieldNumber.getText(); // Get the train number from the correct text field
                    if (trainNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter the Train Number to search", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String searchQuery = "SELECT train_name, train_number, train_seat_number, train_ticket, train_type, train_description FROM trains WHERE train_number = ?";
                    PreparedStatement pstmt = con.c.prepareStatement(searchQuery);
                    pstmt.setString(1, trainNumber);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        textFieldName.setText(rs.getString("train_name"));
                        textFieldNumber.setText(rs.getString("train_number"));
                        textFieldSeat.setText(rs.getString("train_seat_number")); // Ensure this field name matches your definition
                        textFieldTicket.setText(rs.getString("train_ticket")); // Ensure this field name matches your definition
                        comboBoxType.setSelectedItem(rs.getString("train_type"));
                        textAreaDescription.setText(rs.getString("train_description"));
                    } else {
                        JOptionPane.showMessageDialog(null, "Train not found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 446, 1056, 186);
        scrollPane.setPreferredSize(new Dimension(1056, 303));
        add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JLabel lblSeat = new JLabel("Seat");
        lblSeat.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblSeat.setBounds(224, 120, 149, 25);
        add(lblSeat);

        textFieldSeat = new JTextField();
        textFieldSeat.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textFieldSeat.setBounds(396, 120, 227, 32);
        add(textFieldSeat);
        textFieldSeat.setColumns(10);

        JLabel lblTicket = new JLabel("Ticket");
        lblTicket.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        lblTicket.setBounds(224, 160, 149, 25);
        add(lblTicket);

        textFieldTicket = new JTextField();
        textFieldTicket.setFont(new Font("Sitka Text", Font.PLAIN, 18));
        textFieldTicket.setBounds(396, 160, 227, 32);
        add(textFieldTicket);
        textFieldTicket.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\RailwaySystem\\Images\\008 Rainy Ashville.png"));
        lblNewLabel.setBounds(0, 0, 1086, 447);
        add(lblNewLabel);

        refreshTable();
    }

    private void refreshTable() {
        Conn con = new Conn();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);  // Clear existing rows
        try {
            String selectQuery = "SELECT * FROM trains";
            ResultSet rs = con.s.executeQuery(selectQuery);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("train_id"), rs.getString("train_name"), rs.getString("train_number"), rs.getInt("train_seat_number"), rs.getDouble("train_ticket"), rs.getString("train_type"), rs.getString("train_description"), rs.getString("created_at"), rs.getString("updated_at")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
