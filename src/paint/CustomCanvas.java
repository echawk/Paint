/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.Stack;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Pair;

/**
 *
 * @author ethan
 */
public class CustomCanvas extends ECanvas{
	
	private Pair<Double,Double> mouseCoord; //Pair for the mouse coordinates
	private Point2D imc; //initial mouse coordinates
	private Stack<Image> undoStack = new Stack(); 
	private Stack<Image> redoStack = new Stack();
	
	private Stack<Pair> freeNgonStack = new Stack(); //make this Point2D
	private Image drag_drop_image = null;
	private Rectangle r; //potentially replace this with a 'live' canvas, that gets cleared out and drawn on?
	private Line l;
	private Polygon p;
	private Ellipse ell;
	private ECanvas livecanvas = new ECanvas();
	private Color targetColor;
	public CustomCanvas(){
		super();
				
		this.mouseCoord = new Pair(0, 0);
		
		this.setOnMousePressed(e -> {
			this.mouseCoord = new Pair(e.getX(), e.getY());
			this.imc = new Point2D(e.getX(), e.getY());
			
			if (Paint.getMode() == Paint.EDIT_MODE) {
				switch (Paint.edittoolbar.getDrawSelection()) {
					case EditToolBar.COLOR_GRAB:
						Paint.colorpick.setValue(this.getImage().getPixelReader().getColor(
							roundDouble(e.getX()),
							roundDouble(e.getY())
						));	
						break;
					case EditToolBar.RECTANGLE:
					case EditToolBar.SQUARE:
						this.r = new Rectangle(imc.getX(), imc.getY(), 0, 0);
						this.r.setFill(Paint.colorpick.getValue());
						Paint.getCurrentTab().pane.getChildren().add(this.r);
						break;
					case EditToolBar.BLUR:
					case EditToolBar.CROP:
					case EditToolBar.DRAGDROP:
					case EditToolBar.SEPIA:
					case EditToolBar.ROTATE:
						this.r = new Rectangle(imc.getX(), imc.getY(), 0, 0);
						this.r.setFill(Paint.colorpick.getValue()
							.grayscale()
							.deriveColor(-360, 1, 1, .5));
						Paint.getCurrentTab().pane.getChildren().add(this.r);
						break;
					case EditToolBar.LINE:
						this.l = new Line(this.imc.getX(), this.imc.getY(), this.imc.getX(), this.imc.getY());
						this.l.setStroke(Paint.colorpick.getValue());
						Paint.getCurrentTab().pane.getChildren().add(this.l);
						break;
					case EditToolBar.TRIANGLE:
						this.p  = new Polygon();
						this.p.getPoints().addAll(
							this.convPairToArray(
								super.getPolygonPoints(
									3, 
									this.imc, 
									(int) e.getX()
								)
							)
						);
						this.p.setFill(Paint.colorpick.getValue());
						Paint.getCurrentTab().pane.getChildren().add(this.p);
						break;
					case EditToolBar.NGON:
						this.p = new Polygon();
						this.p.getPoints().addAll(
							this.convPairToArray(
								super.getPolygonPoints(
									Integer.parseInt(
										Paint.edittoolbar.getOptionsField()
									), 
									this.imc, 
									(int) e.getX()
								)
							)
						);
						this.p.setFill(Paint.colorpick.getValue());
						Paint.getCurrentTab().pane.getChildren().add(this.p);
						break;
					case EditToolBar.ELLIPSE:
					case EditToolBar.CIRCLE:
						this.ell = new Ellipse();
						this.ell.setFill(Paint.colorpick.getValue());
						Paint.getCurrentTab().pane.getChildren().add(this.ell);
						break;
					case EditToolBar.FREENGON:
						this.freeNgonStack.add(new Pair(e.getX(), e.getY()));
						this.p = new Polygon();
						//Paint.getCurrentTab().pane.getChildren().add(this.p);
						break;
					case EditToolBar.BUCKETFILL:
						this.targetColor = this.getImage().getPixelReader().getColor((int) e.getX(), (int) e.getY());
						break;
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
			boolean fill = Paint.edittoolbar.getFill();
			
			if (Paint.getMode() == Paint.EDIT_MODE) {
				switch (Paint.edittoolbar.getDrawSelection()) {
					case EditToolBar.LINE:
						super.drawLine(this.imc, e.getX(), e.getY());
						postDraw();
						break;
					case EditToolBar.CIRCLE:
						super.drawCircle(this.imc, e.getX(), e.getY(), fill);
						postDraw();
						break;
					case EditToolBar.ELLIPSE:
						super.drawEllipse(this.imc, e.getX(), e.getY(), fill);
						postDraw();
						break;
					case EditToolBar.RECTANGLE:
						super.drawRectangle(this.imc, e.getX(), e.getY(), fill);
						postDraw();
						break;
					case EditToolBar.SQUARE:
						super.drawSquare(this.imc, e.getX(), e.getY(), fill);
						postDraw();
						break;
					case EditToolBar.TEXTBOX:
						this.gc.setFont(new Font(Paint.brushSize));
						super.drawText(imc, Paint.edittoolbar.getOptionsField(), fill);
						postDraw();
						break;
					case EditToolBar.TRIANGLE:
						super.drawTriangle(this.imc, e.getX(), fill);
						postDraw();
						break;
						
					case EditToolBar.NGON:
						int n = 0;
						try {
							n = Integer.parseInt(Paint.edittoolbar.getOptionsField());
						} catch (NumberFormatException ex) {
							System.out.println("Failed to parse options field: " + ex);
							return; // to keep from drawing a shape			
						}							
						super.drawNGon(this.imc, e.getX(), n, fill);
						postDraw();
						break;
						
					case EditToolBar.CROP:
					{ //Proper scoping
						//1 - save selection to image
						//2 - set the canvas to be the new image
						
						//1
						Image wi = super.getSelectionAsImage(this.imc, e.getX(), e.getY());
						//2
						Paint.getCurrentTab().setImage(wi);
						postDraw();
						break;
					}
					case EditToolBar.DRAGDROP:
						if (this.drag_drop_image == null) {
							//Three steps:
							//1 - get the image & make it globally accessible
							//2 - clear out a rectangle of the same size
							
							//1
							this.drag_drop_image = super.getSelectionAsImage(this.imc, e.getX(), e.getY());
							//2
							this.gc.clearRect(
								roundDouble(this.imc.getX()),
								roundDouble(this.imc.getY()),
								roundDouble(e.getX() - this.imc.getX()),
								roundDouble(e.getY() - this.imc.getY())
							);

							postDraw();
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
						Paint.getCurrentTab().imgHasBeenSaved = false;
						break;
					case EditToolBar.BLUR:
						{
						super.applyEffectToSelection(this.imc, e.getX(), e.getY(), new GaussianBlur());
						postDraw();
						break;
						}
					case EditToolBar.SEPIA:
						{
						super.applyEffectToSelection(this.imc, e.getX(), e.getY(), new SepiaTone());
						postDraw();
						break;
						}
					case EditToolBar.ROTATE:
						{
						double deg;
						try {
							deg = Double.parseDouble(Paint.edittoolbar.getOptionsField());
						} catch (NumberFormatException ex) {
							deg = 0;
							System.out.println("CustomCanvas.java; Failed to parse the options field:" + ex);
						}
						super.rotateSelection(this.imc, e.getX(), e.getY(), deg);
						postDraw();
						break;
						}
					case EditToolBar.FREENGON:
						if (this.freeNgonStack.size() == Integer.parseInt(Paint.edittoolbar.getOptionsField())) {
							Pair freengon = convStackToPair(this.freeNgonStack);
							this.gc.setFill(Paint.colorpick.getValue());
							super.drawGon((double[]) freengon.getKey(),
								(double[]) freengon.getValue(), 
								Integer.parseInt(Paint.edittoolbar.getOptionsField()),
								fill);
							this.freeNgonStack.clear();	
						}
						break;
					case EditToolBar.BUCKETFILL:
						super.bucketFill(this.imc, targetColor, Paint.colorpick.getValue());
						postDraw();
						break;
					default:
						break;
				}	
			this.imgToStack(this.getImage());
			}
			
		});
		
		/*
		this.setOnMouseEntered(e -> {
			if (Paint.getMode() == Paint.EDIT_MODE) {
				switch (Paint.edittoolbar.getDrawSelection()) {
					case EditToolBar.FREENGON:
						if (this.freeNgonStack.size() + 1 == Integer.parseInt(Paint.edittoolbar.getOptionsField())) {
							Stack tstack = (Stack) this.freeNgonStack.clone();
							tstack.add(new Pair(e.getX(), e.getY()));
							Pair tpair = convStackToPair(tstack);
							
							this.p.getPoints().addAll(this.convPairToArray(tpair));
						}
						break;
					default:
						break;
				}
			}
		});
		*/
		
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
						postDraw();
						break; 
				//this.imgToStack(this.getImage());
					case EditToolBar.PENCIL:
						this.gc.setFill(Paint.colorpick.getValue());
						this.gc.fillRect(x, y, bsize, bsize);
						this.imgToStack(this.getImage());
						postDraw();
						break;
					case EditToolBar.BLUR:
					case EditToolBar.DRAGDROP:
					case EditToolBar.ROTATE:
					case EditToolBar.SEPIA:
					case EditToolBar.RECTANGLE:
					case EditToolBar.CROP:
						this.r.setWidth(e.getX() - this.imc.getX());
						this.r.setHeight(e.getY() - this.imc.getY());
						break;
					case EditToolBar.SQUARE:
						double s;
						if (e.getX() >= e.getY()) {
							s = e.getX() - this.imc.getX();
						} else {
							s = e.getY() - this.imc.getY();
						}
						this.r.setWidth(s);
						this.r.setHeight(s);
						break;
					case EditToolBar.LINE:
						this.l.setEndX(e.getX());
						this.l.setEndY(e.getY());
						break;
					case EditToolBar.TRIANGLE:
						this.p.getPoints().addAll(
							this.convPairToArray(
								super.getPolygonPoints(
									3, 
									this.imc, 
									(int) e.getX()
								)
							)
						);
						break;
					case EditToolBar.NGON:
						this.p.getPoints().addAll(
							this.convPairToArray(
								super.getPolygonPoints(
									Integer.parseInt(
										Paint.edittoolbar.getOptionsField()
									), 
									this.imc, 
									(int) e.getX()
								)
							)
						);
						break;
					case EditToolBar.ELLIPSE:
						{
						double cx = this.imc.getX() + (e.getX() - this.imc.getX()) / 2;
						double cy = this.imc.getY() + (e.getY() - this.imc.getY()) / 2;
						this.ell.setCenterX(cx);
						this.ell.setCenterY(cy);
						this.ell.setRadiusX((e.getX() - this.imc.getX()) / 2);
						this.ell.setRadiusY((e.getY() - this.imc.getY()) / 2);
						break;
						}
					case EditToolBar.CIRCLE:
						double rad;
						if (e.getX() >= e.getY()) {
							rad = (e.getX() - this.imc.getX()) / 2;

						} else {
							rad = (e.getY() - this.imc.getY()) / 2;
						}
						this.ell.setCenterX(this.imc.getX() + rad);
						this.ell.setCenterY(this.imc.getY() + rad);
						this.ell.setRadiusX(rad);
						this.ell.setRadiusY(rad);
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
			//setDimensions(Paint.getCurrentTab().opened_image.getWidth(), Paint.getCurrentTab().opened_image.getHeight());
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
		setDimensions((int) i.getWidth(), (int) i.getHeight());
		//this.livecanvas.setHeight(i.getHeight());
		//this.livecanvas.setWidth(i.getWidth());
	}
	
	//this is a really hackyway of doing this, I want to make this much cleaner
	//(ie refactor)
	public void updateDimensions(boolean inc_zoom) {
		if (inc_zoom) {
			//if we want to increase the zoom
			//this.setScaleX(this.getScaleX() * 2);
			//this.setScaleY(this.getScaleY() * 2);
			this.setWidth(this.getWidth() * 2);
			this.setHeight(this.getHeight() * 2);
		} else {
			//if we want to decrease the zoom
			//this.setScaleX(this.getScaleX() * .5);
			//this.setScaleY(this.getScaleY() * .5);
			this.setWidth(this.getWidth() / 2);
			this.setHeight(this.getHeight() / 2);
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
	/**
	 * Method to be ran after an image is drawn to the canvas; removes live 
	 * drawing shapes, and marks the image as unsaved.
	 */
	public void postDraw() {
		Paint.getCurrentTab().pane.getChildren().remove(this.r);
		this.r = null;
		Paint.getCurrentTab().pane.getChildren().remove(this.l);
		this.l = null;
		Paint.getCurrentTab().pane.getChildren().remove(this.p);
		this.p = null;
		Paint.getCurrentTab().pane.getChildren().remove(this.ell);
		this.ell = null;
		this.targetColor = null;
		Paint.getCurrentTab().imgHasBeenSaved = false;
	}
	/**
	 * Interweaves a pair of double arrays; this is used to convert the output of 
	 * the 'getPolygonPoints' to be usable with the javafx polygon object.
	 * @param pts A pair of the X and Y points, both in the proper order
	 * @return A Double[] of the X and Y points
	 */
	public Double[] convPairToArray(Pair pts) {
		double[] xp = (double[]) pts.getKey();
		double[] yp = (double[]) pts.getValue();
		Double[] rp = new Double[xp.length + yp.length];
		for (int i = 0; i < xp.length; i++) {
			rp[i * 2] = xp[i];
			rp[i * 2 + 1] = yp[i];
		}
		return rp;
	}
	/**
	 * Converts a stack object full of pairs, into a singular pair object, with the
	 * key being all of the keys and the value being all of the values.
	 * @param s A stack of pairs.
	 * @return 
	 */
	public Pair<double[],double[]> convStackToPair(Stack s) {
		int sSize = s.size();
		double[] xp = new double[sSize];
		double[] yp = new double[sSize];
		Pair pt;
		for (int i = 0; i < sSize; i++) {
			pt = (Pair) s.pop();
			xp[i] = (double) pt.getKey();
			yp[i] = (double) pt.getValue();
		}
		return new Pair(xp, yp);
	}
	
}
