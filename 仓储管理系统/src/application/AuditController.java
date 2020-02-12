package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class AuditController implements Initializable{
	
	private ObservableList<String> dataList = FXCollections.observableArrayList();
	// ��������ѯ
	@FXML private TableColumn<Purchase, String> pNumber, gName, mName, gNumber, price, wName, time, uid;
	@FXML private TableColumn<Purchase, Boolean> button;
	@FXML private TableView<Purchase> purchaseInfo;
	@FXML private DatePicker startTime, endTime;
	@FXML private ComboBox<String> statusCombox;
	// ��Ʒ��ѯ
	@FXML private ComboBox<String> gNameComboBox, mNameComboBox, wNameComboBox;
	@FXML private TableColumn<Goods, String> gid, gName2, mName2, wName2, gNumber2;
	@FXML private TableView<Goods> goodsInfo;
	// ��������ѯ
	@FXML private TableColumn<Sale, String> sNumber, gName3, mName3, gNumber3, price3, time3, uid3;
	@FXML private TableColumn<Sale, Boolean> button3;
	@FXML private TableView<Sale> saleInfo;
	@FXML private DatePicker startTime2, endTime2;
	@FXML private ComboBox<String> statusCombox2;
	// ȱ������
	@FXML private TableColumn<Stockout, String> sNumber2, gName4, gNumber4, status;
	@FXML private TableColumn<Stockout, Boolean> button4;
	@FXML private TableView<Stockout> stockoutInfo;
	@FXML private TextField sNumber3;
	@FXML private ComboBox<String> gNameComboBox2, statusCombox3;

	 
    @FXML void onQueryButtonClick1(ActionEvent event) {
    	purchaseQuery();
    }
    
    @FXML void onQueryButtonClick2(ActionEvent event) {
    	goodsQuery();
    }
    
    @FXML void onQueryButtonClick3(ActionEvent event) {
    	saleQuery();
    }
    
    @FXML void onQueryButtonClick4(ActionEvent event) {
    	stockoutQuery();
    }
    
    @FXML void onExitButtonClick(ActionEvent event) {

		try {
			Parent Operation_Parent = FXMLLoader.load(getClass().getResource(Global.loginFxml));
			Global.sceneChange(Operation_Parent,event);
		} catch (IOException e) {
			Global.messageDialogShow("ע��ʧ��!");
		}
    	
    }
    
    
    // ��ʼ��
    @Override public void initialize(URL location, ResourceBundle resources) {
    	
    	startTime.setValue(LocalDate.now().minusDays(1));
    	startTime2.setValue(LocalDate.now().minusDays(1));
		endTime.setValue(LocalDate.now());
		endTime2.setValue(LocalDate.now());
		
		statusCombox.getItems().addAll("�����","��ͨ��","�Ѳ���");
		statusCombox2.getItems().addAll("�����","��ͨ��","�Ѳ���");
		statusCombox3.getItems().addAll("ȱ����","�ɴ���","");
		statusCombox.setValue("�����");
		statusCombox2.setValue("�����");
		
		purchaseQuery();
		saleQuery();
		goodsQuery();
		stockoutQuery();
		
		autoCompleteComboBox(gNameComboBox,0);
		autoCompleteComboBox(mNameComboBox,1);
		autoCompleteComboBox(wNameComboBox,2);
		autoCompleteComboBox(gNameComboBox2,3);
	}
    
    // ��tableview�б���
    private void bound() {
    	
    	pNumber.setCellValueFactory(new PropertyValueFactory<>("pNumber"));
    	gName.setCellValueFactory(new PropertyValueFactory<>("gName"));
    	mName.setCellValueFactory(new PropertyValueFactory<>("mName"));
    	gNumber.setCellValueFactory(new PropertyValueFactory<>("gNumber"));
    	time.setCellValueFactory(new PropertyValueFactory<>("time"));
    	price.setCellValueFactory(new PropertyValueFactory<>("price"));
    	wName.setCellValueFactory(new PropertyValueFactory<>("wName"));
    	uid.setCellValueFactory(new PropertyValueFactory<>("uid"));
    	
    	// define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        button.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, Boolean>, ObservableValue<Boolean>>() {
        	@Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Purchase, Boolean> features) {
        		return new SimpleBooleanProperty(features.getValue() != null);
        	}
        });
        
        // create a cell value factory with an add button for each row in the table.
        button.setCellFactory(new Callback<TableColumn<Purchase, Boolean>, TableCell<Purchase, Boolean>>() {
        	@Override public TableCell<Purchase, Boolean> call(TableColumn<Purchase, Boolean> purchaseBooleanTableColumn) {
        		String status = statusCombox.getValue();
        		return new AddAuditCell(purchaseInfo,status);
          }
        });
    	
    }
    
    private void bound2() {
    	
    	gid.setCellValueFactory(new PropertyValueFactory<>("gid"));
    	gName2.setCellValueFactory(new PropertyValueFactory<>("gName"));
    	mName2.setCellValueFactory(new PropertyValueFactory<>("mName"));
    	wName2.setCellValueFactory(new PropertyValueFactory<>("wName"));
    	gNumber2.setCellValueFactory(new PropertyValueFactory<>("gNumber"));
    	
    }
    
    private void bound3() {
    	
    	sNumber.setCellValueFactory(new PropertyValueFactory<>("sNumber"));
    	gName3.setCellValueFactory(new PropertyValueFactory<>("gName"));
    	mName3.setCellValueFactory(new PropertyValueFactory<>("mName"));
    	gNumber3.setCellValueFactory(new PropertyValueFactory<>("gNumber"));
    	time3.setCellValueFactory(new PropertyValueFactory<>("time"));
    	price3.setCellValueFactory(new PropertyValueFactory<>("price"));
    	uid3.setCellValueFactory(new PropertyValueFactory<>("uid"));
    	
    	// define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        button3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sale, Boolean>, ObservableValue<Boolean>>() {
        	@Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Sale, Boolean> features) {
        		return new SimpleBooleanProperty(features.getValue() != null);
        	}
        });
        
        // create a cell value factory with an add button for each row in the table.
        button3.setCellFactory(new Callback<TableColumn<Sale, Boolean>, TableCell<Sale, Boolean>>() {
        	@Override public TableCell<Sale, Boolean> call(TableColumn<Sale, Boolean> purchaseBooleanTableColumn) {
        		String status = statusCombox2.getValue();
        		return new AddAuditCell2(saleInfo,status);
          }
        });
    }
    
    private void bound4() {
    	
    	sNumber2.setCellValueFactory(new PropertyValueFactory<>("sNumber"));
    	gName4.setCellValueFactory(new PropertyValueFactory<>("gName"));
    	gNumber4.setCellValueFactory(new PropertyValueFactory<>("gNumber"));
    	status.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	// define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
       button4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Stockout, Boolean>, ObservableValue<Boolean>>() {
        	@Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Stockout, Boolean> features) {
        		return new SimpleBooleanProperty(features.getValue() != null);
        	}
        });
        
        // create a cell value factory with an add button for each row in the table.
        button4.setCellFactory(new Callback<TableColumn<Stockout, Boolean>, TableCell<Stockout, Boolean>>() {
        	@Override public TableCell<Stockout, Boolean> call(TableColumn<Stockout, Boolean> purchaseBooleanTableColumn) {
        		return new AddAuditCell3(stockoutInfo); 
          }
        });
    }
    
    // ��ѯ��ǰѡ��״̬�Ľ�����
    private void purchaseQuery() {
    	String startTimeInput = startTime.getEditor().getText() + " 00:00:00";
    	String endTimeInput = endTime.getEditor().getText() + " 23:59:59";
    	String status = statusCombox.getValue();
    	 
    	bound();
    	ObservableList<Purchase> dataList = PurchaseQuery.getPurchaseInfo3(status, startTimeInput, endTimeInput);
    	purchaseInfo.setItems(dataList);
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
  				if(type == 2) setWName();
  				if(type == 3) setGName2();
  				comboBox.show();
  			}
  	    });
  	}
  	
  	// ��ȡ��Ʒ�����б�
    private void setGName() {
		String gNameInput = gNameComboBox.getEditor().getText();
		dataList = PurchaseQuery.getGNameList(gNameInput);
		gNameComboBox.setItems(dataList);
	}
    
    private void setGName2() {
		String gNameInput = gNameComboBox2.getEditor().getText();
		dataList = PurchaseQuery.getGNameList(gNameInput);
		gNameComboBox2.setItems(dataList);
	}
    
    // ��ȡ���������б�
    private void setMName() {
		String gNameInput = gNameComboBox.getEditor().getText();
		String mNameInput = mNameComboBox.getEditor().getText();
		dataList = PurchaseQuery.getMNameList(gNameInput,mNameInput);
		mNameComboBox.setItems(dataList);
	}
    
    // ��ȡ�ֿ�����
    private void setWName() {
		String wNameInput = wNameComboBox.getEditor().getText();
		dataList = PurchaseQuery.getWNameList(wNameInput);
		wNameComboBox.setItems(dataList);
	}
    
    // ��ѯ�ֿ��л�Ʒ��Ϣ
    private void goodsQuery() {
    	String gName = gNameComboBox.getEditor().getText();
    	String mName = mNameComboBox.getEditor().getText();
    	String wName = wNameComboBox.getEditor().getText();
    	
    	bound2();
    	ObservableList<Goods> data = AuditQuery.getGoodsList(gName, mName, wName);
    	goodsInfo.setItems(data);
    }
    
    // ��ѯ��ǰѡ��״̬�Ľ������ĳ�����
    private void saleQuery() {
    	String startTimeInput = startTime2.getEditor().getText() + " 00:00:00";
    	String endTimeInput = endTime2.getEditor().getText() + " 23:59:59";
    	String status = statusCombox2.getValue();
    	
    	bound3();
    	ObservableList<Sale> dataList = SaleQuery.getSaleInfo2(status, startTimeInput, endTimeInput);
    	saleInfo.setItems(dataList);
    }
    
    // ��ѯȱ����
    private void stockoutQuery() {
    	String sNumber = sNumber3.getText();
    	String gName = gNameComboBox2.getEditor().getText();
    	String status = statusCombox3.getValue();
    	
    	bound4();
    	ObservableList<Stockout> dataList = AuditQuery.getStockoutInfo(sNumber,gName,status);
    	stockoutInfo.setItems(dataList);
    }
    
    
    private class AddAuditCell extends TableCell<Purchase, Boolean>{

    	ComboBox<String> comboBox = new ComboBox<>();
    	StackPane paddedComboBox = new StackPane();	//�����ڵ�Ԫ���о�����ʾ��ť
    	
    	public AddAuditCell(TableView<Purchase> table,String status) {
    		if(status.equals("��ͨ��")) {
    			comboBox.getItems().addAll("�޸�");
    		}
    		else if(status.equals("�����")) {
    			comboBox.getItems().addAll("ͨ��","����","�޸�");
    		}		
    		paddedComboBox.setPadding(new Insets(3)); 
    		paddedComboBox.getChildren().add(comboBox);
    	    comboBox.setOnAction(new EventHandler<ActionEvent>() {
    	        @Override public void handle(ActionEvent actionEvent) {
    	        	table.getSelectionModel().select(getTableRow().getIndex());
    	        	String operation = comboBox.getValue();
    	        	String pNumber = table.getSelectionModel().getSelectedItem().getPNumber();
    	        	String gName = table.getSelectionModel().getSelectedItem().getGName();
    	        	if(operation.equals("ͨ��")) {
    	        		if(PurchaseQuery.passPurchase(pNumber)) {
    	        			if(AuditQuery.checkStockout(gName)) {
    	        				Global.messageDialogShow("���������ͨ��");
    	        				purchaseQuery();
    	        				return ;
    	        			}        			
    	        		}
    	        		else {
    	        			Global.messageDialogShow("��˳���,���Ժ�����");
    	        		}        		
    	        	}
    	        	else if(operation.equals("����")){
    	        		if(PurchaseQuery.denyPurchase(pNumber)) {
    	        			Global.messageDialogShow("���������سɹ�");
    	        		}
    	        		else {
    	        			Global.messageDialogShow("���س���,���Ժ�����");
    	        		}
    	        	}
    	        	else if(operation.equals("�޸�")){
    	        		Global.pNumber = pNumber;
    	        		String status = PurchaseQuery.getPurchaseInfo2(pNumber).get(5);
    	        		if(status.equals("2")) {
    	        			Global.messageDialogShow("�Ѳ���,�޷��޸�");
    	        			return ;
    	        		}
    	        		try {
    	        			Parent Operation_Parent = FXMLLoader.load(getClass().getResource(Global.PurchaseModifyFxml));
    	        			Global.newScene(Operation_Parent, "�޸�", Global.icon, actionEvent);
    	        		} catch (IOException e) {
    	        			Global.messageDialogShow("��תʧ��!");
    	        		}
    	        	}	
    	        }
    	    });
    	}
    	
    	/** places an add button in the row only if the row is not empty. */
        @Override protected void updateItem(Boolean item, boolean empty) {
          super.updateItem(item, empty);
          if (!empty) {
        	  setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        	  setGraphic(paddedComboBox);
          } 
          else {
        	  setGraphic(null);
          }
        }
    	
    }
    
    private class AddAuditCell2 extends TableCell<Sale, Boolean>{

    	ComboBox<String> comboBox = new ComboBox<>();
    	StackPane paddedComboBox = new StackPane();	//�����ڵ�Ԫ���о�����ʾ��ť
    	
    	public AddAuditCell2(TableView<Sale> table,String status) {
    		if(status.equals("��ͨ��")) {
    			comboBox.getItems().addAll("�޸�");
    		}
    		else if(status.equals("�����")) {
    			comboBox.getItems().addAll("ͨ��","����","�޸�");
    		}		
    		paddedComboBox.setPadding(new Insets(3));
    		paddedComboBox.getChildren().add(comboBox);
    	    comboBox.setOnAction(new EventHandler<ActionEvent>() {
    	        @Override public void handle(ActionEvent actionEvent) {
    	        	table.getSelectionModel().select(getTableRow().getIndex());
    	        	String operation = comboBox.getValue();
    	        	String sNumber = table.getSelectionModel().getSelectedItem().getSNumber();
    	        	if(operation.equals("ͨ��")) {
    	        		try {
    						if(SaleQuery.passSale(sNumber)) {
    							String note = SaleQuery.getNote(sNumber);
    							Global.messageDialogShow("���������ͨ��\n"+note);
    							saleQuery();
    							return ;
    						}
    						else {
    							Global.messageDialogShow("��˳���,���Ժ�����");
    						}
    					} 
    	        		catch (Exception e) {
    						System.out.println("ȱ���Ǽ�");
    					}        		
    	        	}
    	        	else if(operation.equals("����")){
    	        		if(SaleQuery.denySale(sNumber)) {
    	        			Global.messageDialogShow("���������سɹ�");
    	        		}
    	        		else {
    	        			Global.messageDialogShow("���س���,���Ժ�����");
    	        		}
    	        	}
    	        	else if(operation.equals("�޸�")){
    	        		Global.sNumber = sNumber;
    	        		String status = SaleQuery.getSaleInfo2(sNumber).get(4);
    	        		if(status.equals("2")) {
    	        			Global.messageDialogShow("�Ѳ���,�޷��޸�");
    	        			return ;
    	        		}
    	        		try {
    	        			Parent Operation_Parent = FXMLLoader.load(getClass().getResource(Global.SaleModifyFxml));
    	        			Global.newScene(Operation_Parent, "�޸�", Global.icon, actionEvent);
    	        		} catch (IOException e) {
    	        			Global.messageDialogShow("��תʧ��!");
    	        		}
    	        	}	
    	        }
    	    });
    	}
    	
    	/** places an add button in the row only if the row is not empty. */
        @Override protected void updateItem(Boolean item, boolean empty) {
          super.updateItem(item, empty);
          if (!empty) {
        	  setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        	  setGraphic(paddedComboBox);
          } 
          else {
        	  setGraphic(null);
          }
        }
    	
    }
    
    private class AddAuditCell3 extends TableCell<Stockout, Boolean>{

    	final Button modifyButton = new Button("����");
    	final StackPane paddedButton = new StackPane();	//�����ڵ�Ԫ���о�����ʾ��ť
    	
    	public AddAuditCell3(TableView<Stockout> table) {
    		paddedButton.setPadding(new Insets(3));
    	    paddedButton.getChildren().add(modifyButton);
    	    modifyButton.setOnAction(new EventHandler<ActionEvent>() {
    	        @Override public void handle(ActionEvent actionEvent) {
    	        	table.getSelectionModel().select(getTableRow().getIndex());
    	        	String status = table.getSelectionModel().getSelectedItem().getStatus();
    	        	String sNumber = table.getSelectionModel().getSelectedItem().getSNumber();
    	        	String gName = table.getSelectionModel().getSelectedItem().getGName();
    	        	String gNumber = table.getSelectionModel().getSelectedItem().getGNumber();
    	        	if(status.equals("ȱ����")) {
    	        		Global.messageDialogShow("ȱ���У��޷�����");
    	        	}
    	    		else {
    	    			AuditQuery.disposeStockout(sNumber, gName, Integer.valueOf(gNumber));
    	    			String note = SaleQuery.getNote(sNumber);
    	    			Global.messageDialogShow("����ɹ�\n" + note);
    	    			stockoutQuery();
    	    		}
    	        }
    	    });
    	}
    	
    	/** places an add button in the row only if the row is not empty. */
        @Override protected void updateItem(Boolean item, boolean empty) {
          super.updateItem(item, empty);
          if (!empty) {
        	  setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        	  setGraphic(paddedButton);
          } 
          else {
        	  setGraphic(null);
          }
        }
    	
    }
    
}
