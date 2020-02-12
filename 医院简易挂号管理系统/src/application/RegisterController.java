package application;

import data.*;
import global.*;
import caculate.*;

import java.awt.Event;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RegisterController implements Initializable{

	String name,categoriesNumber;	//病人名称、号种编号
	Double registerFee, balance, balance2;	//挂号费、病人缴费前余额、病人缴费后余额
	private ObservableList<String> dataList = FXCollections.observableArrayList();
    @FXML private Button confirmButton, exitButton, cleanButton;
    @FXML private ComboBox<String> departmentName, doctorName, categoriesType, categoriesName;
    @FXML private TextField amountPaid, amountShoundPaid, amountChange, registerNumber;
    @FXML private Label welcomeLabel, balanceLabel, errorLabel;
    
    // 按下确认按钮后，进行非空检查及扣费
    @FXML void onConfirmButtonClick(ActionEvent event) throws IOException {
    	setWelcomeLabel();
    	if(departmentName.getEditor().getText().isEmpty()) {
    		errorLabel.setText("请输入科室名称");
    	}
    	else if(doctorName.getEditor().getText().isEmpty()) {
    		errorLabel.setText("请输入医生名称");
    	}
    	else if(categoriesType.getEditor().getText().isEmpty()) {
    		errorLabel.setText("请选择号种类别");
    	}
    	else if(categoriesName.getEditor().getText().isEmpty()) {
    		errorLabel.setText("请输入号种名称");
    	}
    	else if(amountPaid.getText() == null) {
    		errorLabel.setText("请先缴费");
    	}
    	
    	int maxRegisterNumber = Caculate.getMAXRegisterNumber(categoriesNumber);	//限定挂号人次
    	Integer registerNumber1 = Caculate.getRegisterNumber(categoriesNumber);		//已挂号人次
    	if(registerNumber1>=maxRegisterNumber) {
    		//挂号人次达到上限
    		String info = "挂号人次达到上限,请稍后重试";
    		JOptionPane.showMessageDialog(null, info, "提示", JOptionPane.INFORMATION_MESSAGE);
    		return ;
    	}
    	//获取当前时间、医生编号
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
    	String doctorNumber = Caculate.getDoctorNumber(doctorName.getEditor().getText());
    	String registerNumber2 = Caculate.getRegisterNumber();   	
    	//缴费并更新余额
    	if(balance>=registerFee) {
    		caculateBalance();
    		Caculate.updatePatientBalance(balance2);
    		
    	}
    	else {
    		caculateChange(null);
    	}   	
    	//插入挂号信息
    	ObservableList<String> registerInfo = FXCollections.observableArrayList();
    	registerInfo.addAll(registerNumber2,categoriesNumber,doctorNumber,Global.userNumber,registerNumber1.toString(),registerFee.toString(),nowTime);
    	if(!Caculate.insertRegisterInfo(registerInfo)) {
    		String info = "挂号失败,请稍后重试";
    		JOptionPane.showMessageDialog(null, info, "提示", JOptionPane.INFORMATION_MESSAGE);
    		return ;
    	}  	
    	// 挂号成功，更新挂号人次等
    	String startTime = LocalDate.now() + " 00:00:00";
    	String overTime = LocalDate.now().plusDays(1) + " 00:00:00";
    	Caculate.updateRegisterNumber(categoriesNumber, startTime, overTime);   	
    	registerNumber.setText(registerNumber2);
    	balanceLabel.setText(balance2.toString());
		String info = "挂号成功,您的挂号号码为" + registerNumber2 + "您的当前余额为" + balance2 + "元";
		JOptionPane.showMessageDialog(null, info, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    @FXML void onCleanButtonClick(ActionEvent event) {
    	departmentName.setValue(null);
    	doctorName.setValue(null);
    	categoriesType.setValue(null);
    	categoriesName.setValue(null);
    	amountPaid.setText(null);
    	amountShoundPaid.setText(null);
    	amountChange.setText(null);
    	registerNumber.setText(null);
    }

    @FXML
    void onExitButtonClick(ActionEvent event) throws IOException {
    	sceneChange(Global.loginFxml,event);
    }

    // 获取科室名称
    @FXML void setDepartmentName() {
    	String initial = departmentName.getEditor().getText();
		dataList = Caculate.getDepartmentName(initial);
		departmentName.setItems(dataList);
    }
    
    // 获取医生姓名
    @FXML void setDoctorName() {
		String departmentNameGet = departmentName.getEditor().getText();
		String initial = doctorName.getEditor().getText();
		dataList = Caculate.getDoctorName(departmentNameGet,initial);
		doctorName.setItems(dataList);
	}
    
    // 获取号种类别
    @FXML void setCategoriesType() {
    	String doctorNameGet = doctorName.getEditor().getText();
		dataList = Caculate.getCategoriesType(doctorNameGet);
		categoriesType.setItems(dataList);
    }
    
    // 获取号种名称
    @FXML void setCategoriesName() {
    	String departmentNameGet = departmentName.getEditor().getText();
    	String categoriesTypeGet = categoriesType.getEditor().getText();
    	String initial = categoriesName.getEditor().getText();
    	if(categoriesTypeGet.equals("普通号")) {
    		dataList = Caculate.getCategoriesName(departmentNameGet, false, initial);
    	}
    	if(categoriesTypeGet.equals("专家号")) {
    		dataList = Caculate.getCategoriesName(departmentNameGet, true, initial);
    	}
		categoriesName.setItems(dataList);
    }
    
    // 获取挂号费
    void setRegisterFee() {
    	String categoriesTypeGet = categoriesType.getEditor().getText();
    	String categoriesNameGet = categoriesName.getEditor().getText();
    	
    	if(categoriesTypeGet.equals("普通号")) {
    		categoriesNumber = Caculate.getCategoriesNumber(categoriesNameGet, false);
    	}
    	if(categoriesTypeGet.equals("专家号")) {
    		categoriesNumber = Caculate.getCategoriesNumber(categoriesNameGet, true);   		
    	}
    	if(categoriesNumber != null) {
    		registerFee = Caculate.getRegisterFee(categoriesNumber);
    		amountShoundPaid.setText(registerFee.toString());
    	} 	
    }
    
    // 余额充足时更新挂号费和缴费后余额
    @FXML void caculateBalance() {
    	setRegisterFee();
    	if(balance>=registerFee) {
    		errorLabel.setText("余额充足，无需缴费");
    		amountPaid.setText(balance.toString());
    		amountPaid.setEditable(false);
    		balance2 = balance-registerFee;
    		amountChange.setText(balance2.toString());
    	}
    	else {
    		amountPaid.setEditable(true);
    	}
    }
    
    // 余额不足时更新挂号费和找零金额信息
    @FXML boolean caculateChange(ActionEvent event) {
    	setRegisterFee();  	
    	Double balance3 = Double.valueOf(amountPaid.getText().toString());
    	if(balance3>=registerFee) {
    		errorLabel.setText(null);
    		balance2 = balance3-registerFee;
    		amountChange.setText(balance2.toString());
    		balance2 = balance;
    		return true;
    	}
    	else {
    		errorLabel.setText("缴费不足");
    		return false;
    	}
    } 
 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		setWelcomeLabel();			
		autoCompleteComboBoxPlus(departmentName, 1);
		autoCompleteComboBoxPlus(doctorName, 2);
		autoCompleteComboBoxPlus(categoriesType, 3);
		autoCompleteComboBoxPlus(categoriesName, 4);
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
 	
 	// 更新欢迎信息
 	private boolean setWelcomeLabel() {
 		final String nameQuery = "select brmc,ycje from t_brxx where brbh = " + Global.userNumber;
 		try {
			if(Data.query(nameQuery) == true) {
				ResultSet nameGet = Data.resultSet;
				if(nameGet.next()) {
					name = nameGet.getString("brmc");
					balance = nameGet.getDouble("ycje");
					welcomeLabel.setText(name + ",你好!");
					balanceLabel.setText("余额：" + balance);
				}
				return true;
			}
			else {
				welcomeLabel.setText("数据库获取失败，请稍后重新登录");
				balanceLabel.setText(null);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("病人信息获取失败：" + e.getMessage());
		}
 		return false;
 	}

 	// 实现下拉列表功能
 	private void autoCompleteComboBoxPlus(ComboBox<String> comboBox, int type){
 	    comboBox.getEditor().focusedProperty().addListener(observable -> {
 	        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
 	            comboBox.getEditor().setText(null);
 	        }
 	    });
 	    comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
 	    comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

 			private boolean moveCaretToPos = false;
 			private int caretPos;
 		
 			@Override
 			public void handle(KeyEvent event)
 			{
 				if (event.getCode() == KeyCode.UP)
 				{
 					caretPos = -1;
 					if (comboBox.getEditor().getText() != null)
 					{
 						moveCaret(comboBox.getEditor().getText().length());
 					}
 					return;
 				}
 				else if (event.getCode() == KeyCode.DOWN)
 				{
 					if (!comboBox.isShowing())
 						comboBox.show();
 					caretPos = -1;
 					if (comboBox.getEditor().getText() != null)
 						moveCaret(comboBox.getEditor().getText().length());
 					return;
 				}
 				else if ((event.getCode() == KeyCode.BACK_SPACE)||(event.getCode() == KeyCode.DELETE))
 				{
 					if (comboBox.getEditor().getText() != null)
 					{
 						moveCaretToPos = true;
 						caretPos = comboBox.getEditor().getCaretPosition();
 					}
 				}
 				else if (event.getCode() == KeyCode.ENTER)
 					return;
 		
 				if (event.getCode() == KeyCode.RIGHT ||
 					event.getCode() == KeyCode.LEFT ||
 					event.getCode().equals(KeyCode.SHIFT) ||
 					event.getCode().equals(KeyCode.CONTROL) ||
 					event.isControlDown() || event.getCode() == KeyCode.HOME ||
 					event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB)
 				{
 					return;
 				}
 				
 				
 				if(type == 1) {
 					setDepartmentName();
 				}
 				if(type == 2) {
 					setDoctorName();
 				}
 				if(type == 3) {
 					setCategoriesType();
 				}
 				if(type == 4) {
 					setDepartmentName();
 				}
 				comboBox.show();
 			}
 		
 			private void moveCaret(int textLength) {
 				if (caretPos == -1)
 					comboBox.getEditor().positionCaret(textLength);
 				else
 					comboBox.getEditor().positionCaret(caretPos);
 				moveCaretToPos = false;
 			}
 	    });
 	}
 	
}
