/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.Stack;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

/**
 *
 * @author ethan
 */
public class CustomCanvas extends Canvas{
	
	public GraphicsContext gc; //pointer to the graphics context of the canvas
	
	public double brushSize = 5; //set a default size of 5
	//public ColorPicker colorpick = new ColorPicker();

	private Pair<Double,Double> mouseCoord; //Pair for the mouse coordinates

	private Stack<Image> undoStack = new Stack(); 
	private Stack<Image> redoStack = new Stack();
	
	private Image drag_drop_image = null;
	
	public CustomCanvas(){
		super();
		
		this.gc = this.getGraphicsContext2D();
		//this.colorpick.setValue(Color.BLACK);
		this.mouseCoord = new Pair(0, 0);
		
		this.setOnMousePressed(e -> {
			this.mouseCoord = new Pair(e.getX(), e.getY());
			
			if (Paint.getMode() == Paint.EDIT_MODE) {
				if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.COLOR_GRAB)) {
					
					Paint.colorpick.setValue(this.getImage().getPixelReader().getColor(
						roundDouble(e.getX()),
						roundDouble(e.getY())
					));
				}
			}
			//this.imgToStack(this.getImage());
		});
		
		this.setOnMouseReleased(e -> {
			this.gc.setFill(Paint.colorpick.getValue());
			this.gc.setStroke(Paint.colorpick.getValue());

			if (Paint.getMode() == Paint.EDIT_MODE) {
				if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.LINE)) {
					
					this.gc.setLineWidth(this.brushSize);
					this.gc.strokeLine(
						this.mouseCoord.getKey(), 
						this.mouseCoord.getValue(),
						e.getX(),
						e.getY()
					);
					
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.CIRCLE)) {
					
					double l;
					//Use the larger dimension for drawing the circle
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
				
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.RECTANGLE)) {
					
					this.gc.fillRect(
						this.mouseCoord.getKey(), 
						this.mouseCoord.getValue(), 
						(e.getX() - this.mouseCoord.getKey()), 
						(e.getY() - this.mouseCoord.getValue())
					);
					
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.SQUARE)) {
					
					double s;
					//Use the larger dimension for drawing the square
					if (e.getX() >= e.getY()) {
						s = (e.getX() - this.mouseCoord.getKey());
					} else {
						s = (e.getY() - this.mouseCoord.getValue());
					}
					this.gc.fillRect(
						this.mouseCoord.getKey(),
						this.mouseCoord.getValue(),
						s,
						s
					);
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.TEXTBOX)) {
					
					if (Paint.TABBED) {
						this.gc.setFont(new Font(Paint.getCurrentTab().imgcanvas.brushSize));
					} else {
						this.gc.setFont(new Font(Paint.imgcanvas.brushSize));
					}
					
					this.gc.fillText(Paint.edittoolbar.getOptionsField(),
						this.mouseCoord.getKey(),
						this.mouseCoord.getValue()
					);
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.TRIANGLE)){
					
					Pair PolygonPts = getPolygonPoints(
						3, 
						this.mouseCoord, 
						roundDouble(e.getX())
					);
					
					double[] xp = (double[]) PolygonPts.getKey();
					double[] yp = (double[]) PolygonPts.getValue();
					
					this.gc.fillPolygon(xp, yp, 3);
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.NGON)) {
					
					int n = 0;
					
					try {
						n = Integer.parseInt(Paint.edittoolbar.getOptionsField());
					} catch (Exception ex) {
						System.out.println("Failed to parse options field: " + ex);
						return; // to keep from drawing a shape
					}
					Pair PolygonPts = getPolygonPoints(
						n,
						this.mouseCoord,
						roundDouble(e.getX())
					);
					
					double[] xp = (double[]) PolygonPts.getKey();
					double[] yp = (double[]) PolygonPts.getValue();
					
					this.gc.fillPolygon(xp, yp, n);
					
				} else if (Paint.edittoolbar.getDrawSelection().equals(
					Paint.edittoolbar.CROP)) {
					
					//I could use somethign similar to what I've done
					//here for the drag and drop method
					PixelReader r = this.getImage().getPixelReader();
					WritableImage wi = new WritableImage(
						r,
						roundDouble(this.mouseCoord.getKey()),
						roundDouble(this.mouseCoord.getValue()),
						roundDouble(e.getX() - this.mouseCoord.getKey()),
						roundDouble(e.getY() - this.mouseCoord.getValue())
					);
					if (Paint.TABBED) {
						Paint.getCurrentTab().setImage(wi);
					} else {
						Paint.setImage(wi);
					}
				} else if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.DRAGDROP)) {
					
					if (this.drag_drop_image == null) {
						//Three steps:
						//1 - get the image
						//2 - make the image globally accessible
						//3 - clear out a rectangle of the same size
						
						//1
						PixelReader r = this.getImage().getPixelReader();
						WritableImage wi = new WritableImage(
							r,
							roundDouble(this.mouseCoord.getKey()),
							roundDouble(this.mouseCoord.getValue()),
							roundDouble(e.getX() - this.mouseCoord.getKey()),
							roundDouble(e.getY() - this.mouseCoord.getValue())
						);
						//2
						this.drag_drop_image = wi;
						//3
						this.gc.clearRect(
							roundDouble(this.mouseCoord.getKey()),
							roundDouble(this.mouseCoord.getValue()),
							roundDouble(e.getX() - this.mouseCoord.getKey()),
							roundDouble(e.getY() - this.mouseCoord.getValue())
						);
						//Exit
						return;
					}
					
					this.gc.drawImage(
						this.drag_drop_image, 
						e.getX(), 
						e.getY()
					);
					//set the image back to null
					this.drag_drop_image = null;
				}
				this.imgToStack(this.getImage());
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
					
					this.gc.setFill(Paint.colorpick.getValue());
					this.gc.fillRect(x, y, bsize, bsize);
					
				} 
				/*
				else if (Paint.edittoolbar.getDrawSelection().equals(
						Paint.edittoolbar.BLUR)) {
					
					this.gc.setEffect(new GaussianBlur());
					this.gc.setFill(Color.TRANSPARENT);
					this.gc.fillOval(x, y, bsize, bsize);
					this.gc.fillRect(x, y, bsize, bsize);
					this.gc.setEffect(null);
				}	
				*/
			}
			this.imgToStack(this.getImage());
		});
		
	}
	
	/**
	 * 
	 * This method runs when Images are opened, or when the canvas is resized, and sets the canvas width to be the proper size.
	 * 
	 */
	public void updateDimensions() {
		//Also potential thought, I may have the image be a proportion of the current window size,
		//so that when the main window is resized, the image resizes with it.
		if (Paint.TABBED) {
			if (Paint.getCurrentTab().opened_image != null) {
				this.setHeight(Paint.getCurrentTab().opened_image.getHeight());
				this.setWidth(Paint.getCurrentTab().opened_image.getWidth());
			} else {
				this.setHeight(0);
				this.setWidth(0);
			}
		} else {
			if (Paint.opened_image != null) {	
				this.setHeight(Paint.opened_image.getHeight());
				this.setWidth(Paint.opened_image.getWidth());
			} else { // if the image is null, set the dimensions to zero
				this.setHeight(0);
				this.setWidth(0);
			}
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
		this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
		if (Paint.TABBED) {
			Paint.getCurrentTab().setScrollPrefSize(this.getWidth(), this.getHeight());
		} else {
			Paint.setScrollPrefSize(this.getWidth(), this.getHeight());
		}
		this.imgToStack(this.getImage());

	}
	public void zoomOut(){
		this.updateDimensions(false); // zoom out
		this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
		if (Paint.TABBED) {
			Paint.getCurrentTab().setScrollPrefSize(this.getWidth(), this.getHeight());
		} else {
			Paint.setScrollPrefSize(this.getWidth(), this.getHeight());
		}	
		this.imgToStack(this.getImage());

	}
	/**
	 * Add an Image to the undo Stack 
	 * 
	 * @param i The image to add to the stack
	 */
	private void imgToStack(Image i) {
		this.undoStack.push(i);
		System.out.println("Added Image to undo Stack");
	}
	
	/**
	 * Rounds Any Double values to integers, may be removed in favor of type casting.
	 * 
	 * @param d
	 * @return An Integer rounded via the Math Library.
	 */
	
	private int roundDouble(double d) {
		return (int) Math.round(d);
	}
	
	/**
	 * 
	 * This Method is responsible for undoing actions that are taken by the user, by pop-ing them off of the 
	 * undo stack, and setting the canvas to be the next image in line, so to speak.
	 * 
	 */
	public void undo() {
		if (! undoStack.empty()) { //if the image stack is not empty
			redoStack.add(undoStack.pop());
			if (! undoStack.empty()) {
				Paint.setImage(undoStack.pop());
			}
		}
	}
	
	public void redo() {
		if (! redoStack.empty()) {
			Image lastimg = redoStack.pop(); //get the last image
			Paint.setImage(lastimg);
			undoStack.add(lastimg);
		}
	}
	
	/**
	 * 
	 * This method is a helper method for drawing polygons on the canvas, and handles calculating the proper points.
	 * 
	 * @param n An integer for the number of sides the polygon should have.
	 * @param initMouseCoord The initial mouse coordinates 
	 * @param cx The current X value 
	 * @return A Pair of double Arrays, with the key corresponding to the X points, and the value corresponding to the Y points.
	 */
	private Pair<double[],double[]> getPolygonPoints(int n, Pair initMouseCoord, int cx){
		double ix = (double) initMouseCoord.getKey();
                double iy = (double) initMouseCoord.getValue();
                double radius = cx - ix;

		double[] xp = new double[n];
		double[] yp = new double[n];

		for (int i = 0; i < n; i++) {
			xp[i] = (ix + (radius * Math.cos(2 * Math.PI * i / n)));
			yp[i] = (iy + (radius * Math.sin(2 * Math.PI * i / n)));
		}

		return new Pair(xp, yp);
	}
	
	
	
}
