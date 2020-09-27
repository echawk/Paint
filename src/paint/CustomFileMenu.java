/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author ethan
 */
public class CustomFileMenu extends Menu {
	//This file will need extensive rewrites
	public CustomFileMenu(){
		super();
		this.setText("File");
			MenuItem saveas = new MenuItem("Save As");
			saveas.setOnAction((ActionEvent event) -> {
				CustomFileHandler.saveAsFile(Paint.window);
			});
			saveas.setAccelerator(new KeyCodeCombination(KeyCode.S, 
						KeyCombination.SHIFT_DOWN, 
						KeyCombination.CONTROL_DOWN)
			);
		
		MenuItem save = new MenuItem("Save");
			save.setOnAction((ActionEvent event) -> {
				CustomFileHandler.saveFile();
			});
			save.setAccelerator(new KeyCodeCombination(KeyCode.S, 
						KeyCombination.CONTROL_DOWN)
			);
		
		
		MenuItem open = new MenuItem("Open");
			open.setOnAction((ActionEvent event) -> {
				try {
					File f = CustomFileHandler.openFile(Paint.window);
					Paint.addTab(f);
				} catch (FileNotFoundException ex) {
					System.out.println("File was not found");
				}
			});
			open.setAccelerator(new KeyCodeCombination(KeyCode.O, 
						KeyCombination.CONTROL_DOWN)
			);

		MenuItem quit = new MenuItem("Quit");
			quit.setOnAction((ActionEvent event) -> {
				Paint.close();
			});
			quit.setAccelerator(new KeyCodeCombination(KeyCode.Q, 
						KeyCombination.SHIFT_DOWN, 
						KeyCombination.CONTROL_DOWN)
			);

		MenuItem clear = new MenuItem("Clear");
			clear.setOnAction((ActionEvent event) -> {
				Paint.getCurrentTab().clearImage();
			});
			
		MenuItem newimage = new MenuItem("New");
			newimage.setOnAction((ActionEvent event) -> {
				//Show the create new Image dialog
				Popup.launchCreateNewImage();
			});
			newimage.setAccelerator(new KeyCodeCombination(KeyCode.N, 
						KeyCombination.CONTROL_DOWN)
			);
		
		//add all of the menu items to the file menu
		this.getItems().addAll(newimage, open, save, saveas, clear, quit);

	}
}
