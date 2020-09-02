/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import static paint.Paint.opened_file;

/**
 *
 * @author ethan
 */
public class CustomFileHandler {
	
		
	//move these methods to a file handler class?
	public static File openFile(Window stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
		File sel_file = fileChooser.showOpenDialog(stage);
		return sel_file;
	}
	
	public static void saveAsFile(Window stage) {
		// add ', File f' to the args?
		//Image out_img = imgv.getImage();
		//Image out_img = Paint.imgcanvas.snapshot(null, null); //need to update this part ( see gabe)
		Image out_img = Paint.imgcanvas.getImage();


		//Check to make sure there is a file to save
		if (out_img == null){
			System.out.println("Warning. No image in Canvas. Failed to save.");
			return; // should raise an error here (like a pop-up box)
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
		File out_file = fileChooser.showSaveDialog(stage);
		//need to have a catcher for if the save dialog is cancelled
		
		try {
			//there is a bug where the imgcanvas.snapshot wont allow me to save images other than as a png#############
			ImageIO.write(SwingFXUtils.fromFXImage(out_img, null), 
				getFileExtension(out_file), out_file);
			System.out.println("Saved Image");
		} catch (IOException e) {
			System.out.println("Failed to save image: " + e);
		}

	}
	
	public static void saveFile() {
		//Line below needs to change
		//Image out_img = imgv.getImage();
		//Image out_img = Paint.imgcanvas.snapshot(null, null);
		Image out_img = Paint.imgcanvas.getImage();

		//ask gabe?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		if  (out_img == null){
			System.out.println("Warning. No image in Canvas. Failed to save.");
			return;
		}
		try {
			//there is a bug where the imgcanvas.snapshot wont allow me to save images other than as a png#############
			ImageIO.write(SwingFXUtils.fromFXImage(out_img, null), 
				getFileExtension(opened_file), opened_file);
			System.out.println("Saved Image");
		} catch (IOException e) {
			System.out.println("Failed to save Image: " + e);
		}
	}
	
	//helper code
	private static String getFileExtension(File f){
		String fn = f.getName(); //get the file name (not full path)
		int pos = fn.lastIndexOf("."); //get the pos of the last period
		if (pos > 0) {
			return fn.substring(pos + 1); //return the substring that is one greater than the last period
		}
		return "";  //might want to change this to be a sensible default
	}
	
	
	
}
