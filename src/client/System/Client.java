/*
 * 
 */
package client.System;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.util.RSystem;
import raiti.RaitisAPI.util.SystemOutUtility;
import raiti.RaitisAPI.util.PrintStream.DualFieldPrintStream;

import client.EventRunning;
import client.Main;
import client.System.Registry.Config;
import client.gui.Item;
import client.gui.MainController;
import client.gui.Dialog.AddNameDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** <h1>Client</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Client {
	
	/**
	 * イベント処理クラス
	 */
	private EventRunning eventRun;
	
	/**
	 * イベント処理スレッド
	 */
	private Thread eventRunThread;
	
	/**
	 * ファイルが出力先のプリントストリーム
	 */
	private static PrintStream filePrintstream;
	
	/**
	 * 出力先
	 */
	private static DualFieldPrintStream printStream;
	
	/**
	 * メインコントローラー
	 */
	private MainController MC;
	
	/**
	 * 追加ダイアログのFXMLのパス
	 */
	private URL ADDITEM_DIALOG;
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 * 初期化
	 * @param mc
	 */
	public Client(MainController mc) {
		MC = mc;
		SystemRegistry.RegistryInitialize();//レジストリーの初期化
		//------------------------------------------------------ファイルチェック
		LogCheck();//ログファイルの容量チェック。
		GUIInitialize();//GUIの初期化(データ部分)
		
		MC.listSetup(SystemRegistry.StoryList());
		ADDITEM_DIALOG = getClass().getClassLoader().getResource("client/gui/Dialog/AddNameDialog.fxml");
		eventRun = new EventRunning();//イベントスレッド
		eventRunThread = new Thread(eventRun,"EventRunning");
		eventRunThread.start();//イベントスレッド起動
	}
	
	/**
	 * <h1>GUIInitialize</h1>
	 * GUIの読み込み部分の初期化をします。<br>
	 */
	private void GUIInitialize() {
		MC.upDate();//コントローラーのアップデート呼び出し
	}
	
	/**
	 * <h1>setPrintStream</h1>
	 * デフォルトプリントストリームをセットします。<br>
	 * @param print
	 */
	public static void setPrintStream(ByteArrayOutputStream out) {
		FileCheck(new File(Config.LOGDIRPATH), false, true);//Logフォルダが存在するか。
		try {
			printStream = new DualFieldPrintStream(true);
			PrintStream print2 = new PrintStream(out,true);
			filePrintstream = new PrintStream(Config.LOGDIRPATH+RSystem.getDateTime("yyyy-MM-dd-HH-mm-ss")+".txt");
			printStream.setPrint1(filePrintstream);
			printStream.setPrint2(print2);
			if(Main.debugMode == true) {
				SystemOutUtility.OutSeter(printStream);
			}else {
				System.setOut(printStream);
			}
		}catch(IOException e) {
			Exception(e);
		}
	}
	
	/**
	 * <h1>finish</h1>
	 * clientの終了処理<br>
	 * @return 終了できた場合true
	 */
	public boolean finish() {
		if(eventRun.end()) {
			printStream.close();
			filePrintstream.close();
			return true;
		}
		return false;
	}
	
	/** <h1>getMC</h1>
	 * {@link Client#mC}の取得<br>
	 * @return mC
	 */
	public MainController getMC() {
		return MC;
	}
	
	/**<h1>showAddNameDialog</h1>
	 * アイテム追加ダイアログを表示します。<br>
	 * @param select
	 * @return
	 */
	public boolean showAddNameDialog(Item select) {
		try {
			FXMLLoader fxml = new FXMLLoader(ADDITEM_DIALOG);//FXMLローダーの作成
			fxml.load();//FXMLの読み込み
			
			Parent root = fxml.getRoot();//パネルの取得
			AddNameDialog con = fxml.getController();//コントローラーの取得
			Scene sc = new Scene(root);//シーンの作成
			Stage stage = new Stage(StageStyle.UTILITY);//ステージの作成
			//------------------------------------------------------ステージのセットアップ
			stage.setScene(sc);
			stage.initOwner(Main.mainStage);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("漫画を追加");
			
			stage.showAndWait();//ウィンドウ表示
			System.out.println("ID:"+con.getID());//ログ
			System.out.println("Name:"+con.getName());//ログ
			//*********************************************************************************追加できるかチェック
			if(con.getName().equals("")) {
				System.out.println("名前が入ってません");//エラー
				return false;
				
			}else if(!SystemRegistry.ComicTitle().containsKey(con.getName())){	//------------------------------------------------------アイテムの書き換え
				select.setTitle(con.getName());
				select.setId(con.getID());
				select.setAddingItem(false);
				SystemRegistry.ComicTitle().setProperty(con.getName(), con.getID());
				return true;
			}else {
				System.out.println("その名前はすでに存在しています");//エラー
				return false;
			}
			
		}catch (Exception e) {
			Exception(e);
		}
		
		return false;
	}
	
	/**
	 * <h1>LoadIndexList</h1>
	 * ストーリーリストを読み込みます。<br>
	 * @param name
	 */
	public void LoadIndexList(String name) {
		SystemRegistry.Story().load(name);
		SystemRegistry.StoryUpdate();
		
	}
	
	/**
	 * <h1>FileCheck</h1>
	 * ファイルが存在するかをチェックします<br>
	 * @param file チェックするファイルです。
	 * @param isFile ファイルがフォルダの場合false、ファイルの場合trueです。
	 * @param make ファイルが存在しない場合、ファイルを作成するかです。
	 * @return boolean ファイルが存在する場合true、しない場合falseです。
	 */
	public static boolean FileCheck(File file,boolean isFile,boolean make) {
		System.out.println("Check!!："+file.getPath());
		if(isFile == true) {
			if(file.isDirectory()){
				System.out.println("is Directory... Delete"+file.getPath());
				file.delete();
			}
			if(!file.exists() && make == true) {
				System.out.println("False!! Make..."+file.getPath());
				file.MakeFile();
			}else return true;
		}else {
			if(file.isFile()){
				System.out.println("is File... Delete"+file.getPath());
				file.delete();
			}
			if(!file.exists() && make == true) {
				System.out.println("False!! Make..."+file.getPath());
				file.mkdirs();
			}else return true;
		}
		
		return false;
	}
	
	/**
	 * <h1>LoggCheck</h1>
	 * ログファイルの消去<br>
	 */
	public static void LogCheck() {
		Integer maxsize = Integer.valueOf(SystemRegistry.Config().getProperty(Config.MAXLOGS));
		File file = new File(Config.LOGDIRPATH);
		List<java.io.File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
		if(files.size() < maxsize) {
			return;
		}
		files.sort(new Comparator<java.io.File>() {

			@Override
			public int compare(java.io.File o1, java.io.File o2) {
				return (int)(o1.lastModified()-o2.lastModified());
			}
		});
		int delete = files.size() - maxsize;
		for(int i = 0; i < delete ; i++) {
			System.out.println("Out of Max logFiles delete..."+files.get(i).getName());
			files.get(i).delete();
		}
		
	}
	
	/**
	 * <h1>Exception</h1>
	 * 例外発生時の処理<br>
	 * @param e 例外
	 */
	public static void Exception(Exception e) {
		e.printStackTrace();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setResizable(true);
		DialogPane pane = alert.getDialogPane();
		pane.setHeaderText(e.getClass().toString()+"\r\n"+e.getMessage());
		pane.setExpanded(true);
		String text = e.toString()+"\r\n\r\n";
		for(StackTraceElement ste:e.getStackTrace()) {
			text += ste.toString()+"\r\n";
		}
		pane.setExpandableContent(new TextArea(text));
		alert.showAndWait();
	}
	
	
	
	
}
