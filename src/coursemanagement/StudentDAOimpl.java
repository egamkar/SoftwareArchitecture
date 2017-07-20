package coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAOimpl implements StudentDAO {
	
	private static final String findById = "Select * from student where uuid=?";

	@Override
	public Student returnStudentInfo(int uuid) {
		
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(findById);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Student stud = new Student(rs.getInt("uuid"),rs.getString("name"),rs.getString("address"),rs.getString("phoneno"));
				return stud;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertStudentInfo(Student student) {
		
		
	}

	@Override
	public void enterAcademicRecord(Integer courseid, String grade, Integer instuuid, String termyear, String comment) {
		
		
	}

}
