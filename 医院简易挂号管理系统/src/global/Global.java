package global;

public class Global {
	// JDBC驱动名及数据库URL
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/jcoursetest?"
			+ "useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&"
			+ "useLegacyDatetimeCode=false&serverTimezone=UTC";			//设置为utf-8
	//public static final String DB_URL = "120.78.79.23:3306/hospital";
			
	//数据库用户名和密码
	public static final String USER = "root";
	public static final String PASS = "xxs159357";
	//public static final String USER = "root";
	//public static final String PASS = "hustcs1606";
	
	public static final String loginFxml = "/source/登录界面.fxml";			//登录界面文件名
	public static final String registerFxml = "/source/门诊挂号界面.fxml";	//门诊挂号界面文件名
	public static final String doctorFxml = "/source/医生查询界面.fxml";		//医生查询界面文件名
	public static String userNumber = "000001";	// 系统当前用户编号
}
