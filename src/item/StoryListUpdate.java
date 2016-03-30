/*
 * 
 */
package item;

/** <h1>StoryListUpdate</h1>
 * ストーリーリストの更新<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class StoryListUpdate extends EventItem{
	
	/**
	 * ID
	 */
	private String id;
	
	/**<B>コンストラクター</B><br>
	 * 
	 */
	public StoryListUpdate(String id) {
		this.id = id;
	}
	

	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		
	}
	
	/**<h1>toString</h1>
	 * オーバーライド
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ストーリーリスト更新 id:"+id;
	}
	
}
