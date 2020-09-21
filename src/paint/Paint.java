/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author ethan
 */
public class Paint extends Application {
	
	//Constants
	final static String PROGRAM_NAME = "Pain(t)";
	final static String PROGRAM_VER = "0.1.1";
	final static String RELEASE_STR = "Paint it Black";
	final static int DEFAULT_MODE = 0;
	final static int EDIT_MODE = 1;
	
	//Some global variables that control some vital parts of the program
	public static CustomMenuBar menub;
	public static EditToolBar edittoolbar;
	
	public static int mode = DEFAULT_MODE; //default to default...
	
	//pointers
	public static Stage window; //basically primaryStage
	
	public static TabPane tab;
	
	//Moving these to CustomTab
	public static File opened_file; //whatever file is opened
	public static Image opened_image; 
	public static ScrollPane scroll;
	public static CustomCanvas imgcanvas = new CustomCanvas();
	public static ColorPicker colorpick = new ColorPicker();
	
	@Override
	public void start(Stage primaryStage) {
	
		
		
	//setup the window pointer
		Paint.window = primaryStage; //have window refer to primaryStage
		
	//menu bar
		Paint.menub = new CustomMenuBar();
		
	//edit toolbar
		Paint.edittoolbar = new EditToolBar();
		Paint.edittoolbar.setVisible(false);
		//would like it to be completely invisible, maybe adjust the max width and height
	//tab pane
		Paint.tab = new TabPane();
		Paint.tab.getTabs().add(
			new CustomTab("asdf")
		);
	//Paint.tab.getSelectionModel().getSelectedItem();

	//scroll pane
		Paint.scroll = new ScrollPane();
		scroll.setContent(imgcanvas);
		//scroll.setVisible(false);
	
	//root
		VBox root = new VBox(); //set up how the windows will laid out
		root.getChildren().addAll(menub, edittoolbar, Paint.tab);
		//root.getChildren().addAll(imgcanvas, menub);
		//root.setAlignment(menub, Pos.TOP_CENTER); //center the menubar at the top of the screen
		//root.setAlignment(imgv, Pos.BOTTOM_CENTER);
	
	//scene setup
		Scene scene = new Scene(root, 1000, 500); //create the scene
		//themeing code down here??
		//scene.getRoot().setStyle("-fx-base:black");
		//scene.getStylesheets().add("dark-theme.css");
		//scene.getStylesheets().add(getClass().getResource("dark-theme-2.css").toString());
	//setup the main window
		primaryStage.setTitle(PROGRAM_NAME + " - " + PROGRAM_VER);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Maybe have a welcome window? 
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	//moved to CustomTab
	public static void clearImage(){
		//set the image to be nothing
		Paint.opened_file = null;
			/* set the opened_file to be null,to prevent accidentally 
			saving & deleting the image
			*/
		Paint.opened_image = null; //Same reasoning as above ^^^
		Paint.setImage(null);
	}
	
	//moved to CustomTab
	public static void setImage(Image img){
		Paint.opened_image = img; //set the opened_image pointer to image
		Paint.imgcanvas.updateDimensions(); //update the canvas dimensions
		Paint.imgcanvas.gc.drawImage(opened_image, 0, 0);
	}
	
	public static void close() {
		Paint.window.close(); //close the main window/stage
		System.exit(0); //Have a successful exit code.
	}
	
	public static int getMode(){
		return Paint.mode;
	}
	
	public static void setMode(int i) {
		if (i == Paint.EDIT_MODE) {
			Paint.mode = i;
		} else { //if 0, or something else, return to default.
			Paint.mode = Paint.DEFAULT_MODE;
		}
	}
	
	public static void showEditToolBar() {
		Paint.edittoolbar.setVisible(true);
	}
	
	public static void hideEditToolBar() {
		Paint.edittoolbar.setVisible(false);
	}
	//moved to CustomTab
	public static void setScrollPrefSize(double x, double y){
		Paint.scroll.setPrefSize(x, y);
	}

	public static void update() {
		// gen purpose update script
		
		if (getMode() == Paint.EDIT_MODE) {
			showEditToolBar();
		} else {
			hideEditToolBar();
		}
	}
	
	
	//To be implemented....
	public static void addTab(String lbl, Image i) {
		
		CustomTab t = new CustomTab(lbl);
		t.setImage(i);
		Paint.tab.getTabs().add(
			t
		);
		Paint.tab.getSelectionModel().select(t);
	}
	
	public static CustomTab getCurrentTab() {
		return (CustomTab) Paint.tab.getSelectionModel().getSelectedItem();
	}
	
}
