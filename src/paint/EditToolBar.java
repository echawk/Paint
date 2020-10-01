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
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/**
 *
 * @author ethan
 */
public class EditToolBar extends ToolBar{

	private ComboBox drawoptionmenu;
	private TextField brushfld;
	private TextField options_fld;

	public static final String LINE = "Line";
	public static final String PENCIL = "Pencil";
	public static final String SQUARE = "Square";
	public static final String RECTANGLE = "Rectangle";
	public static final String CIRCLE = "Circle";
	public static final String ELLIPSE = "Ellipse";
	public static final String ERASE = "Erase";
	public static final String COLOR_GRAB = "Color Grab";
	public static final String NONE = "None";
	public static final String TEXTBOX = "Text Box";
	public static final String TRIANGLE = "Triangle";
	public static final String NGON = "N-gon";
	public static final String DRAGDROP = "Drag & Drop";
	//Extras
	public static final String BLUR = "Blur";
	public static final String CROP = "Crop";
	public static final String ROTATE = "Rotate"; //kinda broken
	public static final String SEPIA = "Sepia";

	public EditToolBar() {

		super();

		Button undobtn = new Button("Undo");
			undobtn.setOnAction((ActionEvent event)-> {
				try {
					Paint.getCurrentTab().undo();
				} catch (Exception e) {
					System.out.println("EditToolBar.java; Undo Button On-Action Failed:" + e);
					e.printStackTrace();
				}
			});
			undobtn.setTooltip(new Tooltip("Undo a draw action"));
		Button redobtn = new Button("Redo");
			redobtn.setOnAction((ActionEvent event) -> {
				try {
					Paint.getCurrentTab().redo();
				} catch (Exception e) {
					System.out.println("EditToolBar.java; Redo Button On-Action Failed:" + e);
					e.printStackTrace();
				}
			});
			redobtn.setTooltip(new Tooltip("Redo an undone action"));
		Label drawlbl = new Label("Draw:");

		this.drawoptionmenu = new ComboBox();
		//drawoptionmenu.autosize();
		drawoptionmenu.getItems().addAll(
				EditToolBar.NONE,
				EditToolBar.LINE,
				EditToolBar.PENCIL,
				EditToolBar.SQUARE,
				EditToolBar.RECTANGLE,
				EditToolBar.CIRCLE,
				EditToolBar.ELLIPSE,
				EditToolBar.TRIANGLE,
				EditToolBar.NGON,
				EditToolBar.COLOR_GRAB,
				EditToolBar.TEXTBOX,
				EditToolBar.CROP,
				EditToolBar.DRAGDROP,
				EditToolBar.ROTATE, // broken
				EditToolBar.BLUR,
				EditToolBar.ERASE
		);
		drawoptionmenu.setValue(this.NONE); //Set our default value to be NONE
		drawoptionmenu.setTooltip(new Tooltip("Select a tool to draw with"));

		Label optionslbl = new Label("Option:");
		options_fld = new TextField();
		options_fld.setMaxWidth(60); //Set to 60 to try to minimize the amount of wasted space.

		Label brushlbl = new Label("Brush (px):");

		brushfld = new TextField(Double.toString(Paint.brushSize));

		brushfld.setMaxWidth(60); //change the Max width to something smaller (helps fit more on the first line)
		//set the action for brushsize
		brushfld.setOnAction((ActionEvent event) -> {
			try {
				Paint.brushSize = Double.parseDouble(brushfld.getText());
			} catch (Exception e) {
				System.out.println("EditToolBar.java; Brush Field On-Action Failed:" + e);
			}
		});

		Label colorlbl = new Label("Color:");

		ColorPicker colorpick = Paint.colorpick;
		Button resetbtn = new Button();
		resetbtn.setText("Reset");
		resetbtn.setOnAction((ActionEvent event) -> {
			setDefaults();
			//update the values?
		});
		resetbtn.setTooltip(new Tooltip("Reset draw settings"));

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
	 * Returns whatever string is present in options_fld.
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
		Paint.brushSize = Double.parseDouble(brushfld.getText());
		this.drawoptionmenu.setValue(this.NONE);
		this.options_fld.setText(null);
	}
}
