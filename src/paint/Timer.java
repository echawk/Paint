/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

/**
 *
 * @author ethan
 */
public class Timer implements Runnable {
	
	public int time;
	public int autosavemultiple = 20;
	
	@Override
	public void run() {
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
	
}
