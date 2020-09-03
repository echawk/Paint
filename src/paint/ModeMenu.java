/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author ethan
 */
public class ModeMenu extends Menu {
	
	
	private CheckMenuItem editm;
	private CheckMenuItem defaultm;
	private int mode; //might be wotth looking to see if it can be in the main file
	
	public ModeMenu() {
		
		super();
		
		this.editm = new CheckMenuItem("Edit");
		
			this.editm.setOnAction((ActionEvent event) -> {
				System.out.println("Clicked Edit");
				System.out.println("Status: " + editm.isSelected());
				this.setMode(1);
				Popup.launchEditOptionsWindow();
			});
			
		this.defaultm = new CheckMenuItem("Default");
			
			this.defaultm.setOnAction((ActionEvent event) -> {
				System.out.println("Clicked Default");
				System.out.println("Status: " + defaultm.isSelected());
				this.setMode(0);
			});
			
		this.getItems().addAll(defaultm, editm);
		//this.setText("Mode");
		//this.setMode(0);
	
	}
	
	
	private void update() {
		if (this.mode == 1) {
			//The other modes
			this.defaultm.setSelected(false);
			//The mode
			this.editm.setSelected(true);
		} else {
			//The other modes
			this.editm.setSelected(false);
			//the mode
			this.defaultm.setSelected(true);
		}
	}
	
	
	public void setMode(int i) {
		this.mode = i;
		this.update();
		Paint.setMode(i);
	}
	
	public int getMode(){ 
		return this.mode;
	}
	
}
