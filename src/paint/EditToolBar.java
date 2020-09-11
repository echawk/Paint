/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;

/**
 *
 * @author ethan
 */
public class EditToolBar extends ToolBar{
	
	private ComboBox drawoptionmenu;
	private TextField brushfld;

	public final String LINE = "Line";
	public final String PENCIL = "Pencil";
	public final String SQUARE = "Square";
	public final String RECTANGLE = "Rectangle";
	public final String CIRCLE = "Circle";
	public final String ELLIPSE = "Ellipse";
	public final String ERASE = "Erase";
	public final String COLOR_GRAB = "Color Grab";
	public final String NONE = "None";

	
	public EditToolBar() {
		
		super();		
		
		Button undobtn = new Button("Undo");
			undobtn.setOnAction((ActionEvent event)-> {
				Paint.imgcanvas.undo();
			});
		
		Label drawlbl = new Label("Draw:");
		
		this.drawoptionmenu = new ComboBox();
		//drawoptionmenu.autosize();
		drawoptionmenu.getItems().addAll(
				this.NONE,
				this.LINE, 
				this.PENCIL, 
				this.SQUARE,
				this.RECTANGLE, 
				this.CIRCLE, 
				this.ELLIPSE,
				this.COLOR_GRAB,
				this.ERASE);
		drawoptionmenu.setValue(this.NONE);
				
		Label brushlbl = new Label("Brush (px): ");
		brushfld = new TextField(Double.toString(Paint.imgcanvas.brushSize));
		brushfld.setMaxWidth(60); //change the Max width to something smaller (helps fit more on the first line)
		//set the action for brushsize
		brushfld.setOnAction((ActionEvent event) -> {
			Paint.imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
			//init_canvas();
		});
		Button brushapplybtn = new Button();
		brushapplybtn.setText("Apply");
		brushapplybtn.setOnAction((ActionEvent event) -> {
			Paint.imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
		});

		Label colorlbl = new Label("Color: ");
		//set the action for hexcolorfield
		ColorPicker colorpick = Paint.imgcanvas.colorpick;


		Button resetbtn = new Button();
		resetbtn.setText("Reset Settings");
		resetbtn.setOnAction((ActionEvent event) -> {
			setDefaults();
			//update the values?
		});

		this.getItems().addAll(
			undobtn, 
			drawlbl, 
			drawoptionmenu, 
			brushlbl, 
			brushfld, 
			colorlbl, 
			colorpick, 
			resetbtn);
	}	
	
	
	public String getDrawSelection(){
		return this.drawoptionmenu.getValue().toString();
	}
	
	private void setDefaults(){
		Paint.imgcanvas.colorpick.setValue(Color.BLACK);
		Paint.imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
		this.brushfld.setText("5");
		this.drawoptionmenu.setValue(this.NONE);
	}
}
