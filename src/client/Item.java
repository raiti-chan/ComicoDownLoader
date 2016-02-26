/*
 * 
 */
package client;

/** <h1>Item</h1>
 * <br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class Item {
	
	/**
	 * タイトル
	 */
	private String name;
	
	/**
	 * 作品id
	 */
	private String id;
	
	/**
	 * このアイテムは追加要素か
	 */
	private boolean isAdd;
	
	//-------------------------------------コンストラクター
	/**
	 * <B>コンストラクター</B><br>
	 * @param name 名前
	 */
	public Item(String name,String id) {
		this.name = name;
		this.id = id;
		this.isAdd = false;
	}
	
	/**
	 * <B>コンストラクター</B><br>
	 * @param name 名前
	 */
	public Item(String name) {
		this.name = name;
		this.isAdd = false;
	}
	
	/**
	 * <B>コンストラクター</B><br>
	 */
	public Item() {
	}
	
	/**
	 * <B>コンストラクター</B><br>
	 * @param name 名前
	 * @param isAdd この要素が追加要素か
	 */
	public Item(String name,boolean isAdd) {
		this.name = name;
		this.isAdd = isAdd;
	}

	/** <h1>getName</h1>
	 * {@link Item#name}の取得<br>
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/** <h1>setName</h1>
	 * {@link Item#name}をセットします<br>
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** <h1>getUrl</h1>
	 * {@link Item#id}の取得<br>
	 * @return id
	 */
	public String getId() {
		return this.id;
	}

	/** <h1>setUrl</h1>
	 * {@link Item#id}をセットします<br>
	 * @param id セットする id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/** <h1>isAdd</h1>
	 * {@link Item#isAdd}の取得<br>
	 * @return isAdd
	 */
	public boolean isAdd() {
		return this.isAdd;
	}

	/** <h1>setAdd</h1>
	 * {@link Item#isAdd}をセットします<br>
	 * @param isAdd セットする isAdd
	 */
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	
	/**<h1>toString</h1>
	 * オーバーライド
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	
}
