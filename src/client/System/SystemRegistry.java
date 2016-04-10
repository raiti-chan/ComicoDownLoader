/*
 * 
 */
package client.System;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import client.System.Registry.ComicTitle;
import client.System.Registry.Config;
import client.System.Registry.Event;
import client.System.Registry.Story;
import item.StoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** <h1>SystemRegistry</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class SystemRegistry {
	
	/**
	 * コンフィグファイルの場所
	 */
	public static String config_ini = "Config/config.ini";
	
	/**
	 * コンフィグプロパティー
	 */
	private static  Config config;
	
	/**
	 * IDリストのプロパティー
	 */
	private static ComicTitle comictitle;
	
	/**
	 * ストーリーリストのプロパティー
	 */
	private static Story story;
	
	/**
	 * イベントのリスト
	 */
	private static Event event;
	
	
	/**
	 * ストーリーリスト
	 */
	private static ObservableList<StoryItem> storyList;
	
	
	public static void RegistryInitialize() {
		try {
			config = new Config();
			comictitle = new ComicTitle();
			story = new Story();
			event = new Event();
			storyList = FXCollections.observableArrayList();
		} catch (IOException e) {
			Client.Exception(e);
		}
	}
	
	/**
	 * <h1>Config</h1>
	 * コンフィグプロパティー<br>
	 * @return コンフィグプロパティー
	 */
	public synchronized static Config Config() {
		return config;
	}
	
	/**
	 * <h1>ComicTitle</h1>
	 * IDリストのプロパティー<br>
	 * @return IDプロパティー
	 */
	public synchronized static ComicTitle ComicTitle() {
		return comictitle;
	}
	
	/**
	 * <h1>Story</h1>
	 * ストーリーリストのプロパティー<br>
	 * @return ストーリープロパティー
	 */
	public synchronized static Story Story() {
		return story;
	}
	
	/**
	 * イベントリスト<h1>Event</h1>
	 * <br>
	 * @return
	 */
	public synchronized static Event Event() {
		return event;
	}
	
	/**
	 * <h1>StoryUpdate</h1>
	 * ストーリーリストを更新します<br>
	 */
	public static void StoryUpdate() {
		Story().getList(StoryList());
	}
	
	/**
	 * <h1>StorySort</h1>
	 * ストーリーリストをソートします<br>
	 * @param revase
	 */
	public synchronized static void StorySort(boolean revase) {
		StoryList().sort(new Comparator<StoryItem>() {
			@Override
			public int compare(StoryItem o1, StoryItem o2) {
				return o1.getIndex()-o2.getIndex();
			}
		});
		if(revase == false)Collections.reverse(storyList);
	}
	
	/**
	 * <h1>StoryList</h1>
	 * ストーリーリスト<br>
	 * @return
	 */
	public synchronized static ObservableList<StoryItem> StoryList(){
		return storyList;
	}
	
	/**
	 * Comicoの漫画のトップページURL 後ろに作品ナンバーを付けて使用
	 */
	public static final String MAINPAGEURL = "http://www.comico.jp/articleList.nhn?titleNo=";

	/**
	 * 漫画ページのURL 後ろに作品ナンバーとストーリーインデックスを付けます。
	 */
	public static final String COMICPAGEURL = "http://www.comico.jp/detail.nhn?titleNo=";

	/**
	 * ストーリーインデックスのベースです。後ろにインデックスを付与してください
	 */
	public static final String STORYINDEXBASE = "&articleNo=";

	/**
	 * ページインデックスタグ
	 */
	public static final String PAGEINDEXTAG ="&page=";

	/**
	 * ヘッダー画像のURL
	 */
	public static final String HEADERURL ="http://images.comico.jp/up/r01/";

	/**
	 * ヘッダー画像のURL2
	 */
	public static final String HEADERURL2 = "_kv1600x430.jpg";
	
}
