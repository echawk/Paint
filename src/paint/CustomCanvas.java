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
import javafx.util.Pair;

/**
 *
 * @author ethan
 */
public class CustomCanvas extends Canvas{
	
	public GraphicsContext gc; //pointer to the graphics context of the canvas
	
	public double brushSize = 5; //set a default size of 5
	public ColorPicker colorpick = new ColorPicker();

	private Pair<Double,Double> mouseCoord; //Pair for the mouse coordinates

	public CustomCanvas(){
		super();
		
		this.gc = this.getGraphicsContext2D();
		this.colorpick.setValue(Color.BLACK);
		this.mouseCoord = new Pair(0, 0);
		
		this.setOnMousePressed(e -> {
			this.mouseCoord = new Pair(e.getX(), e.getY());
			System.out.println(mouseCoord.toString());
			// Keeping this if statement here, in case if this handler 
			// becomes more complex in the future
			if (Paint.getMode() == Paint.EDIT_MODE) {
				if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.LINE)) {
					
					System.out.println("X: " + e.getX() + "Y: " + e.getY());
				}
			}
			
		});
		
		//make sure to update both the Circle and Ellipse methods!!!!
		this.setOnMouseReleased(e -> {
			this.gc.setFill(this.colorpick.getValue());

			if (Paint.getMode() == Paint.EDIT_MODE) {
				if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.LINE)) {
					this.gc.setLineWidth(this.brushSize);
					this.gc.setStroke(this.colorpick.getValue());
					this.gc.strokeLine(
						this.mouseCoord.getKey(), 
						this.mouseCoord.getValue(),
						e.getX(),
						e.getY()
					);
					
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.CIRCLE)) {
					double l;
					if (e.getX() >= e.getY()) {
						l = (e.getX() - this.mouseCoord.getKey());
					} else {
						l = (e.getY() - this.mouseCoord.getValue());
					}
					this.gc.fillOval(
						this.mouseCoord.getKey(), 
						this.mouseCoord.getValue(),
						l, 
						l
					);

					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.ELLIPSE)) {
					this.gc.fillOval(
						this.mouseCoord.getKey(),
						this.mouseCoord.getValue(),
						(e.getX() - this.mouseCoord.getKey()),
						(e.getY() - this.mouseCoord.getValue())
					);
				}
			}
		});
				
		this.setOnMouseDragged(e -> {
			
			double bsize = this.brushSize;
			double x = e.getX() - bsize / 2;
			double y = e.getY() - bsize / 2;
			
			//if in edit mode
			if (Paint.getMode() == Paint.EDIT_MODE) {
				if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.ERASE)) {
					
					this.gc.clearRect(x, y, bsize, bsize);
				} else if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.PENCIL)) {
					
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
	
	//this is a really hackyway of doing this, I want to make this much cleaner
	//(ie refactor)
	public void updateDimensions(boolean inc_zoom) {
		if (inc_zoom) {
			//if we want to increase the zoom
			this.setWidth(this.getWidth() * 2);
			this.setHeight(this.getHeight() * 2);
		} else {
			//if we want to decrease the zoom
			this.setWidth(this.getWidth() / 2);
			this.setHeight(this.getHeight() / 2);
		}
	}
	
	/**
	 * This method returns an image of the current canvas.
	 * 
	 * @return An Image Object of the canvas.
	 */
	public Image getImage() {
		WritableImage wi = this.snapshot(null, null);
		ImageView iv = new ImageView(wi);
		return iv.getImage();
	}

	//need to preserve the image modifications
	public void zoomIn(){
		this.updateDimensions(true); // zoom in
		this.gc.drawImage(Paint.opened_image, 0, 0, this.getWidth(), this.getHeight());
		Paint.setScrollPrefSize(this.getWidth(), this.getHeight());
	}
	public void zoomOut(){
		this.updateDimensions(false); // zoom out
		this.gc.drawImage(Paint.opened_image, 0, 0, this.getWidth(), this.getHeight());
		Paint.setScrollPrefSize(this.getWidth(), this.getHeight());

	}
	
}
