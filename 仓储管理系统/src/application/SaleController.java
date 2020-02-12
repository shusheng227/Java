package application;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

import caculate.*;
import global.Global;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class SaleController implements Initializable{
	private ObservableList<String> dataList = FXCollections.observableArrayList();
	@FXML private TextArea note;
    @FXML private TextField uid, date, sNumber;			// ���ɱ༭���ı���
    @FXML private TextField price, gNumber;				// �ɱ༭���ı���
    @FXML private ComboBox<String> gName, mName;	// �ɱ༭��ѡ���
    @FXML private Button submitButton, clearButton, exitButton;
    @FXML private TableColumn<Sale, String> sNumberInTable, gNameInTable, mNameInTable, gNumberInTable;
    @FXML private TableColumn<Sale, String> timeInTable, priceInTable, status;
    @FXML private TableColumn<Sale, Boolean> buttonInTable;
    @FXML private DatePicker startTime, endTime;
    @FXML private TableView<Sale> SaleInfo;
    

    @FXML void onSubmitButtonClick(ActionEvent event) {
    	
    	// �Ƚ��зǿռ��
    	if(isEmpty()) return;
    	// ���ҳ��Һ�		
	    String mNameInput = mName.getEditor().getText();
	    Integer mid = SaleQuery.getMid(mNameInput);
	    if(mid == 0) {
	    	Global.messageDialogShow("�ύʧ��,���Ժ�����!");
    		return ;
	    }
    	// ��ȡ��Ʒ�������������������ۡ���ע
	    String gNameInput = gName.getEditor().getText();
    	String gNumberInput = gNumber.getText();
    	String priceInput = price.getText();
    	String noteInput = note.getText();
    	// ��ȡ��ǰʱ��
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
    	// ��ȡ�������
    	Integer number = SaleQuery.getNumber();
    	if(number == 0) {
    		Global.messageDialogShow("�ύʧ��,���Ժ�����!");
    		return ;
    	}
    	
    	// �����������Ϣ 
    	Vector<String> saleInfo = new Vector<>();
    	saleInfo.add(number.toString());
    	saleInfo.add(Global.userNumber);
    	saleInfo.add(gNameInput);
    	saleInfo.add(mid.toString());
    	saleInfo.add(gNumberInput); 	
    	saleInfo.add(priceInput); 
    	saleInfo.add(nowTime);
    	saleInfo.add(noteInput);   	
    	if(!SaleQuery.insertSale(saleInfo)) {
    		Global.messageDialogShow("�ύʧ��,���Ժ�����!");
    		return ;
    	}
    	else {
    		String info = "�ύ�ɹ����������Ϊ" + number;
    		Global.messageDialogShow(info);
    	}
    	
    	// ���½����еĳ������ںͳ������
    	date.setText(nowTime);
    	sNumber.setText(number.toString());
    }

    @FXML void onClearButtonClick(ActionEvent event) {
    	note.setText(null);
    	date.setText(null);
    	sNumber.setText(null);
    	price.setText(null);
    	gNumber.setText(null);
    	gName.getEditor().setText(null);
    	mName.getEditor().setText(null);
    }

    @FXML void onExitButtonClick(ActionEvent event) {

		try {
			Parent Operation_Parent = FXMLLoader.load(getClass().getResource(Global.loginFxml));
			Global.sceneChange(Operation_Parent,event);
		} catch (IOException e) {
			Global.messageDialogShow("ע��ʧ��!");
		}
    	
    }

    @FXML void onQueryButtonClick(ActionEvent event) {
    	saleQuery();
    }
    
    // ��ʼ��
    @Override public void initialize(URL location, ResourceBundle resources) {
    	autoCompleteComboBox(gName, 0);
    	autoCompleteComboBox(mName, 1);
    	
    	setGName();
    	setMName();
    	
    	uid.setText(Global.userNumber);
    	
    	startTime.setValue(LocalDate.now().minusDays(1));
		endTime.setValue(LocalDate.now());
		
		bound();
		saleQuery();
	}
	
    // ��ѡ�������¼���������
    // ʵ�������б���
  	private void autoCompleteComboBox(ComboBox<String> comboBox, int type){
  	    comboBox.getEditor().focusedProperty().addListener(observable -> {
  	        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
  	            comboBox.getEditor().setText(null);
  	        }
  	    });
  	    comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
  	    comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
  		
  			@Override
  			public void handle(KeyEvent event) {
  				if (event.getCode() == KeyCode.UP) {
  					if (comboBox.getEditor().getText() != null) {
  						comboBox.getEditor().positionCaret(comboBox.getEditor().getText().length());
  					}
  					return;
  				}
  				if (event.getCode() == KeyCode.DOWN) {					
  					comboBox.show();
  					if ((comboBox.getEditor().getText() != null)&&(!comboBox.getEditor().getText().isEmpty())) {
  						comboBox.getEditor().positionCaret(comboBox.getEditor().getText().length());
  						return ;
  					}		 
  				}
  				if (event.getCode() == KeyCode.ENTER ||
  					event.getCode() == KeyCode.BACK_SPACE ||
  					event.getCode() == KeyCode.DELETE ||
  					event.getCode() == KeyCode.RIGHT ||
  					event.getCode() == KeyCode.LEFT ||
  					event.getCode().equals(KeyCode.SHIFT) ||
  					event.getCode().equals(KeyCode.CONTROL) ||
  					event.isControlDown() || 
  					event.getCode() == KeyCode.HOME ||
  					event.getCode() == KeyCode.END || 
  					event.getCode() == KeyCode.TAB) {
  					return;
  				}
  				
  				if(type == 0) setGName();
  				if(type == 1) setMName();
  				comboBox.show();
  			}
  	    });
  	}

    // ��ȡ��Ʒ�����б�
    private void setGName() {
		String gNameInput = gName.getEditor().getText();
		dataList = PurchaseQuery.getGNameList(gNameInput);
		gName.setItems(dataList);
	}
    
    // ��ȡ���������б�
    private void setMName() {
		String mNameInput = mName.getEditor().getText();
		dataList = PurchaseQuery.getMNameList(null,mNameInput);
		mName.setItems(dataList);
	}
    
    // �ǿռ��
    private boolean isEmpty() {
    	
    	String gNameInput = gName.getEditor().getText();
    	String mNameInput = mName.getEditor().getText();
    	String gNumberInput = gNumber.getText();
    	String priceInput = price.getText();
    	if(gNameInput==null||gNameInput.isEmpty()) {
    		Global.messageDialogShow("�������Ʒ��!");
    		return true;
    	}
    	if(mNameInput==null||mNameInput.isEmpty()) {
    		Global.messageDialogShow("�����볧����!");
    		return true;
    	}
    	if(gNumberInput==null||gNumberInput.isEmpty()) {
    		Global.messageDialogShow("�������Ʒ����!");
    		return true;
    	}
    	if(priceInput==null||priceInput.isEmpty()) {
    		Global.messageDialogShow("�����봦����!");
    		return true;
    	}
    	
		return false;   	
    }
    
    // ��tableview�б���
    private void bound() {
    	
    	sNumberInTable.setCellValueFactory(new PropertyValueFactory<>("sNumber"));
    	gNameInTable.setCellValueFactory(new PropertyValueFactory<>("gName"));
    	mNameInTable.setCellValueFactory(new PropertyValueFactory<>("mName"));
    	gNumberInTable.setCellValueFactory(new PropertyValueFactory<>("gNumber"));
    	timeInTable.setCellValueFactory(new PropertyValueFactory<>("time"));
    	priceInTable.setCellValueFactory(new PropertyValueFactory<>("price"));
    	status.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	// define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        buttonInTable.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sale, Boolean>, ObservableValue<Boolean>>() {
        	@Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Sale, Boolean> features) {
        		return new SimpleBooleanProperty(features.getValue() != null);
        	}
        });
        
        // create a cell value factory with an add button for each row in the table.
        buttonInTable.setCellFactory(new Callback<TableColumn<Sale, Boolean>, TableCell<Sale, Boolean>>() {
        	@Override public TableCell<Sale, Boolean> call(TableColumn<Sale, Boolean> purchaseBooleanTableColumn) {
        		return new AddSaleCell(SaleInfo);
          }
        });
    }
    
    // ��ѯ��ǰ������Ա��ʷ������
    private void saleQuery() {
    	String startTimeInput = startTime.getEditor().getText() + " 00:00:00";
    	String endTimeInput = endTime.getEditor().getText() + " 23:59:59";
    	
    	ObservableList<Sale> dataList = SaleQuery.getSaleInfo(Global.userNumber, startTimeInput, endTimeInput);
    	SaleInfo.setItems(dataList);
    }
}
