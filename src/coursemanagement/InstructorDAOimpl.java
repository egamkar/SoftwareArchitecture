package coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorDAOimpl implements InstructorDAO {
	
	private static final String findById = "Select * from instructor where uuid=?";


	@Override
	public Instructor returnInstructorInfo(int uuid) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(findById);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Instructor inst = new Instructor(rs.getInt("uuid"),rs.getString("name"),rs.getString("address"),rs.getString("phoneno"));
				return inst;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
}

	@Override
	public void insertInstructorInfo(Instructor inst) {
		// TODO Auto-generated method stub
		
	}

}
