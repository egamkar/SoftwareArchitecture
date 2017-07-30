package main.coursemanagement;

import java.util.HashSet;

public interface coursesDAO {
	public HashSet<Integer> returnPrereqsForCourse(int cid);

}
