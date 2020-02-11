package application;
	
import java.io.IOException;

import data.Data;
import global.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {		
		try {
			// ����¼���浼��
			Parent root = FXMLLoader.load(getClass().getResource(Global.loginFxml));
			
			Data.connect();		//�������ݿ�
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("�ִ�����ϵͳ");
			primaryStage.getIcons().add(new Image(Global.icon)); // ���ô��ڵ�ͼ��
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
