/*
 * 
 */
package client;


import client.System.SystemRegistry;
import client.gui.MainController;

/** <h1>EventRunning</h1>
 * キューに登録されたイベントの処理<br>
 * @author Raiti
 * @version 1.0.0
 * 
 */
public class EventRunning implements Runnable{
	
	/**
	 * 終了フラグ
	 */
	private volatile boolean end = true;
	
	/**
	 * スレッドが停止している場合true
	 */
	private volatile boolean isSleep = false;
	
	
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
	public boolean end() {
		System.out.println("try...End");
		if(SystemRegistry.Event().getList().size() == 0) {
			end = false;
			MainController.client.eventRunningRestart();
			System.out.println("END!!");
			return true;
		}else {
			System.err.println("Failure!! -> RunningEvent is "+SystemRegistry.Event().getList().size()+" Event");
			return false;
		}

	}
	
	
	/** <h1>isSleep</h1>
	 * {@link EventRunning#isSleep}の取得<br>
	 * @return isSleep
	 */
	public boolean isSleep() {
		return this.isSleep;
	}

	/**
	 * <h1>mainLoop</h1>
	 * メインループ<br>
	 */
	private synchronized void mainLoop() {
		if(SystemRegistry.Event().getList().size() != 0){
			SystemRegistry.Event().run();
			SystemRegistry.Event().getList().remove(0);
			MainController.client.ListUP();
		}else {
			try {
				this.isSleep = true;
				this.wait();
			} catch (InterruptedException e) {
				this.isSleep = false;
			}
		}
		
	}
}
