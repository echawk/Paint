/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	
	public void drawRectangle(Pair ic, double cx, double cy) {
		this.drawRectangle((double) ic.getKey(), (double) ic.getValue(), cx, cy);
	}
	
	public void drawRectangle(double ix, double iy, double cx, double cy) {
		this.gc.fillRect(				
			ix,
			iy,
			(cx - ix),				
			(cy - iy)
		);	
	}
	
	public void drawEllipse(Pair ic, double cx, double cy) {
		this.drawEllipse((double) ic.getKey(), (double) ic.getValue(), cx, cy);
	}
	public void drawEllipse(double ix, double iy, double cx, double cy) {
		this.gc.fillOval(
			ix, 
			iy, 
			(cx - ix), 
			(cy - iy)
		);
	}
	
	public void drawSquare(Pair ic, double cx, double cy) {
		this.drawSquare((double) ic.getKey(), (double) ic.getValue(), cx, cy);
	}
	public void drawSquare(double ix, double iy, double cx, double cy) {
		double s;
		if (cx >= cy) {
			s = (cx - ix);
		} else {
			s = (cy - iy);
		}
		this.gc.fillRect(
			ix, 
			iy, 
			s, 
			s
		);
	}
	
	public void drawCircle(Pair ic, double cx, double cy) {
		this.drawCircle((double) ic.getKey(), (double) ic.getValue(), cx, cy);
	}
	public void drawCircle(double ix, double iy, double cx, double cy) {
		double d; //diameter
		if (cx >= cy) {
			d = (cx - ix);
		} else {
			d = (cy - iy);
		}	
		this.gc.fillOval(
			ix,
			iy,
			d,
			d
		);
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
	
	public void drawTriangle(Pair ic, double cx) {
		Pair PolygonPts = getPolygonPoints(
			3,
			ic,
			roundDouble(cx)
		);		
		double[] xp = (double[]) PolygonPts.getKey();
		double[] yp = (double[]) PolygonPts.getValue();
		this.gc.fillPolygon(xp, yp, 3);
	}
	
	public void drawNGon(Pair ic, double cx, int n) {
		Pair PolygonPts = getPolygonPoints(
			n,
			ic,
			roundDouble(cx)
		);		
		//2
		double[] xp = (double[]) PolygonPts.getKey();
		double[] yp = (double[]) PolygonPts.getValue();
		//3
		this.gc.fillPolygon(xp, yp, n);

	}
	
	private int roundDouble(double d) {
		return (int) Math.round(d);
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
	
	/**
	 * Clears out the canvas of any drawn image by drawing a 'null' image.
	 */
	public void clear() {
		this.gc.drawImage(null, 0, 0);
	}
	
	
	
}
