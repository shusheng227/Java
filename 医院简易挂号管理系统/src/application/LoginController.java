package application;


/**
 * 登录界面事件驱动
 */

import data.Data;
import global.Global;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;

public class LoginController implements Initializable{
	@FXML
	private ComboBox<String> identityComboBox;
	@FXML
	private TextField numberInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private Label errorPrompt;

	// Event Listener on Button[#btConfirm].onAction
	@FXML
	public void onButtonClick(ActionEvent event) throws IOException, SQLException {
		String number = numberInput.getText();
		String password = passwordInput.getText();
				
		if(number.isEmpty()) {
			errorPrompt.setText("请输入编号!");
		}
		else if(password.isEmpty()) {
			errorPrompt.setText("请输入密码!");
		}
		else if(identityComboBox.getValue() == null) {
			errorPrompt.setText("请选择身份");
		}
		else {
			String identity = identityComboBox.getValue();
			final String patientPasswordQuery = "select dlkl from t_brxx where brbh = " + number;
			final String doctorPasswordQuery = "select dlkl from t_ksys where ysbh = " + number;
			
			if(identity.equals("病人")) {
				if(Data.query(patientPasswordQuery) == true) {
					ResultSet passwordGet = Data.resultSet;
					if(passwordGet.next()) {
						if(passwordGet.getString(1).equals(password)) {
							Global.userNumber = number;
							sceneChange(Global.registerFxml,event);
						}
						else {
							errorPrompt.setText("密码错误!");
						}
					}
					else {
						errorPrompt.setText("编号不存在!");
					}
				}
				else {
					errorPrompt.setText("数据库未连接!");
				}
			}	
			else if(identity.equals("医生")) {
				if(Data.query(doctorPasswordQuery) == true) {
					ResultSet passwordGet = Data.resultSet;
					if(passwordGet.next()) {
						if(passwordGet.getString(1).equals(password)) {
							Global.userNumber = number;
							sceneChange(Global.doctorFxml,event);
						}
						else {
							errorPrompt.setText("密码错误!");
						}
					}
					else {
						errorPrompt.setText("编号不存在!");
					}
				}
				else {
					errorPrompt.setText("数据库未连接!");
				}
			}
		}
		
	}

	// 点击取消按钮清除编号和密码
	@FXML
	public void onCancelButtonClick(ActionEvent event) {
		numberInput.setText(null);
		passwordInput.setText(null);
		errorPrompt.setText(null);
	}

	// 借用Initializable接口进行身份选择框的初始化
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		identityComboBox.getItems().addAll("医生", "病人");
	}

	// 变换场景
	private void sceneChange(String string, ActionEvent event) throws IOException {
		Parent Operation_Parent = FXMLLoader.load(getClass().getResource(string));
		Scene Operation_Creating_Scene = new Scene(Operation_Parent);
		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		CreateOperation_Stage.hide();
		CreateOperation_Stage.setScene(Operation_Creating_Scene);
		CreateOperation_Stage.show();
	}
}
