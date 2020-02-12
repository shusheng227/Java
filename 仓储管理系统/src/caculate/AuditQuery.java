package caculate;

import java.sql.ResultSet;
import java.sql.SQLException;

import data.Data;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuditQuery {

	// ��ѯ�����б�
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
			System.out.println("��ȡ��Ʒ�б�ʧ��!");
		}
		return data;
		
	}
	
	// ����ȱ����Ϣ
	public static boolean insertStockout(String sNumber, String gName, int gNumber, int status) throws SQLException {
		String stockoutInsert = "insert into `stockout` values(" 
					+ sNumber + ", '" + gName + "', " + gNumber + "," + status + ")";
		
		return Data.update(stockoutInsert);
	}
	
	// ���ݻ�Ʒ���鿴ȱ��������Ӧ�����ܷ���
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
							// �ɴ���
							final String stockoutUpdate = "update `stockout` set status=1 where number=" + sNumber;
							if(!Data.update(stockoutUpdate)) return false;
						}
					}
					return true;
				}
			}
		}
		catch (Exception e) {
			System.out.println("����ȱ����ʧ��!");
		}
		
		return false;
	}	
	
	// ��ȡȱ����
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
			int statusInput = (status.equals("ȱ����")?0:1);
			stockoutQuery += " and status=" + statusInput + " ";
		}
		
		try {
			if(Data.query(stockoutQuery)) {
				ResultSet stockoutGet = Data.resultSet;
				while(stockoutGet.next()) {
					String statusGet = ((stockoutGet.getInt("status")==0)?"ȱ����":"�ɴ���");
					String sNumberGet = stockoutGet.getString("number");
					String gNameGet =  stockoutGet.getString("gname");
					String gNumberGet = stockoutGet.getString("gnumber");
					data.add( new Stockout(sNumberGet, gNameGet, gNumberGet, statusGet));
				}
			}
		}
		catch (Exception e) {
			System.out.println("��ȡȱ����ʧ��!");
		}
		
		return data;
	}
	
	
	// ȱ������
	public static boolean disposeStockout(String sNumber, String gName, int gNumber){
		final String stockoutDelete = "delete from `stockout` where number=" + sNumber;
		
		if(!SaleQuery.isEnough(gName, gNumber)) {
			Global.messageDialogShow("ȱ���У��޷�����");
			return false;
		}		
		
		try {
			Data.connection.setAutoCommit(false);
			if(!Data.update(stockoutDelete)) {	//ɾ��ȱ����Ϣ
				Global.messageDialogShow("����ʧ�ܣ����Ժ����ԣ�");
				return false;
			}
			if(!SaleQuery.updateStorage(sNumber, gName, gNumber)) {	//���������¿��
				insertStockout(sNumber, gName, gNumber, 1);
				Global.messageDialogShow("����ʧ�ܣ����Ժ����ԣ�");
				return false;
			}
			Data.connection.commit();
			Data.connection.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				Data.connection.rollback();
			} 
			catch (SQLException e1) {
				Global.messageDialogShow("�ع�ʧ�ܣ����ݿ���ܳ���");
				return false;
			}
			Global.messageDialogShow("����ʧ�ܣ����Ժ����ԣ�");
			return false;
		}
		
		return true;	
	}
	
}
