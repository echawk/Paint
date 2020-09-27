/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author ethan
 */
public class Timer extends Thread {
	
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
					//if the current tab's image hasn't been saved
					if (!Paint.getCurrentTab().imgHasBeenSaved) {
						CustomFileHandler.saveFile();
					}
				}
				System.out.println("Current Time:" + this.time);
			}	
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
	
}
