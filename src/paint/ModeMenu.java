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
	
	public ModeMenu() {
		
		super();
		
		CheckMenuItem editm = new CheckMenuItem("Edit");
		
			editm.setOnAction((ActionEvent event) -> {
				this.setMode(1);
			});
			
		CheckMenuItem defaultm = new CheckMenuItem("Default");
			
			defaultm.setOnAction((ActionEvent event) -> {
				this.setMode(0);
			});
			
		this.getItems().addAll(defaultm, editm);
		this.setMode(0);
	
	}
	
	
	public void setMode(int i) {
		switch (i){
			case 1:
				this.editm.setSelected(true);
				this.defaultm.setSelected(false);
			default:
				this.editm.setSelected(false);
				this.defaultm.setSelected(true);
				break;
		}
	}
	
	public int getMode(){ 
		if (defaultm.isSelected()) {
			return 0;
		} else if (editm.isSelected()) {
			return 1;
		} else {
			return 0; //assume 0
		}
	}
	
}
