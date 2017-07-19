package coursemanagement;

import java.util.ArrayList;
import java.util.List;

public class Prereqs {
	public ArrayList<Integer>courseIds = new ArrayList();
	public int uuid;
	
	public Prereqs(int uuid)
	{
		this.uuid = uuid;
	}
	
	public void addPreReq(ArrayList<Integer> ids)
	{
		this.courseIds = ids;
	}
	
	public ArrayList<Integer> getPreqs()
	{
		return courseIds;
	}

}
