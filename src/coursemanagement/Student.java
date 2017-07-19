package coursemanagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import spark.*;

public class Student extends User {
	
	private  ArrayList<academicRecord>records= new ArrayList<academicRecord>();
	private academicRecord test;

	public Student(int uuid, String name, String address, String phoneNo) {
		super(uuid, name, address, phoneNo);
	}

	
	
	
	public  void enterAcademicRecord(Integer courseid,String grade,Integer instuuid,String termyear,String comment){
		boolean found = false;
		for(int i=0;i<this.records.size();i++)
		{
			if(this.records.get(i).courseid == courseid && this.records.get(i).termyear.equals(termyear)){
				this.records.get(i).grade = grade;
				this.records.get(i).instuuid = instuuid;
				this.records.get(i).comment = comment;
				found = true;
				
			}
		}
		if(!found){
		academicRecord ar= new academicRecord(courseid,grade,instuuid,termyear,comment);
		this.records.add(ar);
		}
	}
	
	public ArrayList<academicRecord> returnAcademicRecord(){
		return this.records;
		
		
	}
	
	
	
	public Set<Integer> listofCoursesTaken(){
		Set s2 = new HashSet();
		for(int i= 0;i<records.size();i++){
			if(records.get(i).grade.equals("A")||records.get(i).grade.equals("B")||records.get(i).grade.equals("C"))
				s2.add(records.get(i).courseid);
		}
		
		
		return s2;
		
		
		
	}
	
	public  int checkIfQualify(int cid,ArrayList<Prereqs> preq){
		String grade = "";
		ArrayList<Integer> preCids = new ArrayList<>();
		
		for(int i=0;i<this.records.size();i++){
			if(this.records.get(i).courseid == cid)
			{
				 grade = this.records.get(i).grade;
				if(grade.equals("A") || grade.equals("B") || grade.equals("C"))
					return 0;
					
			}	
		}	
				
		for(int j=0;j<preq.size();j++){
			if(preq.get(j).uuid == cid)
						preCids = preq.get(j).getPreqs();
			}
		
		if(listofCoursesTaken().containsAll(preCids))
			return 1;
		else return -1;

	}
	
	public static Route fetchStudent = (Request req, Response resp)->{
		
		return "<html><head>hello</head><body></body></html>";
		
		
	};
	
	
	
	
	
}
