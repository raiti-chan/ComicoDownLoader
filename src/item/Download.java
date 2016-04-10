/*
 * 
 */
package item;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.net.DownLoader;

import client.System.Client;
import client.System.SystemRegistry;
import client.System.Registry.Config;
import client.gui.Item;

/** <h1>Download</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Download extends EventItem{
	
	private StoryItem si;
	
	private Item ci;
	
	//-------------------------------------コンストラクター
	public Download(StoryItem si,Item ci) {
		this.si = si;
		this.ci = ci;
	}

	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		File dir = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+ci.getTitle()+"/"+si.getIndex()+"/");
		Client.FileCheck(dir, false, true);
		//------------------------------------------------------サムネのダウンロード
		try {
			DownLoader dlimg = new DownLoader(si.getImgURL(),true);
			dlimg.Connecting();
			System.out.println(dir.getPath()+"\\"+ci.getTitle()+si.getIndex()+".jpg");
			dlimg.download(dir.getPath()+"\\"+ci.getTitle()+si.getIndex()+".jpg");
		}catch(IOException e) {
			Client.Exception(e);
			return;
		}
		//------------------------------------------------------漫画のimgURLの解析
		File html = null;//漫画HTMLファイル
		try {//漫画HTMLのダウンロード
			DownLoader dlhtml = new DownLoader(SystemRegistry.COMICPAGEURL+ci.getId()+SystemRegistry.STORYINDEXBASE+si.getIndex());
			dlhtml.Connecting();
			html = new File(dlhtml.download(SystemRegistry.Config().getProperty(Config.TEMPDIRPATH)+"ComicHtml").getPath());
		}catch(IOException e) {
			Client.Exception(e);
			return;
		}
		//------------------------------------------------------解析
		List<String> imgurls = new ArrayList<>();
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(html),"UTF-8");
				BufferedReader read = new BufferedReader(reader)){
			String text = null;
			while ((text=read.readLine())!=null) {//イメージの始まりのタグの検索
				if(text.indexOf(COMICTOP)!=-1) break;
			}
			while ((text = read.readLine())!=null) {//イメージURLの解析
				if(text.indexOf("img") == -1)break;
				int start = text.indexOf("=\"")+2;//urlの始まりのインデックス
				String url = text.substring(start,text.indexOf("\"",start));//タグからURLの取得
				imgurls.add(url);
			}
			System.out.println("Index of "+imgurls.size());//log
		}catch (IOException e) {
			Client.Exception(e);
			return;
		}
		
		//------------------------------------------------------イメージのダウンロード
		for(int i = 0 ; i < imgurls.size() ; i++) {
			System.out.println(imgurls.get(i));
			try {
				DownLoader dl = new DownLoader(imgurls.get(i), true);
				dl.Connecting();
				dl.download(dir.getPath()+"\\"+ci.getTitle()+indexmaker(si.getIndex())+indexmaker(i)+".jpg");
			} catch (IOException e) {
				Client.Exception(e);
			}
		}
		
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
	

	
	
	/**<h1>toString</h1>
	 * オーバーライド
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "イメージのダウンロード title="+ci.getTitle()+" episodetitle="+si.getTitle();
	}
	
	/**
	 * 漫画イメージの始点キーワード
	 */
	public static final String COMICTOP ="m-section-detail__body";
}
