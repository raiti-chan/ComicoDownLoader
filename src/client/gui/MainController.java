package client.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import raiti.RaitisAPI.io.File;

import client.Main;
import client.System.Client;
import client.System.SystemRegistry;
import client.System.Registry.Config;
import item.StoryItem;
import item.StoryListUpdate;
import item.TitlePageUp;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private ListView<String> eventlist;
	
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
	
	/**
	 * リストが最新順だとfalse
	 */
	private boolean listreverse = false;
	
	private StringBuffer buffer;
	
	//------------------------------------------------------code
	/**<h1>initialize</h1>
	 * オーバーライド<br>
	 * 初期化処理
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buffer = new StringBuffer();
		ByteArrayOutputStream out = new ByteArrayOutputStream() {
			@Override
			public void flush() throws IOException {
				buffer.append(toString());
				reset();
				Platform.runLater(() ->{
					textUp();
				});
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
		clsbt.setOnAction(e -> {
			logOut.setText("");
		});
		//------------------------------------------------------全チェックボタン
		allCheck.setOnAction(e -> allCheck(true));
		//------------------------------------------------------全非チェックボタン
		alldeCheck.setOnAction(e -> allCheck(false));
		//------------------------------------------------------ソートボタン
		sortbt.setOnAction(e -> sort());
		//------------------------------------------------------DLしていない章を自動選択
		ndlCheck.setOnAction(e -> autoCheck());
		//------------------------------------------------------漫画セレクトボックス
		combo.setOnAction(e -> ComicSelect());
		
		//------------------------------------------------------更新ボタン
		updatebt.setOnAction(e -> {
			Item item = combo.getValue();
			if(item != null)SystemRegistry.Event().addEvent(new StoryListUpdate(item.getId(),item.getTitle(),true));
			else {
				Alert alert = new Alert(AlertType.ERROR,"漫画が選択されていません");
				alert.showAndWait();
			};
			});
		
		//------------------------------------------------------ダウンロードボタン
		downloadbt.setOnAction(this::Download);
		//------------------------------------------------------HTMLBuildボタン
		htmlBuildbt.setOnAction(this::HtmlBuild);
		
		titleUp.setOnAction(this::titleUp);
		
	}
	
	private void textUp() {
		logOut.appendText(buffer.toString());
		buffer.delete(0, buffer.length());
	}
	
	/**
	 * <h1>Download</h1>
	 * ダウンロード実行<br>
	 * @param e
	 */
	private void Download(ActionEvent e) {
		List<StoryItem> dllist = new ArrayList<>();
		SystemRegistry.StoryList().forEach(o -> {
			if(o.isSelect() == true) {
				dllist.add(o);
			}
		});
		if(dllist.size() == 0)return;
		dllist.sort(this::comparat);
		//------------------------------------------------------漫画フォルダが存在しているかのチェック
		File comicFile = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH));
		Client.FileCheck(comicFile, false, true);

		dllist.forEach(o -> {
			SystemRegistry.Event().addEvent(new item.Download(o,combo.getValue()));
			SystemRegistry.Event().addEvent(new item.HTMLBuild(o,combo.getValue()));
		});
		
		dllist.forEach(o -> {
			SystemRegistry.Event().addEvent(new item.HTMLBuild(o,combo.getValue()));
		});
	}
	
	/**
	 * <h1>HtmlBuild</h1>
	 * HTMLビルドボタン<br>
	 * @param e
	 */
	private void HtmlBuild(ActionEvent e) {
		List<StoryItem> dllist = new ArrayList<>();
		SystemRegistry.StoryList().forEach(o ->{
			if(o.isSelect() == true) {
				dllist.add(o);
			}
		});
		if(dllist.size() == 0)return;
		dllist.sort(this::comparat);
		//------------------------------------------------------漫画フォルダが存在しているかのチェック
		File comicFile = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH));
		Client.FileCheck(comicFile, false, true);
		dllist.forEach(o -> {
			SystemRegistry.Event().addEvent(new item.HTMLBuild(o,combo.getValue()));
		});
	}
	
	/**
	 * <h1>titleUp</h1>
	 * タイトルページ更新<br>
	 * @param e
	 */
	private void titleUp(ActionEvent e) {
		if(combo.getValue() == null) return;
		SystemRegistry.Event().addEvent(new TitlePageUp(this.combo.getValue()));
	}
	
	/**
	 * <h1>ComicSelect</h1>
	 * <br>
	 */
	public void ComicSelect() {
		Item select = combo.getValue();
		System.out.println("Select:"+select.toString()+"="+select.getId());
		if(select.isAddingItem()) {
			System.out.println("addIng");//log
			if(client.showAddNameDialog(select)) combo.getItems().add(new Item());
		}
		client.LoadIndexList(select.getTitle());
		SystemRegistry.StorySort(listreverse);
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
	
	/**<h1>sort</h1>
	 * ソートボタン<br>
	 * @return
	 */
	private void sort() {
		listreverse = !listreverse;
		if(listreverse == true) {
			sortbt.setText("sort:↓");
		}else {
			sortbt.setText("sort:↑");
		}
		SystemRegistry.StorySort(listreverse);
	}
	
	/**
	 * <h1>autoCheck</h1>
	 * ダウンロードしていない章を自動選択<br>
	 */
	private void autoCheck() {
		if(combo.getValue() == null)return;
		File Cdir = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+combo.getValue().getTitle());
		Client.FileCheck(Cdir, false, true);
		String[] paths = Cdir.list();
		System.out.println(paths.length + "Files");
		if(paths.length == 0) {
			allCheck(true);
			return;
		}
		ArrayList<String> indexs = new ArrayList<>();
		for(String path:paths) {
			Matcher m = pattern.matcher(path);
			if(m.find()) {
				if(Client.haveHTMLdir(new File(Cdir.getPath()+"\\"+path))) indexs.add(path);
			}
		}
		SystemRegistry.StoryList().forEach(o -> {
			String index = o.getIndex()+"";
			if(indexs.contains(index)) {
				System.out.println(o.getTitle()+":true");
				o.getCb().setSelected(false);
			}else {
				System.out.println(o.getTitle()+":false");
				o.getCb().setSelected(true);
			}
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
	
	/**
	 * <h1>listupdate</h1>
	 * イベントリストの更新<br>
	 * @param list
	 */
	public void listupdate(ObservableList<String> list) {
		this.eventlist.setItems(list);
	}
	
	/**
	 * パターン
	 */
	public static Pattern pattern = Pattern.compile("\\A[0-9]+\\z");
	
	/**
	 * <h1>comparat</h1>
	 * ソート用メソッド<br>
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int comparat(StoryItem o1,StoryItem o2) {
		if(o1.getIndex() < o2.getIndex()) {
			return -1;
		}
		if(o1.getIndex() > o2.getIndex()) {
			return 1;
		}
		return 0;
	}
	
}

