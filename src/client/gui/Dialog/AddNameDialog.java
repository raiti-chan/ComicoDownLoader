/*
 * 
 */
package client.gui.Dialog;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/** <h1>AddNameDialog</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class AddNameDialog implements Initializable{
	
	/**
	 * URL入力フィールド
	 */
	@FXML
	private TextField url;
	
	/**
	 * URL先の名前フィールド
	 */
	@FXML
	private TextField name;
	
	/**
	 * 追加ボタン
	 */
	@FXML
	private Button button;
	

	/**<h1>initialize</h1>
	 * オーバーライド
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		button.setOnAction(e -> {
			this.button.getScene().getWindow().hide();
		});
		
	}
	
	/**
	 * <h1>getURL</h1>
	 * 入力されたURLを取得します<br>
	 * @return 入力されたURL
	 */
	public String getID() {
		return url.getText();
	}
	
	/**
	 * <h1>getName</h1>
	 * 入力されたタイトルを取得します<br>
	 * @return 入力されたタイトル
	 */
	public String getName() {
		return name.getText();
	}
	
	
}
