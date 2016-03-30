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
public abstract class EventItem {
	
	private boolean isFinish = false;
	
	//-------------------------------------コンストラクター
	public EventItem() {
		//TODO 自動生成されたコンストラクター
	}
	
	/**
	 * <h1>run</h1>
	 * イベント処理<br>
	 */
	public synchronized void run() {
		if(isFinish == true) return;
		code();
	}
	
	/**
	 * <h1>code</h1>
	 * 処理コード<br>
	 */
	public abstract void code();

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
