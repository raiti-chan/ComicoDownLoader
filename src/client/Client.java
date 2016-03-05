/*
 * 
 */
package client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import raiti.RaitisAPI.arrays.sort.OrderDescending;
import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.io.FileChecker;
import raiti.RaitisAPI.io.html.HTMLParser;
import raiti.RaitisAPI.io.html.Tag;
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
	 * タイトルのリスト
	 */
	public List<String> titlelist;
	
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
		this.titlelist = new ArrayList<String>();
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
		
		//------------------------------------------------------MainCSS.css
		System.out.println("Check ->"+"MainCSS.css");
		File css = new File("MainCSS.css");
		if(css.isDirectory()) {
			System.out.println("isDirectory..."+"MainCSS.css");
			css.delete();
		}
		if(!css.exists()) {
			System.out.println("FALSE!! Copy..."+"MainCSS.css");
			System.out.println("Copy -> "+getClass().getClassLoader().getResource("Resource/MainCSS.txt").getPath()+" to "+"MainCSS.css");
			try {
				InputStream input = getClass().getClassLoader().getResourceAsStream("Resource/MainCSS.txt");
				FileOutputStream output = new FileOutputStream("MainCSS.css");
				byte[] buffer = new byte[2480];
				int length;
				do {
					length = input.read(buffer);
					output.write(buffer, 0, length);
				} while (length == -1);
				output.flush();
				output.close();
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		this.titlelist.clear();//タイトルリストの消去
		Properties cpro = new Properties();//読み込むプロパティーの取得。
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(CONFIGDIR+item.getName()+".ini"),"UTF-8")){
			cpro.load(reader);//読み込み
			reader.close();//リーダーを閉じる
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		List<Integer> keyList = new ArrayList<Integer>();//キーリストの作成
		cpro.forEach((o1,o2) -> {
			keyList.add(Integer.valueOf((String) o1));//キーリストの取得
		});
		keyList.sort(new OrderDescending());//大きい順にソート。
		Collections.reverse(keyList);
		
		keyList.forEach(o -> {//チェックボックスの作成
			String v = cpro.getProperty(o.toString());
			CheckBox cb = new CheckBox(o+":");
			System.out.println("Load ->"+o+"="+v);
			String name = v.substring(0,v.indexOf(URLMARK));
			titlelist.add(name);
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
		String htmlUrl = Main.MAINPAGEURL + item.getId();//HTMLへのURL
		//String htmlUrl = "file:///E:/Program/Java/Project/ComicoDownloader/TestHTML/tempHTML.txt";
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
					String url = Main.MAINPAGEURL + item.getId() + Main.PAGEINDEXTAG + file;//ページのURL
					//String url = "file:///E:/Program/Java/Project/ComicoDownloader/TestHTML/tempHTML_"+file+".txt";
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
	
	/** 漫画のダウンロード
	 * <h1>MainDownload</h1>
	 * <br>
	 */
	public void MainDownload(Item item) {
		Properties comicpro = new Properties();//漫画のプロパティーの読み込み
		try(InputStreamReader input = new InputStreamReader(new FileInputStream(CONFIGDIR+item.getName()+".ini"), "UTF-8");){
			comicpro.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Integer> indexlist = new ArrayList<Integer>(); //ダウンロードする漫画のナンバーリスト
		checkMap.forEach((key,o)->{
			if(o.isSelected()) {
				String text = o.getText().substring(0,o.getText().length()-1);//ナンバーの取得
				indexlist.add(Integer.valueOf(text));//リストに追加
			}
		});
		indexlist.sort(new OrderDescending());//小さい順にソート
		//------------------------------------------------------漫画フォルダが存在しているかのチェック
		File comicFile = new File(item.getName());
		if(comicFile.isFile()) {
			System.out.println("isFile...delete");
			comicFile.delete();
		}
		if(!comicFile.exists()) {
			System.out.println("Maike ->"+item.getName()+"/");
			comicFile.mkdirs();
		}
		comicFile = null;
		
		//------------------------------------------------------ダウンロードなど
		indexlist.forEach(i -> {
			String pro = comicpro.getProperty(Integer.toString(i));
			String title = pro.substring(0,pro.indexOf(URLMARK));
			String imgUrl = pro.substring(pro.indexOf(URLMARK)+3,pro.length());
			download(i, title, imgUrl,item.getName(),item.getId());//ダウンロード
			HTMLBuild(i, title, item.getName(), item.getId());//HTMLの生成
		});
	}
	
	/** ダウンロード
	 * <h1>download</h1>
	 * <br>
	 * @param namber 漫画ナンバー
	 * @param title 漫画タイトル
	 */
	@SuppressWarnings("static-method")
	private void download(int namber,String title,String imgUrl,String comicname,String id) {
		System.out.println("Build -> "+namber+":"+title+" @img:"+imgUrl);
		//------------------------------------------------------フォルダチェック
		File dir = new File(comicname+"/"+Integer.toString(namber));
		if(dir.isFile()) {
			System.out.println("isFile...Delete");
			dir.delete();
		}
		if(!dir.exists()) {
			System.out.println("FALSE! MakeFile "+comicname+"/"+namber +"/");
			dir.mkdir();
		}
		String dirURL = dir.getPath()+"/";
		try {//サムネのDL
			DownLoader dl = new DownLoader(imgUrl,true);
			dl.Connecting();
			dl.download(dirURL+comicname+namber+".jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		File html = null;
		try {//漫画のHTMLをダウンロード
			DownLoader dl = new DownLoader(Main.COMICPAGEURL+id+Main.STORYINDEXBASE+namber,true);
			dl.Connecting();
			html = new File(dl.download(TEMPDIT+"dlHTML.txt").getPath());
		} catch(IOException e) {
			e.printStackTrace();
		}
		//------------------------------------------------------漫画ページの解析
		int index = 0;//イメージ数初期値1
		List<String> urlList = null;
		try(InputStreamReader input = new InputStreamReader(new FileInputStream(html), "UTF-8");
				BufferedReader reader = new BufferedReader(input)){
			String text = null;
			while ((text=reader.readLine())!=null) {//イメージの始まりのタグの検索
				if(text.indexOf(COMICTOP)!=-1) break;
			}
			urlList = new ArrayList<String>();
			while ((text = reader.readLine())!=null) {//イメージURLの解析
				if(text.indexOf("img") == -1)break;
				int start = text.indexOf("=\"")+2;//urlの始まりのインデックス
				String url = text.substring(start,text.indexOf("\"",start));//タグからURLの取得
				urlList.add(url);
				index++;
			}
			System.out.println("Index of "+index);//log
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//------------------------------------------------------イメージのダウンロード
		for(int i = 1;i <= index;i++) {
			System.out.println(urlList.get(i-1));
			try {
				DownLoader dl = new DownLoader(urlList.get(i-1), true);
				dl.Connecting();
				dl.download(dirURL+comicname+indexmaker(namber)+indexmaker(i-1)+".jpg");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/** ボタンから呼び出された場合のビルダーの起動
	 * <h1>ButtonHTMLBuild</h1>
	 * <br>
	 * @param item
	 */
	public void ButtonHTMLBuild(Item item) {
		Properties comicpro = new Properties();//漫画のプロパティーの読み込み
		try(InputStreamReader input = new InputStreamReader(new FileInputStream(CONFIGDIR+item.getName()+".ini"), "UTF-8");){
			comicpro.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Integer> indexlist = new ArrayList<Integer>(); //ビルドする漫画のナンバーリスト
		checkMap.forEach((key,o)->{
			if(o.isSelected()) {
				String text = o.getText().substring(0,o.getText().length()-1);//ナンバーの取得
				indexlist.add(Integer.valueOf(text));//リストに追加
			}
		});
		indexlist.sort(new OrderDescending());//小さい順にソート
		//------------------------------------------------------漫画フォルダが存在しているかのチェック
		File comicFile = new File(item.getName());
		if(comicFile.isFile()) {
			System.out.println("isFile...delete");
			comicFile.delete();
		}
		if(!comicFile.exists()) {
			System.out.println("Maike ->"+item.getName()+"/");
			comicFile.mkdirs();
		}
		comicFile = null;
		
		//------------------------------------------------------ダウンロードなど
		indexlist.forEach(i -> {
			String pro = comicpro.getProperty(Integer.toString(i));
			String title = pro.substring(0,pro.indexOf(URLMARK));
			HTMLBuild(i, title, item.getName(), item.getId());//HTMLの生成
		});
	}
	
	/** 漫画HTMLの生成
	 * <h1>HTMLBuild</h1>
	 * <br>
	 * @param namber
	 * @param title
	 * @param comicname
	 * @param id
	 */
	public void HTMLBuild(int namber,String title,String comicname,String id) {
		System.out.println("HTMLBUILD!!");
		System.out.println("Build -> "+namber+":"+title);
		//------------------------------------------------------フォルダチェック
		File dir = new File(comicname+"/"+Integer.toString(namber));
		if(dir.isFile()) {
			System.out.println("isFile...Delete");
			dir.delete();
		}
		if(!dir.exists()) {
			System.out.println("FALSE!"+dir.getPath());
			System.out.println("漫画IMGが存在しません。Downloadで再試行して下さい");
			return;
		}
		String dirURL = dir.getPath()+"/";
		String htmlURL = dirURL+title+".html";
		try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(htmlURL), "UTF-8") {
			
			/**
			 * <h1>write</h1>
			 * オーバーライド
			 * @see java.io.Writer#write(java.lang.String)
			 */
			@Override
			public void write(String str) throws IOException {
				super.write(str);
				System.out.print(str);
			};
		}){
			write(writer,comicname,title,namber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** HTMLへの書き込み
	 * <h1>write</h1>
	 * <br>
	 * @param write
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	private void write(OutputStreamWriter write,String name,String title,int namber) throws IOException {
		System.out.println("HTMLBuilding");
		System.out.println("Write-----------------------------------\r\n");
		write.write("<html>");
		write.write("<meta charset=\"utf-8\">\r\n\r\n");
		
		write.write("<head>\r\n");
		write.write("<link rel=\"stylesheet\" href=\"../../MainCSS.css\" type=\"text/css\">\r\n");
		write.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
		write.write("<title >"+title+"</title>\r\n");
		write.write("</head>\r\n\r\n");
		
		write.write("<body>\r\n");
		write.write("<div class=\"heder\"  style=\"background-image:url('../"+name+".jpg')\">\r\n");
		write.write("<div class=\"title\">\r\n");
		write.write(name+"\r\n");
		write.write("</div>\r\n</div>\r\n");
		
		//------------------------------------------------------メニューの書き込み
		write.write("<div class=\"menu\">\r\n");
		if(namber > 1) {
			File file = new File(name+"\\"+(namber-1)+"\\");
			System.out.println(file.getPath());
			if(file.exists()) {
				FileChecker che = new FileChecker(file);
				String filename = che.ReverseCheck("l");
				String titlename  = titleget(new File(name+"\\"+(namber-1)+"\\"+filename));
				if(filename != null) {
					write.write("<a href=\"../"+(namber-1)+"/"+filename+"\"title=\"\">"+"前へ:"+titlename+"</a>\r\n");
				}
			}

		}
		write.write("<a href=\"../"+title+".html\" title=\"戻る\">戻る</a>\r\n");
		
		String nextdir = name+"\\"+(namber+1)+"\\";
		File nextdirfile = new File(nextdir);
		if(nextdirfile.exists()) {
			FileChecker che = new FileChecker(nextdirfile);
			String filename = che.ReverseCheck("l");
			String titlename = titleget(new File(nextdir+filename));
			if(filename != null) {
				write.write("<a href=\"../"+(namber+1)+"/"+filename+"\"title=\"\">"+"次へ:"+titlename+"</a>\r\n");
			}
		}
		nextdirfile = null;
		write.write("</div>\r\n");
		
		//------------------------------------------------------イメージ部分
		FileChecker imgfc = new FileChecker(new File(name+"\\"+namber+"\\"));
		write.write("<div class=\"main\">\r\n");
		String[] names = imgfc.ReverseCheckAll("jpg");
		for(String jpg:names) {
			if(!jpg.equals(name+namber+".jpg")) {
				write.write("<img src=\""+jpg+"\"><br>\r\n");
			}
		}
		write.write("</div>\r\n");
		
		//------------------------------------------------------メニューの書き込み
		write.write("<div class=\"menu\">\r\n");
		if(namber > 1) {
			File file = new File(name+"\\"+(namber-1)+"\\");
			System.out.println(file.getPath());
			if(file.exists()) {
				FileChecker che = new FileChecker(file);
				String filename = che.ReverseCheck("l");
				String titlename  = titleget(new File(name+"\\"+(namber-1)+"\\"+filename));
				if(filename != null) {
					write.write("<a href=\"../"+(namber-1)+"/"+filename+"\"title=\"\">"+"前へ:"+titlename+"</a>\r\n");
				}
			}

		}
		write.write("<a href=\"../"+title+".html\" title=\"戻る\">戻る</a>\r\n");
		
		nextdir = name+"\\"+(namber+1)+"\\";
		nextdirfile = new File(nextdir);
		if(nextdirfile.exists()) {
			FileChecker che = new FileChecker(nextdirfile);
			String filename = che.ReverseCheck("l");
			String titlename = titleget(new File(nextdir+filename));
			if(filename != null) {
				write.write("<a href=\"../"+(namber+1)+"/"+filename+"\"title=\"\">"+"次へ:"+titlename+"</a>\r\n");
			}
		}
		nextdirfile = null;
		write.write("</div>\r\n");
		
		write.write("</html>\r\n");
		write.flush();
		write.close();
		System.out.println("-----------------------------------------\r\n");
		System.out.println("End");
	}
	
	/** HTMLのタイトルの取得
	 * <h1>titleget</h1>
	 * <br>
	 * @param file
	 * @return
	 */
	private static String titleget(File file) {
		try {
			HTMLParser par = new HTMLParser(file);
			Tag tag;
			while (par.hasNext()) {
				tag = par.next();
				if(tag.getTagName().equals("title")) {
					return tag.getTagText();
				}
				
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** インデックス値を適切な形式に変換します。
	 * <h1>indexmaker</h1>
	 * <br>
	 * @param index
	 * @return
	 */
	private static String indexmaker(int index) {
		if(index<10) {
			return "_00"+index;
		}else if(index<100){
			return "_0"+index;
		}else {
			return "_"+index;
		}
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
	
	/**
	 * 漫画イメージの始点キーワード
	 */
	public static final String COMICTOP ="m-section-detail__body";
	
}
