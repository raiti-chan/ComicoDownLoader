/*
 * 
 */
package client.gui;

/** <h1>Item</h1>
 * 作品データアイテム<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Item {
	
	/**
	 * 作品名
	 */
	private String title;
	
	/**
	 * 作品id
	 */
	private String id;
	
	/**
	 * このアイテムが作品追加のトリガーアイテムか。これがtrueの場合作品追加ダイアログが開く
	 */
	private boolean isAddingItem;
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 * アイテムを新規作成します。<{@link Item#isAddingItem}はfalseです
	 * @param title 作品名
	 * @param id 作品id
	 */
	public Item(String title,String id) {
		this.title = title;
		this.id = id;
		this.isAddingItem = false;
	}
	
	/**
	 * <B>コンストラクター</B><br>
	 * 作品追加用アイテムを作成します。
	 */
	public Item() {
		this.title = "追加";
		this.id = "unknown";
		this.isAddingItem = true;
	}

	/**
	 * 作品名を取得します。
	 * @return 作品名
	 */
	public String getTitle() {
	    return title;
	}

	/**
	 * 作品名を設定します。
	 * @param title 作品名
	 */
	public void setTitle(String title) {
	    this.title = title;
	}

	/**
	 * 作品idを取得します。
	 * @return 作品id
	 */
	public String getId() {
	    return id;
	}

	/**
	 * 作品idを設定します。
	 * @param id 作品id
	 */
	public void setId(String id) {
	    this.id = id;
	}

	/**
	 * このアイテムが作品追加のトリガーアイテムか。これがtrueの場合作品追加ダイアログが開くを取得します。
	 * @return このアイテムが作品追加のトリガーアイテムか。これがtrueの場合作品追加ダイアログが開く
	 */
	public boolean isAddingItem() {
	    return isAddingItem;
	}

	/**
	 * このアイテムが作品追加のトリガーアイテムか。これがtrueの場合作品追加ダイアログが開くを設定します。
	 * @param isAddingItem このアイテムが作品追加のトリガーアイテムか。これがtrueの場合作品追加ダイアログが開く
	 */
	public void setAddingItem(boolean isAddingItem) {
	    this.isAddingItem = isAddingItem;
	}
}
