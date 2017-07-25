package coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class coursesDAOImpl implements coursesDAO {
	
	private static final String prereqSQL = "select prereqcourseid from prereqs where courseid=? ";
	
	

	@Override
	public HashSet<Integer> returnPrereqsForCourse(int cid) {
		HashSet<Integer>prereqs = new HashSet<>();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(prereqSQL);
			stmt.setInt(1, cid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				prereqs.add(rs.getInt("prereqcourseid"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prereqs;
		
	}

}
