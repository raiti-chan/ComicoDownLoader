package client.gui;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import raiti.RaitisAPI.util.RSystem;
import raiti.RaitisAPI.util.SystemOutUtility;
import raiti.RaitisAPI.util.PrintStream.DualFieldPrintStream;

import client.Client;
import client.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class MainController implements Initializable{
	
	/**
	 * メイン
	 */
	private static Client client;
	


	/**
	 * 標準の出力先
	 */
	public static DualFieldPrintStream dfps;
	
	/**
	 * Logエリアへの出力先
	 */
	public static PrintStream ps;
	
	/**
	 * LogファイルへのPrintStream
	 */
	public static PrintStream logps;
	
	/**
	 * ファイルとLogエリアへの出力ストリーム
	 */
	public static DualFieldPrintStream dps;
	
	/**
	 * Log用出力パネル
	 */
	@FXML
	private TextArea logOut;
	
	/**
	 * ダウンロードするシリーズの一覧
	 */
	@FXML
	private ComboBox<Item> combo;
	
	/**
	 * タイトルのリスト
	 */
	@FXML
	private ListView<String> list;
	
	/**
	 * すべてをオンにするボタン
	 */
	@FXML
	private CheckBox allCheck;
	
	/**
	 * すべてをオフにするボタン
	 */
	@FXML
	private CheckBox alldeCheck;
	
	/**
	 * ダウンロードボタン
	 */
	@FXML
	private Button downloadbt;
	
	/**
	 * HTMLBuildボタン
	 */
	@FXML
	private Button htmlBuildbt;
	
	/**
	 * ComboBoxのリスト
	 */
	private ObservableList<Item> name;
	
	private ObservableList<String> storylist;
	
	/**
	 * クリアボタン
	 */
	@FXML
	private Button clsbutton;

	/**
	 * アップデートボタン
	 */
	@FXML
	private Button updatebt;
	
	/**
	 * タブパネル
	 */
	@FXML
	private TabPane tabpane;
	
	
	//------------------------------------------------------code
	/**<h1>initialize</h1>
	 * オーバーライド<br>
	 * 初期化処理
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = new Client();
		//------------------------------------------------------標準出力変更
		ps = new PrintStream(new ByteArrayOutputStream() {
			@Override
			public void flush() throws IOException {
				logOut.appendText(toString());
				this.reset();
			}
		},true);
		
		try {
			String logfile = Client.LOGDOR+RSystem.getDateTime("yyyy-MM-dd HH-mm")+".log";
			logps = new PrintStream(logfile);
			System.out.println("MakeLogFile -> "+logfile);
			dps = new DualFieldPrintStream(ps, logps);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		dfps = SystemOutUtility.OutSeter(dps != null ? dps : ps);
		System.out.println("Initializeing...");
		
		client.initialize();
		//------------------------------------------------------clsボタン
		clsbutton.setOnAction(e -> logOut.clear());
		
		//------------------------------------------------------コンボボックス
		comboini();
		combo.setOnAction(e -> comboAction(e));
		//------------------------------------------------------List
		storylist = FXCollections.observableArrayList();
		list.setItems(storylist);
		list.setCellFactory(new CheckCellFactory());
		
		//------------------------------------------------------全チェック/非チェックボタン
		allCheck.setOnAction(e -> {
			((CheckBox)e.getSource()).setSelected(true);
			client.allCheck(true);
		});
		alldeCheck.setOnAction(e -> {
			((CheckBox)e.getSource()).setSelected(false);
			client.allCheck(false);
		});
		
		//------------------------------------------------------アップデートボタン
		updatebt.setOnAction(e -> {
			System.out.println("Update ->" + combo.getValue());
			if(combo.getValue() == null)return;
			client.HTMLDownload(combo.getValue(),this.storylist);
		});
	}
	
	
	/** コンボボックスアクション
	 * <h1>comboAction</h1>
	 * <br>
	 * @param e
	 */
	private void comboAction(ActionEvent e) {

		if(client.win == null)client.win = tabpane.getScene().getWindow();
		Item select = combo.getValue();//selectアイテムの取得
		System.out.println("Select :"+select.getName()+"="+select.getId());//log
		if(select.isAdd()) {
			System.out.println("addIng");//log
			if(client.showAddNameDialog(select)) name.add(new Item("追加", true));
		}
		client.loadindexList(select,storylist);
	}
	
	
	/** コンボボックスの初期化
	 * <h1>comboini</h1>
	 * <br>
	 */
	private void comboini() {
		name = FXCollections.observableArrayList();
		client.idPro.forEach((o1,o2) -> {
			name.add(new Item((String)o1,(String)o2));
			System.out.println("Add:"+o1+"="+o2);
		});
		name.add(new Item("追加", true));
		combo.setItems(name);
	}
	
	/** <h1>getClient</h1>
	 * {@link MainController#client}の取得<br>
	 * @return client
	 */
	public static Client getClient() {
		return client;
	}
	
}

