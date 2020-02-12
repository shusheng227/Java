package caculate;
/**
 * ʵ��tableview�еİ�ť����
 * @author 86131
 *
 */

import java.io.IOException;

import global.Global;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AddPurchaseCell extends TableCell<Purchase, Boolean>{
	
	final Button modifyButton = new Button("�޸�");
	final StackPane paddedButton = new StackPane();	//�����ڵ�Ԫ���о�����ʾ��ť
	
	public AddPurchaseCell(TableView<Purchase> table) {
		paddedButton.setPadding(new Insets(3));
	    paddedButton.getChildren().add(modifyButton);
	    modifyButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override public void handle(ActionEvent actionEvent) {
	        	table.getSelectionModel().select(getTableRow().getIndex());
	        	String status = table.getSelectionModel().getSelectedItem().getStatus();
	        	if(status.equals("��ͨ��")||status.equals("�Ѳ���")) {
	        		Global.messageDialogShow("����ˣ��޷��޸ģ�");
	        	}
	        	else {
	        		Global.pNumber = table.getSelectionModel().getSelectedItem().getPNumber();
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
    	  setGraphic(paddedButton);
      } 
      else {
    	  setGraphic(null);
      }
    }
}
