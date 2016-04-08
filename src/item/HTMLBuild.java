/*
 * 
 */
package item;

import client.gui.Item;

/** <h1>HTMLBuild</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class HTMLBuild extends EventItem{
	
	private StoryItem si;
	
	private Item ci;
	
	//-------------------------------------コンストラクター
	public HTMLBuild(StoryItem si,Item ci) {
		this.si = si;
		this.ci = ci;
	}

	
	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		
		
	}
}
