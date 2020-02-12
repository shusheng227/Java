package caculate;

public class Goods {

	String gid, gName, mName, wName, gNumber;
	
	public Goods(String gid, String gName, String mName, String wName, String gNumber) {
		
		this.gid = gid;
		this.gName = gName;
		this.mName = mName;
		this.wName = wName;
		this.gNumber = gNumber;
		
	}
	
	// set
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public void setGName(String gName) {
		this.gName = gName;
	}
	
	public void setMName(String mName) {
		this.mName = mName;
	}
	
	public void setWName(String wName) {
		this.wName = wName;
	}
	
	public void setGNumber(String gNumber) {
		this.gNumber = gNumber;
	}
	
	// get
	public String getGid() {
		return gid;
	}
	
	public String getGName() {
		return gName;
	}
	
	public String getMName() {
		return mName;
	}
	
	public String getWName() {
		return wName;
	}
	
	public String getGNumber() {
		return gNumber;
	}
	
}
