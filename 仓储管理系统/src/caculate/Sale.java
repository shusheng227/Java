package caculate;
/**
 * 显示出货单的适配器
 * @author 86131
 *
 */
public class Sale {

	private String sNumber, gName, mName, gNumber;
	private String price, time, status, uid;
	
	public Sale(String sNumber, String gName, String mName, String gNumber, String time, String price, String status) {
		
		this.sNumber = sNumber;
		this.gNumber = gNumber;
		this.price = price;		
		this.time = time;
		this.gName = gName;
		this.mName = mName;
		this.status = status;
		
	}

	public Sale(String sNumber, String gName, String mName, String gNumber, String time, String price, String status, String uid) {
		
		this.sNumber = sNumber;
		this.gNumber = gNumber;
		this.price = price;		
		this.time = time;
		this.gName = gName;
		this.mName = mName;
		this.status = status;
		this.uid = uid;
		
	}
	
	// set
	public void setSNumber(String sNumber) {
		this.sNumber = sNumber;
	}
	
	public void setGNumber(String gNumber) {
		this.gNumber = gNumber;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public void setGName(String gName) {
		this.gName = gName;
	}
	
	public void setMName(String mName) {
		this.mName = mName;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	// get
	public String getSNumber() {
		return sNumber;
	}
		
	public String getGNumber() {
		return gNumber;
	}
		
	public String getPrice() {
		return price;
	}
	
	public String getTime() {
		return time;
	}
		
	public String getGName() {
		return gName;
	}
		
	public String getMName() {
		return mName;
	}
		
	public String getStatus() {
		return status;
	}	
	
	public String getUid() {
		return uid;
	}
	
}
