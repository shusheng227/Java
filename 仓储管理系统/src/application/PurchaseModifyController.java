package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import caculate.PurchaseQuery;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PurchaseModifyController implements Initializable {

	private ObservableList<String> dataList = FXCollections.observableArrayList();
    @FXML  private ComboBox<String> gName, mName, wName;
    @FXML private TextField pNumber, gNumber, price;

    @FXML
    void onModifyButtonClick(ActionEvent event) {
    	
    	// �Ƚ��зǿռ��
    	if(isEmpty()) return;
    	// ���һ�Ʒ��
    	Integer gid;
		try {
			String gNameInput = gName.getEditor().getText();
	    	String mNameInput = mName.getEditor().getText();	    	
			gid = PurchaseQuery.getGid(gNameInput, mNameInput);
		} catch (Exception e) {
			Global.messageDialogShow("�û�Ʒ�����ڣ������������룡");
    		return ;
		}
    	// ��ȡ���������������ۡ��ֿ��
    	String gNumberInput = gNumber.getText();
    	String priceInput = price.getText();
    	String wNameInput = wName.getEditor().getText();
    	Integer wid = PurchaseQuery.getWid(wNameInput);
    	
    	// �޸Ľ�������Ϣ
    	Vector<String> purchaseInfo = new Vector<>();
    	purchaseInfo.add(gid.toString());
    	purchaseInfo.add(gNumberInput);
    	purchaseInfo.add(priceInput); 
    	purchaseInfo.add(wid.toString());  	
    	if(!PurchaseQuery.updatePurchase(purchaseInfo,Global.pNumber)) {
    		Global.messageDialogShow("�޸�ʧ�ܣ����Ժ�����!");
    	}
    	else {
    		Global.messageDialogShow("�޸ĳɹ�!");
    	}
    	
    }

    @FXML
    void onClearButtonClick(ActionEvent event) {
    	
    	gName.getEditor().setText(null);
    	mName.getEditor().setText(null);
    	wName.getEditor().setText(null);
    	gNumber.setText(null);
    	price.setText(null);  	
    	
    }

    // ��ʼ��
    @Override public void initialize(URL location, ResourceBundle resources) {
    	autoCompleteComboBox(gName, 0);
    	autoCompleteComboBox(mName, 1);
    	autoCompleteComboBox(wName, 2);
    	
    	setGName();
    	setMName();
    	setWName();
    	
    	Vector<String> data = PurchaseQuery.getPurchaseInfo2(Global.pNumber);
    	if((data!=null)&&(!data.isEmpty())) {
    		gName.getEditor().setText(data.get(0));
    		mName.getEditor().setText(data.get(1));
    		gNumber.setText(data.get(2));
    		price.setText(data.get(3));
    		wName.getEditor().setText(data.get(4));
    	}
    	pNumber.setText(Global.pNumber);
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
  					if (comboBox.getEditor().getText() != null) {
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
  				
  				if(type==0) setGName();
  				if(type==1) setMName();
  				if(type==2) setWName();
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
		String gNameInput = gName.getEditor().getText();
		String mNameInput = mName.getEditor().getText();
		dataList = PurchaseQuery.getMNameList(gNameInput,mNameInput);
		mName.setItems(dataList);
	}
    
    // ��ȡ�ֿ�����
    private void setWName() {
		String wNameInput = wName.getEditor().getText();
		dataList = PurchaseQuery.getWNameList(wNameInput);
		wName.setItems(dataList);
	}
    
    // �ǿռ��
    private boolean isEmpty() {
    	
    	String gNameInput = gName.getEditor().getText();
    	String mNameInput = mName.getEditor().getText();
    	String gNumberInput = gNumber.getText();
    	String priceInput = price.getText();
    	String wNameInput = wName.getEditor().getText();
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
    		Global.messageDialogShow("�����������!");
    		return true;
    	}
    	if(wNameInput==null||wNameInput.isEmpty()) {
    		Global.messageDialogShow("����������ֿ���!");
    		return true;
    	}
    	
		return false;   	
    }
    
    
}

