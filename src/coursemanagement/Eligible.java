package coursemanagement;

import java.util.ArrayList;
import java.util.List;

public class Eligible {
	public int uuid;
	private List<Integer> eligibleCourse = new ArrayList();
	
	public Eligible(int uuid){
		this.uuid = uuid;
	}
	public void addEligiblity(List<Integer>courses){
		this.eligibleCourse = courses;
	}
	
	public void printEligibility(int uuid){
		for(Integer d:eligibleCourse)
		{
			System.out.print(d);
		}
	
	}

}
