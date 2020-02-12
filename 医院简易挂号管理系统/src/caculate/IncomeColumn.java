package caculate;

public class IncomeColumn {
	private String departmentName;
	private String doctorNumber;
	private String doctorName;
	private String categoriesType;
	private String registerCount;
	private String income;
	
	public IncomeColumn(String departmentName, String doctorNumber, String doctorName,
			String categoriesType, String registerCount, String income) {
		
		this.departmentName = departmentName;
		this.doctorNumber = doctorNumber;
		this.doctorName = doctorName;
		this.categoriesType = categoriesType;
		this.registerCount = registerCount;
		this.income = income;
		
	}
	
	// get
	public String getDepartmentName() {
		return departmentName;
	}
	
	public String getDoctorNumber() {
		return doctorNumber;
	}
	
	public String getDoctorName() {
		return doctorName;
	}
	
	public String getCategoriesType() {
		return categoriesType;
	}
	
	public String getRegisterCount() {
		return registerCount;
	}
	
	public String getIncome() {
		return income;
	}
	
	// set
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public void setDoctorNumber(String doctorNumber) {
		this.doctorNumber = doctorNumber;
	}
	
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
	public void setCategoriesType(String categoriesType) {
		this.categoriesType = categoriesType;
	}
	
	public void setRegisterCount(String registerCount) {
		this.registerCount = registerCount;
	}
	
	public void setIncome(String income) {
		this.income = income;
	}
}
