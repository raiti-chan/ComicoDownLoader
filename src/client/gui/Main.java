package client.gui;
	
import raiti.RaitisAPI.util.PrintStream.DualPrintStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static DualPrintStream dps;
	
	/**
	 * Comicoの漫画のトップページURL 後ろに作品ナンバーを付けて使用
	 */
	public static final String MAINPAGEURL = "http://www.comico.jp/articleList.nhn?titleNo=";
	
	/**
	 * 漫画ページのURL 後ろに作品ナンバーとストーシーインデックスを付けます。
	 */
	public static final String COMICPAGEURL = "http://www.comico.jp/detail.nhn?titleNo=";
	
	/**
	 * ストーリーインデックスのベースです。後ろにインデックスを付与してください
	 */
	public static final String STORYINDEXBASE = "&articleNo=";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainGUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("ComicoDownloader");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
