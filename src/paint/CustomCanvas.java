/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author ethan
 */
public class CustomCanvas extends Canvas{
	
	public GraphicsContext gc; //pointer to the graphics context of the canvas
	
	public double brushSize = 5; //set a default size of 5
	public static ColorPicker colorpick = new ColorPicker();


	public CustomCanvas(){
		super();
		
		this.gc = this.getGraphicsContext2D();
		this.colorpick.setValue(Color.BLACK);
		
		this.setOnMouseDragged(e -> {
			
			double bsize = this.brushSize;
			double x = e.getX() - bsize / 2;
			double y = e.getY() - bsize / 2;
			
			//if in edit mode
			if (Paint.getMode() == Paint.EDIT_MODE) {
				if (Paint.menub.eraserSelected()) {
					this.gc.clearRect(x, y, bsize, bsize);
				} else if (Paint.menub.drawLineSelected()) {
					this.gc.setFill(this.colorpick.getValue());
					this.gc.fillRect(x, y, bsize, bsize);
				}
			}
		});
		
		
	}
	
	
	public void updateDimensions() {
		//Also potential thought, I may have the image be a proportion of the current window size,
		//so that when the main window is resized, the image resizes with it.
		if (Paint.opened_image != null) {
			this.setHeight(Paint.opened_image.getHeight());
			this.setWidth(Paint.opened_image.getWidth());
		} else { // if the image is null, set the dimensions to zero
			this.setHeight(0);
			this.setWidth(0);
		}
	}

	public Image getImage() {
		WritableImage wi = this.snapshot(null, null);
		ImageView iv = new ImageView(wi);
		return iv.getImage();
	}

	
}
