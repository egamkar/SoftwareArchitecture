package coursemanagement;

import java.sql.SQLException;

public interface InstructorDAO {

	public Instructor returnInstructorInfo(int uuid);

	

	public boolean hireInstructor(int uuid) throws SQLException;

	public void leaveInstructor(int uuid);

	void insertInstructorInfo(String name, String address, String phoneNo);

}
