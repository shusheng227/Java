/**
  *  病人列表表项定义
 * @author 86131
  *  病人列表的ObservableList类型的适配器
 */

package caculate;

public class PatientColumn {
	private String registerNumber;  
    private String patientName;  
    private String registerDate;
    private String categoriesType;
    
    public PatientColumn(String registerNumber, String patientName,String registerDate, String categoriesType) {
		this.registerNumber = registerNumber;
		this.patientName = patientName;
		this.registerDate = registerDate;
		this.categoriesType = categoriesType;
	}
    
    // get
    public String getRegisterNumber() {
    	return registerNumber;
    }
    
    public String getPatientName() {
    	return patientName;
    }
    
    public String getRegisterDate() {
    	return registerDate;
    }
    
    public String getCategoriesType() {
    	return categoriesType;
    }
    
    
    // set
    public void setRegisterNumber(String registerNumber) {
    	this.registerNumber = registerNumber;
    }
    
    public void setPatientName(String patientName) {
    	this.registerNumber=patientName;
    }
    
    public void setRegisterDate(String registerDate) {
    	this.registerNumber=registerDate;
    }
    
    public void setCategoriesType(String categoriesType) {
    	this.registerNumber=categoriesType;
    }
}
