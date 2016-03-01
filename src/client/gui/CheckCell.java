/*
 * 
 */
package client.gui;

import javafx.scene.control.ListCell;

/** <h1>CheckCell</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class CheckCell extends ListCell<String>{
	/**<h1>updateItem</h1>
	 * オーバーライド
	 * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
	 */
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if(!empty) {
			setGraphic(MainController.getClient().checkMap.get(item));
			setText(item);
		}
	}
}
