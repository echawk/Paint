/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.Stack;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Pair;

/**
 *
 * @author ethan
 */
public class CustomCanvas extends ECanvas{
	
	private Pair<Double,Double> mouseCoord; //Pair for the mouse coordinates

	private Stack<Image> undoStack = new Stack(); 
	private Stack<Image> redoStack = new Stack();
	
	private Image drag_drop_image = null;
	
	private Rectangle r; //potentially replace this with a 'live' canvas, that gets cleared out and drawn on?
	private ECanvas livecanvas = new ECanvas();
	public CustomCanvas(){
		super();
		
		//this.imgToStack(this.getImage());
		
		this.mouseCoord = new Pair(0, 0);
		
		this.setOnMousePressed(e -> {
			this.mouseCoord = new Pair(e.getX(), e.getY());

			
			if (Paint.getMode() == Paint.EDIT_MODE) {
				switch (Paint.edittoolbar.getDrawSelection()) {
					case EditToolBar.COLOR_GRAB:
						Paint.colorpick.setValue(this.getImage().getPixelReader().getColor(
							roundDouble(e.getX()),
							roundDouble(e.getY())
						));	
						break;
					case EditToolBar.BLUR:
					case EditToolBar.RECTANGLE:
					case EditToolBar.SQUARE:
						//this.livecanvas.clear();
						//this.livecanvas.drawRectangle(this.mouseCoord, 0, 0);
						//Paint.getCurrentTab().pane.getChildren().add(this.livecanvas);
						
						this.r = new Rectangle(
							this.mouseCoord.getKey(),
							this.mouseCoord.getValue(),
							0,
							0
						);
						this.r.setFill(Paint.colorpick.getValue());
						break;
					case EditToolBar.CROP:
					case EditToolBar.DRAGDROP:
						this.r = new Rectangle(
							this.mouseCoord.getKey(),
							this.mouseCoord.getValue(),
							0,
							0
						);
						this.r.setFill(Paint.colorpick.getValue()
							.grayscale()
							.deriveColor(-255, 1, 1, .5));
					default:
						break;
				}
			}
			//this.imgToStack(this.getImage());
		});
		
		this.setOnMouseReleased((MouseEvent e) -> {
			this.gc.setFill(Paint.colorpick.getValue());
			this.gc.setStroke(Paint.colorpick.getValue());
			this.gc.setLineWidth(Paint.brushSize);

			if (Paint.getMode() == Paint.EDIT_MODE) {
				switch (Paint.edittoolbar.getDrawSelection()) {
					case EditToolBar.LINE:
						super.drawLine(this.mouseCoord, e.getX(), e.getY());
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.CIRCLE:
						super.drawCircle(this.mouseCoord, e.getX(), e.getY());
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.ELLIPSE:
						super.drawEllipse(this.mouseCoord, e.getX(), e.getY());
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.RECTANGLE:
						super.drawRectangle(this.mouseCoord, e.getX(), e.getY());
						//Paint.getCurrentTab().pane.getChildren().remove(this.livecanvas);
						Paint.getCurrentTab().pane.getChildren().remove(this.r);
						this.r = null;
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.SQUARE:
						super.drawSquare(this.mouseCoord, e.getX(), e.getY());
						Paint.getCurrentTab().pane.getChildren().remove(this.r);
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.TEXTBOX:
						this.gc.setFont(new Font(Paint.brushSize));
						this.gc.fillText(Paint.edittoolbar.getOptionsField(),
							this.mouseCoord.getKey(),
							this.mouseCoord.getValue()
						);	
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.TRIANGLE:
						super.drawTriangle(this.mouseCoord, e.getX());
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
						
					case EditToolBar.NGON:
						//1 - get number of sides
						//2 - get the proper points
						//3 - fill the poly gon
							
						//1
						int n = 0;
						try {
							n = Integer.parseInt(Paint.edittoolbar.getOptionsField());
						} catch (Exception ex) {
							System.out.println("Failed to parse options field: " + ex);
							return; // to keep from drawing a shape			
						}							
						super.drawNGon(this.mouseCoord, e.getX(), n);
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
						
					case EditToolBar.CROP:
					{ //Proper scoping
						//1 - save selection to image
						//2 - set the canvas to be the new image
						
						//1
						PixelReader r = this.getImage().getPixelReader();
						WritableImage wi = new WritableImage(
							r,
							roundDouble(this.mouseCoord.getKey()),
							roundDouble(this.mouseCoord.getValue()),
							roundDouble(e.getX() - this.mouseCoord.getKey()),
							roundDouble(e.getY() - this.mouseCoord.getValue())
							);						//2
						Paint.getCurrentTab().setImage(wi);
						Paint.getCurrentTab().pane.getChildren().remove(this.r);
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					}
					case EditToolBar.DRAGDROP:
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
							
							//for live draw
							Paint.getCurrentTab().pane.getChildren().remove(this.r);
							//Paint.getCurrentTab().pane.getChildren().remove(this.livecanvas);
							this.r = null;
							//Exit
							return;
						}	
						this.gc.drawImage(
							this.drag_drop_image,
							e.getX(),
							e.getY()
						);	//set the image back to null
						this.drag_drop_image = null;
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.BLUR:
						{
							//INCOMPLETE
							
							//Three steps:
							//1 get image
							//2 apply blur effect to image
							//3 draw the new image
							
							//1
							PixelReader r = this.getImage().getPixelReader();
							WritableImage wi = new WritableImage(
								r,
								roundDouble(this.mouseCoord.getKey()),
								roundDouble(this.mouseCoord.getValue()),
								roundDouble(e.getX() - this.mouseCoord.getKey()),
								roundDouble(e.getY() - this.mouseCoord.getValue())
							);		
							CustomCanvas t = new CustomCanvas();
							t.updateDimensions(wi); //need to make sure the canvas has dimensions
							t.gc.setEffect(new GaussianBlur());
							t.gc.drawImage(wi, 0, 0);
							//Popup.showImage(t.getImage()); //DEBUG
							this.gc.drawImage(
								t.getImage(),
								this.mouseCoord.getKey(),
								this.mouseCoord.getValue()
							);
							Paint.getCurrentTab().pane.getChildren().remove(this.r);
							this.r = null;
							Paint.getCurrentTab().imgHasBeenSaved = false;
							break;
						}
					case EditToolBar.SEPIA:
						{
							PixelReader r = this.getImage().getPixelReader();
							WritableImage wi = new WritableImage(
								r,
								roundDouble(this.mouseCoord.getKey()),
								roundDouble(this.mouseCoord.getValue()),
								roundDouble(e.getX() - this.mouseCoord.getKey()),
								roundDouble(e.getY() - this.mouseCoord.getValue())
							);		
							CustomCanvas t = new CustomCanvas();
							t.updateDimensions(wi); //need to make sure the canvas has dimensions
							t.gc.setEffect(new SepiaTone());
							t.gc.drawImage(wi, 0, 0);
							//Popup.showImage(t.getImage()); //DEBUG
							this.gc.drawImage(
								t.getImage(),
								this.mouseCoord.getKey(),
								this.mouseCoord.getValue()
							);		
							Paint.getCurrentTab().imgHasBeenSaved = false;
							break;
						}
					case EditToolBar.ROTATE:
						{
							//INCOMPLETE
							//Three steps:
							//1 - get selection
							//2 - rotate selection
							//3 - draw rotated selection
							
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
							CustomCanvas t = new CustomCanvas();
							t.updateDimensions(wi);
							t.gc.save();
							t.gc.rotate(Double.parseDouble(Paint.edittoolbar.getOptionsField()));
							t.gc.drawImage(wi, 0, 0);
							t.gc.restore();
							//Popup.showImage(t.getImage()); //DEBUG
							//3
							this.gc.drawImage(
								t.getImage(),
								this.mouseCoord.getKey(),
								this.mouseCoord.getValue()
							);		
							Paint.getCurrentTab().imgHasBeenSaved = false;
							break;
						}
					default:
						break;
				}	
			this.imgToStack(this.getImage());
			}
			
		});
				
		this.setOnMouseDragged(e -> {
			
			double bsize = Paint.brushSize;
			double x = e.getX() - bsize / 2;
			double y = e.getY() - bsize / 2;
			
			//if in edit mode
			if (Paint.getMode() == Paint.EDIT_MODE) {
				switch (Paint.edittoolbar.getDrawSelection()) {
					case EditToolBar.ERASE:
						this.gc.clearRect(x, y, bsize, bsize);
						this.imgToStack(this.getImage());
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break; 
				//this.imgToStack(this.getImage());
					case EditToolBar.PENCIL:
						this.gc.setFill(Paint.colorpick.getValue());
						this.gc.fillRect(x, y, bsize, bsize);
						this.imgToStack(this.getImage());
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.BLUR:
					case EditToolBar.DRAGDROP:
					case EditToolBar.RECTANGLE:
					case EditToolBar.CROP:
						/*
						this.livecanvas.clear();
						this.livecanvas.drawRectangle(this.mouseCoord, e.getX(), e.getY());
						try {
							Paint.getCurrentTab().pane.getChildren().add(this.livecanvas);
						} catch (Exception ex) {
							System.out.println("CustomCanvas.java; Failed to add livecanvas to pane:" + ex);
						}
						*/
						this.r.setWidth(e.getX() - this.mouseCoord.getKey());
						this.r.setHeight(e.getY() - this.mouseCoord.getValue());
						Paint.getCurrentTab().pane.getChildren().add(this.r);
						break;
					case EditToolBar.SQUARE:
						double s;
						if (e.getX() >= e.getY()) {
							s = e.getX() - this.mouseCoord.getKey(); 
						} else {
							s = e.getY() - this.mouseCoord.getValue();
						}
						this.r.setWidth(s);
						this.r.setHeight(s);
						Paint.getCurrentTab().pane.getChildren().add(this.r);
						break;
					default:
						break;
				}
			}
		});
		
	}
	
	/**
	 * 
	 * This method runs when Images are opened, or when the canvas is resized,
	 * and sets the canvas width to be the proper size.
	 * 
	 */
	public void updateDimensions() {
		//Also potential thought, I may have the image be a proportion of the current window size,
		//so that when the main window is resized, the image resizes with it.
		if (Paint.getCurrentTab().opened_image != null) {
			this.setHeight(Paint.getCurrentTab().opened_image.getHeight());
			this.setWidth(Paint.getCurrentTab().opened_image.getWidth());
		} else {
			this.setHeight(0);
			this.setWidth(0);
		}
	}
	
	/**
	 * Update the canvas dimensions to be that of an image.
	 * 
	 * @param i The image who's dimensions you want to set the canvas to
	 */
	
	public void updateDimensions(Image i) {
		this.setHeight(i.getHeight());
		this.setWidth(i.getWidth());
		this.livecanvas.setHeight(i.getHeight());
		this.livecanvas.setWidth(i.getWidth());
	}
	
	//this is a really hackyway of doing this, I want to make this much cleaner
	//(ie refactor)
	public void updateDimensions(boolean inc_zoom) {
		if (inc_zoom) {
			//if we want to increase the zoom
			this.setWidth(this.getWidth() * 2);
			this.setHeight(this.getHeight() * 2);
			this.livecanvas.setHeight(this.getHeight() * 2);
			this.livecanvas.setWidth(this.getWidth() * 2);
		} else {
			//if we want to decrease the zoom
			this.setWidth(this.getWidth() / 2);
			this.setHeight(this.getHeight() / 2);
			this.livecanvas.setHeight(this.getHeight() / 2);
			this.livecanvas.setWidth(this.getWidth() / 2);
		}
	}
	

	//need to preserve the image modifications
	public void zoomIn(){
		this.updateDimensions(true); // zoom in
		this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
		Paint.getCurrentTab().setScrollPrefSize(this.getWidth(), this.getHeight());
		this.imgToStack(this.getImage());

	}
	public void zoomOut(){
		this.updateDimensions(false); // zoom out
		this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
		Paint.getCurrentTab().setScrollPrefSize(this.getWidth(), this.getHeight());
		this.imgToStack(this.getImage());

	}
	/**
	 * Add an Image to the undo Stack 
	 * 
	 * @param i The image to add to the stack
	 */
	private void imgToStack(Image i) {
		this.undoStack.push(i);
		this.redoStack.clear(); // reset the redo stack (should only be able to redo what you've undone
		System.out.println("CustomCanvas.java; Added Image to undo Stack");
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
	 * This Method is responsible for undoing actions that are taken by the 
	 * user, by pop-ing them off of the undo stack, and setting the canvas 
	 * to be the next image in line, so to speak.
	 * 
	 */
	public void undo() {
		if (! this.undoStack.empty()) { //if the image stack is not empty
			//System.out.println("CustomCanvas.java; undoStack isn't empty");
			Image i = this.undoStack.pop();
			//Popup.showImage(i);
			try { 
				this.redoStack.add(i); //this line breaks undo for some reason (null pointer exception)
			} catch (Exception e) {
				System.out.println("CustomCanvas.java; Failed to add i to redoStack:" + e);
			}
			try {
				Paint.getCurrentTab().setImage(i);
			} catch (Exception e) {
				System.out.println("CustomCanvas.java; Failed to execute setImage():" + e);
			}
			
			/*
			if (! undoStack.empty()) {
				Paint.getCurrentTab().setImage(undoStack.pop());
				System.out.println("Undo was Successful!");
			}
			*/
		} else {
			System.out.println("CustomCanvas.java; undoStack is empty");
		}
	}
	/**
	 * This Method is responsible for redo-ing actions that have been undone,
	 * by setting the image to be the last image to be whatever pops off the 
	 * redo stack, and adding that image back onto the undo stack.
	 */
	public void redo() {
		if (! redoStack.empty()) {
			Image lastimg = redoStack.pop(); //get the last image
			Paint.getCurrentTab().setImage(lastimg);
			undoStack.add(lastimg);
		}
	}
}
