package client.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import client.Main;
import client.System.Client;
import client.System.SystemRegistry;
import item.EventItem;
import item.StoryItem;
import javafx.collections.ObservableList;
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
	private ListView<StoryItem> list;
	
	/**
	 * イベントリスト
	 */
	@FXML
	private ListView<EventItem> eventlist;
	
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
		
		//------------------------------------------------------バツボタン
		Main.mainStage.setOnCloseRequest(e -> {
			if(client.finish()) {
				
			}else {
				e.consume();
			}
		});
		
		//------------------------------------------------------LogClearボタン
		clsbt.setOnAction(e -> logOut.setText(""));
		//------------------------------------------------------全チェックボタン
		allCheck.setOnAction(e -> allCheck(true));
		//------------------------------------------------------全非チェックボタン
		alldeCheck.setOnAction(e -> allCheck(false));
		//------------------------------------------------------漫画セレクトボックス
		combo.setOnAction(e -> ComicSelect());
		
		
	}
	
	/**
	 * <h1>ComicSelect</h1>
	 * <br>
	 */
	private void ComicSelect() {
		Item select = combo.getValue();
		System.out.println("Select:"+select.toString()+"="+select.getId());
		if(select.isAddingItem()) {
			System.out.println("addIng");//log
			if(client.showAddNameDialog(select)) combo.getItems().add(new Item());
		}
		client.LoadIndexList(select.getTitle());
	}
	
	/**
	 * <h1>allCheck</h1>
	 * 全チェック/非チェック<br>
	 * @param check
	 */
	private void allCheck(boolean check) {
		if(check == true) {
			System.out.println("AllCheck!!");
			allCheck.setSelected(true);
		}else {
			System.out.println("AllCheck Remove!!");
			alldeCheck.setSelected(false);
		}
		SystemRegistry.StoryList().forEach(o -> {
			StoryItem item = o;
			item.setSelect(check);
		});
	}
	
	/**
	 * <h1>upDate</h1>
	 * GUIの読み込み部分のアップデートを行います<br>
	 */
	public void upDate() {
		this.combo.setItems(SystemRegistry.ComicTitle().getList());
	}
	
	/**
	 * <h1>listSetup</h1>
	 * リストをセットアップします<br>
	 * @param list
	 */
	public void listSetup(ObservableList<StoryItem> list) {
		this.list.setItems(list);
		this.list.setCellFactory(new CheckCellFactory());
	}
	
}

