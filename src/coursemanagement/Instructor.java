package coursemanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instructor extends User {
	private static final boolean INACTIVE = false;
	private static final boolean ACTIVE = true;
	private Map<String,Integer> teachingRecord = new HashMap<>();

	private ArrayList<Integer> eligibleCourse = new ArrayList();
	private boolean state = INACTIVE;
	
	public void addEligibleCourseIds(ArrayList<Integer> courseids){
		this.eligibleCourse = courseids;
	}
	

	public Instructor(int uuid, String name, String address, String phoneNo) {
		super(uuid, name, address, phoneNo);
	}
	
	public ArrayList<Integer> getEligibleCourses(){
		return eligibleCourse;
		
	}
	
	public void setState(boolean stateVal){
		this.state = stateVal;
	}
	
	public boolean getState(){
		return this.state;
	}
	
	public void  teachingRecord(String termWithYear,int courseuuid){
		this.teachingRecord.put(termWithYear, courseuuid);
		
	}
	public HashMap<String, Integer> getTeachingRecord(){
		return (HashMap<String, Integer>) teachingRecord;
	}
}
