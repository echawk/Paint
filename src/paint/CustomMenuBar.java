/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author ethan
 */
public class CustomMenuBar extends MenuBar {
	
	public CustomMenuBar() {
		super();
		
	//File menu code
		Menu filemenu = new CustomFileMenu();
		
		
	//mode menu (move to separate file (needs more complicated logic))
		ModeMenu modemenu = new ModeMenu();
			modemenu.setText("Mode");
			modemenu.setMode(0);
					
	//Help menu code
		Menu helpmenu = new Menu("Help");

		MenuItem about = new MenuItem("About");
			about.setOnAction((ActionEvent event) -> {
				Popup.launchAboutWindow();
			});
			
		helpmenu.getItems().addAll(about);
		
	
	//View menu
		Menu viewmenu = new Menu("View");
		
		MenuItem zoomin = new MenuItem("Zoom In");
			zoomin.setOnAction((ActionEvent event) -> {
				Paint.imgcanvas.zoomIn();
			});
			zoomin.setAccelerator(new KeyCodeCombination(KeyCode.I, 
						KeyCombination.CONTROL_DOWN));

		
		MenuItem zoomout = new MenuItem("Zoom Out");
			zoomout.setOnAction((ActionEvent event) -> {
				Paint.imgcanvas.zoomOut();
			});
			zoomout.setAccelerator(new KeyCodeCombination(KeyCode.D, 
						KeyCombination.CONTROL_DOWN));
			
		MenuItem resetview = new MenuItem("Reset");
			resetview.setOnAction((ActionEvent event) -> {
				Paint.imgcanvas.updateDimensions();
			});
			
		viewmenu.getItems().addAll(zoomin, zoomout, resetview);
	//Add all of the menus to the MenuBar
		this.getMenus().addAll(filemenu, viewmenu, modemenu, helpmenu);
		
	
	}
	
}
