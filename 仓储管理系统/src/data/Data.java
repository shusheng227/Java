package data;

import global.Global;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {
	public static Connection connection;
	public static ResultSet resultSet;
	
	//��ʼ��,�������ݿ�
	public static boolean connect(){
		try {
					
			// ����JDBC����
			System.out.println("���ڼ�������...");
			Class.forName(Global.JDBC_DRIVER);
			System.out.println("�������سɹ�!");
			
			// �������ݿ�
			System.out.println("�����������ݿ�...");
			connection = DriverManager.getConnection(Global.DB_URL, Global.USER, Global.PASS);
			System.out.println("���ӳɹ�!");
			return true;
		}
		catch(Exception ex) {
			System.out.println("����ʧ��!");
			return false;
			//ex.printStackTrace();
		}
		
	}
	
	//��ѯ,string�ǲ�ѯ���
	public static boolean query(String string){
		try {
			if(connection != null) {
				Statement statement = connection.createStatement();
				resultSet = statement.executeQuery(string);
				return true;
			}
			else {
				System.out.println("���ݿ�δ����!");
				return false;
			}	
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("��ѯʧ��!");
			return false;		
		}	
	}
	
	//����,string�Ǹ������
	public static boolean update(String string) throws SQLException {
		if(connection != null) {
			Statement statement = connection.createStatement();
			int i = statement.executeUpdate(string);
			return (i>0)?true:false;
		}
		else {
			System.out.println("���ݿ�δ����!");
			return false;
		}
	}
		
	//�ر����ݿ�
	public static boolean close(){
		try {
			if(connection==null)	return false;
			else connection.close();
				
			if(resultSet!=null)	resultSet.close();
			System.out.println("���ݿ��ѹر�!");
			return true;
		} catch (SQLException e) {
			System.out.println("���ݿ�ر�ʧ�ܣ�");
			return false;
			//e.printStackTrace();
		}					
	}
	
}
