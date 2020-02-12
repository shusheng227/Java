package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import data.*;
import global.*;
import caculate.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class doctorController implements Initializable{

	private String startTime;	//开始时间
	private String overTime;	//结束时间
	ObservableList<PatientColumn> registerData = FXCollections.observableArrayList();	// 病人列表表项
	ObservableList<IncomeColumn> incomeData = FXCollections.observableArrayList();	// 收入列表表项
	
	@FXML private Label welcomeLabel;
    @FXML private TableView<PatientColumn> registerInfo;
    @FXML private TableView<IncomeColumn> incomeInfo; 
    @FXML private TableColumn<PatientColumn, String> registerNumber, patientName, registerDate, categoriesType1;	// 病人列表表项
    @FXML private TableColumn<IncomeColumn, String> departmentName, doctorNumber, doctorName, categoriesType2;		// 收入列表表项
    @FXML private TableColumn<IncomeColumn, String> registerCount, income;
    @FXML private DatePicker startDay, overDay;
    @FXML private ComboBox<String> startHour, startMinute, overHour, overMinute;

    // 刷新
    @FXML
    void onRefreshButtonClick(ActionEvent event){
    	getRegisterInfo();
    	getIncomeInfo();
    	setWelcomeLabel();    	
    }

    // 退出登录
    @FXML
    void onExitButtonClick(ActionEvent event) throws IOException {
    	sceneChange(Global.loginFxml,event);
    }
    
    private void getRegisterInfo(){
    	registerNumber.setCellValueFactory(new PropertyValueFactory<PatientColumn, String>("registerNumber"));
    	patientName.setCellValueFactory(new PropertyValueFactory<PatientColumn, String>("patientName"));
    	registerDate.setCellValueFactory(new PropertyValueFactory<PatientColumn, String>("registerDate"));
    	categoriesType1.setCellValueFactory(new PropertyValueFactory<PatientColumn, String>("categoriesType"));
    	
    	registerData = Caculate.getPatientList();
    	registerInfo.setItems(registerData);
    }
    
    private void getIncomeInfo() {
    	
    	departmentName.setCellValueFactory(new PropertyValueFactory<IncomeColumn, String>("departmentName"));
    	doctorNumber.setCellValueFactory(new PropertyValueFactory<IncomeColumn, String>("doctorNumber"));
    	doctorName.setCellValueFactory(new PropertyValueFactory<IncomeColumn, String>("doctorName"));
    	categoriesType2.setCellValueFactory(new PropertyValueFactory<IncomeColumn, String>("categoriesType"));
    	registerCount.setCellValueFactory(new PropertyValueFactory<IncomeColumn, String>("registerCount"));
    	income.setCellValueFactory(new PropertyValueFactory<IncomeColumn, String>("income"));
    	
    	getTime();
    	incomeData = Caculate.getIncomeList(startTime, overTime);	
    	incomeInfo.setItems(incomeData);
    	
    }
    
    // 获取开始时间和截至时间
    private void getTime() {
    	int S_1 = Integer.parseInt(startHour.getSelectionModel().getSelectedItem().toString().replace("点", "").replace("分", "").trim());
    	int S_2 = Integer.parseInt(startMinute.getSelectionModel().getSelectedItem().toString().replace("点", "").replace("分", "").trim());
    	int E_1 = Integer.parseInt(overHour.getSelectionModel().getSelectedItem().toString().replace("点", "").replace("分", "").trim());
    	int E_2 = Integer.parseInt(overMinute.getSelectionModel().getSelectedItem().toString().replace("点", "").replace("分", "").trim());
    	startTime = startDay.getValue().toString() + " " + String.format("%02d", S_1) + ":" + String.format("%02d", S_2) + ":00";
    	overTime = overDay.getValue().toString() + " " + String.format("%02d", E_1) + ":" + String.format("%02d", E_2) + ":00";
    }
    
    // 初始化时间
    private void initTime() {
    	startDay.setValue(LocalDate.now());
		overDay.setValue(startDay.getValue().plusDays(1));
    	
    	for(int i = 0; i < 24; i++) {
 			startHour.getItems().addAll(i+"点");
 			overHour.getItems().addAll(i+"点");
 		}		
 		startHour.getSelectionModel().select(0);
 		overHour.getSelectionModel().select(0);
 		
 		for(int i = 0; i < 60; i++) {
 			startMinute.getItems().addAll(i+"分");
 			overMinute.getItems().addAll(i+"分");
 		}
 		startMinute.getSelectionModel().select(0);
 		overMinute.getSelectionModel().select(0);
    }
    
    // 更新欢迎标签
    private void setWelcomeLabel() {
    	final String nameQuery = "select ysmc from t_ksys where ysbh = " + Global.userNumber;
 		try {
			if(Data.query(nameQuery) == true) {
				ResultSet nameGet = Data.resultSet;
				if(nameGet.next()) {
					String name = nameGet.getString(1);
					welcomeLabel.setText(name + ",欢迎您!");
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("医生姓名获取失败：" + e.getMessage());
		}
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
 	
 	// 借用Initializable接口进行初始化
 	@Override
 	public void initialize(URL location, ResourceBundle resources) {
 		//初始化起始日期和截至日期
 		initTime();	
 		setWelcomeLabel();
 		
 		getRegisterInfo();
 		getIncomeInfo();
 			
 	}
}
