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

import raiti.RaitisAPI.io.File;

import client.System.Client;
import client.System.SystemRegistry;
import client.gui.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** <h1>ComicTitle</h1>
 * 漫画タイトルのレジストリです。
 * ここに各漫画の名前、IDを登録、保存します。<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class ComicTitle extends Properties{
	
	
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 * レジストリーを作成します。
	 * @throws IOException 
	 */
	public ComicTitle() throws IOException {
		Client.FileCheck(new File(SystemRegistry.Config().getProperty(Config.CONFIGPATH)+"IDlist.ini"), true, true);
		InputStreamReader input  = new InputStreamReader(new FileInputStream(SystemRegistry.Config().getProperty(Config.CONFIGPATH)+"IDlist.ini"),"utf-8");
		this.load(input);
		input.close();
	}
	
	/**<h1>setProperty</h1>
	 * オーバーライド
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized Object setProperty(String key, String value) {
		Object old = super.setProperty(key, value);
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(SystemRegistry.Config().getProperty(Config.CONFIGPATH)+"IDlist.ini"), "utf-8");
			this.store(out, "");
			out.close();
		} catch (IOException e) {
			Client.Exception(e);
		}
		
		return old;
	}
	
	/**
	 * <h1>getList</h1>
	 * アイテムリストを返します<br>
	 * @return
	 */
	public synchronized ObservableList<Item> getList(){
		ObservableList<Item> item = FXCollections.observableArrayList();
		this.forEach((name,id) -> {
			item.add(new Item((String)name, (String)id));
		});
		item.add(new Item());
		return item;
	}
	
}
