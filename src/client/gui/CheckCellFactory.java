/*
 * 
 */
package client.gui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/** <h1>CheckCellFactory</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class CheckCellFactory implements Callback<ListView<String>, ListCell<String>>{

	/**<h1>call</h1>
	 * オーバーライド
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public ListCell<String> call(ListView<String> param) {
		return new CheckCell();
	}
	
}
