/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
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
				WritableImage wi = new WritableImage(
					Integer.parseInt(widthfld.getText()),
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
					+ Paint.PROGRAM_VER + " - " 
					+ Paint.RELEASE_STR);
		Text copytxt = new Text("Copyright 2020 Ethan Hawk");
		Text licetxt = new Text("Licensed under the GPLv3");
	
		Button closebtn = new Button();
		closebtn.setText("Ok");
			closebtn.setOnAction((ActionEvent aboutevent) -> {
				aboutStage.close();
			});

		Hyperlink rel_notes = new Hyperlink("https://raw.githubusercontent.com/ehawkvu/Paint/master/RELEASE_NOTES.txt");
		rel_notes.setWrapText(true);
		
		gp.add(nametxt, 0, 0);
		gp.add(copytxt, 0, 1);
		gp.add(licetxt, 0, 2);
		gp.add(new Text("Release Notes:"), 0, 3);
		gp.add(rel_notes, 1, 3);
		
		gp.add(closebtn, 0, 5);
		
		Scene aboutScene = new Scene(gp, 300, 200);
		aboutStage.setScene(aboutScene);
		aboutStage.setTitle("About");
		aboutStage.show();
	}
}
