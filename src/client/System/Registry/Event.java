/*
 * 
 */
package client.System.Registry;

import java.util.ArrayList;

import item.EventItem;

/** <h1>Event</h1>
 * 処理リスト<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Event {
	
	private ArrayList<EventItem> list;
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 *
	 */
	public Event() {
		list = new ArrayList<>();
	}
	
	/**
	 * <h1>run</h1>
	 * イベントを起動します<br>
	 */
	public void run() {
		list.get(0).run();
		list.remove(0);
	}
	
	
	/** <h1>getList</h1>
	 * {@link Event#list}の取得<br>
	 * @return list
	 */
	public synchronized ArrayList<EventItem> getList() {
		return list;
	}
}
