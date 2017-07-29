package coursemanagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class StudentDAOimpl implements StudentDAO {

	private static final String findById = "Select * from student where uuid=?";
	private static final String insertAcademicRecord = "Insert into academicRecord values(?,?,?,?,?,?)";
	private static final String returnRecord = "Select * from academicrecord where studentuuid=?";
	private static final String ifCompSuccess = "Select count(*) as rowcount from academicrecord where studentuuid=? AND courseuuid=? AND (grade='A' OR grade ='B' OR grade='C')";
	private static final String allCoursesByStudent = "Select courseuuid from academicrecord where studentuuid=? AND (grade='A' OR grade ='B' OR grade='C')";
	private static final String ifAlreadyEnrolled = "Select count(*) as rowcount from academicrecord where studentuuid=? AND courseuuid=? AND grade='NA'";
	private static final String canbeEnrolled = "Select *  from courseselection where courseuuid=? AND seminfo=?";
	private static final String insertStudent = "insert into student(uuid,name,address,phoneno) select MAX(uuid)+1,?,?,? FROM student";
	
	
	@Override
	public Student returnStudentInfo(int uuid) {

		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(findById);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Student stud = new Student(rs.getInt("uuid"), rs.getString("name"), rs.getString("address"),
						rs.getString("phoneno"));
				return stud;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertStudentInfo(String name, String address, String phoneNo) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(insertStudent);
			stmt.setString(1, name);
			stmt.setString(2, address);
			stmt.setString(3, phoneNo);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enterAcademicRecord(Integer studId, Integer courseid, String grade, Integer instuuid, String termyear,
			String comment) {
		DBConnection conn = new DBConnection();
		try {
			
			PreparedStatement stmt = conn.dbConnection().prepareStatement("select count(*) as rowcount from academicRecord where studentuuid=? AND courseuuid=? AND termyear=?   ");
			stmt.setInt(1, studId);
			stmt.setInt(2, courseid);
			stmt.setString(3, termyear);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			if(rs.getInt("rowcount") == 0)
			{
				PreparedStatement stmt1 = conn.dbConnection().prepareStatement(insertAcademicRecord);
				stmt1.setInt(1, studId);
				stmt1.setInt(2, instuuid);
				stmt1.setInt(3, courseid);
				stmt1.setString(4, grade);
				stmt1.setString(5, comment);
				stmt1.setString(6, termyear);
				System.out.println("Academic record INSERT executed");

				int rs1 = stmt1.executeUpdate();
			}
			else
			{
				PreparedStatement stmt1 = conn.dbConnection().prepareStatement("Update academicRecord SET grade=?, comment=? WHERE studentuuid=? AND courseuuid=? AND termyear=? ");
				stmt1.setString(1, grade);
				stmt1.setString(2, comment);
				stmt1.setInt(3, studId);
				stmt1.setInt(4, courseid);
				stmt1.setString(5, termyear);
				System.out.println("Academic record update executed");
				int rs1 = stmt1.executeUpdate();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public ArrayList<academicRecord> returnRecordForStudent(int uuid) {

		ArrayList<academicRecord> recordList = new ArrayList<>();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(returnRecord);
			stmt.setInt(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				academicRecord record = new academicRecord(rs.getInt("studentuuid"), rs.getInt("courseuuid"),
						rs.getString("grade"), rs.getInt("instuuid"), rs.getString("termyear"),
						rs.getString("comment"));
				recordList.add(record);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return recordList;

	}
	
	public  String requestCourse(int studid,int cid,String term){
		
		boolean isCourseOffered = new termDAOImpl().isCourseTaughtInTheTerm(cid, term);
		if(!isCourseOffered)
			return "Course is not offered this sem";
		boolean alreadyCompletedSuccessfully = checkifCourseAlreadyCompletedSuccessfully(studid,cid);
		if(alreadyCompletedSuccessfully)
			return "Course is already completed";
		boolean isAlreadyEnrolled = checkifAlreadyEnrolled(studid,cid);
		if(isAlreadyEnrolled)
				return "Already enrolled in this course";
		boolean checkifPreRequsSatisifed = checkifPreReqsSatisifed(studid,cid);
		if(!checkifPreRequsSatisifed)
			return "Please check prereqs, they are not yet satisfied";
		boolean checkifEnrolled = checkifCanEnroll(cid,studid);
		if(!checkifEnrolled)
			return "Failure to enroll, no availability ";
		enrollStudent(studid,cid);
		
		return "Success, enrolled for the course";
	}
	
	

	private static void enrollStudent(int studid, int cid) {
		
		
		
		
	}

	private  boolean checkifAlreadyEnrolled(int studid, int cid) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(ifAlreadyEnrolled);
			stmt.setInt(1, studid);
			stmt.setInt(2, cid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt("rowcount")> 0)
					return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private  boolean checkifPreReqsSatisifed(int studid, int cid) {
		
		HashSet<Integer> completedCourses = new HashSet<>();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(allCoursesByStudent);
			stmt.setInt(1, studid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				completedCourses.add(rs.getInt("courseuuid"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashSet<Integer> prereqs = new coursesDAOImpl().returnPrereqsForCourse(cid);
		
		if(completedCourses.containsAll(prereqs))
			return true;
		else
			return false;
		}

	private static boolean checkifCourseAlreadyCompletedSuccessfully(int studid, int cid) {
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(ifCompSuccess);
			stmt.setInt(1, studid);
			stmt.setInt(2, cid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt("rowcount")> 0)
					return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
}
	
	
	
	public   boolean checkifCanEnroll(int cid,int studid){
		ArrayList<courseSelection> courseselections = new ArrayList<>();
		DBConnection conn = new DBConnection();
		try {
			PreparedStatement stmt = conn.dbConnection().prepareStatement(canbeEnrolled);
			stmt.setInt(1, cid);
			stmt.setString(2, getCurrentTermWithYear());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				courseSelection cos = new courseSelection(rs.getInt("courseuuid"), rs.getString("seminfo"), rs.getInt("instuuid"),
						rs.getInt("studuuid"));
				courseselections.add(cos);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<courseselections.size();i++){
			 System.out.println("Enrolled count is"+courseselections.get(i).enrolledCount);
			if(courseselections.get(i).enrolledCount < 3)
			{
				DBConnection conn1 = new DBConnection();
				try {
					PreparedStatement stmt = conn1.dbConnection().prepareStatement("UPDATE courseselection SET studuuid=studuuid+1 WHERE instuuid =? AND  courseuuid =? AND seminfo= ?");
					stmt.setInt(1, courseselections.get(i).instuuid);
					stmt.setInt(2, courseselections.get(i).courseId);
					stmt.setString(3, courseselections.get(i).termInfo);
					//stmt.setString(4, getCurrentTermWithYear());
					stmt.executeUpdate();
					enterAcademicRecord(studid, courseselections.get(i).courseId, "NA", courseselections.get(i).instuuid, courseselections.get(i).termInfo,
							null); 
					return true;
					

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return false;
		
		
		
		
	}
	
	public static String getCurrentTermWithYear(){
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
