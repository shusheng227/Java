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
			// 将登录界面导入
			Parent root = FXMLLoader.load(getClass().getResource(Global.loginFxml));
			
			Data.connect();		//连接数据库
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("仓储管理系统");
			primaryStage.getIcons().add(new Image(Global.icon)); // 设置窗口的图标
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
