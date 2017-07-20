package coursemanagement;


public interface StudentDAO {
	
	public Student returnStudentInfo(int uuid);
	public void insertStudentInfo(Student student);
	public void enterAcademicRecord(Integer courseid,String grade,Integer instuuid,String termyear,String comment);

}

