/*
 * 
 */
package client.gui;

import item.StoryItem;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/** <h1>CheckCellFactory</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class CheckCellFactory implements Callback<ListView<StoryItem>, ListCell<StoryItem>>{

	/**<h1>call</h1>
	 * オーバーライド
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public ListCell<StoryItem> call(ListView<StoryItem> param) {
		return new Checkcell();
	}
	
}
