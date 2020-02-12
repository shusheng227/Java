package caculate;

import java.io.IOException;

import global.Global;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public class AddSaleCell extends TableCell<Sale, Boolean> {
	final Button modifyButton = new Button("�޸�");
	final StackPane paddedButton = new StackPane();	//�����ڵ�Ԫ���о�����ʾ��ť
	
	public AddSaleCell(TableView<Sale> table) {
		paddedButton.setPadding(new Insets(3));
	    paddedButton.getChildren().add(modifyButton);
	    modifyButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override public void handle(ActionEvent actionEvent) {
	        	table.getSelectionModel().select(getTableRow().getIndex());
	        	String status = table.getSelectionModel().getSelectedItem().getStatus();
	        	if(!status.equals("�����")) {
	        		Global.messageDialogShow("����ˣ��޷��޸ģ�"); 
	        	}
	        	else {
	        		Global.sNumber = table.getSelectionModel().getSelectedItem().getSNumber();
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
    	  setGraphic(paddedButton);
      } 
      else {
    	  setGraphic(null);
      }
    }
}
