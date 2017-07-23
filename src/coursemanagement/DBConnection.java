package coursemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {

	public Connection dbConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/softwareArch", "root",
					"password");
			return conn;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}

}
