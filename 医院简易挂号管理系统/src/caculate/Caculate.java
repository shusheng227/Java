package caculate;

import java.sql.ResultSet;

import data.Data;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Caculate {
	
	// 获取病人列表
	public static ObservableList<PatientColumn> getPatientList() {
		final ObservableList<PatientColumn> data = FXCollections.observableArrayList();
		final String registerInfoQuery = "select t_ghxx.ghbh,t_brxx.brmc,rqsj,sfzj"
    			+ " from t_ghxx,t_brxx,t_hzxx"
    			+ " where t_brxx.brbh=t_ghxx.brbh and t_ghxx.hzbh=t_hzxx.hzbh"
    			+ " and t_ghxx.ysbh =" + Global.userNumber
    			+ " and thbz = false"
    			+ " order by ghbh";
		
		try {
			if(Data.query(registerInfoQuery) == true) {
				ResultSet registerInfoGet = Data.resultSet;
				while(registerInfoGet.next()) {
					
					boolean sfzj = registerInfoGet.getBoolean("sfzj");
					String categoriesType = (sfzj?"普通号":"专家号");
					data.addAll(
							new PatientColumn(registerInfoGet.getString(1),registerInfoGet.getString(2),
									registerInfoGet.getString(3),categoriesType)
					);					
				}	
			}
			else {
				System.out.println("病人列表获取失败!");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("病人列表获取失败：" + e.getMessage());
		}
		return data;
	}

	// 获取收入列表
	public static ObservableList<IncomeColumn> getIncomeList(String startTime, String overTime) {
		final ObservableList<IncomeColumn> data = FXCollections.observableArrayList();
		final String doctorInfoQuery = "select ksmc,ysbh,ysmc,sfzj"
				+ " from t_ksys,t_ksxx"
				+ " where t_ksys.ksbh=t_ksxx.ksbh"
				+ " order by ysbh";
						
		try {
			if(Data.query(doctorInfoQuery) == true) {
				ResultSet doctorInfoGet = Data.resultSet;
				while(doctorInfoGet.next()) {
					String departmentName = doctorInfoGet.getString("ksmc");
					String doctorNumber = doctorInfoGet.getString("ysbh");					
					String doctorName = doctorInfoGet.getString("ysmc");
					boolean sfzj = doctorInfoGet.getBoolean("sfzj");
					
					// 查询普通号数量及收入
					String registerCountQuery1 = "select count(*),sum(t_hzxx.ghfy)"
							+ " from t_ghxx,t_hzxx"
							+ " where t_ghxx.hzbh=t_hzxx.hzbh"
							+ " and sfzj=false and thbz=false"
							+ " and rqsj between '" + startTime + "' and '" + overTime + "'"
							+ " and ysbh=" + doctorNumber;
					// 查询专家数量及收入
					String registerCountQuery2 = "select count(*),sum(t_hzxx.ghfy)"
							+ " from t_ghxx,t_hzxx"
							+ " where t_ghxx.hzbh=t_hzxx.hzbh"
							+ " and sfzj=true and thbz=false"
							+ " and rqsj between '" + startTime + "' and '" + overTime + "'"
							+ " and ysbh=" + doctorNumber;
					
					if(Data.query(registerCountQuery1) == true) {
						ResultSet registerCountGet = Data.resultSet;
						if(registerCountGet.next()) {
							String registerCount = registerCountGet.getString(1);
							String income = "0";
							if(!registerCount.equals("0")) {
								income = registerCountGet.getString(2);
							}
							data.addAll(
								new IncomeColumn(departmentName,doctorNumber,doctorName,"普通号",registerCount,income)
							);
						}
					}
					
					if(sfzj == true) {	// 该医生是专家
						if(Data.query(registerCountQuery2) == true) {
							ResultSet registerCountGet = Data.resultSet;
							if(registerCountGet.next()) {
								String registerCount = registerCountGet.getString(1);
								String income = "0";
								if(!registerCount.equals("0")) {
									income = registerCountGet.getString(2);
								}
								data.addAll(
									new IncomeColumn(departmentName,doctorNumber,doctorName,"专家号",registerCount,income)
								);								
							}
						}
					}
					
				}	
			}
			else {
				System.out.println("收入列表获取失败!");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("收入列表获取失败：" + e.getMessage());
		}
		return data;
	}

	// 根据拼音字首获取科室信息列表
	public static ObservableList<String> getDepartmentName(String initial) {
		ObservableList<String> data = FXCollections.observableArrayList();		
		if(initial == null) initial = "";
		final String departmentNameQuery = "select ksmc from t_ksxx where pyzs like '" + initial + "%'";
		
		try {
			if(Data.query(departmentNameQuery) == true) {
				ResultSet departmentNameGet = Data.resultSet;
				while(departmentNameGet.next()) {
					String departmentName = departmentNameGet.getString("ksmc");
					data.add(departmentName);
				}
			}	
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取科室名称失败!");
		}
		return data;
	}
	
	// 根据科室名称及医生名称拼音字首获取科室医生及其编号
	public static ObservableList<String> getDoctorName(String departmentName, String initial) {
		ObservableList<String> data = FXCollections.observableArrayList();
		if(initial == null) initial = "";
		final String doctorNameQuery = "select ysmc"
				+ " from t_ksys, t_ksxx"
				+ " where t_ksys.ksbh=t_ksxx.ksbh"
				+ " and t_ksxx.ksmc='" + departmentName + "'"
				+ " and t_ksys.pyzs like '" + initial + "%'";
		
		try {
			if(Data.query(doctorNameQuery) == true) {
				ResultSet doctorNameGet = Data.resultSet;
				while(doctorNameGet.next()) {
					String doctorName = doctorNameGet.getString("ysmc");
					data.add(doctorName);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取医生名称失败!");
		}
		return data;
	}

	// 根据医生名称判断是否为专家并获取号种类别列表
	public static ObservableList<String> getCategoriesType(String doctorName) {
		ObservableList<String> data = FXCollections.observableArrayList();
		final String sfzjQuery = "select sfzj from t_ksys where t_ksys.ysmc='" + doctorName + "'";
		
		try {
			if(Data.query(sfzjQuery) == true) {
				ResultSet sfzjGet = Data.resultSet;
				if(sfzjGet.next()) {
					boolean sfzj=sfzjGet.getBoolean("sfzj");
					if(sfzj) {
						data.addAll("普通号","专家号");
					}
					else {
						data.addAll("普通号");
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取是否专家失败!");			
		}
		return data;
	}

	// 根据科室名称及号种名称拼音字首获取号种名称
	public static ObservableList<String> getCategoriesName(String departmentName, boolean categoriesType, String initial) {
		ObservableList<String> data = FXCollections.observableArrayList();
		if(initial == null) initial = "";
		final String categoriesNameQuery = "select hzmc"
				+ " from t_hzxx,t_ksxx"
				+ " where t_hzxx.ksbh=t_ksxx.ksbh"
				+ " and ksmc='" + departmentName + "'"
				+ " and sfzj=" + categoriesType
				+ " and t_hzxx.pyzs like '" + initial + "%'";
		
		try {
			if(Data.query(categoriesNameQuery) == true) {
				ResultSet categoriesNameGet = Data.resultSet;
				while(categoriesNameGet.next()) {
					data.addAll(categoriesNameGet.getString("hzmc"));
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取科室名称失败!");			
		}
		return data;
	}

	// 根据医生名称获取医生编号
	public static String getDoctorNumber(String doctorName) {
		final String doctorNumberQuery = "select ysbh from t_ksys where t_ksys.ysmc='" + doctorName + "'";
		
		try {
			if(Data.query(doctorNumberQuery) == true) {
				ResultSet doctorNumberGet = Data.resultSet;
				if(doctorNumberGet.next()) {
					String doctorNumber=doctorNumberGet.getString("ysbh");
					return doctorNumber;
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取是否专家失败!");			
		}
		return null;
	}
	
	// 根据号种名称获取号种编号
	public static String getCategoriesNumber(String categoriesName, boolean categoriesType) {
		final String registerFeeQuery = "select hzbh from t_hzxx"
				+ " where sfzj=" + categoriesType
				+ " and hzmc='" + categoriesName + "'";
			
		try {
			if(Data.query(registerFeeQuery) == true) {
				ResultSet registerFeeGet = Data.resultSet;
				if(registerFeeGet.next()) {
					return registerFeeGet.getString("hzbh");
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取挂号费失败!");			
		}
		return null;
	}
	
	// 根据号种编号获取挂号费
	public static double getRegisterFee(String categoriesNumber) {
		final String registerFeeQuery = "select ghfy from t_hzxx where hzbh=" + categoriesNumber;
		
		try {
			if(Data.query(registerFeeQuery) == true) {
				ResultSet registerFeeGet = Data.resultSet;
				if(registerFeeGet.next()) {
					return registerFeeGet.getDouble("ghfy");
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取挂号费失败!");			
		}
		return 0;
	}
	
	// 根据号种编号获取限定挂号人数
	public static int getMAXRegisterNumber(String categoriesNumber) {
		final String registerFeeQuery = "select ghrs from t_hzxx where hzbh=" + categoriesNumber;
			
		try {
			if(Data.query(registerFeeQuery) == true) {
				ResultSet registerFeeGet = Data.resultSet;
				if(registerFeeGet.next()) {
					return registerFeeGet.getInt("ghrs");
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取限定挂号人数失败!");			
		}
		return 0;
	}	

	// 根据号种编号获取已挂号人次
	public static int getRegisterNumber(String categoriesNumber) {
		final String registerNumberQuery = "select ghrc from t_ghxx where hzbh=" + categoriesNumber;
		
		try {
			if(Data.query(registerNumberQuery) == true) {
				ResultSet registerNumberGet = Data.resultSet;
				if(registerNumberGet.next()) {
					return registerNumberGet.getInt("ghrc");
				}
				else {
					return 0;
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取已挂号人次失败!");			
		}
		return Integer.MAX_VALUE;
	}
	
	// 获取挂号编号
	public static String getRegisterNumber() {
		final String registerNumberQuery = "select max(ghbh) from t_ghxx";
		
		try {
			if(Data.query(registerNumberQuery) == true) {
				ResultSet registerNumberGet = Data.resultSet;
				if(registerNumberGet.next()) {
					Integer temp = registerNumberGet.getInt(1) + 1;
					return temp.toString();
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取挂号编号失败!");			
		}
		return null;
	}
	
	// 更新病人余额
	public static boolean updatePatientBalance(Double balance) {
		final String balanceUpdate = "update t_brxx set ycje=" + balance 
				+ " where brbh=" + Global.userNumber;
		
		return Data.update(balanceUpdate);
	}
	
	// 更新当天已挂号人次
	public static boolean updateRegisterNumber(String categoriesNumber, String startTime, String overTime) {
		final String registerNumberUpdate = "update t_ghxx set ghrc=ghrc+1"
				+ " where hzbh='" + categoriesNumber + "'"
				+ " and rqsj between '" + startTime + "' and '" + overTime + "'";
		
		return Data.update(registerNumberUpdate);
	}
	
	// 插入挂号信息
	public static boolean insertRegisterInfo(ObservableList<String> registerInfo) {
		final String registerInfoInsert = "insert into t_ghxx values('"
				+ registerInfo.get(0) + "','" + registerInfo.get(1) + "','" + registerInfo.get(2) + "','"
				+ registerInfo.get(3) + "','" + registerInfo.get(4) + "',false,'"
				+ registerInfo.get(5) + "','" + registerInfo.get(6) + "')";
		
		return Data.update(registerInfoInsert);
	}
}
