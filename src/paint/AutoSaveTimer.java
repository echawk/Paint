/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;

/**
 *
 * @author ethan
 */
public class AutoSaveTimer extends Timer {
	
	public int time;
	public int autosavemultiple = 20000;
	
	public AutoSaveTimer() {
		
		this.scheduleAtFixedRate(
			new TimerTask() {
				public void run() {
					if (Paint.getCurrentTab().opened_image != null) {
						if (!Paint.getCurrentTab().imgHasBeenSaved) {
							try {
							CustomFileHandler.saveFile(); //gives me an error
							} catch (Exception ex) {
								System.out.println("Error with 'saveFile()':" + ex);
							}
						}
					}
	
				}
			}, 
			this.autosavemultiple,
			this.autosavemultiple);
		
	}
	
	/*
	protected Object call() throws Exception {
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	try {
			while (true) {
				TimeUnit.SECONDS.sleep(1);
				this.time++;
				//if 5 minutes have passed
				if (this.time % this.autosavemultiple == 0) {
					System.out.println("Attempting Autosave...");
					//if the current tab's image hasn't been saved
					if (Paint.getCurrentTab().opened_image != null) {
						if (!Paint.getCurrentTab().imgHasBeenSaved) {
							try {
							CustomFileHandler.saveFile(); //gives me an error
							} catch (Exception ex) {
								System.out.println("Error with 'saveFile()':" + ex);
							}
						}
					}
				}
				System.out.println("Current Time:" + this.time);
			}	
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
	*/
}
