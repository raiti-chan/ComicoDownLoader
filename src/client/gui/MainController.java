package client.gui;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.util.SystemOutUtility;
import raiti.RaitisAPI.util.PrintStream.DualFieldPrintStream;

import client.Item;
import client.gui.Dialog.AddNameDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable{
	
	/**
	 * AddNameDialogのFXMLの場所
	 */
	private final URL ADD_NAME_DIALOG_FXML = getClass().getResource("Dialog/AddNameDialog.fxml");
	
	/**
	 * URLLISTファイル場所
	 */
	private final String URLLIST_FILE = "config/urllist.ini"; 
	
	
	/**
	 * 標準の出力先
	 */
	public static DualFieldPrintStream dfps;
	
	/**
	 * Logエリアへの出力先
	 */
	public static PrintStream ps;
	
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
	 * ComboBoxのリスト
	 */
	private ObservableList<Item> name;
	
	/**
	 * nameのプロパティー
	 */
	public Properties prop;
	//------------------------------------------------------button
	/**
	 * クリアボタン
	 */
	@FXML
	private Button clsbutton;
	
	
	//------------------------------------------------------code
	/**<h1>initialize</h1>
	 * オーバーライド<br>
	 * 初期化処理
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//------------------------------------------------------標準出力変更
		ps = new PrintStream(new ByteArrayOutputStream() {
			@Override
			public void flush() throws IOException {
				logOut.appendText(toString());
				this.reset();
			}
		},true);

		dfps = SystemOutUtility.OutSeter(ps);
		System.out.println("Initializeing...");
		
		//------------------------------------------------------ファイルが存在するかのチェック
		System.out.println("check -> "+URLLIST_FILE);
		File ulf = new File(URLLIST_FILE);
		if(ulf.isDirectory()) {
			System.out.println(URLLIST_FILE+":delete");
			ulf.delete();
		}
		if(!ulf.exists()) {

			System.out.println(URLLIST_FILE + ":Make");
			ulf.MakeFile();
		}
		//------------------------------------------------------プロパティーの読み込み及び作成
		prop = new Properties();
		System.out.println("Loading ->"+URLLIST_FILE);
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(ulf),"UTF-8")) {
			prop.load(reader);
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		//------------------------------------------------------イニシャライズ
		//------------------------------------------------------CLSボタン
		clsbutton.setOnAction(e -> logOut.clear());
		
		//------------------------------------------------------コンボボックス
		combo.setOnAction(e -> comboAction(e));
		combolistInitalize();
		combo.setItems(name);
	}
	
	/**
	 * <h1>comboAction</h1>
	 * コンボボックスのアクション<br>
	 * @param e
	 */
	@FXML
	private void comboAction(ActionEvent e) {
		Item select = combo.getValue();
		System.out.println("Combo:"+select+" ID:"+select.getId());
		if(select.isAdd()) {
			showAddNameDialog(select);
			try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(URLLIST_FILE), "UTF-8")) {
				prop.store(writer, "");
				System.out.println("Write -> "+URLLIST_FILE);
				writer.close();
			}catch (IOException e1) {
				
			}
		}
	}
	
	/**
	 * <h1>showAddNameDialog</h1>
	 * 追加ダイアログを表示し、処理をします<br>
	 * @param addItem
	 */
	private void showAddNameDialog(Item addItem) {
		try {
			System.out.println("addIng"); //ログ
			
			FXMLLoader fxml = new FXMLLoader(ADD_NAME_DIALOG_FXML); //FMLXのローダー作成
			fxml.load();//ロード
			
			Parent p = fxml.getRoot();//FMLXからパネルを取得
			AddNameDialog d = fxml.getController();//コントローラーを取得
			Scene s = new Scene(p);//シーンの作成
			Stage addNameDialog = new Stage(StageStyle.UTILITY);//ステージの作成
			{	//------------------------------------------------------ステージのセットアップ
				addNameDialog.setScene(s);
				addNameDialog.initOwner(combo.getScene().getWindow());
				addNameDialog.initModality(Modality.WINDOW_MODAL);
				addNameDialog.setTitle("タイトルを追加");
			}
			d.setStage(addNameDialog);//コントローラーにステージを渡す
			
			addNameDialog.showAndWait();//ダイアログを表示
			
			System.out.println("ID:"+d.getID());//ログ
			System.out.println("Name:"+d.getName());//ログ
			
			//*********************************************************************************追加できるかの判定
			if(d.getName().equals("")) {
				System.out.println("名前が入ってません");//エラー
				
			}else if(!hasName(d.getName())){	//------------------------------------------------------アイテムの書き換え
				addItem.setName(d.getName());
				addItem.setId(d.getID());
				addItem.setAdd(false);
				prop.setProperty(d.getName(), d.getID());//URLプロパティーに追加
				name.add(new Item("追加",true));//新しいaddアイテムの追加
				
			}else {
				System.out.println("その名前はすでに存在しています");//エラー
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <h1>hasName</h1>
	 * name要素内に指定した名前のItemが存在するか<br>
	 * @return 存在する場合true
	 * @param 指定キー
	 */
	public boolean hasName(String key) {
		for(Item i:name) {
			if(i.getName().equals(key)) return true;
		}
		return false;
	}
	
	/**<h1>finalize</h1>
	 * オーバーライド
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		dfps.close();
		ps.close();
		super.finalize();
	}
	
	/**
	 * <h1>combolistInitalize</h1>
	 * コンボボックスの初期化<br>
	 */
	public void combolistInitalize() {
		name = FXCollections.observableArrayList();
		prop.forEach((o1,o2)-> {
			if(o1 == null || o1.equals("")) return;
			name.add(new Item((String)o1,(String)o2));
			System.out.println("add -> "+o1+" = "+o2);
		});
		name.add(new Item("追加",true));
	}
	
	
}
