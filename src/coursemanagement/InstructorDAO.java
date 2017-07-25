package coursemanagement;

import java.sql.SQLException;

public interface InstructorDAO {

	public Instructor returnInstructorInfo(int uuid);

	public void insertInstructorInfo(Instructor inst);

	public boolean hireInstructor(int uuid) throws SQLException;

	public void leaveInstructor(int uuid);

}
