/*
 * 
 */
package item;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import raiti.RaitisAPI.io.File;
import raiti.RaitisAPI.io.FileChecker;
import raiti.RaitisAPI.net.DownLoader;

import client.System.Client;
import client.System.SystemRegistry;
import client.System.Registry.Config;
import client.gui.Item;

/** <h1>TitlePageUp</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class TitlePageUp extends EventItem{
	
	private Item ci;
	
	//-------------------------------------コンストラクター
	public TitlePageUp(Item ci) {
		this.ci = ci;
	}

	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		System.out.println("TitlepageUp ->"+ci.getTitle());
		File dir = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+ci.getTitle());
		Client.FileCheck(dir, false, true);
		
		//------------------------------------------------------ヘッダー画像がない場合ダウンロード
		File ifile = new File(dir.getPath()+"\\"+ci.getTitle()+".jpg");
		if(ifile.isDirectory()) {
			ifile.delete();
		}
		if(!ifile.exists()) {
			System.out.println("HeaderDownload");
			try {
				DownLoader dl = new DownLoader(SystemRegistry.HEADERURL+ci.getId()+SystemRegistry.HEADERURL2, true);
				dl.Connecting();
				dl.download(ifile);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		//------------------------------------------------------書き込み
		try(FileOutputStream out = new FileOutputStream(dir.getPath()+"\\"+ci.getTitle()+".html");
				OutputStreamWriter write = new OutputStreamWriter(out, "UTF-8") {
			@Override
			public void write(String str) throws IOException {
				super.write(str);
				System.out.print(str);
			};
		}){
			
			write.write("<html>\r\n\r\n");
			write.write("<head>\r\n"
					+ "<link rel=\"stylesheet\" href=\"../MainCSS.css\" type=\"text/css\"> \r\n"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> \r\n"
					+ "</head>\r\n\r\n");
			
			write.write("<body>\r\n");
			write.write("<div class=\"heder\"  style=\"background-image:url('"+ci.getTitle()+".jpg')\">\r\n\r\n"
					+ "<div class=\"title\">\r\n"
					+ ci.getTitle()+"\r\n"
					+ "</div>\r\n"
					+ "</div>\r\n"
					+ "<br>\r\n\r\n");
			write.write("<div class=\"listbox\">\r\n");
			
			//------------------------------------------------------ディレクトリの解析
			String[] paths = dir.list();
			File[] files = new File[paths.length];
			for(int i = 0;i < paths.length;i++) {
				System.out.println(paths[i]);
				files[i] = new File(dir.getPath()+"\\"+paths[i]);
			}
			Arrays.sort(files,0,files.length,this::sort);
			
			for(File file:files) {
				String filename = getHTMLFILE(file);
				String title = file.getName()+"/"+filename;
				
				if(filename != null) {
					write.write("<a href=\""+title+"\">\r\n"
							+ "   <div class=\"list\">\r\n"
							+ "<div class=\"titleimg\" style=\"background-image:url("+file.getName()+"/"+ci.getTitle()+file.getName()+".jpg"+");\">\r\n"
							+ "</div>\r\n"
							+ "<h1 class=\"title_text\">\r\n"
							+ filename.substring(0, filename.length()-5)+"\r\n"
							+ "</h1>\r\n"
							+ "</div>\r\n\r\n");
				}
			}
			write.write("<div>\r\n");
			write.write("</body>\r\n");
			write.write("</html>");
			
			write.flush();
			write.close();
		}catch (IOException e) {
			Client.Exception(e);
		}
		
	}
	
	/**
	 * <h1>sort</h1>
	 * <br>
	 * @param f1
	 * @param f2
	 * @return
	 */
	private int sort(File f1,File f2) {
		if(f1.isFile()) {
			return 0;
		}
		if(f2.isFile()) {
			return 0;
		}
		int i1 = Integer.valueOf(f1.getName());
		int i2 = Integer.valueOf(f2.getName());
		if(i1 < i2) {
			return 1;
		}
		else if(i1 > i2){
			return -1;
		}
		return 0;
	}
	
	/** htmlファイルの取得
	 * <h1>getHTMLFILE</h1>
	 * <br>
	 * @param file
	 * @return
	 */
	private static String getHTMLFILE(File file) {
		if(!file.exists() || file.isFile()) {
			return null;
		}
		FileChecker fc = new FileChecker(file);
		String filename = fc.ReverseCheck("html");
		
		return filename;
	}
	
}
