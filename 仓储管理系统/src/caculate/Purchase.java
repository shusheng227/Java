package caculate;
/**
 * 显示进货单的适配器
 * @author 86131
 *
 */
public class Purchase {
	
	private String pNumber, gNumber, price, wName;
	private String time, gName, mName, status, uid;
	
	public Purchase(String pNumber, String gName, String mName, String gNumber, String time, String price, String wName, String status) {

		this.pNumber = pNumber;
		this.gNumber = gNumber;
		this.price = price;		
		this.time = time;
		this.gName = gName;
		this.mName = mName;
		this.wName = wName;
		this.status = status;
		
	}
	
	public Purchase(String pNumber, String gName, String mName, String gNumber, String time, String price, String wName, String status, String uid) {

		this.pNumber = pNumber;
		this.gNumber = gNumber;
		this.price = price;		
		this.time = time;
		this.gName = gName;
		this.mName = mName;
		this.wName = wName;
		this.status = status;
		this.uid = uid;
		
	}
	
	// set
	public void setPNumber(String pNumber) {
		this.pNumber = pNumber;
	}
	
	public void setGNumber(String gNumber) {
		this.gNumber = gNumber;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public void setWName(String wName) {
		this.wName = wName;
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
	public String getPNumber() {
		return pNumber;
	}
		
	public String getGNumber() {
		return gNumber;
	}
		
	public String getPrice() {
		return price;
	}
	
	public String getWName() {
		return wName;
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
