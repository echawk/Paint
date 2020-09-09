/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 *
 * @author ethan
 */
public class CustomMenuBar extends MenuBar {
	//see if I can avoid these 
	private CheckMenuItem EraserBox; 
	private CheckMenuItem DrawLineBox;
	
	public CustomMenuBar() {
		super();
		
	//File menu code
		Menu filemenu = new CustomFileMenu();
		
		
	//mode menu (move to separate file (needs more complicated logic))
		ModeMenu modemenu = new ModeMenu();
		modemenu.setText("Mode");
		modemenu.setMode(0);
				
	//Edit menu code
		Menu editmenu = new Menu("Edit");
		//maybe make this menu only show up when in the 'edit' mode?
		
		//nested Draw menu code
			Menu editdraw = new Menu("Draw");
				//MenuItem editdrawline = new MenuItem("Line");
				DrawLineBox = new CheckMenuItem("Line");
				//make it a popup window with all of the controls for the 
				// line drawing (have a box for the width, color picker, etc, etc
				DrawLineBox.setOnAction((ActionEvent event) -> {
					if (DrawLineBox.isSelected()) {
						//Popup.launchEditOptionsWindow();
					}
				});

				MenuItem editdrawsquare = new MenuItem("Square");
				
				MenuItem editdrawcircle = new MenuItem("Circle");

				
			editdraw.getItems().addAll(DrawLineBox, editdrawsquare, editdrawcircle);
		
		EraserBox = new CheckMenuItem("Erase");
			//maybe have the same dialog as the draw line? it makes sense (maybe not the color picker though)
			//but the brush size definitely does.
			EraserBox.setOnAction((ActionEvent event) -> { 
				if (EraserBox.isSelected()) {
					//Popup.launchEditOptionsWindow();
				}
			});
		
		
		editmenu.getItems().addAll(editdraw, EraserBox);
		
		
	//Help menu code
		Menu helpmenu = new Menu("Help");

		MenuItem about = new MenuItem("About");
			about.setOnAction((ActionEvent event) -> {
				Popup.launchAboutWindow();
			});

		MenuItem drawoptions = new MenuItem("Show Draw Options");
			drawoptions.setOnAction((ActionEvent event) -> {
				Popup.launchEditOptionsWindow();
			});
			
		helpmenu.getItems().addAll(about, drawoptions);
		
		
		
		
	//Add all of the menus to the MenuBar
		this.getMenus().addAll(filemenu, modemenu, editmenu, helpmenu);
		
	
	}
	
	
	public boolean eraserSelected(){
		return EraserBox.isSelected();
	}
	
	public boolean drawLineSelected(){
		return DrawLineBox.isSelected();
	}
	
	
	
}
