/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author ethan
 */
public class ModeMenu extends Menu {
	
	
	private CheckMenuItem editm;
	private CheckMenuItem defaultm;
	private int mode;
	
	public ModeMenu() {
		
		super();
		
		this.editm = new CheckMenuItem("Edit");
		
			this.editm.setOnAction((ActionEvent event) -> {
				this.setMode(Paint.EDIT_MODE);
				Paint.update();
			});
			editm.setAccelerator(new KeyCodeCombination(KeyCode.E, 
					KeyCombination.CONTROL_DOWN));
			
		this.defaultm = new CheckMenuItem("Default");
			
			this.defaultm.setOnAction((ActionEvent event) -> {
				this.setMode(Paint.DEFAULT_MODE);
				Paint.update();
			});
			defaultm.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE));
			
		this.getItems().addAll(defaultm, editm);	
	}
	
	/**
	 * This method is responsible for updating the Menu, and making sure that 
	 * only one Mode is selected at at time.
	 * 
	 */
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
	
	/**
	 * This method sets the mode for Paint (and the menu)
	 * 
	 * @param i Integer Representing Which mode to switch to.
	 */
	public void setMode(int i) {
		this.mode = i;
		this.update();
		Paint.setMode(i);
	}
	/**
	 * 
	 * 
	 * @return The Currently selected Mode of the Menu. Should be the same as Paint.getMode()
	 */
	public int getMode(){ 
		return this.mode;
	}
	
}
