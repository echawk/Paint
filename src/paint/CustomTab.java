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
	
	public void setImage(Image img) {
		this.opened_image = img; //set the opened_image pointer to image
		this.imgcanvas.updateDimensions(); //update the canvas dimensions
		this.imgcanvas.gc.drawImage(
			this.opened_image, 
			0, 
			0
		);
	}

	public void clearImage(){
		//set the image to be nothing
		this.opened_file = null;
			/* set the opened_file to be null,to prevent accidentally 
			saving & deleting the image
			*/
		this.opened_image = null; //Same reasoning as above ^^^
		this.setImage(null);
	}

	public void setScrollPrefSize(double x, double y){
		this.scroll.setPrefSize(x, y);
	}
}
