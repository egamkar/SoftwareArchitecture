package coursemanagement;

import java.util.ArrayList;

public class courseSelection {
	
	public Integer courseId;
	public Integer termyear;
	public String termInfo;
	public Integer instuuid;
	public ArrayList<Integer> assignedStudentIds = new ArrayList<>();
	
	public courseSelection(int cid,int termyear, String termInfo,int intuuid){
		this.courseId = cid;
		this.termyear = termyear;
		this.termInfo = termInfo;
		this.instuuid = intuuid;
	}
	
	public int enrollStudent(int studid){
		
		if(this.assignedStudentIds.size() ==3)
			return 0;
		else if (this.assignedStudentIds.contains(studid)){
			return -1;
		}
		this.assignedStudentIds.add(studid);
		return 1;
		
		
		
	}
	
	
	
	

}
