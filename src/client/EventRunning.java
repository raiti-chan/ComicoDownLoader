/*
 * 
 */
package client;


import client.System.SystemRegistry;

/** <h1>EventRunning</h1>
 * キューに登録されたイベントの処理<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class EventRunning implements Runnable{
	
	private boolean end = true;
	
	/**
	 * <h1>run</h1>
	 * オーバーライド
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		do {
			mainLoop();
		} while (end);
	}
	
	/**
	 * <h1>end</h1>
	 * スレッドを終了させます<br>
	 * @return 終了できた場合true
	 */
	public synchronized boolean end() {
		if(SystemRegistry.Event().getList().size() == 0) {
			end = false;
			return true;
		}else {
			return false;
		}

	}
	
	/**
	 * <h1>mainLoop</h1>
	 * メインループ<br>
	 */
	public synchronized void mainLoop() {
		if(SystemRegistry.Event().getList().size() != 0){
			SystemRegistry.Event().run();
		}
		
	}
}
