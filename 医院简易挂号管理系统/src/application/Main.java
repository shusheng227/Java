package application;

/**
 * ����������
 */

import data.Data;
import global.Global;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		// ����¼���浼��
		Parent root = FXMLLoader.load(getClass().getResource(Global.loginFxml));

		Data.initial();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("�ҺŹ���ϵͳ");
		primaryStage.getIcons().add(new Image("/source/ͼ��.jpg")); // ���ô��ڵ�ͼ��
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
