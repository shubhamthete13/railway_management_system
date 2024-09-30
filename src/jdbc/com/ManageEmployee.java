package jdbc.com;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class ManageEmployee extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldName;
    private JTextField textFieldMobile;
    private JTextField textFieldEmailId;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JTable table;
    private JComboBox<String> comboBoxStatus;
    private JComboBox<String> comboBoxRole;
    private DefaultTableModel model;
    private int selectedEmployeeId = -1;
    private JTextField textFieldAddress;

    public ManageEmployee() {
        setLayout(null);

        model = new DefaultTableModel(new Object[][] {},
            new String[] {"ID", "Name", "Mobile", "Email", "Username", "Password", "Address", "Status", "Role"});

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
        // Add labels and text fields
        addLabelsAndTextFields();

        // Combo boxes for status and role
        comboBoxStatus = new JComboBox<>(new String[] {"Active", "Inactive"});
        comboBoxStatus.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        comboBoxStatus.setBounds(432, 287, 204, 28);
        add(comboBoxStatus);
        
        comboBoxRole = new JComboBox<>();
        comboBoxRole.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        comboBoxRole.setBounds(432, 327, 204, 28);
        add(comboBoxRole);
        fetchRoles();

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 461, 1096, 288);
        add(scrollPane);

        // Add action listeners for buttons
        addButtons();

        // Fetch employee data
        fetchEmployeeData();
    }

    private void addLabelsAndTextFields() {
        String[] labels = {"Employee Name", "Mobile Number", "Email Id", "Username", "Password", "Address", "Status", "Role"};
        int yPos = 40;
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            lbl.setBounds(224, yPos, 149, 25);
            add(lbl);
            yPos += 40;
        }

        textFieldName = new JTextField();
        textFieldMobile = new JTextField();
        textFieldEmailId = new JTextField();
        textFieldUsername = new JTextField();
        passwordField = new JPasswordField();
        textFieldAddress = new JTextField();

        JTextField[] textFields = {textFieldName, textFieldMobile, textFieldEmailId, textFieldUsername, passwordField, textFieldAddress};
        yPos = 44;
        for (JTextField textField : textFields) {
            textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            textField.setBounds(432, yPos, 204, 28);
            add(textField);
            yPos += 40;
        }
    }

    private void fetchRoles() {
        Conn conn = new Conn();
        try {
            String query = "SELECT role_id, role_name FROM roles";
            Statement stmt = conn.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                comboBoxRole.addItem(rs.getString("role_name"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButtons() {
        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        btnAdd.setBounds(128, 395, 132, 46);
        btnAdd.addActionListener(e -> addEmployee());
        add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        btnUpdate.setBounds(317, 395, 132, 46);
        btnUpdate.addActionListener(e -> updateEmployee());
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        btnDelete.setBounds(502, 395, 135, 46);
        btnDelete.addActionListener(e -> deleteEmployee());
        add(btnDelete);

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        btnSearch.setBounds(678, 395, 132, 46);
        btnSearch.addActionListener(e -> searchEmployee());
        add(btnSearch);
    }

    private void addEmployee() {
        Conn conn = new Conn();
        try {
            if (!validateFields()) return;

            String query = "INSERT INTO employees (employee_name, employee_mobile, employee_email, employee_username, employee_password, employee_address, status, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psmt = conn.c.prepareStatement(query);
            psmt.setString(1, textFieldName.getText());
            psmt.setString(2, textFieldMobile.getText());
            psmt.setString(3, textFieldEmailId.getText());
            psmt.setString(4, textFieldUsername.getText());
            psmt.setString(5, new String(passwordField.getPassword()));
            psmt.setString(6, textFieldAddress.getText());
            psmt.setString(7, comboBoxStatus.getSelectedItem().toString());

            int roleId = getRoleId(comboBoxRole.getSelectedItem().toString());
            psmt.setInt(8, roleId);
            psmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Employee added successfully!");
            fetchEmployeeData();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        textFieldName.setText("");
        textFieldMobile.setText("");
        textFieldEmailId.setText("");
        textFieldUsername.setText("");
        passwordField.setText("");
        textFieldAddress.setText("");
        
    }

    private int getRoleId(String roleName) {
        Conn conn = new Conn();
        try {
            String query = "SELECT role_id FROM roles WHERE role_name = ?";
            PreparedStatement psmt = conn.c.prepareStatement(query);
            psmt.setString(1, roleName);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("role_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Role not found
    }

    private boolean validateFields() {
        if (textFieldName.getText().isEmpty() || textFieldMobile.getText().isEmpty() || textFieldEmailId.getText().isEmpty() || textFieldUsername.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty() || textFieldAddress.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled out");
            return false;
        }
        return true;
    }

    private void updateEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee to update", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateFields()) return;

        Conn conn = new Conn();
        try {
            String query = "UPDATE employees SET employee_name = ?, employee_mobile = ?, employee_email = ?, employee_username = ?, employee_password = ?, employee_address = ?, status = ?, role_id = ? WHERE employee_id = ?";
            PreparedStatement psmt = conn.c.prepareStatement(query);
            psmt.setString(1, textFieldName.getText());
            psmt.setString(2, textFieldMobile.getText());
            psmt.setString(3, textFieldEmailId.getText());
            psmt.setString(4, textFieldUsername.getText());
            psmt.setString(5, new String(passwordField.getPassword()));
            psmt.setString(6, textFieldAddress.getText());
            psmt.setString(7, comboBoxStatus.getSelectedItem().toString());

            int roleId = getRoleId(comboBoxRole.getSelectedItem().toString());
            psmt.setInt(8, roleId);
            psmt.setInt(9, selectedEmployeeId);
            psmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Employee updated successfully!");
            fetchEmployeeData();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(null, "Please select an employee to delete", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Conn conn = new Conn();
        try {
            String query = "DELETE FROM employees WHERE employee_id = ?";
            PreparedStatement psmt = conn.c.prepareStatement(query);
            psmt.setInt(1, selectedEmployeeId);
            psmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Employee deleted successfully!");
            fetchEmployeeData();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchEmployee() {
        String searchTerm = JOptionPane.showInputDialog("Enter employee name or ID to search:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        Conn conn = new Conn();
        try {
            String query = "SELECT e.employee_id, e.employee_name, e.employee_mobile, e.employee_email, e.employee_username, e.employee_password, e.employee_address, e.status, r.role_name " +
                           "FROM employees e JOIN roles r ON e.role_id = r.role_id " +
                           "WHERE e.employee_name LIKE ? OR e.employee_id = ?";
            PreparedStatement psmt = conn.c.prepareStatement(query);
            psmt.setString(1, "%" + searchTerm + "%");
            psmt.setString(2, searchTerm);
            ResultSet rs = psmt.executeQuery();
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("employee_id"),
                    rs.getString("employee_name"),
                    rs.getString("employee_mobile"),
                    rs.getString("employee_email"),
                    rs.getString("employee_username"),
                    rs.getString("employee_password"),
                    rs.getString("employee_address"),
                    rs.getString("status"),
                    rs.getString("role_name") // Fetch role_name instead of role_id
                });
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchEmployeeData() {
        Conn conn = new Conn();
        try {
            String query = "SELECT e.employee_id, e.employee_name, e.employee_mobile, e.employee_email, e.employee_username, e.employee_password, e.employee_address, e.status, r.role_name " +
                           "FROM employees e JOIN roles r ON e.role_id = r.role_id";
            Statement stmt = conn.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("employee_id"),
                    rs.getString("employee_name"),
                    rs.getString("employee_mobile"),
                    rs.getString("employee_email"),
                    rs.getString("employee_username"),
                    rs.getString("employee_password"),
                    rs.getString("employee_address"),
                    rs.getString("status"),
                    rs.getString("role_name") // Fetch role_name instead of role_id
                });
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    selectedEmployeeId = (int) table.getValueAt(table.getSelectedRow(), 0);
                    textFieldName.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                    textFieldMobile.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
                    textFieldEmailId.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
                    textFieldUsername.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
                    passwordField.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
                    textFieldAddress.setText(table.getValueAt(table.getSelectedRow(), 6).toString());
                    comboBoxStatus.setSelectedItem(table.getValueAt(table.getSelectedRow(), 7).toString());
                    comboBoxRole.setSelectedItem(table.getValueAt(table.getSelectedRow(), 8).toString());
                }
            }
        });
    }
}
