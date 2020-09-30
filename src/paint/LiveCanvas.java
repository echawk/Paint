/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author ethan
 */
public class LiveCanvas extends ECanvas{

	public GraphicsContext gc;
	
	public LiveCanvas() {
		super();
		this.gc = this.getGraphicsContext2D();
	}
	
	public void setDimensions(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
	
}