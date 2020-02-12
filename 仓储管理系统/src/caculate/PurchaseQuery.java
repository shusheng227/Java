package caculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import data.Data;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PurchaseQuery {
	
	// �������׻�ȡ��Ʒ�б�
	public static ObservableList<String> getGNameList(String initial) {	
		// �淶�ַ���
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
			System.out.println("��ȡ��Ʒ�б�ʧ��!");
		}
		return data;
	}
	
	// ���ݻ�Ʒ�������׻�ȡ�����б�
	public static ObservableList<String> getMNameList(String gName,String initial) {
		final ObservableList<String> data = FXCollections.observableArrayList();
		final String mNameQuery;
		// �淶�ַ���
		if(initial == null) initial = "";
		else {
			initial = initial.replace("\'", "\\\'");
		}
		// ȷ����ѯ���
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
			System.out.println("��ȡ�����б�ʧ��!");
		}
		return data;
	}
	
	// �������׻�ȡ�ֿ��б�
	public static ObservableList<String> getWNameList(String initial) {	
		// �淶�ַ���
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
			System.out.println("��ȡ�ֿ��б�ʧ��!");
		}
		return data;
	}
		
	// ���ݻ�Ʒ�����������һ�ȡ��Ʒid
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
			System.out.println("��ȡ��Ʒ��ʧ��!");
		}
		return 0;
	}
	
	// ���ݲֿ�����ȡ�ֿ��
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
			System.out.println("��ȡ�ֿ��ʧ��!");
		}
		return 0;
	}
	
	// ��ȡ�������
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
			System.out.println("��ȡ�������ʧ��!");
		}
		return 0;
	}
	
	// �����������Ϣ
	public static boolean insertPurchase(Vector<String> purchaseInfo) {
		final String purchaseInset = "insert into `purchase` values("
				+ purchaseInfo.get(0) + "," + purchaseInfo.get(1) + "," + purchaseInfo.get(2) + ",'"
				+ purchaseInfo.get(3) + "'," + purchaseInfo.get(4) + "," + purchaseInfo.get(5) + ",'"
				+ purchaseInfo.get(6) + "','" + purchaseInfo.get(7) + "',0)";
		
		try {
			return Data.update(purchaseInset);
		} catch (SQLException e) {
			System.out.println("�����������Ϣʧ�ܣ�");
			return false;
		}
	}
	
	// ���ݲɹ���Ա��ź�ʱ���ȡ��������Ϣ
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
					String status = (statusGet==0)?"�����":((statusGet==1)?"��ͨ��":"�Ѳ���");
					data.add(new Purchase(purchaseInfoGet.getString("number"), purchaseInfoGet.getString("gname"), purchaseInfoGet.getString("mname"), 
							purchaseInfoGet.getString("gnumber"), purchaseInfoGet.getString("time"), purchaseInfoGet.getString("price"), purchaseInfoGet.getString("wName"), status));
				}
			}	
		}
		catch (Exception e) {
			System.out.println("��ȡ��������Ϣʧ��!");
		}
		
		return data;
	}
	
	// ���ݻ�����Ż�ȡ��������Ϣ
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
				Global.messageDialogShow("���ݿ����ӳ���");
			}
		}
		catch (Exception e) {
			System.out.println("��ȡ��������Ϣʧ��!");
		}
		
		return data;
	}
	
	// ���ݽ�����״̬��ʱ���ȡ��������Ϣ
	public static ObservableList<Purchase> getPurchaseInfo3(String status, String startTime, String endTime) {
		final ObservableList<Purchase> data = FXCollections.observableArrayList();
		int statusInput = (status.equals("�����")?0:(status.equals("��ͨ��")?1:2));
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
			System.out.println("��ȡ��������Ϣʧ��!");
		}
		
		return data;
	}
	
	// �޸Ľ�������Ϣ
	public static boolean updatePurchase(Vector<String> purchaseInfo, String pNumber){
		final String purchaseUpdate = "update `purchase` "
				+ " set gid=" + purchaseInfo.get(0) + ", gnumber="  + purchaseInfo.get(1)
				+ ", price=" + purchaseInfo.get(2) + ", wid=" + purchaseInfo.get(3)			
				+ " where number=" + pNumber;
		
		try {
			return Data.update(purchaseUpdate);
		} catch (SQLException e) {
			System.out.println("�޸Ľ�������Ϣʧ��!");
			return false;
		}
	}
	
	// ���������ͨ��
	public static boolean passPurchase(String pNumber) {
		final String purchasePass = "update `purchase` set status=1 where number=" + pNumber;
		final String infoQuery = "select gid, wid, gnumber from `purchase` where number=" + pNumber;
		
		try {
			Data.connection.setAutoCommit(false);
			if(!Data.update(purchasePass)) return false; //���Ľ�����״̬
			if(!Data.query(infoQuery)) return false;	//��ѯ������Ʒ����������
			ResultSet InfoGet = Data.resultSet;
			if(InfoGet.next()) {
				String gid = InfoGet.getString("gid");
				String wid = InfoGet.getString("wid");
				String gnumber = InfoGet.getString("gnumber");
				if(!updateStorage(gid, wid, gnumber)) {	//���¿��
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
				Global.messageDialogShow("�ع�ʧ�ܣ����ݿ���ܳ���");
			}
			System.out.println("���½�����ʧ��!");
			return false;
		}	
		
		return true;
	}
	
	// ��������˲���
	public static boolean denyPurchase(String pNumber) {
		final String purchaseDeny = "update `purchase` set status=2 where number=" + pNumber;
		
		try {
			return Data.update(purchaseDeny);
		} catch (SQLException e) {
			System.out.println("����������ʧ��!");
			return false;
		}
	}
	
	// �洢��Ʒ
	public static boolean updateStorage(String gid, String wid, String number) throws SQLException {
		final String storageQuery = "select number from `storage` "
				+ " where gid=" + gid + " and wid=" + wid;
		final String storageInsert = "insert into `storage` values(" + gid + "," + wid + "," + number +")";
		final String storageUpdate = "update `storage` set number=number+" + number 
				+ " where gid=" + gid +" and wid=" + wid;

		if(Data.query(storageQuery)) {
			ResultSet storageGet = Data.resultSet;
			if(storageGet.next()) {
				// �ֿ���ڸû����������
				return Data.update(storageUpdate);
			}
			else {
				// �ֿⲻ���ڸû������
				return Data.update(storageInsert);
			}
		}
		return false;
	}
	
}
