/*
 * 
 */
package item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import raiti.RaitisAPI.net.DownLoader;

import client.System.Client;
import client.System.SystemRegistry;
import client.System.Registry.Config;

/** <h1>StoryListUpdate</h1>
 * ストーリーリストの更新<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class StoryListUpdate extends EventItem{
	
	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 漫画タイトル
	 */
	private String title;
	
	/**
	 * すべてのリストを更新するか
	 */
	private boolean allUpdate;
	
	/**<B>コンストラクター</B><br>
	 * @param id リスト漫画
	 * @param allUpdate すべての項目を更新するか。trueの場合重くなります
	 */
	public StoryListUpdate(String id,String title,boolean allUpdate) {
		this.id = id;
		this.allUpdate = allUpdate;
		this.title = title;
	}
	

	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		String url = SystemRegistry.MAINPAGEURL + id;//漫画メインページのURL
		//url = "file:///D:/Raiti/Documents/Java/Project/ComicoDownloader/テスト/Data/MainHtml";//テスト用ローカルHTML
		File temp = null;//ダウンロードする一時ファイル
		try {
			DownLoader dl = new DownLoader(url,true);
			dl.Connecting();
			temp = dl.download(SystemRegistry.Config().getProperty(Config.TEMPDIRPATH)+"MainHtml_1");
		}catch (IOException e) {
			Client.Exception(e);
		}
		
		if(allUpdate == true) {
			truecode(url,temp);
		}else {
			falsecode(url,temp);
		}
		
	}
	
	/**
	 * <h1>truecode</h1>
	 * すべてのリスト更新がtrueの場合のコード<br>
	 */
	@SuppressWarnings("resource")
	private void truecode(String url,File temp) {
		try(InputStreamReader insr = new InputStreamReader(new FileInputStream(temp), "utf-8");//ファイル読み込みストリーム
				BufferedReader read = new BufferedReader(insr);//バッファー
				){
			Properties pro;//新規プロパティー
			InputStreamReader reader2 = null;//2ページ目以降用のリーダー
			String text;//読み込みライン文字列
			int line = 1;//現在の読み込みライン
			while ((text = read.readLine()) != null) {
				if(text.indexOf(HTMLMARK) != -1) break;//m-thumb-episodeのラインを検索
				read.mark(100);//m-thumb-episodeライン直前の一にマーカーをつける
				line++;//読み込みラインを追加
			}
			System.out.println("Out -> Line:"+line);//log
			text = read.readLine();//次の読み込みラインへ
			
			int indexofp = text.indexOf(STORYINDEXBASE);//Numberパラメータの始まり部(&articleNo=**)
			System.out.println("Out of Numberparameter -> col:"+indexofp);//log
			int indexofe = text.indexOf("\"", indexofp);//Numberパラメータより後にある"の位置
			System.out.println("Out of end -> col"+text.indexOf("\"", indexofe));//log
			int length = Integer.parseInt(text.substring(indexofp + STORYINDEXBASE.length() , indexofe));//最新話のインデックス
			System.out.println("NewestStoryNo = "+length);
			read.reset();//リーダーのリセット
			
			//------------------------------------------------------解析
			String imgurl=null,title=null;//イメージのurlとタイトル文字列
			int index = length,page = 1;//漫画のインデックスと、トップページのインデックス
			BufferedReader read2 = read;//リーダー
			pro = new Properties();
			
			while (index > 0) {
				int loop = 0;
				while ((text = read2.readLine()) != null) {
					if((text.indexOf(HTMLMARK)) != -1) {
						read2.readLine();//破棄
						read2.readLine();//破棄
						text = read2.readLine();//サムネのURLのあるライン
						
						int indexu = text.indexOf("url(");
						int indexe = text.indexOf(")",indexu);
						imgurl = text.substring(indexu+4, indexe);//サムネイルのURL
						
						text = read2.readLine();//タイトルのあるライン
						
						indexu = text.indexOf("name\">");
						indexe = text.indexOf("</h1>");
						title = text.substring(indexu+6,indexe);//タイトルの取得
						
						System.out.println(index+":"+title+URLMARK+imgurl);//log
						pro.setProperty(Integer.toString(index), title + URLMARK + imgurl);//プロパティーの追加
						index --;
						loop++;
					}
					if(loop == 10) break;
				}
				if(page != 1) {
					read2.close();
					reader2.close();
				}
				if(index <= 0) break;
				page++;
				//------------------------------------------------------次のページの読み込み
				String nexturl = url + SystemRegistry.PAGEINDEXTAG + page;//次ページのURL
				//nexturl = url +"_"+page;//テスト用ローカルHTML
				DownLoader dl = new DownLoader(nexturl, true);//ダウンローダーの作成
				dl.Connecting();//URLへ接続
				File temp2 = dl.download(SystemRegistry.Config().getProperty(Config.TEMPDIRPATH)+"MainHtml_"+page);
				reader2 = new InputStreamReader(new FileInputStream(temp2), "UTF-8");
				read2 = new BufferedReader(reader2);
				
				
			}
			OutputStreamWriter writ = new OutputStreamWriter(new FileOutputStream(SystemRegistry.Config().getProperty(Config.CONFIGPATH)+this.title+".ini"), "UTF-8");
			pro.store(writ, "");
			writ.close();
			insr.close();
			read.close();
			read2.close();
			reader2.close();
		}catch (IOException e) {
			Client.Exception(e);
		}
		
				
	}
	
	/**
	 * <h1>falsecode</h1>
	 * すべてのリスト更新がfalseの場合のコード<br>
	 */
	private void falsecode(String url,File temp) {
		
	}
	
	/**<h1>toString</h1>
	 * オーバーライド
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ストーリーリスト更新 "+"Title="+title+" id="+id;
	}
	
	
	/**
	 * 各エピソードのデータの最初にこのタグがある
	 */
	public static final String HTMLMARK = "<section class=\"m-thumb-episode\">";
	
	/**
	 * エピソードナンバーのしるし
	 */
	public static final String STORYINDEXBASE = "&articleNo=";
	
	/**
	 * ここから先はURLというマーク
	 */
	public static final String URLMARK = "://";
	
}
