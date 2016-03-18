package client.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import client.Main;
import client.System.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable{
	
	//------------------------------------------------------FXML
	/**
	 * タイトルページを更新メニューアイテム
	 */
	@FXML
	private MenuItem titleUp;
	
	/**
	 * メインページを更新メニューアイテム
	 */
	@FXML
	private MenuItem mainUp;
	
	//==================================================================================================================
	/**
	 * ストーリーリスト更新ボタン
	 */
	@FXML
	private Button updatebt;
	
	/**
	 * ダウンロードボタン
	 */
	@FXML
	private Button downloadbt;
	
	/**
	 * 漫画HTMLの生成ボタン
	 */
	@FXML
	private Button htmlBuildbt;
	
	/**
	 * ログパネルの内容を消去ボタン
	 */
	@FXML
	private Button clsbt;
	
	/**
	 * ダウンロードしていない章を自動チェックボタン
	 */
	@FXML
	private Button ndlCheck;
	
	/**
	 * 章リストのソートボタン
	 */
	@FXML
	private Button sortbt;
	
	//==================================================================================================================
	/**
	 * リストを全チェックボタン
	 */
	@FXML
	private CheckBox allCheck;
	
	/**
	 * リストを全非チェックボタン
	 */
	@FXML
	private CheckBox alldeCheck;
	
	//==================================================================================================================
	/**
	 * タイトルを選択するコンボボックス
	 */
	@FXML
	private ComboBox<Item> combo;
	
	/**
	 * 章一覧リスト
	 */
	@FXML
	private ListView<String> list;
	
	/**
	 * イベントリスト
	 */
	@FXML
	private ListView<?> eventlist;
	
	/**
	 * ログの出力先
	 */
	@FXML
	private TextArea logOut;
	
	/**
	 * イベントの進行度
	 */
	@FXML
	private ProgressBar running;
	
	/**
	 * メインパネル
	 */
	@FXML
	private BorderPane main;
	
	//==================================================================================================================
	//------------------------------------------------------その他フィールド
	/**
	 * client
	 */
	public static Client client;
	

	
	//------------------------------------------------------code
	/**<h1>initialize</h1>
	 * オーバーライド<br>
	 * 初期化処理
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override

	public void initialize(URL location, ResourceBundle resources) {
		ByteArrayOutputStream out = new ByteArrayOutputStream() {
			@Override
			public void flush() throws IOException {
				logOut.appendText(toString());
				reset();
			}
		};
		Client.setPrintStream(out);
		
		Main.mainStage.setOnCloseRequest(e -> {
			if(client.finish()) {
				
			}else {
				e.consume();
			}
		});
	}
	
	
	
	
}

