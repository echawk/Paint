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
	private TextField options_fld;

	public final String LINE = "Line";
	public final String PENCIL = "Pencil";
	public final String SQUARE = "Square";
	public final String RECTANGLE = "Rectangle";
	public final String CIRCLE = "Circle";
	public final String ELLIPSE = "Ellipse";
	public final String ERASE = "Erase";
	public final String COLOR_GRAB = "Color Grab";
	public final String NONE = "None";
	public final String TEXTBOX = "Text Box";
	public final String TRIANGLE = "Triangle";
	public final String NGON = "N-gon";
	public final String DRAGDROP = "Drag & Drop";
	//Extras
	public final String BLUR = "Blur";
	public final String CROP = "Crop";
	
	public EditToolBar() {
		
		super();		
		
		Button undobtn = new Button("Undo");
			undobtn.setOnAction((ActionEvent event)-> {
				if (Paint.TABBED) {
					Paint.getCurrentTab().imgcanvas.undo();	
				} else {
					Paint.imgcanvas.undo();
				}
			});
		
		Button redobtn = new Button("Redo");
			redobtn.setOnAction((ActionEvent event) -> {
				if (Paint.TABBED) {
					Paint.getCurrentTab().imgcanvas.redo();
				} else {
					Paint.imgcanvas.redo();
				}
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
				this.TRIANGLE,
				this.NGON,
				this.COLOR_GRAB,
				this.TEXTBOX,
				this.BLUR,
				this.CROP,
				this.DRAGDROP,
				this.ERASE
		);
		drawoptionmenu.setValue(this.NONE); //Set our default value to be NONE
		
		
		Label optionslbl = new Label("Option:");
		options_fld = new TextField();
		options_fld.setMaxWidth(60); //Set to 60 to try to minimize the amount of wasted space.
		
		Label brushlbl = new Label("Brush (px):");
		brushfld = new TextField(Double.toString(Paint.imgcanvas.brushSize));
					//Paint.getCurrentTab().imgcanvas.brushSize;
		brushfld.setMaxWidth(60); //change the Max width to something smaller (helps fit more on the first line)
		//set the action for brushsize
		brushfld.setOnAction((ActionEvent event) -> {
			if (Paint.TABBED) {
				Paint.getCurrentTab().imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
			} else {
				Paint.imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
			}
		});
		
		Label colorlbl = new Label("Color:");
		//ColorPicker colorpick = Paint.imgcanvas.colorpick; //this line will need to be updated?
		
		ColorPicker colorpick = Paint.colorpick;
		Button resetbtn = new Button();
		resetbtn.setText("Reset");
		resetbtn.setOnAction((ActionEvent event) -> {
			setDefaults();
			//update the values?
		});

		this.getItems().addAll(
			undobtn, 
			redobtn,
			drawlbl, 
			drawoptionmenu, 
			brushlbl, 
			brushfld, 
			colorlbl, 
			colorpick, 
			optionslbl,
			options_fld,
			resetbtn
		);
	}	
	
	/**
	 * This method is gets the string value of the selected option; this method is 
	 * used primarily in the CustomCanvas Handling code.
	 * 
	 * @return the String of the combobox's selected option
	 */
	public String getDrawSelection(){
		return this.drawoptionmenu.getValue().toString();
	}
	/**
	 * 
	 * @return The String of the options field.
	 */
	public String getOptionsField(){
		return this.options_fld.getText();
	}
	
	/**
	 * This method sets all of the configurable part of the edit tool bar back to their default values.
	 */
	private void setDefaults(){
		Paint.colorpick.setValue(Color.BLACK);
		this.brushfld.setText("5");
		if (Paint.TABBED) {
			Paint.getCurrentTab().imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
		} else {
			Paint.imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
		}
		this.drawoptionmenu.setValue(this.NONE);
		this.options_fld.setText(null);
	}
}
