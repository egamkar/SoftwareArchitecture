package coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorDAOimpl implements InstructorDAO {

	private static final String findById = "Select * from instructor where uuid=?";
	private static final String setStateInst = "Update instructor set state=? where uuid=?";
	private static final String teachCourse = "Select * from instructor where uuid=?";
	private static final String checkEligible = "Select count(*) AS rowcount from eligibleCourses where instuuid=? AND courseuuid=?";
	private static final String alreadyTeaching = "Select count(*) AS rowcount from teachingRecord where instuuid=? AND termyear=?";
	private static final String updateTeachingRecord = "INSERT into teachingRecord values(?,?)";
	private static final String updateCourseSelection = "INSERT INTO courseselection values(?,?,?,?)";
	private static final String insertInstructor = "insert into instructor(uuid,name,address,phoneno) select MAX(uuid)+1,?,?,? FROM instructor";

	
	@Override
	public Instructor returnInstructorInfo(int uuid) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(findById);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Instructor inst = new Instructor(rs.getInt("uuid"), rs.getString("name"), rs.getString("address"),
						rs.getString("phoneno"), rs.getString("state"));
				return inst;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertInstructorInfo(String name, String address, String phoneNo) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(insertInstructor);
			stmt.setString(1, name);
			stmt.setString(2, address);
			stmt.setString(3, phoneNo);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean hireInstructor(int uuid) throws SQLException {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(setStateInst);
			stmt.setInt(2, uuid);
			stmt.setString(1, "ACTIVE");
			stmt.executeUpdate();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public void leaveInstructor(int uuid) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(setStateInst);
			stmt.setInt(2, uuid);
			stmt.setString(1, "INACTIVE");
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String teachCourse(int uuid, int cid){
		
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(teachCourse);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(rs.getString("state").equals("INACTIVE"))
					return "Error, instructor is not hired first";
				else if(!checkIfEligible(uuid,cid))
					return "Error, instructor is not eligible to teach this course";
				else if(alreadyTeachingCourse(uuid,cid))
					return "Error, instructor is already teaching different course this sem";
				else{
					createTeachingRecord(uuid,cid);
					updateCourseSelection(uuid,cid);
					return "Success, instructor is enrolled for teaching";
				}
				
			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Sorry, can not process this operation due to unknown command";
		}
		
		
		
		return "Sorry, can not process this operation due to unknown command";
	}
	
	

	private void updateCourseSelection(int uuid, int cid) {
		
		String termWithYear = getCurrentTermWithYear();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(updateCourseSelection);
			stmt.setInt(1, 0);
			stmt.setInt(2, uuid);
			stmt.setInt(3, cid);
			stmt.setString(4, termWithYear);
			stmt.executeUpdate();
			
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	
	}

	private void createTeachingRecord(int uuid, int cid) {
		String termWithYear = getCurrentTermWithYear();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(updateTeachingRecord);
			stmt.setInt(1, uuid);
			stmt.setString(2, termWithYear);
			stmt.executeUpdate();
			
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}

	public boolean checkIfEligible(int instid,int courseid){
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(checkEligible);
			stmt.setInt(1, instid);
			stmt.setInt(2, courseid);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt("rowcount");
			if(count > 0)
				return true;
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	public boolean alreadyTeachingCourse(int uuid, int courseid){
		
		String termWithYear = getCurrentTermWithYear();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(alreadyTeaching);
			stmt.setInt(1, uuid);
			stmt.setString(2, termWithYear);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt("rowcount");
			if(count > 0)
				return true;
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	public String getCurrentTermWithYear(){
		DBConnection conn = new DBConnection();
		String[] semesters = { "Fall", "Winter", "Spring", "Summer" };

		int semYear=0,semIndex=0;
		String query1 = "Select * from semTable";
		try {
			PreparedStatement preparedStmt = conn.dbConnection().prepareStatement(query1);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				semYear = rs.getInt("termyear");
				semIndex = rs.getInt("semIndex");
				
				System.out.println("Updates are " + semYear + ", index" + semIndex);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String termWithYear = semesters[semIndex]+semYear;
		return termWithYear;
		
		
	}
	
	
	

}
