/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

/**
 *
 * @author ethan
 */
public class CustomFileHandler {
	
	/**
	 * This method uses a FileChooser Object to select a File and return 
	 * said File. Requires a Window object as input, and will always be 
	 * "Paint.window"
	 * @param stage Parent Stage 
	 * @return The Selected File
	 */
	public static File openFile(Window stage) {
		File sel_file = null;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
		try {
			sel_file = fileChooser.showOpenDialog(stage);
		} catch (Exception e){
			System.out.println("CustomFileHandler.java; Failed to set sel_file:" + e);
		}
		return sel_file;
	}
	
	/**
	 * This method is responsible for the "Save As" functionality of Paint
	 * 
	 * @param stage 
	 */
	public static void saveAsFile(Window stage) {
		// add ', File f' to the args?
		Image out_img;
		out_img = Paint.getCurrentTab().imgcanvas.getImage();
		if (out_img == null){ //Check to make sure there is a file to save
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
		
		saveImage(out_img, out_file);
		Paint.getCurrentTab().opened_file = out_file; // update the name of the tab too
		Paint.getCurrentTab().setText(out_file.getName());
	}
	
	/**
	 * This method is responsible for saving Paint.opened_image into Paint.opened_file.
	 * It implements a "Smart" save, because if opened_file is null, it will instead launch 
	 * the "saveAsFile" method.
	 */
	public static void saveFile() {
		//Line below needs to change
		Image out_img;
		out_img = Paint.getCurrentTab().imgcanvas.getImage();
		
		if  (out_img == null){
			System.out.println("Warning. No image in Canvas. Failed to save.");
			return;
		}
		if (Paint.getCurrentTab().opened_file == null) {
			saveAsFile(Paint.window);
			return;
		}
		saveImage(out_img, Paint.getCurrentTab().opened_file);
		Paint.getCurrentTab().imgHasBeenSaved = true;
	}
	
	/**
	 * 
	 * Helper method for the other methods.
	 * 
	 * @param f A input file
	 * @return A string of the file extension
	 */
	private static String getFileExtension(File f){
		String fn = f.getName(); //get the file name (not full path)
		int pos = fn.lastIndexOf("."); //get the pos of the last period
		if (pos > 0) {
			return fn.substring(pos + 1).toLowerCase();
		//return the substring that is one greater than the last period
		}
		return "";  //might want to change this to be a sensible default
	}
	
	/**
	 * This method is responsible for saving out_img to opened_file, and
	 * handles more output formats than the previous method of saving the image.
	 * It also handles the new buffered image to create, based upon the opened_file's 
	 * file extension, by removing the alpha channel from jpg images.
	 * 
	 * @param out_img
	 * @param opened_file 
	 */
	private static void saveImage(Image out_img, File opened_file){
		// Get buffered image:
		BufferedImage image = SwingFXUtils.fromFXImage(out_img, null);
		BufferedImage imageRGB;
		//If the file extensions are different, show a popup warning
		if (Paint.getCurrentTab().opened_file != null) {
			if (getFileExtension(Paint.getCurrentTab().opened_file).equals(
				getFileExtension(opened_file))) {
				System.out.println("Image Formats are the same.");
			} else {
				Popup.saveWarning();
			}
		}
		
		if (getFileExtension(opened_file).equals("jpg")){
			// Remove alpha-channel from buffered image:
			imageRGB = new BufferedImage(
				image.getWidth(),
				image.getHeight(),
				BufferedImage.OPAQUE
			);
		} else {
			imageRGB = new BufferedImage(
				image.getWidth(),
				image.getHeight(),
				BufferedImage.TRANSLUCENT
			);
		}
		
		Graphics2D graphics = imageRGB.createGraphics();

		graphics.drawImage(image, 0, 0, null);
		try {
			ImageIO.write(
				imageRGB, 
				getFileExtension(opened_file), 
				opened_file
			);
			System.out.println("Saved Image:" + opened_file);
		} catch (IOException ex) {
			System.out.println("Failed to Save Image:" + ex);
		}
		//cleanup
		graphics.dispose ();
	}
}
