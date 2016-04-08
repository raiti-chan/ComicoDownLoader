/*
 * 
 */
package item;

import raiti.RaitisAPI.io.File;

import client.System.Client;
import client.System.SystemRegistry;
import client.System.Registry.Config;
import client.gui.Item;

/** <h1>Download</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Download extends EventItem{
	
	private StoryItem si;
	
	private Item ci;
	
	//-------------------------------------コンストラクター
	public Download(StoryItem si,Item ci) {
		this.si = si;
		this.ci = ci;
	}

	/**<h1>code</h1>
	 * オーバーライド
	 * @see item.EventItem#code()
	 */
	@Override
	public void code() {
		File dir = new File(SystemRegistry.Config().getProperty(Config.MAINDIRPATH)+ci.getTitle()+"/"+si.getIndex());
		Client.FileCheck(dir, false, true);
		//------------------------------------------------------サムネのダウンロード
	}
}
