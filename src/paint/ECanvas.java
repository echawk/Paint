/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.Stack;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 *
 * @author ethan
 */
public class ECanvas extends Canvas{
	
	public GraphicsContext gc;
	
	public ECanvas() {
		super();
		this.gc = this.getGraphicsContext2D();
	}
	
	public void drawRectangle(Pair ic, double cx, double cy, boolean f) {
		this.drawRectangle((double) ic.getKey(), (double) ic.getValue(), cx, cy, f);
	}
	public void drawRectangle(double ix, double iy, double cx, double cy, boolean f) {
		if (f) {
		this.gc.fillRect(				
			ix,
			iy,
			(cx - ix),				
			(cy - iy)
		);	
		} else {
		this.gc.strokeRect(
			ix, 
			iy, 
			(cx - ix), 
			(cy - iy)
		);
		}
	}
	
	public void drawEllipse(Pair ic, double cx, double cy, boolean f) {
		this.drawEllipse((double) ic.getKey(), (double) ic.getValue(), cx, cy, f);
	}
	public void drawEllipse(double ix, double iy, double cx, double cy, boolean f) {
		if (f) {
		this.gc.fillOval(
			ix, 
			iy, 
			(cx - ix), 
			(cy - iy)
		);
		} else {
		this.gc.strokeOval(
			ix, 
			iy, 
			(cx - ix), 
			(cy - iy)
		);
		}
	}
	
	public void drawSquare(Pair ic, double cx, double cy, boolean f) {
		this.drawSquare((double) ic.getKey(), (double) ic.getValue(), cx, cy, f);
	}
	/**
	 * Method to draw a square on the canvas, the side length is determined by
	 * whatever value is greater, cx or cy
	 * @param ix initial X 
	 * @param iy initial Y
	 * @param cx current X
	 * @param cy current Y
	 * @param f whether the shape is to be filled in or a frame
	 */
	public void drawSquare(double ix, double iy, double cx, double cy, boolean f) {
		double s;
		if (cx >= cy) {
			s = (cx - ix);
		} else {
			s = (cy - iy);
		}
		if (f) {
		this.gc.fillRect(
			ix, 
			iy, 
			s, 
			s
		);
		} else {
		this.gc.strokeRect(
			ix, 
			iy, 
			s, 
			s
		);
		}
	}
	
	public void drawCircle(Pair ic, double cx, double cy, boolean f) {
		this.drawCircle((double) ic.getKey(), (double) ic.getValue(), cx, cy, f);
	}
	/**
	 * Method to draw a circle on the canvas, the diameter is whatever value is 
	 * greater, cx or cy
	 * @param ix initial X
	 * @param iy initial Y
	 * @param cx current X
	 * @param cy current Y
	 * @param f whether the shape is to be filled in or a frame
	 */
	public void drawCircle(double ix, double iy, double cx, double cy, boolean f) {
		double d; //diameter
		if (cx >= cy) {
			d = (cx - ix);
		} else {
			d = (cy - iy);
		}	
		if (f) {
		this.gc.fillOval(
			ix,
			iy,
			d,
			d
		);
		} else {
		this.gc.strokeOval(
			ix,
			iy,
			d,
			d
		);
		}
	}
	
	public void drawLine(Pair ic, double cx, double cy) {
		this.drawLine((double) ic.getKey(), (double) ic.getValue(), cx, cy);
	}
	public void drawLine(double ix, double iy, double cx, double cy) {
		this.gc.strokeLine(
			ix,
			iy,
			cx,
			cy
		);	
	}
	
	/**
	 * Method to draw a triangle on the canvas.
	 * @param ic A pair of the initial coordinates
	 * @param cx The current X value
	 * @param f whether the shape is to be filled in or a frame
	 */
	public void drawTriangle(Pair ic, double cx, boolean f) {
		Pair PolygonPts = getPolygonPoints(
			3,
			ic,
			roundDouble(cx)
		);		
		double[] xp = (double[]) PolygonPts.getKey();
		double[] yp = (double[]) PolygonPts.getValue();
		if (f) {
		this.gc.fillPolygon(xp, yp, 3);
		} else {
		this.gc.strokePolygon(xp, yp, 3);
		}
	}
	/**
	 * Method to draw a 'n' sided polygon on the canvas.
	 * @param ic A pair of the input coordinates
	 * @param cx The current X value
	 * @param n The number of sides
	 * @param f whether the shape is to be filled in or a frame
	 */
	public void drawNGon(Pair ic, double cx, int n, boolean f) {
		Pair PolygonPts = getPolygonPoints(
			n,
			ic,
			roundDouble(cx)
		);		
		//2
		double[] xp = (double[]) PolygonPts.getKey();
		double[] yp = (double[]) PolygonPts.getValue();
		//3
		drawGon(xp, yp, n, f);
	}
	
	private int roundDouble(double d) {
		return (int) Math.round(d);
	}

	public void drawGon(double[] xp, double[] yp, int n, boolean f) {
			if (f) {
			this.gc.fillPolygon(xp, yp,n);
			} else {
			this.gc.strokePolygon(xp, yp, n);
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
	public Pair<double[],double[]> getPolygonPoints(int n, Pair initMouseCoord, int cx){
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
	/**
	 * Set the dimensions of the canvas.
	 * @param width
	 * @param height 
	 */
	public void setDimensions(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	/**
	 * This method returns the current canvas as an image.
	 * 
	 * @return An Image Object of the canvas.
	 */
	public Image getImage() {
		SnapshotParameters params = new SnapshotParameters();
		//params.setFill(Color.TRANSPARENT);
		WritableImage wi = this.snapshot(params, null);
		ImageView iv = new ImageView(wi);
		return iv.getImage();
	}
	/**
	 * This method returns the current selection as an Image.
	 * @param ic The initial mouse coordinates.
	 * @param cx current X
	 * @param cy current Y
	 * @return the subset of the current image enclosed by the selection
	 */
	public Image getSelectionAsImage(Pair ic, double cx, double cy) {
		PixelReader r = this.getImage().getPixelReader();
		double ix = (double) ic.getKey();
		double iy = (double) ic.getValue();
		WritableImage wi = new WritableImage(
			r,
			roundDouble(ix),
			roundDouble(iy),
			roundDouble(cx - ix),
			roundDouble(cy - iy)
		);
		return (Image) wi;
	}
	/**
	 * This method applies an effect to the current selection of the Image.
	 * @param ic The initial mouse coordinates.
	 * @param cx current X
	 * @param cy current Y
	 * @param e the effect to apply.
	 */
	public void applyEffectToSelection(Pair ic, double cx, double cy, Effect e) {
		Image wi = getSelectionAsImage(ic, cx, cy);
		//2
		CustomCanvas t = new CustomCanvas();
		t.updateDimensions(wi); //need to make sure the canvas has dimensions
		t.gc.setEffect(e);
		t.gc.drawImage(wi, 0, 0);
		this.gc.drawImage(
			t.getImage(), 
			(double) ic.getKey(), 
			(double) ic.getValue()
		);
	}
	/**
	 * This method rotates the current selection by a the provided number of degrees
	 * @param ic the initial mouse coordinates
	 * @param cx current X
	 * @param cy current Y
	 * @param deg the degrees to rotate the image by
	 */
	public void rotateSelection(Pair ic, double cx, double cy, double deg) {
		Image wi = getSelectionAsImage(ic, cx, cy);
		CustomCanvas t = new CustomCanvas();
		t.updateDimensions(wi); //need to make sure the canvas has dimensions
		t.gc.save();
		t.gc.rotate(deg);
		t.gc.drawImage(wi, 0, 0);
		t.gc.restore();
		this.gc.drawImage(
			t.getImage(), 
			(double) ic.getKey(), 
			(double) ic.getValue()
		);

	}
	
	/**
	 * Clears out the canvas of any drawn image by drawing a 'null' image.
	 */
	public void clear() {
		this.gc.drawImage(null, 0, 0);
	}

	//Clean up this section of the code!!!!!
	public void bucketFill(Pair ic, Color targetCol, Color replacementCol) {
		final double E = 0.3; //tolerance
		Stack<Point2D> ptStack = new Stack<>();
		Image oi = this.getImage();
		WritableImage wi = new WritableImage(
			oi.getPixelReader(),
			(int) oi.getWidth(),
			(int) oi.getHeight()
		);
		PixelReader wiReader = wi.getPixelReader();
		PixelWriter wiWriter = wi.getPixelWriter();
		
		Point2D startPt = new Point2D((double) ic.getKey(), (double) ic.getValue());
		
		ptStack.push(startPt);
		
		while (!ptStack.isEmpty()) {
			Point2D pt = ptStack.pop();
			int x = (int) pt.getX();
			int y = (int) pt.getY();
			if (filled(wiReader, x, y, targetCol, E)) {
				continue;
			}
			
			wiWriter.setColor(x, y, replacementCol);
			push(ptStack, x - 1, y - 1, wi);
			push(ptStack, x - 1, y    , wi);
			push(ptStack, x - 1, y + 1, wi);
			push(ptStack, x    , y + 1, wi);
			push(ptStack, x + 1, y + 1, wi);
			push(ptStack, x + 1, y    , wi);
			push(ptStack, x + 1, y - 1, wi);
			push(ptStack, x,     y - 1, wi);
		}
		
		this.gc.drawImage(wi, 0, 0);
		
	}
	
	private void push(Stack<Point2D> stack, int x, int y, Image i) {
            if (x < 0 || x > i.getWidth() ||
                y < 0 || y > i.getHeight()) {
                return;
            }

            stack.push(new Point2D(x, y));
        }

	
	private boolean filled(PixelReader reader, int x, int y, Color targetCol, double epsilon) {
            Color color = reader.getColor(x, y);

            return !withinTolerance(color, targetCol, epsilon);
        }

        private boolean withinTolerance(Color a, Color b, double epsilon) {
            return
                    withinTolerance(a.getRed(),   b.getRed(),   epsilon) &&
                    withinTolerance(a.getGreen(), b.getGreen(), epsilon) &&
                    withinTolerance(a.getBlue(),  b.getBlue(),  epsilon);
        }

        private boolean withinTolerance(double a, double b, double epsilon) {
            return Math.abs(a - b) < epsilon;
        }
}
