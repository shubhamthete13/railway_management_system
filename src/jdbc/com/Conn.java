package jdbc.com;
import java.sql.*;
public class Conn {
	Connection c;
	Statement s;
	public Conn() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railwayDB","root","root");
			
			s=c.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
