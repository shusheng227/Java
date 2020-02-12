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

	private String startTime;	//��ʼʱ��
	private String overTime;	//����ʱ��
	ObservableList<PatientColumn> registerData = FXCollections.observableArrayList();	// �����б����
	ObservableList<IncomeColumn> incomeData = FXCollections.observableArrayList();	// �����б����
	
	@FXML private Label welcomeLabel;
    @FXML private TableView<PatientColumn> registerInfo;
    @FXML private TableView<IncomeColumn> incomeInfo; 
    @FXML private TableColumn<PatientColumn, String> registerNumber, patientName, registerDate, categoriesType1;	// �����б����
    @FXML private TableColumn<IncomeColumn, String> departmentName, doctorNumber, doctorName, categoriesType2;		// �����б����
    @FXML private TableColumn<IncomeColumn, String> registerCount, income;
    @FXML private DatePicker startDay, overDay;
    @FXML private ComboBox<String> startHour, startMinute, overHour, overMinute;

    // ˢ��
    @FXML
    void onRefreshButtonClick(ActionEvent event){
    	getRegisterInfo();
    	getIncomeInfo();
    	setWelcomeLabel();    	
    }

    // �˳���¼
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
    
    // ��ȡ��ʼʱ��ͽ���ʱ��
    private void getTime() {
    	int S_1 = Integer.parseInt(startHour.getSelectionModel().getSelectedItem().toString().replace("��", "").replace("��", "").trim());
    	int S_2 = Integer.parseInt(startMinute.getSelectionModel().getSelectedItem().toString().replace("��", "").replace("��", "").trim());
    	int E_1 = Integer.parseInt(overHour.getSelectionModel().getSelectedItem().toString().replace("��", "").replace("��", "").trim());
    	int E_2 = Integer.parseInt(overMinute.getSelectionModel().getSelectedItem().toString().replace("��", "").replace("��", "").trim());
    	startTime = startDay.getValue().toString() + " " + String.format("%02d", S_1) + ":" + String.format("%02d", S_2) + ":00";
    	overTime = overDay.getValue().toString() + " " + String.format("%02d", E_1) + ":" + String.format("%02d", E_2) + ":00";
    }
    
    // ��ʼ��ʱ��
    private void initTime() {
    	startDay.setValue(LocalDate.now());
		overDay.setValue(startDay.getValue().plusDays(1));
    	
    	for(int i = 0; i < 24; i++) {
 			startHour.getItems().addAll(i+"��");
 			overHour.getItems().addAll(i+"��");
 		}		
 		startHour.getSelectionModel().select(0);
 		overHour.getSelectionModel().select(0);
 		
 		for(int i = 0; i < 60; i++) {
 			startMinute.getItems().addAll(i+"��");
 			overMinute.getItems().addAll(i+"��");
 		}
 		startMinute.getSelectionModel().select(0);
 		overMinute.getSelectionModel().select(0);
    }
    
    // ���»�ӭ��ǩ
    private void setWelcomeLabel() {
    	final String nameQuery = "select ysmc from t_ksys where ysbh = " + Global.userNumber;
 		try {
			if(Data.query(nameQuery) == true) {
				ResultSet nameGet = Data.resultSet;
				if(nameGet.next()) {
					String name = nameGet.getString(1);
					welcomeLabel.setText(name + ",��ӭ��!");
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("ҽ��������ȡʧ�ܣ�" + e.getMessage());
		}
	}

    // �任����
 	private void sceneChange(String string, ActionEvent event) throws IOException {
 		Parent Operation_Parent = FXMLLoader.load(getClass().getResource(string));
 		Scene Operation_Creating_Scene = new Scene(Operation_Parent);
 		Stage CreateOperation_Stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
 		CreateOperation_Stage.hide();
 		CreateOperation_Stage.setScene(Operation_Creating_Scene);
 		CreateOperation_Stage.show();
 	}
 	
 	// ����Initializable�ӿڽ��г�ʼ��
 	@Override
 	public void initialize(URL location, ResourceBundle resources) {
 		//��ʼ����ʼ���ںͽ�������
 		initTime();	
 		setWelcomeLabel();
 		
 		getRegisterInfo();
 		getIncomeInfo();
 			
 	}
}
