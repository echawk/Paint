/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author ethan
 */
public class EditToolBar extends ToolBar{
	
	private ComboBox drawoptionmenu;
	
	
	public EditToolBar() {
		
		super();		
		
		Label drawlbl = new Label("Draw");
		
		this.drawoptionmenu = new ComboBox();
		//drawoptionmenu.autosize();
		drawoptionmenu.getItems().addAll("Line", "Pencil", "Square", "Circle", "Erase");
		drawoptionmenu.setValue("Line");
		drawoptionmenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (drawoptionmenu.getValue().equals("Line")) {
					System.out.println("Selected Line");
				} else if (drawoptionmenu.getValue().equals("Pencil")) {
					System.out.println("Selected Pencil");
				} else {
					System.out.println(getDrawSelection());
				}
			}
		});		
		
		Label brushlbl = new Label("Brush Size (px): ");
		TextField brushfld = new TextField(Double.toString(Paint.imgcanvas.brushSize));
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
			colorpick.setValue(Color.BLACK);
			brushfld.setText("5");
			Paint.imgcanvas.brushSize = Double.parseDouble(brushfld.getText());
			//update the values?
		});

		this.getItems().addAll(drawlbl, drawoptionmenu, brushlbl, brushfld, colorlbl, colorpick, resetbtn);
	}	
	
	
	public String getDrawSelection(){
		return this.drawoptionmenu.getValue().toString();
	}
	
}
