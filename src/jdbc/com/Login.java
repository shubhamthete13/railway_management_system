package jdbc.com;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.*;

public class Login extends JFrame implements ActionListener{
	JLabel label,label2;
	JTextField nametxTextField;
	JPasswordField passwordField;
	JButton login,signButton,del,update;
	public Login() {
		setLayout(null);
		
		label = new JLabel("ENTER NAME ");
		label.setBounds(50, 50, 100, 50);
		add(label);
		
		nametxTextField = new JTextField();
		nametxTextField.setBounds(150, 60, 100, 30);
		add(nametxTextField);
		
		label2 = new JLabel("ENTER password ");
		label2.setBounds(50, 100, 100, 50);
		add(label2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(200, 120, 100, 30);
		add(passwordField);
		
		login = new JButton("add");
		login.setBounds(50,200,100, 50);
		login.addActionListener(this);
		add(login);
		
		signButton = new JButton("print");
		signButton.setBounds(150,200,100, 50);
		signButton.addActionListener(this);
		add(signButton);
		
		del = new JButton("delete");
		del.setBounds(250,200,100, 50);
		del.addActionListener(this);
		add(del);
		
		update = new JButton("Update");
		update.setBounds(350,200,100,50);
		update.addActionListener(this);
		add(update);
		
		
		
		setSize(500,500);
		setLocation(500, 50);
		setVisible(true);
		
	}
	public static void main(String args[]) {
		new Login();
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		Conn con = new Conn();
		if(ae.getSource()==signButton) {
			try {
				ResultSet rSet = con.s.executeQuery("select * from admin");
				
				if(rSet.next()) {
					JOptionPane.showMessageDialog(null,"Name :"+rSet.getString(1)+"pass : "+rSet.getString("pass"));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(ae.getSource()==login) {
			try {
				
				int ans = con.s.executeUpdate("insert into admin values('"+nametxTextField.getText()+"','"+passwordField.getText()+"')");
			
				if(ans>0) {
					JOptionPane.showMessageDialog(null,"Inserted success");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if(ae.getSource()==del) {
			try {
				int ans = con.s.executeUpdate("delete from admin where name = '"+nametxTextField.getText()+"' ");
			
				if(ans>0) {
					JOptionPane.showMessageDialog(null,"deleted Successfully");
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(ae.getSource()==update) {
			try {
				int ans = con.s.executeUpdate("update admin set pass = '"+passwordField.getText()+"' where name = '"+nametxTextField.getText()+"' ");
			
				if(ans>0) {
					JOptionPane.showMessageDialog(null,"Updated Successfully");
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
