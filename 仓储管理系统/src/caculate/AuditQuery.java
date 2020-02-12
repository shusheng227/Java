package caculate;

import java.sql.ResultSet;
import java.sql.SQLException;

import data.Data;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuditQuery {

	// 查询货物列表
	public static ObservableList<Goods> getGoodsList(String gName,String mName,String wName) {
		final ObservableList<Goods> data = FXCollections.observableArrayList();
		String goodsQuery = " select * from `goods_show` where number>0 ";
	
		if((gName!=null)&&(!gName.isEmpty())) {
			goodsQuery+=" and gname='" + gName + "' ";
		}
		if((mName!=null)&&(!mName.isEmpty())) {
			goodsQuery+=" and mname='" + mName + "' ";
		}
		if((wName!=null)&&(!wName.isEmpty())) {
			goodsQuery+=" and wname='" + wName + "' ";
		}
		
		try {
			if(Data.query(goodsQuery)) {
				ResultSet goodsGet = Data.resultSet;
				while(goodsGet.next()) {
					data.add( new Goods(goodsGet.getString(1), goodsGet.getString(2), 
					goodsGet.getString(3), goodsGet.getString(4), goodsGet.getString(5)) );
				}
			}	
		}
		catch (Exception e) {
			System.out.println("获取货品列表失败!");
		}
		return data;
		
	}
	
	// 插入缺货信息
	public static boolean insertStockout(String sNumber, String gName, int gNumber, int status) throws SQLException {
		String stockoutInsert = "insert into `stockout` values(" 
					+ sNumber + ", '" + gName + "', " + gNumber + "," + status + ")";
		
		return Data.update(stockoutInsert);
	}
	
	// 根据货品名查看缺货表中相应货物能否处理
	public static boolean checkStockout (String gName) {
		final String numberQuery = "select number from `storage`,`goods` "
				+ " where `storage`.gid=`goods`.gid and gname='" + gName + "' "
				+ " group by gname";
		final String stockoutQuery = "select number,gnumber from `stockout` "
				+ " where gname='" + gName + "'";
		
		try {
			if(Data.query(numberQuery)) {
				ResultSet numberGet = Data.resultSet;
				if(!numberGet.next()) return false;
				int number = numberGet.getInt(1);
				if(Data.query(stockoutQuery)) {
					ResultSet stockoutGet = Data.resultSet;
					while(stockoutGet.next()) {
						int gNumber = stockoutGet.getInt("gnumber");
						String sNumber = stockoutGet.getString("number");
						if(gNumber <= number) {
							// 可处理
							final String stockoutUpdate = "update `stockout` set status=1 where number=" + sNumber;
							if(!Data.update(stockoutUpdate)) return false;
						}
					}
					return true;
				}
			}
		}
		catch (Exception e) {
			System.out.println("处理缺货表失败!");
		}
		
		return false;
	}	
	
	// 获取缺货表
	public static ObservableList<Stockout> getStockoutInfo(String sNumber, String gName, String status) {
		final ObservableList<Stockout> data = FXCollections.observableArrayList();
		String stockoutQuery = "select * from `stockout` where gnumber>0 ";
		if((sNumber!=null)&&(!sNumber.isEmpty())) {
			stockoutQuery += " and number=" + sNumber + " ";
		}
		if((gName!=null)&&(!gName.isEmpty())) {
			stockoutQuery += " and gname='" + gName + "' ";
		}
		if((status!=null)&&(!status.isEmpty())) {
			int statusInput = (status.equals("缺货中")?0:1);
			stockoutQuery += " and status=" + statusInput + " ";
		}
		
		try {
			if(Data.query(stockoutQuery)) {
				ResultSet stockoutGet = Data.resultSet;
				while(stockoutGet.next()) {
					String statusGet = ((stockoutGet.getInt("status")==0)?"缺货中":"可处理");
					String sNumberGet = stockoutGet.getString("number");
					String gNameGet =  stockoutGet.getString("gname");
					String gNumberGet = stockoutGet.getString("gnumber");
					data.add( new Stockout(sNumberGet, gNameGet, gNumberGet, statusGet));
				}
			}
		}
		catch (Exception e) {
			System.out.println("获取缺货表失败!");
		}
		
		return data;
	}
	
	
	// 缺货处理
	public static boolean disposeStockout(String sNumber, String gName, int gNumber){
		final String stockoutDelete = "delete from `stockout` where number=" + sNumber;
		
		if(!SaleQuery.isEnough(gName, gNumber)) {
			Global.messageDialogShow("缺货中，无法受理！");
			return false;
		}		
		
		try {
			Data.connection.setAutoCommit(false);
			if(!Data.update(stockoutDelete)) {	//删除缺货信息
				Global.messageDialogShow("处理失败，请稍后重试！");
				return false;
			}
			if(!SaleQuery.updateStorage(sNumber, gName, gNumber)) {	//出货，更新库存
				insertStockout(sNumber, gName, gNumber, 1);
				Global.messageDialogShow("处理失败，请稍后重试！");
				return false;
			}
			Data.connection.commit();
			Data.connection.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				Data.connection.rollback();
			} 
			catch (SQLException e1) {
				Global.messageDialogShow("回滚失败，数据库可能出错！");
				return false;
			}
			Global.messageDialogShow("处理失败，请稍后重试！");
			return false;
		}
		
		return true;	
	}
	
}
