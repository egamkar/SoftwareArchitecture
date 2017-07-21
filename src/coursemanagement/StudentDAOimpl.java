package coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAOimpl implements StudentDAO {
	
	private static final String findById = "Select * from student where uuid=?";
	private static final String insertAcademicRecord = "Insert into academicRecord values(?,?,?,?,?,?)";
	private static final String returnRecord = "Select * from academicrecord where studentuuid=?";

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
	public void enterAcademicRecord(Integer studId, Integer courseid, String grade, Integer instuuid, String termyear, String comment) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(insertAcademicRecord);
			stmt.setInt(1, studId);
			stmt.setInt(2, instuuid);
			stmt.setInt(3, courseid);
			stmt.setString(4, grade);
			stmt.setString(5, comment);
			stmt.setString(6, termyear);
			int rs = stmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public ArrayList<academicRecord> returnRecordForStudent(int uuid) {
		
		ArrayList<academicRecord> recordList= new ArrayList<>();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(returnRecord);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				academicRecord record = new academicRecord(rs.getInt("studentuuid"),rs.getInt("courseuuid"),rs.getString("grade"),rs.getInt("instuuid"),rs.getString("termyear"),rs.getString("comment"));
				recordList.add(record);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return recordList;
		
		
		
		
	}
	
	

}
