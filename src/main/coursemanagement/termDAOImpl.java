package main.coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class termDAOImpl implements termDAO {
	
	private static String courseByTerm = "Select count(*) AS rowcount from term where uuid=? AND termInfo=?";

	@Override
	public boolean isCourseTaughtInTheTerm(int cid, String term) {
		
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(courseByTerm);
			stmt.setInt(1, cid);
			stmt.setString(2, term);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt("rowcount")>0)
					return true;
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	
		return false;
	}

}
