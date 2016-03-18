package client;

import client.System.Client;
import client.gui.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	
	/**
	 * バージョン
	 */
	public static final String Ver = "1.0.0";
	
	/**
	 * このアプリケーションのメインステージ
	 */
	public static Stage mainStage;
	
	/**
	 * デバックモードの場合true
	 */
	public static boolean debugMode = false;

	
	@Override
	public void start(Stage primaryStage) {
		try {
			Main.mainStage = primaryStage;
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("gui/MainGUI.fxml"));
			Parent root = (Parent)fxml.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("gui/application.css").toExternalForm());
			primaryStage.setTitle("ComicoDownloader Ver."+Ver);
			primaryStage.setScene(scene);
			primaryStage.show();
			MainController.client = new Client();
		} catch(Exception e) {
			Client.Exception(e);
		}
	}
	
	public static void main(String[] args) {
		
		if(args.length != 0 &&args[0].equals("-d")) debugMode = true;
		launch(args);
	}
}
