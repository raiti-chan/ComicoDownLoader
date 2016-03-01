/*
 * 
 */
package client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.net.DownLoader;

import client.gui.Dialog.AddNameDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
	 * リストのチェックボックスのマップ。Key:タイトル、V:対応するチェックボックス。
	 */
	public HashMap<String, CheckBox> checkMap;
	
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
		this.checkMap = new HashMap<String,CheckBox>();
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
	private static void Filecheck() {
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
		
		//------------------------------------------------------.TEMP/
		System.out.println("Check ->"+TEMPDIT);
		File temp = new File(TEMPDIT);
		if(temp.isFile()) {
			System.out.println("isFile...Delete");
			temp.delete();
		}
		if(!temp.exists()) {
			System.out.println("FALSE!! MakeDirectory..."+TEMPDIT);
			temp.mkdir();
		}
		
		//------------------------------------------------------Logs/
		System.out.println("Check ->"+LOGDOR);
		File log = new File(LOGDOR);
		if(log.isFile()) {
			System.out.println("isFile...Delete");
			log.delete();
		}
		if(!log.exists()) {
			System.out.println("FALSE!! MakeDirectory..."+LOGDOR);
			log.mkdir();
		}
		
	}
	
	/** タイトルリストの読み込み
	 * <h1>loadindexList</h1>
	 * <br>
	 * @param item
	 */
	public void loadindexList(Item item,List<String> list) {
		if(item.isAdd())return;//アイテムが選択されてなければ強制リターン
		list.clear();//アイテムリストを消去
		
		System.out.println("FileCheck");//log
		System.out.println("Check -> "+CONFIGDIR+item.getName()+".ini");//log
		
		File ulf = new File(CONFIGDIR+item.getName()+".ini");//ファイルの読み込み
		if(ulf.isDirectory()) {
			System.out.println("isDirectry...Delete");//log
			ulf.delete();//フォルダーの消去
		}
		if(!ulf.exists()) {
			System.out.println("FALSE!! MakeFile..."+CONFIGDIR+item.getName()+".ini");//log
			ulf.MakeFile();//ファイルの作成
		}
		System.out.println("Loading ->"+CONFIGDIR+item.getName()+".ini");
		this.checkMap.clear();//マップの消去
		Properties cpro = new Properties();//読み込むプロパティーの取得。
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(CONFIGDIR+item.getName()+".ini"),"UTF-8")){
			cpro.load(reader);//読み込み
			reader.close();//リーダーを閉じる
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		//----------以下の方法だとソートができない。
		cpro.forEach((o1,o2) -> {//チェックボックスの作成
			CheckBox cb = new CheckBox((String)o1+":");
			System.out.println("Load ->"+o1+"="+o2);
			String text = (String)o2;
			String name = text.substring(0,text.indexOf(URLMARK));
			
			this.checkMap.put(name, cb);
			list.add(name);
		});
	
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
			return;
		}
	}
	
	/** すべてをチェック、非チェックに設定
	 * <h1>allCheck</h1>
	 * <br>
	 * @param is
	 */
	public void allCheck(boolean is) {
		checkMap.forEach((key,v) -> {
			v.setSelected(is);
		});
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
	
	/** HTMLのダウンロード
	 * <h1>HTMLDownload</h1>
	 * <br>汚い...
	 */
	public void HTMLDownload(Item item,List<String> list) {
		//String htmlUrl = Main.MAINPAGEURL + item.getId();//HTMLへのURL
		String htmlUrl = "file:///E:/Program/Java/Project/ComicoDownloader/TestHTML/tempHTML.txt";
		File html = null;//DLしたHTMLファイル
		Properties pro = new Properties();//データの保管プロパティー
		try {
			DownLoader dl = new DownLoader(htmlUrl,true);//ダウンローダーの作成
			dl.Connecting();//接続
			html = new File(dl.download(TEMPDIT+"tempHTML.txt").getPath());//ダウンロード
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try (InputStreamReader input = new InputStreamReader(new FileInputStream(html), "UTF-8");
				BufferedReader reader = new BufferedReader(input);){
			String text;
			int line = 1;
			while ((text = reader.readLine()) != null) {
				if(text.indexOf(HTMLMARK) != -1) break;//m-thumb-episodeのラインを検索
				reader.mark(100);
				line++;
			}
			System.out.println("Out -> Line:"+line);//log
			text = reader.readLine();line++;//次のラインへ
			
			int indexofp = text.indexOf(Main.STORYINDEXBASE);//Numberパラメータの始まり部(&articleNo=**)
			System.out.println("Out of Numberparameter -> col:"+indexofp);//log
			int indexofe = text.indexOf("\"", indexofp);//Numberパラメータより後にある"の位置
			System.out.println("Out of end -> col"+text.indexOf("\"", indexofe));//log
			int length = Integer.parseInt(text.substring(indexofp + Main.STORYINDEXBASE.length() , indexofe));//最新話のインデックス
			System.out.println("NewestStoryNo = "+length);
			reader.reset();//リーダーのリセット
			
			//------------------------------------------------------ここから先HTMLの解析及び、プロパティーの作成
			String samneURL = null,Title = null;
			int index = length;
			int file = 1;//読み込むページ
			while(index > 0) {
				switch (file) {
				case 1://メインページの場合
					int loop = 0;
					while ((text = reader.readLine()) != null) {
						if((text.indexOf(HTMLMARK)) != -1) {
							reader.readLine();//破棄
							reader.readLine();//破棄
							text = reader.readLine();//サムネのURLのあるライン
							samneURL = getImg(text);//サムネイルのURL
							text = reader.readLine();//タイトルのあるライン
							Title = getTitle(text);//タイトルの取得
							System.out.println(index+":"+Title+URLMARK+samneURL);//log
							pro.setProperty(Integer.toString(index), Title + URLMARK + samneURL);//プロパティーの追加
							index --;
							loop++;
						}
						if(loop == 10) break;
						
					}
					file++;//ページを一つ加算
					reader.close();//リーダーを閉じる
					break;
					
				default://その他のページの読み込み
					//String url = Main.MAINPAGEURL + item.getId() + Main.PAGEINDEXTAG + file;//ページのURL
					String url = "file:///E:/Program/Java/Project/ComicoDownloader/TestHTML/tempHTML_"+file+".txt";
					DownLoader dl = new DownLoader(url, true);//ダウンローダーの作成
					dl.Connecting();//サーバーへの接続
					File html2 = new File(dl.download(TEMPDIT+"tempHTML_"+file+".txt").getPath());//ファイルのダウンロード
					
					InputStreamReader input2 = new InputStreamReader(new FileInputStream(html2), "UTF-8");
					BufferedReader reader2 = new BufferedReader(input2);
					
					String samneURL2 = null,Title2 = null;
					int loop2 = 0;
					while ((text = reader2.readLine()) != null) {
						if((text.indexOf(HTMLMARK)) != -1) {
							reader2.readLine();//破棄
							reader2.readLine();//破棄
							text = reader2.readLine();//サムネのURLのあるライン
							samneURL2 = getImg(text);//サムネイルのURL
							text = reader2.readLine();//タイトルのあるライン
							Title2 = getTitle(text);//タイトルの取得
							System.out.println(index+":"+Title2+URLMARK+samneURL2);//log
							pro.setProperty(Integer.toString(index), Title2 + URLMARK + samneURL2);//プロパティーの追加
							index --;
							loop2++;
						}
						if(loop2 == 10)break;
					}
					reader2.close();
					input2.close();
					html2.delete();//ダウンロードしたTempファイルの消去
					file++;
					break;
				}
			}
			OutputStreamWriter writ = new OutputStreamWriter(new FileOutputStream(CONFIGDIR + item.getName()+".ini"), "UTF-8");
			pro.store(writ, "");
			writ.close();
			reader.close();//リーダーを閉じる
			input.close();//バッファーリーダーを閉じる
			loadindexList(item, list);//リストの更新
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		html.delete();//tempファイルの消去
	}
	
	/** サムネURLの取得
	 * <h1>getImg</h1>
	 * <br>
	 * @param reader
	 * @return 名前プラスサムネURL
	 */
	private static String getImg(String text) {
		int indexu = text.indexOf("url(");
		int indexe = text.indexOf(")",indexu);
		return text.substring(indexu+4, indexe);
	}
	
	/**　タイトルの取得
	 * <h1>getTitle</h1>
	 * <br>
	 * @param text
	 * @return
	 */
	private static String getTitle(String text) {
		int indexn = text.indexOf("name\">");
		int indexe = text.indexOf("</h1>");
		return text.substring(indexn+6,indexe);
	}
	
	
	/**
	 * IDリストのファイルの場所
	 */
	public static final String IDLIST_FILE = "config/IDlist.ini";
	
	/**
	 * コンフォグファイルのディレクトリ
	 */
	public static final String CONFIGDIR = "config/";
	
	/**
	 * 一時ファイル保管場所
	 */
	public static final String TEMPDIT = ".TEMP/";
	
	/**
	 * ログフォルダー
	 */
	public static final String LOGDOR = "Logs/";
	
	/**
	 * ここから先はURLというマーク
	 */
	public static final String URLMARK = "://";
	
	/**
	 * 解析の目印
	 */
	public static final String HTMLMARK = "<section class=\"m-thumb-episode\">";
	
}
