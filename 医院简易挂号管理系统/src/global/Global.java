package global;

public class Global {
	// JDBC�����������ݿ�URL
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/jcoursetest?"
			+ "useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&"
			+ "useLegacyDatetimeCode=false&serverTimezone=UTC";			//����Ϊutf-8
	//public static final String DB_URL = "120.78.79.23:3306/hospital";
			
	//���ݿ��û���������
	public static final String USER = "root";
	public static final String PASS = "xxs159357";
	//public static final String USER = "root";
	//public static final String PASS = "hustcs1606";
	
	public static final String loginFxml = "/source/��¼����.fxml";			//��¼�����ļ���
	public static final String registerFxml = "/source/����ҺŽ���.fxml";	//����ҺŽ����ļ���
	public static final String doctorFxml = "/source/ҽ����ѯ����.fxml";		//ҽ����ѯ�����ļ���
	public static String userNumber = "000001";	// ϵͳ��ǰ�û����
}
