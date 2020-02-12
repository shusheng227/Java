package caculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;

import data.Data;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class SaleQuery {

	// 获取出货编号
	public static int getNumber() {
		final String numberQuery = "select max(number) from `sale`";
		 
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
			System.out.println("获取出货编号失败!");
		}
		return 0;
	}
	
	// 插入出货单信息
	public static boolean insertSale(Vector<String> saleInfo) {
		final String saleInset = "insert into `sale` values("
				+ saleInfo.get(0) + ",'" + saleInfo.get(1) + "','" + saleInfo.get(2) + "',"
				+ saleInfo.get(3) + "," + saleInfo.get(4) + "," + saleInfo.get(5) + ",'"
				+ saleInfo.get(6) + "','" + saleInfo.get(7) + "',0)";
			
		try {
			return Data.update(saleInset);
		} catch (SQLException e) {
			System.out.println("插入出货单信息失败!");
			return false;
		}
	}
	
	// 根据销售人员编号和时间获取出货单信息
	public static ObservableList<Sale> getSaleInfo(String uid, String startTime, String endTime) {
		final ObservableList<Sale> data = FXCollections.observableArrayList();
		final String saleInfoQuery = "select * from `sale_show` "
				+ " where time between '" + startTime + "' and '" + endTime +"' "
				+ " and uid='" + uid + "' "
				+ " order by time desc";
			
		try {
			if(Data.query(saleInfoQuery)) {
				ResultSet saleInfoGet = Data.resultSet;
				while(saleInfoGet.next()) {
					int statusGet = saleInfoGet.getInt("status");
					String status = (statusGet==0)?"审核中":((statusGet==1)?"已通过":((statusGet==2)?"已驳回":"缺货中"));
					data.add(new Sale(saleInfoGet.getString("number"), saleInfoGet.getString("gname"), saleInfoGet.getString("mname"), 
							saleInfoGet.getString("gnumber"), saleInfoGet.getString("time"), saleInfoGet.getString("price"), status));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取出货单信息失败!");
		}
			
		return data;
	} 
	
	// 根据货单状态和时间获取出货单信息
	public static ObservableList<Sale> getSaleInfo2(String status, String startTime, String endTime) {
		final ObservableList<Sale> data = FXCollections.observableArrayList();
		int statusInput = (status.equals("审核中")?0:(status.equals("已通过")?1:2));
		final String saleInfoQuery = "select * from `sale_show` "
				+ " where time between '" + startTime + "' and '" + endTime +"' "
				+ " and status='" + statusInput + "' "
				+ " order by time desc";
				
		try {
			if(Data.query(saleInfoQuery)) {
				ResultSet saleInfoGet = Data.resultSet;
				while(saleInfoGet.next()) {
					data.add(new Sale(saleInfoGet.getString("number"), saleInfoGet.getString("gname"), saleInfoGet.getString("mname"), 
							saleInfoGet.getString("gnumber"), saleInfoGet.getString("time"), saleInfoGet.getString("price"), status, saleInfoGet.getString("name")));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取出货单信息失败!");
		}
				
		return data;
	} 
	
	// 根据厂家名获取厂家号
	public static int getMid(String mName) {
		mName = mName.replace("\'", "\\\'");
		final String midQuery = "select distinct mid from `manufacturer` where mname='" + mName + "'";
		
		try {
			if(Data.query(midQuery)) {
				ResultSet midGet = Data.resultSet;
				if(midGet.next()) {
					return midGet.getInt("mid");
				}
			}	
		}
		catch (SQLException e) {
			System.out.println("获取厂家号失败!");
		}
		return 0;
	}
	
	// 根据货单编号获取出货单信息
	public static Vector<String> getSaleInfo2(String sNumber) {
		final Vector<String> data = new Vector<>();
		final String purchaseInfoQuery = "select * from `sale_show` where number=" +  sNumber;
		
		try {
			if(Data.query(purchaseInfoQuery)) {
				ResultSet purchaseInfoGet = Data.resultSet;
				if(purchaseInfoGet.next()) {
					
					data.add(purchaseInfoGet.getString("gname"));
					data.add(purchaseInfoGet.getString("mname"));
					data.add(purchaseInfoGet.getString("gnumber"));
					data.add(purchaseInfoGet.getString("price"));
					data.add(purchaseInfoGet.getString("status"));
					
				}
			}
			else {
				Global.messageDialogShow("数据库连接出错");
			}
		}
		catch (Exception e) {
			System.out.println("获取出货单信息失败!");
		}
			
		return data;
	}
		
	// 修改出货表信息
	public static boolean updateSale(Vector<String> saleInfo, String sNumber) {
		final String purchaseUpdate = "update `sale` "
			+ " set gname='" + saleInfo.get(0) + "', mid="  + saleInfo.get(1)
			+ ", gnumber=" +saleInfo.get(2) + ", price=" + saleInfo.get(3)			
			+ " where number=" + sNumber;
		
		try {
			return Data.update(purchaseUpdate);
		} catch (SQLException e) {
			System.out.println("修改进货单信息失败！");
			return false;
		}
	}
	
	// 查看库存是否足够
	public static boolean isEnough(String gName, int gNumber) {
		final String numberQuery = "select isEnough('" + gName + "'," + gNumber + ")";
		
		try {
			if(Data.query(numberQuery)) {
				ResultSet numberGet = Data.resultSet;
				if(numberGet.next()) {
					return (numberGet.getBoolean(1));
				}
			}
		}
		catch (Exception e) {
			System.out.println("查看库存失败!");
		}
		
		return false;
	}
	
	// 从库存中删货
	public static boolean deleteStorage(int gid, int wid) throws SQLException {
		final String storageDelete = "delete from `storage` where gid=" + gid + " and wid=" + wid;
		
		return Data.update(storageDelete);
	}
	
	// 减少库存
	public static boolean decreaseStorage(int gid, int wid, int gNumber) throws SQLException {
		final String storageDecrease = "update `storage` set number=number-" + gNumber + " where gid=" + gid + " and wid=" + wid;
		
		return Data.update(storageDecrease);
	}
	
	// 向出货表添加备注
	public static boolean increaseSale(String sNumber,String note) throws SQLException {
		String saleIncrease = "update `Sale` set note='" + note + "' where number=" + sNumber;
		
		return Data.update(saleIncrease);	
	}
	
	// 从库存中出货
	public static boolean updateStorage(String sNumber, String gName, int gNumber) throws SQLException {
		final String storageQuery = " select `goods`.gid,`storage`.wid,wname,number from `storage`,`goods`,`warehouse` "
				+ " where `storage`.gid=`goods`.gid and `storage`.wid=`warehouse`.wid and gname='" + gName + "' ";
		String note = "出货信息：\n";
		
		if(Data.query(storageQuery)) {
			ResultSet storageGet = Data.resultSet;
			while(storageGet.next()) {
				int number = storageGet.getInt("number");
				if(gNumber>=number) {
					int gid = storageGet.getInt("gid");
					int wid = storageGet.getInt("wid");
					String wname = storageGet.getString("wname");
					note += wname + "、" + gid + "号货物:" + number +"\n";						
					if(!deleteStorage(gid, wid)) return false;
					gNumber-=number;
				}
				else {
					int gid = storageGet.getInt("gid");
					int wid = storageGet.getInt("wid");
					String wName = storageGet.getString("wname");
					note += wName + "、" + gid + "号货物:" + gNumber +"\n";						
					if(!decreaseStorage(gid, wid, gNumber)) return false;
					gNumber=0;
					break;
				}				
			}
		}
		return (increaseSale(sNumber, note));
	}
	
	// 出货单通过
	public static boolean passSale(String sNumber) throws Exception {
		final String infoQuery = "select gname,gnumber from `sale` where number=" + sNumber;
		final String salePass = "update `sale` set status=1 where number=" + sNumber;
		
		try {
			Data.connection.setAutoCommit(false);
			if(Data.query(infoQuery)) {
				ResultSet infoGet = Data.resultSet;
				if(!infoGet.next()) return false;
				String gName = infoGet.getString("gname");
				int gNumber = infoGet.getInt("gnumber");
				if(isEnough(gName, gNumber)) {
					// 货品充足，出货处理
					if(!Data.update(salePass)) return false;	//更改进货单状态
					if(!updateStorage(sNumber, gName, gNumber)) return false;	//更新库存
					Data.connection.commit();
					Data.connection.setAutoCommit(true);
					return true;
				}
				else {
					// 缺货处理
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("缺货处理");
					alert.setHeaderText(null);
					alert.setContentText("货物不足,是否进行缺货登记?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						if(!AuditQuery.insertStockout(sNumber, gName, gNumber,0)) {	//插入缺货信息
							Global.messageDialogShow("登记失败!");
						}
					}
					Data.connection.commit();
					Data.connection.setAutoCommit(true);
					throw new Exception();
				}
			}		
		}
		catch (SQLException e) {
			try {
				Data.connection.rollback();
			} 
			catch (SQLException e1) {
				Global.messageDialogShow("回滚失败，数据库可能出错！");
				return false;
			}
			System.out.println("出货审核失败");
			return false;
		}
		
		return false;
	}

	// 出货单审核驳回
	public static boolean denySale(String sNumber) {
		final String saleDeny = "update `sale` set status=2 where number=" + sNumber;
		
		try {
			return Data.update(saleDeny);
		} catch (SQLException e) {
			System.out.println("出货单驳回失败!");
			return false;
		}
	}
	
	// 获取出货单备注
	public static String getNote(String sNumber) {
		final String noteQuery = "select note from `sale` where number=" + sNumber;
		
		try {
			if(Data.query(noteQuery)) {
				ResultSet noteGet = Data.resultSet;
				if(noteGet.next()) {					
					return noteGet.getString("note");				
				}
			}
			else {
				Global.messageDialogShow("数据库连接出错");
			}
		}
		catch (Exception e) {
			System.out.println("获取备注失败!");
		}
		return null;
	}
	
	
}
