package application;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Data;
import global.Global;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField uid;
    @FXML private PasswordField password;
    @FXML private Label errorLabel;
    @FXML private Button confirmButton, clearButton;

    // ���
    @FXML void onClearButtonClick(ActionEvent event) {
    	uid.setText(null);
    	password.setText(null);
    	errorLabel.setText(null);
    }
    
    // ȷ�ϵ�¼
    @FXML void onConfirmButtonClick(ActionEvent event) throws SQLException {
    	String uidInput = uid.getText();
    	String passwordInput = password.getText();
    	String permissionQuery = "select permission from user"
    			+ " where uid='" + uidInput + "' and pass='" + passwordInput + "'";
    	
    	//�ǿռ��
    	if((uidInput==null)||(uidInput.isEmpty())) {
    		errorLabel.setText("��������!");
    	}
    	else if((passwordInput==null)||(passwordInput.isEmpty())) {
    		errorLabel.setText("����������!");
    	}
    	else {
    		// ��ʼ��ѯ�˶�
    		if(!Data.query(permissionQuery)) {	
    			errorLabel.setText("���ݿ�δ����!");
    		}
    		else {
    			ResultSet permissionGet = Data.resultSet;
    			if(permissionGet.next()) {
    				String permission = permissionGet.getString("permission");
    				Global.userNumber = uidInput;
    				
    				// ����Ȩ��ת������
    				if(permission.equals("0")) sceneChange(Global.PurchaseFxml,event);
    				if(permission.equals("1")) sceneChange(Global.SaleFxml, event);
    				if(permission.equals("2")) sceneChange(Global.AuditFxml, event);
    			}
    			else {
    				errorLabel.setText("��Ż��������!");
    			}
    		}
    	}
    		
    }
    
    // �任����
 	private void sceneChange(String string, ActionEvent event){		
		try {
			
			Parent Operation_Parent;
			Operation_Parent = FXMLLoader.load(getClass().getResource(string));
			Scene Operation_Creating_Scene = new Scene(Operation_Parent);
	 		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	 		CreateOperation_Stage.hide();
	 		CreateOperation_Stage.setScene(Operation_Creating_Scene);
	 		CreateOperation_Stage.show();
	 		
		} catch (IOException e) {
			e.printStackTrace();
		}	
 	}
}
