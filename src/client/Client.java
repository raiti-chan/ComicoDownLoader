/*
 * 
 */
package client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;

import raiti.RaitisAPI.io.File;

import client.gui.Dialog.AddNameDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;


/** <h1>Client</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Client {
	
	/**
	 * IDリストのプロパティー
	 */
	public Properties idPro;
	
	/**
	 * addNameDialogのFXMLのURL
	 */
	public URL ADD_NAME_DIALOG_FXML;
	
	/**
	 * メインのウィンドウ
	 */
	public Window win;
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 *
	 */
	public Client() {
		ADD_NAME_DIALOG_FXML = getClass().getResource("gui/Dialog/AddNameDialog.fxml");
	}
	
	/** 初期化
	 * <h1>initialize</h1>
	 * 初期化します。2度呼び出さない<br>
	 */
	public void initialize() {
		Filecheck();
		loadingFile();
	}
	
	
	/** 名前追加ダイアログ
	 * <h1>showAddNameDialog</h1>
	 * タイトルを追加するダイアログを表示します<br>
	 */
	public boolean showAddNameDialog(Item item) {
		try {
			FXMLLoader fxml = new FXMLLoader(ADD_NAME_DIALOG_FXML);//FXMLローダーの作成
			fxml.load();//ロード
			
			Parent root = fxml.getRoot();//FXMLからパネルの取得
			AddNameDialog con = fxml.getController();//コントローラーの取得
			Scene sc = new Scene(root);//シーンの作成
			Stage stage = new Stage(StageStyle.UTILITY);//ステージの作成
			{//------------------------------------------------------ステージのセットアップ
				stage.setScene(sc);
				stage.initOwner(win);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.setTitle("タイトルを追加");
			}
			con.setStage(stage);//コントローラーにステージを渡す
			
			stage.showAndWait();
			
			System.out.println("ID:"+con.getID());//ログ
			System.out.println("Name:"+con.getName());//ログ
			
			//*********************************************************************************追加できるかのチェック
			if(con.getName().equals("")) {
				System.out.println("名前が入ってません");//エラー
				return false;
				
			}else if(!idPro.containsKey(con.getName())){	//------------------------------------------------------アイテムの書き換え
				item.setName(con.getName());
				item.setId(con.getID());
				item.setAdd(false);
				addURLPro(con.getName(), con.getID());
				return true;
			}else {
				System.out.println("その名前はすでに存在しています");//エラー
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	/**ファイルのチェック
	 * <h1>Filecheck</h1>
	 * <br>
	 */
	private void Filecheck() {
		System.out.println("FileCheck");
		
		//------------------------------------------------------urllist.ini
		System.out.println("Check -> "+IDLIST_FILE);
		File ulf = new File(IDLIST_FILE);
		if(ulf.isDirectory()) {
			System.out.println("isDirectry...Delete");
			ulf.delete();
		}
		if(!ulf.exists()) {
			System.out.println("FALSE!! MakeFile..."+IDLIST_FILE);
			ulf.MakeFile();
		}
		
	}
	
	public void loadindexList(Item item) {
		System.out.println("FileCheck");
		System.out.println("Check -> "+CONFIGDIR+item.getName()+".ini");
		File ulf = new File(CONFIGDIR+item.getName()+".ini");
		if(ulf.isDirectory()) {
			System.out.println("isDirectry...Delete");
			ulf.delete();
		}
		if(!ulf.exists()) {
			System.out.println("FALSE!! MakeFile..."+CONFIGDIR+item.getName()+".ini");
			ulf.MakeFile();
		}
	}
	
	
	
	/** コンフィグファイルのロード
	 * <h1>loadingFile</h1>
	 * ファイルのロード<br>
	 */
	private void loadingFile() {
		//------------------------------------------------------urllist.ini
		idPro = new Properties();
		System.out.println("Loading ->" + IDLIST_FILE);
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(IDLIST_FILE),"UTF-8")){
			idPro.load(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** IDプロパティーの追加
	 * <h1>addURLPro</h1>
	 * <br>
	 * @param name
	 * @param id
	 */
	public void addURLPro(String name,String id) {
		idPro.setProperty(name, id);
		try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(IDLIST_FILE), "UTF-8")){
			idPro.store(writer, "");
			System.out.println("Write -> "+IDLIST_FILE);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * IDリストのファイルの場所
	 */
	private final String IDLIST_FILE = "config/IDlist.ini";
	
	/**
	 * コンフォグファイルのディレクトリ
	 */
	private final String CONFIGDIR = "config/";
	
}
