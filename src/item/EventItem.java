/*
 * 
 */
package item;

/** <h1>EventItem</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class EventItem {
	
	private boolean isFinish = false;
	
	//-------------------------------------コンストラクター
	public EventItem() {
		//TODO 自動生成されたコンストラクター
	}
	
	/**
	 * <h1>run</h1>
	 * イベント処理<br>
	 */
	public void run() {
		if(isFinish == true) return;
		System.out.println("EventItem running");
		
	}

	/**
	 * isFinishを取得します。
	 * @return isFinish
	 */
	public boolean isFinish() {
	    return isFinish;
	}

	/**
	 * isFinishを設定します。
	 * @param isFinish isFinish
	 */
	public void setFinish(boolean isFinish) {
	    this.isFinish = isFinish;
	}
	
	
}
