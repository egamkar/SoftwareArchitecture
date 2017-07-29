package coursemanagement;

import java.util.ArrayList;

public interface StudentDAO {

	public Student returnStudentInfo(int uuid);

	public  void enterAcademicRecord(Integer studId, Integer courseid, String grade, Integer instuuid, String termyear,
			String comment);

	public ArrayList<academicRecord> returnRecordForStudent(int uuid);

	void insertStudentInfo(String name, String address, String phoneNo);
}
