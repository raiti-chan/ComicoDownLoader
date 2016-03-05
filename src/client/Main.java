package client;
	
import raiti.RaitisAPI.util.PrintStream.DualPrintStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	/**
	 * 出力ストリーム
	 */
	public static DualPrintStream dps;
	
	public static final String Ver = "Alpha";
	
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
	
	/**
	 * ページインデックスタグ
	 */
	public static final String PAGEINDEXTAG ="&page=";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("gui/MainGUI.fxml"));
			Parent root = (Parent)fxml.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("gui/application.css").toExternalForm());
			primaryStage.setTitle("ComicoDownloader Ver."+Ver);
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