package main.coursemanagement;

import java.util.ArrayList;

public class academicRecord {
	public Integer instuuid;
	public Integer courseid;
	public String grade;
	public String comment;
	public String termyear;
	public String studentuuid;

	public academicRecord(Integer studentuuid, Integer courseid, String grade, Integer instuuid, String termyear) {
		this.courseid = courseid;
		this.grade = grade;
		this.instuuid = instuuid;
		this.termyear = termyear;
	}

	public academicRecord(Integer studentuuid, Integer courseid, String grade, Integer instuuid, String termyear,
			String comment) {
		this.courseid = courseid;
		this.grade = grade;
		this.instuuid = instuuid;
		this.comment = comment;
		this.termyear = termyear;
	}

}
