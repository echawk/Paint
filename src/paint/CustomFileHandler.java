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
import static paint.Paint.opened_file;

/**
 *
 * @author ethan
 */
public class CustomFileHandler {
		
	/**
	 * This method uses a FileChooser Object to select a File and return 
	 * said File.
	 * @param stage Parent Stage
	 * @return The Selected File
	 */
	public static File openFile(Window stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
		File sel_file = fileChooser.showOpenDialog(stage);
		return sel_file;
	}
	
	/**
	 * This method is responsible for the "Save As" functionality of Paint
	 * 
	 * @param stage 
	 */
	public static void saveAsFile(Window stage) {
		// add ', File f' to the args?
		Image out_img = Paint.imgcanvas.getImage();
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
		Paint.opened_file = out_file;
	}
	
	/**
	 * This method is responsible for saving Paint.opened_image into Paint.opened_file.
	 * It implements a "Smart" save, because if opened_file is null, it will instead launch 
	 * the "saveAsFile" method.
	 */
	public static void saveFile() {
		//Line below needs to change
		Image out_img = Paint.imgcanvas.getImage();		
		if  (out_img == null){
			System.out.println("Warning. No image in Canvas. Failed to save.");
			return;
		}
		
		if (Paint.opened_file == null) {
			saveAsFile(Paint.window);
			return;
		}
		
		saveImage(out_img, opened_file);
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
			return fn.substring(pos + 1); 
		//return the substring that is one greater than the last period
		}
		return "";  //might want to change this to be a sensible default
	}
	
	
	/**
	 * This method is responsible for saving out_img to opened_file, and
	 * handles more output formats than the previous method of saving the image
	 * 
	 * @param out_img
	 * @param opened_file 
	 */
	private static void saveImage(Image out_img, File opened_file){
	// Get buffered image:
	BufferedImage image = SwingFXUtils.fromFXImage(out_img, null);
		
	// Remove alpha-channel from buffered image:
	BufferedImage imageRGB = new BufferedImage(
		image.getWidth(),
		image.getHeight(),
		BufferedImage.OPAQUE);

	Graphics2D graphics = imageRGB.createGraphics();

	graphics.drawImage(image, 0, 0, null);
	try {
		ImageIO.write (imageRGB, getFileExtension(opened_file), opened_file);
		System.out.println("Saved Image");
	} catch (IOException ex) {
		System.out.println("Failed to Save Image:" + ex);
	}
	//cleanup
	graphics.dispose ();
	}
}
