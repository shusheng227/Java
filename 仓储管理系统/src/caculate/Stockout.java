package caculate;

public class Stockout {

	String sNumber, gName, gNumber, status;
	
	public Stockout(String sNumber, String gName, String gNumber, String status) {
		
		this.sNumber = sNumber;
		this.gName = gName;
		this.gNumber = gNumber;
		this.status = status;
		
	}
	
	// get
	public String getSNumber() {
		return sNumber;
	}
	
	public String getGName() {
		return gName;
	}
	
	public String getGNumber() {
		return gNumber;
	}
	
	public String getStatus() {
		return status;
	}
	
	// set
	public void setSNumber(String sNumber) {
		this.sNumber = sNumber;
	}
	
	public void setGName(String gName) {
		this.gName = gName;
	}
	
	public void setGNumber(String gNumber) {
		this.gNumber = gNumber;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
