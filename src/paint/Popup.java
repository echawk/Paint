/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author ethan
 */
public class Popup {
	
	public static void launchCreateNewImage() {
		Stage createnewimgStage = new Stage();
		createnewimgStage.initOwner(Paint.window);
		GridPane gp = new GridPane();
		Text heightlbl = new Text("Height: ");
		TextField heightfld = new TextField();
			heightfld.setText("500");
		
		Text widthlbl = new Text("Width: ");
		TextField widthfld = new TextField();
			widthfld.setText("500");
			
		Button createbtn = new Button();
			createbtn.setText("Create");
			createbtn.setOnAction((ActionEvent event) -> {
				WritableImage wi = new WritableImage(Integer.parseInt(widthfld.getText()),
								Integer.parseInt(heightfld.getText()));
				ImageView iv = new ImageView(wi);
				Paint.setImage(iv.getImage());
				createnewimgStage.close();
			});
			
		Button cancelbtn = new Button();
			cancelbtn.setText("Cancel");
			cancelbtn.setOnAction((ActionEvent event) -> {
				createnewimgStage.close();
			});
		
			
		gp.add(heightlbl, 0, 0);
		gp.add(heightfld, 1, 0);
		
		gp.add(widthlbl, 0, 1);
		gp.add(widthfld, 1, 1);
		
		gp.add(cancelbtn, 0, 2);
		gp.add(createbtn, 1, 2);
		
		Scene createnewimgScene = new Scene(gp, 300, 300);
		createnewimgStage.setScene(createnewimgScene);
		createnewimgStage.setTitle("Create New Image");
		createnewimgStage.show();
	}
	

	public static void launchAboutWindow(){
		//Here is the dialog box related to the about
		//page. I am going to add in some basic info here
		//about the project.
		Stage aboutStage = new Stage();
		aboutStage.initOwner(Paint.window);
		GridPane gp = new GridPane();
		Text nametxt = new Text(Paint.PROGRAM_NAME + " - " 
			+ Paint.PROGRAM_VER + " - " + Paint.RELEASE_STR);
		Text copytxt = new Text("Copyright 2020 Ethan Hawk");
		Text licetxt = new Text("Licensed under the GPLv3");
	
		Button closebtn = new Button();
		closebtn.setText("Ok");
			closebtn.setOnAction((ActionEvent aboutevent) -> {
				aboutStage.close();
			});

		gp.add(nametxt, 0, 0);
		gp.add(copytxt, 0, 1);
		gp.add(licetxt, 0, 2);
		gp.add(closebtn, 0, 3);
		
		Scene aboutScene = new Scene(gp, 300, 200);
		aboutStage.setScene(aboutScene);
		aboutStage.setTitle("About");
		aboutStage.show();
	}
	
	// work on this part
	public static void launchEditOptionsWindow(){
		
		Stage drawStage = new Stage();
		drawStage.initOwner(Paint.window);
		GridPane gp = new GridPane();
		
		Text brushlbl = new Text("Brush Size (px): ");
		TextField brushsize = new TextField(Double.toString(Paint.imgcanvas.brushSize)); 
		//set the action for brushsize
			brushsize.setOnAction((ActionEvent event) ->{ 
				Paint.imgcanvas.brushSize = Double.parseDouble(brushsize.getText());
				//init_canvas();
			});
		Button brushapplybtn = new Button();
			brushapplybtn.setText("Apply");
			brushapplybtn.setOnAction((ActionEvent event) -> {
				Paint.imgcanvas.brushSize = Double.parseDouble(brushsize.getText());
			});
		
		gp.add(brushlbl, 0, 0);
		gp.add(brushsize, 1, 0);
		gp.add(brushapplybtn, 2, 0);
		
		Text colorlbl = new Text("Color: ");
		//set the action for hexcolorfield
		ColorPicker colorpick = Paint.imgcanvas.colorpick;
				
		gp.add(colorlbl, 0, 1);
		gp.add(colorpick, 1, 1);
		
		Button finishedbtn = new Button();
		finishedbtn.setText("Done");
			finishedbtn.setOnAction((ActionEvent lineevent) -> {
				drawStage.close();
				//should probably do some more stuff here
			});	
		
		gp.add(finishedbtn, 0, 2);
		
		Button resetbtn = new Button();
		resetbtn.setText("Reset Settings");
			resetbtn.setOnAction((ActionEvent event) -> {
				colorpick.setValue(Color.BLACK);
				brushsize.setText("5");
				
				Paint.imgcanvas.brushSize = Double.parseDouble(brushsize.getText());
				
				//update the values?
			});
		gp.add(resetbtn, 1, 2);
			
		Scene lineScene = new Scene(gp, 350, 350);
		drawStage.setScene(lineScene);
		drawStage.setTitle("Edit Options");
		drawStage.show();
	}
}
