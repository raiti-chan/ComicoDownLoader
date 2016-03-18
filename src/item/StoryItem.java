/*
 * 
 */
package item;

import javafx.scene.control.CheckBox;

/** <h1>StoryItem</h1>
 * ストーリーアイテム<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class StoryItem {
	
	/**
	 * タイトル
	 */
	private String title;
	
	/**
	 * サムネ画像URL
	 */
	private String imgURL;
	
	/**
	 * この章のインデックス
	 */
	private int index;
	
	/**
	 * リストに表示する際のチェックボックス
	 */
	private CheckBox cb;
	


	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 * @param index
	 * @param title
	 */
	public StoryItem(int index,String title) {
		this.index = index;
		this.title = title.substring(0,title.indexOf(URLMARK));
		this.imgURL = title.substring(title.indexOf(URLMARK)+3,title.length());
		this.cb = new CheckBox(index+":");
	}
	
	/** <h1>getTitle</h1>
	 * {@link StoryItem#title}の取得<br>
	 * @return title
	 */
	public synchronized String getTitle() {
		return this.title;
	}

	/** <h1>getImgURL</h1>
	 * {@link StoryItem#imgURL}の取得<br>
	 * @return imgURL
	 */
	public synchronized String getImgURL() {
		return this.imgURL;
	}

	/** <h1>getIndex</h1>
	 * {@link StoryItem#index}の取得<br>
	 * @return index
	 */
	public synchronized int getIndex() {
		return this.index;
	}
	
	/** <h1>getCb</h1>
	 * {@link StoryItem#cb}の取得<br>
	 * @return cb
	 */
	public synchronized CheckBox getCb() {
		return this.cb;
	}
	
	/**<h1>toString</h1>
	 * オーバーライド
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 	{
		return getTitle();
	}
	
	/**
	 * <h1>isSelect</h1>
	 * この要素が選択されているか<br>
	 * @return
	 */
	public boolean isSelect() {
		return getCb().isSelected();
	}
	
	/**
	 * ここから先はURLというマーク
	 */
	public static final String URLMARK = "://";
}
