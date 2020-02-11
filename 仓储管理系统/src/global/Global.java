package global;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Global {
	// JDBC驱动名及数据库URL
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse?"
			+ "useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&"
			+ "useLegacyDatetimeCode=false&serverTimezone=UTC";			//设置为utf-8
			
	// 数据库用户名和密码
	public static final String USER = "root";
	public static final String PASS = "xxs159357";
	
	// fxml文件位置
	public static final String loginFxml = "/fxml/Login.fxml";			//登录界面文件名
	public static final String PurchaseFxml = "/fxml/Purchase.fxml";	//采购界面文件名
	public static final String SaleFxml = "/fxml/Sale.fxml";			//销售界面文件名
	public static final String AuditFxml = "/fxml/Audit.fxml";			//审核界面文件名
	public static final String PurchaseModifyFxml = "/fxml/PurchaseModify.fxml";	//进货单修改界面文件名
	public static final String SaleModifyFxml = "/fxml/SaleModify.fxml";			//进货单修改界面文件名
	
	public static String userNumber = "000003";	// 系统当前用户编号
	public static String pNumber = "000001";	// 选中的进货单号
	public static String sNumber = "000001";	// 选中的出货单号
	
	// 图片资源
	public static final String icon = "/image/图标.png";
	
	// 变换场景
	public static void sceneChange(Parent Operation_Parent, ActionEvent event){		
		Scene Operation_Creating_Scene = new Scene(Operation_Parent);
		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		CreateOperation_Stage.hide();
		CreateOperation_Stage.setScene(Operation_Creating_Scene);
		CreateOperation_Stage.show();	
 	}
	
	// 显示提示框
	public static void messageDialogShow(String info) {
		JOptionPane.showMessageDialog(null, info, "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// 弹出新的窗口
	public static void newScene(Parent Operation_Parent, String title, String icon, ActionEvent event){
		Stage stage = new Stage();
		Scene Operation_Creating_Scene = new Scene(Operation_Parent);
		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(Operation_Creating_Scene);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(CreateOperation_Stage );
		stage.setTitle(title);
		stage.getIcons().add(new Image(icon)); // 设置窗口的图标
		stage.show();
 	}
}
