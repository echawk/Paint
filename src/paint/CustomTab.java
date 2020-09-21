/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;

/**
 *
 * @author ethan
 */
public class CustomTab extends Tab {
	
	public ScrollPane scroll;
	public File opened_file;
	public Image opened_image;
	public CustomCanvas imgcanvas = new CustomCanvas();
	
	public CustomTab(String label){
		super(label);
		
		this.scroll = new ScrollPane();
		this.scroll.setContent(this.imgcanvas);
		
		//set the tab to have the scroll
		this.setContent(this.scroll);
	}
	/**
	 * Sets this Tab's image to be whatever Image object is provided; calls 
	 * updateDimensions, then draws the image using the Tab's imgcanvas.
	 * 
	 * @param img The image object you want to be in this tab
	 */
	public void setImage(Image img) {
		this.opened_image = img; //set the opened_image pointer to image
		this.imgcanvas.updateDimensions(); //update the canvas dimensions
		this.imgcanvas.gc.drawImage(
			opened_image,
			0, 
			0
		);
	}
	/**
	 * Clears whatever the opened image is for the tab, it is made obsolete by 
	 * the fact Paint is no longer a single image editor, but is here for
	 * historical reasons.
	 */
	public void clearImage(){
		//set the image to be nothing
		this.opened_file = null;
			/* set the opened_file to be null,to prevent accidentally 
			saving & deleting the image
			*/
		this.opened_image = null; //Same reasoning as above ^^^
		this.setImage(null);
	}
	/**
	 * 
	 * Set the ScrollPane's preferred size, this method is called whenever 
	 * either of the zoom methods are called, as to keep the imgcanvas and the 
	 * scrollpane the same size.
	 * 
	 * @param x The width
	 * @param y The height
	 */
	public void setScrollPrefSize(double x, double y){
		this.scroll.setPrefSize(x, y);
	}
}
