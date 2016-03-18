/*
 * 
 */
package client.System.Registry;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import client.System.Client;
import client.System.SystemRegistry;

/** <h1>Story</h1>
 * ストーリーのプロパティ<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Story extends Properties{
	
	/**
	 * 読み込んでいるファイル
	 */
	private String loadFilePath = null;
	
	/**
	 * <h1>load</h1>
	 * リストを読み込みます<br>
	 * @param title
	 */
	public synchronized void load(String title) {
		this.loadFilePath = SystemRegistry.Config().getProperty(Config.CONFIGPATH)+title+".ini";
		try {
			InputStreamReader input = new InputStreamReader(new FileInputStream(this.loadFilePath),"utf-8");
			super.load(input);
			input.close();
		} catch (IOException e) {
			Client.Exception(e);
		}
	}
	
	/**
	 * <h1>save</h1>
	 * 現在読み込んでるファイルをセーブします<br>
	 */
	public synchronized void save() {
		if(loadFilePath == null) return;
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(loadFilePath), "utf-8");
			this.store(out, "");
			out.close();
		}catch (IOException e) {
			Client.Exception(e);
		}
	}
	
}
