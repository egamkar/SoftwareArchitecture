package coursemanagement;

import java.util.List;
import java.util.ArrayList;

public class Term {
	public List<Integer> coursesForTerm = new ArrayList();
	public String termInfo;
	
	public Term(String termInfo)
	{
		this.termInfo = termInfo;
	}
	public void addCourses(List coursesForTerm ){
		this.coursesForTerm = coursesForTerm;
	}
	public void getCoursesByTerm(){
		
		for(Integer courseId:coursesForTerm){
			System.out.print(courseId+"\n");
		}
		
	}

}
