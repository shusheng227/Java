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
	
	//初始化,连接数据库
	public static boolean connect(){
		try {
					
			// 加载JDBC驱动
			System.out.println("正在加载驱动...");
			Class.forName(Global.JDBC_DRIVER);
			System.out.println("驱动加载成功!");
			
			// 连接数据库
			System.out.println("正在连接数据库...");
			connection = DriverManager.getConnection(Global.DB_URL, Global.USER, Global.PASS);
			System.out.println("连接成功!");
			return true;
		}
		catch(Exception ex) {
			System.out.println("连接失败!");
			return false;
			//ex.printStackTrace();
		}
		
	}
	
	//查询,string是查询语句
	public static boolean query(String string){
		try {
			if(connection != null) {
				Statement statement = connection.createStatement();
				resultSet = statement.executeQuery(string);
				return true;
			}
			else {
				System.out.println("数据库未连接!");
				return false;
			}	
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("查询失败!");
			return false;		
		}	
	}
	
	//更新,string是更新语句
	public static boolean update(String string) throws SQLException {
		if(connection != null) {
			Statement statement = connection.createStatement();
			int i = statement.executeUpdate(string);
			return (i>0)?true:false;
		}
		else {
			System.out.println("数据库未连接!");
			return false;
		}
	}
		
	//关闭数据库
	public static boolean close(){
		try {
			if(connection==null)	return false;
			else connection.close();
				
			if(resultSet!=null)	resultSet.close();
			System.out.println("数据库已关闭!");
			return true;
		} catch (SQLException e) {
			System.out.println("数据库关闭失败！");
			return false;
			//e.printStackTrace();
		}					
	}
	
}
