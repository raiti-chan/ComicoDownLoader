/*
 * 
 */
package client.System.Registry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import raiti.RaitisAPI.io.File;

import client.System.Client;
import client.System.SystemRegistry;

/** <h1>Config</h1>
 * メインコンフィグファイル<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Config extends Properties{
	
	/**
	 * 起動時ファイルが存在していなかった場合true
	 */
	private boolean isNew;
	
	
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 * コンフィグの読み込み
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public Config() throws IOException {
		isNew = !Client.FileCheck(new File(SystemRegistry.config_ini), true, true);
		InputStreamReader input  = new InputStreamReader(new FileInputStream(SystemRegistry.config_ini),"utf-8");
		this.load(input);
		input.close();
		if(isNew == true) {
			this.setProperty(CONFIGPATH, "Config/");
			this.setProperty(MAINDIRPATH, "Comico/");
			this.setProperty(TEMPDIRPATH, ".TEMP");
			this.setProperty(MAXLOGS, "10");
			save();
		}
	}
	
	/**
	 * <h1>save</h1>
	 * コンフィグデータを保存します<br>
	 * @throws IOException
	 */
	public void save() throws IOException {
		OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(SystemRegistry.config_ini), "UTF-8");
		this.store(output, "");
		output.flush();
		output.close();
	}
	
	/**
	 * コンフィグフォルダーへのパス
	 */
	public static final String CONFIGPATH = "ConfigPath";
	
	/**
	 * メインフォルダーへのパス
	 */
	public static final String MAINDIRPATH = "MainDirectoryPath";
	
	/**
	 * TEMPフォルダーへのパス
	 */
	public static final String TEMPDIRPATH = "TEMPDirectoryPath";
	
	/**
	 * ログフォルダーへのパス
	 */
	public static final String LOGDIRPATH = "Logs/";
	
	/**
	 * ログファイルの最大数
	 */
	public static final String MAXLOGS = "MaxLogs";
}
