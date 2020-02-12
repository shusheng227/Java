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
	// JDBC�����������ݿ�URL
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse?"
			+ "useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&"
			+ "useLegacyDatetimeCode=false&serverTimezone=UTC";			//����Ϊutf-8
			
	// ���ݿ��û���������
	public static final String USER = "root";
	public static final String PASS = "xxs159357";
	
	// fxml�ļ�λ��
	public static final String loginFxml = "/fxml/Login.fxml";			//��¼�����ļ���
	public static final String PurchaseFxml = "/fxml/Purchase.fxml";	//�ɹ������ļ���
	public static final String SaleFxml = "/fxml/Sale.fxml";			//���۽����ļ���
	public static final String AuditFxml = "/fxml/Audit.fxml";			//��˽����ļ���
	public static final String PurchaseModifyFxml = "/fxml/PurchaseModify.fxml";	//�������޸Ľ����ļ���
	public static final String SaleModifyFxml = "/fxml/SaleModify.fxml";			//�������޸Ľ����ļ���
	
	public static String userNumber = "000003";	// ϵͳ��ǰ�û����
	public static String pNumber = "000001";	// ѡ�еĽ�������
	public static String sNumber = "000001";	// ѡ�еĳ�������
	
	// ͼƬ��Դ
	public static final String icon = "/image/ͼ��.png";
	
	// �任����
	public static void sceneChange(Parent Operation_Parent, ActionEvent event){		
		Scene Operation_Creating_Scene = new Scene(Operation_Parent);
		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		CreateOperation_Stage.hide();
		CreateOperation_Stage.setScene(Operation_Creating_Scene);
		CreateOperation_Stage.show();	
 	}
	
	// ��ʾ��ʾ��
	public static void messageDialogShow(String info) {
		JOptionPane.showMessageDialog(null, info, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// �����µĴ���
	public static void newScene(Parent Operation_Parent, String title, String icon, ActionEvent event){
		Stage stage = new Stage();
		Scene Operation_Creating_Scene = new Scene(Operation_Parent);
		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(Operation_Creating_Scene);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(CreateOperation_Stage );
		stage.setTitle(title);
		stage.getIcons().add(new Image(icon)); // ���ô��ڵ�ͼ��
		stage.show();
 	}
}
