package coursemanagement;

public abstract class User {
	public int uuid;
	public String name;
	public String address;
	public String phoneNo;
	
	public User(int uuid,String name,String address,String phoneNo){
		this.uuid = uuid;
		this.name= name;
		this.address = address;
		this.phoneNo = phoneNo;
		}
}
