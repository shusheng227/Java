package application;

/**
 * 主界面的设计
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
		// 将登录界面导入
		Parent root = FXMLLoader.load(getClass().getResource(Global.loginFxml));

		Data.initial();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("挂号管理系统");
		primaryStage.getIcons().add(new Image("/source/图标.jpg")); // 设置窗口的图标
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
