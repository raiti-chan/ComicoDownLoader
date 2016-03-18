/*
 * 
 */
package client.System;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

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
	
	
	//-------------------------------------コンストラクター
	public Client() {
		SystemRegistry.RegistryInitialize();
		//------------------------------------------------------ファイルチェック
		LogCheck();
		
		
		eventRun = new EventRunning();
		eventRunThread = new Thread(eventRun,"EventRunning");
		eventRunThread.start();
	}
	
	/**
	 * <h1>setPrintStream</h1>
	 * デフォルトプリントストリームをセットします。<br>
	 * @param print
	 */
	public static void setPrintStream(ByteArrayOutputStream out) {
		FileCheck(new File(Config.LOGDIRPATH), false, true);
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
