/*
 * 
 */
package client.System.Registry;


import item.EventItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** <h1>Event</h1>
 * 処理リスト<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Event {
	
	private ObservableList<EventItem> list;
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 *
	 */
	public Event() {
		list = FXCollections.observableArrayList();
	}
	
	/**
	 * <h1>run</h1>
	 * イベントを起動します<br>
	 */
	public synchronized void run() {
		list.get(0).run();
		list.get(0).setFinish(true);
	}
	
	/**
	 * <h1>addEvent</h1>
	 * イベントの追加<br>
	 * @param event
	 */
	public synchronized void addEvent(EventItem event) {
		System.out.println("AddEvent..."+event.toString());
		list.add(event);

	}
	
	/** <h1>getList</h1>
	 * {@link Event#list}の取得<br>
	 * @return list
	 */
	public synchronized ObservableList<EventItem> getList() {
		return list;
	}
}
