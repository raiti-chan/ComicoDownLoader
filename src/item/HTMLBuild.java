/*
 * 
 */
package item;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.io.FileChecker;
import raiti.RaitisAPI.io.html.HTMLParser;
import raiti.RaitisAPI.io.html.Tag;

import client.System.Client;
import client.System.SystemRegistry;
import client.System.Registry.Config;
import client.gui.Item;

/** <h1>HTMLBuild</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class HTMLBuild extends EventItem{
	
	private StoryItem si;
	
	private Item ci;
	
	//-------------------------------------コンストラクター
	public HTMLBuild(StoryItem si,Item ci) {
		this.si = si;
		this.ci = ci;
	}

	
	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		System.out.println("HTMLBuild! Build -> "+ci.getTitle()+" "+si.getIndex());
		
		File dir = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+ci.getTitle()+"/"+si.getIndex());
		if(!Client.FileCheck(dir, false, false)) {
			System.out.println("ダウンロードしたファイルが見つかりません");
			Client.alert("ダウンロードしたファイルが見つかりません", "Downloadボタンで再試行してください");
			return;
		};
		String htmlURL = dir.getPath()+"/"+si.getTitle()+".html";
		try(FileOutputStream out = new FileOutputStream(htmlURL);
				OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8") {
			
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
			write(writer,dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <h1>write</h1>
	 * <br>
	 * @param write
	 * @throws IOException 
	 */
	private void write(OutputStreamWriter write,File dir) throws IOException {
		System.out.println("HTMLBuilding");
		System.out.println("Write-----------------------------------\r\n");
		write.write("<html>");
		write.write("<meta charset=\"utf-8\">\r\n\r\n");
		
		write.write("<head>\r\n");
		write.write("<link rel=\"stylesheet\" href=\"../../MainCSS.css\" type=\"text/css\">\r\n");
		write.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
		write.write("<title >"+si.getTitle()+"</title>\r\n");
		write.write("</head>\r\n\r\n");
		
		write.write("<body>\r\n");
		write.write("<div class=\"heder\"  style=\"background-image:url('../"+ci.getTitle()+".jpg')\">\r\n");
		write.write("<div class=\"title\">\r\n");
		write.write(ci.getTitle()+"\r\n");
		write.write("</div>\r\n</div>\r\n");
		
		menuWrite(write);
		
		//------------------------------------------------------イメージ部分
		FileChecker imgfc = new FileChecker(dir);
		write.write("<div class=\"main\">\r\n");
		String[] names = imgfc.ReverseCheckAll("jpg");
		for(String jpg:names) {
			if(!jpg.equals(ci.getTitle()+si.getIndex()+".jpg")) {
				write.write("<img src=\""+jpg+"\"><br>\r\n");
			}
		}
		write.write("</div>\r\n");
		
		menuWrite(write);
		
		write.write("</html>\r\n");
		write.flush();
		write.close();
		System.out.println("-----------------------------------------\r\n");
		System.out.println("End");
		
	}
	
	private void menuWrite(OutputStreamWriter write) throws IOException {
		write.write("<div class=\"menu\">\r\n");
		if(si.getIndex() > 1) {
			File file = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+ci.getTitle()+"\\"+(si.getIndex()-1));
			System.out.println(file.getPath());
			if(file.exists()) {
				FileChecker che = new FileChecker(file);
				String filename = che.ReverseCheck("html");
				if(filename != null) {
					String titlename  = titleget(new File(file.getPath()+"\\"+filename));
					write.write("<a href=\"../"+(si.getIndex()-1)+"/"+filename+"\"title=\"\">"+"前へ:"+titlename+"</a>\r\n");
				}
			}

		}
		write.write("<a href=\"../"+ci.getTitle()+".html\" title=\"戻る\">戻る</a>\r\n");
		
		String nextdir = SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+ci.getTitle()+"\\"+(si.getIndex()+1);
		File nextdirfile = new File(nextdir);
		if(nextdirfile.exists()) {
			FileChecker che = new FileChecker(nextdirfile);
			String filename = che.ReverseCheck("html");
			if(filename != null) {
				String titlename = titleget(new File(nextdir+"\\"+filename));
				write.write("<a href=\"../"+(si.getIndex()+1)+"/"+filename+"\"title=\"\">"+"次へ:"+titlename+"</a>\r\n");
			}
		}
		nextdirfile = null;
		write.write("</div>\r\n");
	}
	
	/**<h1>toString</h1>
	 * オーバーライド
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HTMLBuild title="+ci.getTitle()+" episodetitle="+si.getTitle();
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
	
}
