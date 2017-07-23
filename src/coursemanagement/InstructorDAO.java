package coursemanagement;

public interface InstructorDAO {
	
	public Instructor returnInstructorInfo(int uuid);
	public void insertInstructorInfo(Instructor inst);


}
