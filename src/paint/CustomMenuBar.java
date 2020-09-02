/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

/**
 *
 * @author ethan
 */
public class CustomMenuBar extends MenuBar {
		
	private CheckMenuItem EraserBox;
	private CheckMenuItem DrawLineBox;
	
	public CustomMenuBar() {
		
		super();
		
	//File menu code
		Menu filemenu = new Menu("File");
		MenuItem saveas = new MenuItem("Save As");
			saveas.setOnAction((ActionEvent event) -> {
				CustomFileHandler.saveAsFile(Paint.window);
			});
		
		MenuItem save = new MenuItem("Save");
			save.setOnAction((ActionEvent event) -> {
				CustomFileHandler.saveFile();
			});
		
		
		MenuItem open = new MenuItem("Open");
			open.setOnAction((ActionEvent event) -> {
				try {
					Paint.opened_file = CustomFileHandler.openFile(Paint.window);
					//convert the opened file to a file input stream, then to an image
					Paint.setImage(new Image(new FileInputStream(Paint.opened_file)));
					Paint.imgcanvas.updateDimensions(); //works!!	
				} catch (FileNotFoundException ex) {
					System.out.println("File was not found");
				}
			});

		MenuItem exit = new MenuItem("Exit");
			exit.setOnAction((ActionEvent event) -> {
				Paint.close();
			});

		MenuItem clear = new MenuItem("Clear");
			clear.setOnAction((ActionEvent event) -> {
				Paint.clearImage();
			});
			
		MenuItem newimage = new MenuItem("New");
			newimage.setOnAction((ActionEvent event) -> {
				//Show the create new Image dialog
				Popup.launchCreateNewImage();
			});
		
		
		//add all of the menu items to the file menu
		filemenu.getItems().addAll(newimage, open, save, saveas, clear, exit);
		
		
	//mode menu (move to separate file (needs more complicated logic))
		Menu modemenu = new Menu("Mode");
		
		CheckMenuItem edittoggle = new CheckMenuItem("Edit");
		
			edittoggle.setOnAction((ActionEvent event) -> {
				if (Paint.mode == 0) {
					Paint.setMode(1);
				}
			});
			
		MenuItem resetmode = new MenuItem("Default");
			
			resetmode.setOnAction((ActionEvent event) -> {
				Paint.setMode(0);
			});
			
		modemenu.getItems().addAll(edittoggle, resetmode);
			
		
	//Edit menu code
		Menu editmenu = new Menu("Edit");
		
		
		//nested Draw menu code
			Menu editdraw = new Menu("Draw");
				//MenuItem editdrawline = new MenuItem("Line");
				DrawLineBox = new CheckMenuItem("Line");
				//make it a popup window with all of the controls for the 
				// line drawing (have a box for the width, color picker, etc, etc
				DrawLineBox.setOnAction((ActionEvent event) -> {
					if (DrawLineBox.isSelected()) {
						Popup.launchEditOptionsWindow();
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
					Popup.launchEditOptionsWindow();
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
