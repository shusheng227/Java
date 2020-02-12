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
import global.*;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;

public class PurchaseController implements Initializable{

	private ObservableList<String> dataList = FXCollections.observableArrayList();
	@FXML private TextArea note;
    @FXML private TextField uid, date, pNumber;			// 不可编辑的文本框
    @FXML private TextField price, gNumber;				// 可编辑的文本框
    @FXML private ComboBox<String> gName, wName, mName;	// 可编辑的选择框
    @FXML private Button submitButton, clearButton, exitButton;
    @FXML private TableColumn<Purchase, String> pNumberInTable, gNameInTable, mNameInTable, gNumberInTable;
    @FXML private TableColumn<Purchase, String> timeInTable, wNameInTable, priceInTable, status;
    @FXML private TableColumn<Purchase, Boolean> buttonInTable;
    @FXML private DatePicker startTime, endTime;
    @FXML private TableView<Purchase> purchaseInfo;
    

    @FXML void onSubmitButtonClick(ActionEvent event) {
    	
    	// 先进行非空检查
    	if(isEmpty()) return;
    	// 查找货品号
    	Integer gid;
		try {
			String gNameInput = gName.getEditor().getText();
	    	String mNameInput = mName.getEditor().getText();	    	
			gid = PurchaseQuery.getGid(gNameInput, mNameInput);
		} catch (Exception e) {
			Global.messageDialogShow("该货品不存在，请检查您的输入！");
    		return ;
		}
    	// 获取进货数量、进货价、仓库号、备注
    	String gNumberInput = gNumber.getText();
    	String priceInput = price.getText();
    	String wNameInput = wName.getEditor().getText();
    	Integer wid = PurchaseQuery.getWid(wNameInput);
    	String noteInput = note.getText();
    	// 获取当前时间
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String nowTime = df.format(new Date());
    	// 获取进货编号
    	Integer number = PurchaseQuery.getNumber();
    	if(number == 0) {
    		Global.messageDialogShow("提交失败,请稍后重试!");
    		return ;
    	}
    	
    	// 插入进货单信息
    	Vector<String> purchaseInfo = new Vector<>();
    	purchaseInfo.add(number.toString());
    	purchaseInfo.add(gid.toString());
    	purchaseInfo.add(gNumberInput);
    	purchaseInfo.add(Global.userNumber);
    	purchaseInfo.add(priceInput); 
    	purchaseInfo.add(wid.toString());
    	purchaseInfo.add(nowTime);
    	purchaseInfo.add(noteInput);   	
    	if(!PurchaseQuery.insertPurchase(purchaseInfo)) {
    		Global.messageDialogShow("提交失败请稍后重试!");
    		return ;
    	}
    	else {
    		String info = "提交成功，进货编号为" + number;
    		Global.messageDialogShow(info);
    	}
    	
    	// 更新界面中的进货日期和进货编号
    	date.setText(nowTime);
    	pNumber.setText(number.toString());
    }

    @FXML void onClearButtonClick(ActionEvent event) {
    	note.setText(null);
    	date.setText(null);
    	pNumber.setText(null);
    	price.setText(null);
    	gNumber.setText(null);
    	gName.getEditor().setText(null);
    	wName.getEditor().setText(null);
    	mName.getEditor().setText(null);
    }

    @FXML void onExitButtonClick(ActionEvent event) {

		try {
			Parent Operation_Parent = FXMLLoader.load(getClass().getResource(Global.loginFxml));
			Global.sceneChange(Operation_Parent,event);
		} catch (IOException e) {
			Global.messageDialogShow("注销失败!");
		}
    	
    }

    @FXML void onQueryButtonClick(ActionEvent event) {
    	purchaseQuery();
    }
    
    // 初始化
    @Override public void initialize(URL location, ResourceBundle resources) {
    	autoCompleteComboBox(gName, 0);
    	autoCompleteComboBox(mName, 1);
    	autoCompleteComboBox(wName, 2);
    	
    	setGName();
    	setMName();
    	setWName();
    	
    	uid.setText(Global.userNumber);
    	
    	startTime.setValue(LocalDate.now().minusDays(1));
		endTime.setValue(LocalDate.now());
		
		bound();
		purchaseQuery();
	}
	
    // 对选择框进行事件驱动设置
    // 实现下拉列表功能
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
  				if(type == 2) setWName();
  				comboBox.show();
  			}
  	    });
  	}

    // 获取货品名称列表
    private void setGName() {
		String gNameInput = gName.getEditor().getText();
		dataList = PurchaseQuery.getGNameList(gNameInput);
		gName.setItems(dataList);
	}
    
    // 获取厂家名称列表
    private void setMName() {
		String gNameInput = gName.getEditor().getText();
		String mNameInput = mName.getEditor().getText();
		dataList = PurchaseQuery.getMNameList(gNameInput,mNameInput);
		mName.setItems(dataList);
	}
    
    // 获取仓库名称
    private void setWName() {
		String wNameInput = wName.getEditor().getText();
		dataList = PurchaseQuery.getWNameList(wNameInput);
		wName.setItems(dataList);
	}
    
    // 非空检查
    private boolean isEmpty() {
    	
    	String gNameInput = gName.getEditor().getText();
    	String mNameInput = mName.getEditor().getText();
    	String gNumberInput = gNumber.getText();
    	String priceInput = price.getText();
    	String wNameInput = wName.getEditor().getText();
    	if(gNameInput==null||gNameInput.isEmpty()) {
    		Global.messageDialogShow("请输入货品名!");
    		return true;
    	}
    	if(mNameInput==null||mNameInput.isEmpty()) {
    		Global.messageDialogShow("请输入厂家名!");
    		return true;
    	}
    	if(gNumberInput==null||gNumberInput.isEmpty()) {
    		Global.messageDialogShow("请输入货品数量!");
    		return true;
    	}
    	if(priceInput==null||priceInput.isEmpty()) {
    		Global.messageDialogShow("请输入进货价!");
    		return true;
    	}
    	if(wNameInput==null||wNameInput.isEmpty()) {
    		Global.messageDialogShow("请输入所存仓库名!");
    		return true;
    	}
    	
		return false;   	
    }
    
    // 绑定tableview列表项
    private void bound() {
    	
    	pNumberInTable.setCellValueFactory(new PropertyValueFactory<>("pNumber"));
    	gNameInTable.setCellValueFactory(new PropertyValueFactory<>("gName"));
    	mNameInTable.setCellValueFactory(new PropertyValueFactory<>("mName"));
    	gNumberInTable.setCellValueFactory(new PropertyValueFactory<>("gNumber"));
    	timeInTable.setCellValueFactory(new PropertyValueFactory<>("time"));
    	priceInTable.setCellValueFactory(new PropertyValueFactory<>("price"));
    	wNameInTable.setCellValueFactory(new PropertyValueFactory<>("wName"));
    	status.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	// define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        buttonInTable.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, Boolean>, ObservableValue<Boolean>>() {
        	@Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Purchase, Boolean> features) {
        		return new SimpleBooleanProperty(features.getValue() != null);
        	}
        });
        
        // create a cell value factory with an add button for each row in the table.
        buttonInTable.setCellFactory(new Callback<TableColumn<Purchase, Boolean>, TableCell<Purchase, Boolean>>() {
        	@Override public TableCell<Purchase, Boolean> call(TableColumn<Purchase, Boolean> purchaseBooleanTableColumn) {
        		return new AddPurchaseCell(purchaseInfo);
          }
        });
    }
    
    // 查询当前采购人员历史进货单
    private void purchaseQuery() {
    	String startTimeInput = startTime.getEditor().getText() + " 00:00:00";
    	String endTimeInput = endTime.getEditor().getText() + " 23:59:59";
    	
    	ObservableList<Purchase> dataList = PurchaseQuery.getPurchaseInfo(Global.userNumber, startTimeInput, endTimeInput);
    	purchaseInfo.setItems(dataList);
    }
    
}
