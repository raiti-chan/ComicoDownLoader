/*
 * 
 */
package client.gui;

import item.StoryItem;
import javafx.scene.control.ListCell;

/** <h1>Checkcell</h1>
 * チェックボックス付きのセル<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Checkcell extends ListCell<StoryItem>{
	/**<h1>updateItem</h1>
	 * オーバーライド
	 * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
	 */
	@Override
	protected void updateItem(StoryItem item, boolean empty) {
		super.updateItem(item, empty);
		if(!empty) {
			setGraphic(item.getCb());
			setText(item.toString());
		}
	}
}
