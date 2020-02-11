package caculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import data.Data;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PurchaseQuery {
	
	// 根据字首获取货品列表
	public static ObservableList<String> getGNameList(String initial) {	
		// 规范字符串
		if(initial == null) initial = "";
		else {
			initial = initial.replace("\'", "\\\'");
		}
		final ObservableList<String> data = FXCollections.observableArrayList();
		final String gNameQuery = "select distinct gname from `goods` where gname like '" + initial + "%'";
		 

		try {
			if(Data.query(gNameQuery)) {
				ResultSet gNameGet = Data.resultSet;
				while(gNameGet.next()) {
					data.add(gNameGet.getString("gname"));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取货品列表失败!");
		}
		return data;
	}
	
	// 根据货品名和字首获取厂家列表
	public static ObservableList<String> getMNameList(String gName,String initial) {
		final ObservableList<String> data = FXCollections.observableArrayList();
		final String mNameQuery;
		// 规范字符串
		if(initial == null) initial = "";
		else {
			initial = initial.replace("\'", "\\\'");
		}
		// 确定查询语句
		if(gName==null||gName.isEmpty()) {
			mNameQuery = "select distinct mname from `manufacturer` where mname like '" + initial + "%'";
		}
		else {
			gName = gName.replace("\'", "\\\'");
			mNameQuery = "select distinct mname from `manufacturer`,`goods` "
					+ "where `manufacturer`.mid=`goods`.mid and "
					+ "mname like '" + initial + "%' and "
					+ "gname='" + gName + "'";
		}
		
		try {
			if(Data.query(mNameQuery)) {
				ResultSet mNameGet = Data.resultSet;
				while(mNameGet.next()) {
					data.add(mNameGet.getString("mname"));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取厂家列表失败!");
		}
		return data;
	}
	
	// 根据字首获取仓库列表
	public static ObservableList<String> getWNameList(String initial) {	
		// 规范字符串
		if(initial == null) initial = "";
		else {
			initial = initial.replace("\'", "\\\'");
		}
		final ObservableList<String> data = FXCollections.observableArrayList();
		final String wNameQuery = "select distinct wname from `warehouse` where wname like '" + initial + "%'";
			

		try {
			if(Data.query(wNameQuery)) {
				ResultSet wNameGet = Data.resultSet;
				while(wNameGet.next()) {
					data.add(wNameGet.getString("wname"));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取仓库列表失败!");
		}
		return data;
	}
		
	// 根据货品名和生产厂家获取货品id
	public static int getGid(String gName,String mName) throws Exception{
		gName = gName.replace("\'", "\\\'");
		mName = mName.replace("\'", "\\\'");
		final String gidQuery = "select distinct gid from `manufacturer`,`goods` "
				+ "where `manufacturer`.mid=`goods`.mid "
				+ "and gName='" + gName + "' "
				+ "and mName='" + mName + "'";
		
		try {
			if(Data.query(gidQuery)) {
				ResultSet gidGet = Data.resultSet;
				if(gidGet.next()) {
					return gidGet.getInt("gid");
				}
				else {
					throw new Exception();
				}
			}	
		}
		catch (SQLException e) {
			System.out.println("获取货品号失败!");
		}
		return 0;
	}
	
	// 根据仓库名获取仓库号
	public static int getWid(String wName) {
		wName = wName.replace("\'", "\\\'");
		final String widQuery = "select distinct wid from `warehouse` where wname='" + wName + "'";
		
		try {
			if(Data.query(widQuery)) {
				ResultSet widGet = Data.resultSet;
				if(widGet.next()) {
					return widGet.getInt("wid");
				}
			}	
		}
		catch (SQLException e) {
			System.out.println("获取仓库号失败!");
		}
		return 0;
	}
	
	// 获取进货编号
	public static int getNumber() {
		final String numberQuery = "select max(number) from `purchase`";
		
		try {
			if(Data.query(numberQuery)) {
				ResultSet numberGet = Data.resultSet;
				if(numberGet.next()) {
					return numberGet.getInt(1)+1;
				}
				else {
					return 1;
				}
			}
			
		}
		catch (SQLException e) {
			System.out.println("获取进货编号失败!");
		}
		return 0;
	}
	
	// 插入进货单信息
	public static boolean insertPurchase(Vector<String> purchaseInfo) {
		final String purchaseInset = "insert into `purchase` values("
				+ purchaseInfo.get(0) + "," + purchaseInfo.get(1) + "," + purchaseInfo.get(2) + ",'"
				+ purchaseInfo.get(3) + "'," + purchaseInfo.get(4) + "," + purchaseInfo.get(5) + ",'"
				+ purchaseInfo.get(6) + "','" + purchaseInfo.get(7) + "',0)";
		
		try {
			return Data.update(purchaseInset);
		} catch (SQLException e) {
			System.out.println("插入进货单信息失败！");
			return false;
		}
	}
	
	// 根据采购人员编号和时间获取进货单信息
	public static ObservableList<Purchase> getPurchaseInfo(String uid, String startTime, String endTime) {
		final ObservableList<Purchase> data = FXCollections.observableArrayList();
		final String purchaseInfoQuery = "select * from purchase_show "
				+ " where time between '" + startTime + "' and '" + endTime +"'  and uid='" + uid + "' "
				+ " order by time desc";
		
		try {
			if(Data.query(purchaseInfoQuery)) {
				ResultSet purchaseInfoGet = Data.resultSet;
				while(purchaseInfoGet.next()) {
					int statusGet = purchaseInfoGet.getInt("status");
					String status = (statusGet==0)?"审核中":((statusGet==1)?"已通过":"已驳回");
					data.add(new Purchase(purchaseInfoGet.getString("number"), purchaseInfoGet.getString("gname"), purchaseInfoGet.getString("mname"), 
							purchaseInfoGet.getString("gnumber"), purchaseInfoGet.getString("time"), purchaseInfoGet.getString("price"), purchaseInfoGet.getString("wName"), status));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取进货单信息失败!");
		}
		
		return data;
	}
	
	// 根据货单编号获取进货单信息
	public static Vector<String> getPurchaseInfo2(String pNumber) {
		final Vector<String> data = new Vector<>();
		final String purchaseInfoQuery = "select * from purchase_show  where number=" +  pNumber;
		
		try {
			if(Data.query(purchaseInfoQuery)) {
				ResultSet purchaseInfoGet = Data.resultSet;
				if(purchaseInfoGet.next()) {
					
					data.add(purchaseInfoGet.getString("gname"));
					data.add(purchaseInfoGet.getString("mname"));
					data.add(purchaseInfoGet.getString("gnumber"));
					data.add(purchaseInfoGet.getString("price"));
					data.add(purchaseInfoGet.getString("wname"));
					data.add(purchaseInfoGet.getString("status"));
					
				}
			}
			else {
				Global.messageDialogShow("数据库连接出错");
			}
		}
		catch (Exception e) {
			System.out.println("获取进货单信息失败!");
		}
		
		return data;
	}
	
	// 根据进货单状态和时间获取进货单信息
	public static ObservableList<Purchase> getPurchaseInfo3(String status, String startTime, String endTime) {
		final ObservableList<Purchase> data = FXCollections.observableArrayList();
		int statusInput = (status.equals("审核中")?0:(status.equals("已通过")?1:2));
		final String purchaseInfoQuery = "select *  from purchase_show "
				+ " where time between '" + startTime + "' and '" + endTime +"' "
				+ " and status=" + statusInput + " "
				+ " order by time desc";
		
		try {
			if(Data.query(purchaseInfoQuery)) {
				ResultSet purchaseInfoGet = Data.resultSet;
				while(purchaseInfoGet.next()) {
					data.add(new Purchase(purchaseInfoGet.getString("number"), purchaseInfoGet.getString("gname"), purchaseInfoGet.getString("mname"), purchaseInfoGet.getString("gnumber"), 
							purchaseInfoGet.getString("time"), purchaseInfoGet.getString("price"), purchaseInfoGet.getString("wName"), status ,purchaseInfoGet.getString("name")));
				}
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取进货单信息失败!");
		}
		
		return data;
	}
	
	// 修改进货表信息
	public static boolean updatePurchase(Vector<String> purchaseInfo, String pNumber){
		final String purchaseUpdate = "update `purchase` "
				+ " set gid=" + purchaseInfo.get(0) + ", gnumber="  + purchaseInfo.get(1)
				+ ", price=" + purchaseInfo.get(2) + ", wid=" + purchaseInfo.get(3)			
				+ " where number=" + pNumber;
		
		try {
			return Data.update(purchaseUpdate);
		} catch (SQLException e) {
			System.out.println("修改进货表信息失败!");
			return false;
		}
	}
	
	// 进货单审核通过
	public static boolean passPurchase(String pNumber) {
		final String purchasePass = "update `purchase` set status=1 where number=" + pNumber;
		final String infoQuery = "select gid, wid, gnumber from `purchase` where number=" + pNumber;
		
		try {
			Data.connection.setAutoCommit(false);
			if(!Data.update(purchasePass)) return false; //更改进货单状态
			if(!Data.query(infoQuery)) return false;	//查询进货货品、进货数量
			ResultSet InfoGet = Data.resultSet;
			if(InfoGet.next()) {
				String gid = InfoGet.getString("gid");
				String wid = InfoGet.getString("wid");
				String gnumber = InfoGet.getString("gnumber");
				if(!updateStorage(gid, wid, gnumber)) {	//更新库存
					return false;
				}
			}
			Data.connection.commit();
			Data.connection.setAutoCommit(true);						
		} catch (SQLException e) {
			try {
				Data.connection.rollback();
			} 
			catch (SQLException e1) {
				Global.messageDialogShow("回滚失败，数据库可能出错！");
			}
			System.out.println("更新进货单失败!");
			return false;
		}	
		
		return true;
	}
	
	// 进货单审核驳回
	public static boolean denyPurchase(String pNumber) {
		final String purchaseDeny = "update `purchase` set status=2 where number=" + pNumber;
		
		try {
			return Data.update(purchaseDeny);
		} catch (SQLException e) {
			System.out.println("进货单驳回失败!");
			return false;
		}
	}
	
	// 存储货品
	public static boolean updateStorage(String gid, String wid, String number) throws SQLException {
		final String storageQuery = "select number from `storage` "
				+ " where gid=" + gid + " and wid=" + wid;
		final String storageInsert = "insert into `storage` values(" + gid + "," + wid + "," + number +")";
		final String storageUpdate = "update `storage` set number=number+" + number 
				+ " where gid=" + gid +" and wid=" + wid;

		if(Data.query(storageQuery)) {
			ResultSet storageGet = Data.resultSet;
			if(storageGet.next()) {
				// 仓库存在该货物，增加数量
				return Data.update(storageUpdate);
			}
			else {
				// 仓库不存在该货物，插入
				return Data.update(storageInsert);
			}
		}
		return false;
	}
	
}
