/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author ethan
 */
public class Paint extends Application {
	
	//Constants
	final static String PROGRAM_NAME = "Pain(t)";
	final static String PROGRAM_VER = "0.2.1";
	final static String RELEASE_STR = "Where is my Mind?";
	final static int DEFAULT_MODE = 0;
	final static int EDIT_MODE = 1;
	final static int AUTOSAVE_INTERVAL = 20; //in seconds
	
	//pointers
	public static Stage window; //basically primaryStage
	public static TabPane tab;
	public static CustomMenuBar menub;
	public static EditToolBar edittoolbar;

	//Variables
	public static int mode = DEFAULT_MODE; //default to default...
	public static boolean AUTOSAVEON = true; //have autosave be on by default
	public static ColorPicker colorpick = new ColorPicker();
	public static double brushSize = 5;
	
	@Override
	public void start(Stage primaryStage) {
		
		//Platform.setImplicitExit(false);
		Platform.setImplicitExit(false);
	//setup the window pointer
		Paint.window = primaryStage; //have window refer to primaryStage
		
	//edit toolbar
		Paint.edittoolbar = new EditToolBar();
		Paint.edittoolbar.setVisible(false);
		//would like it to be completely invisible, maybe adjust the max width and height?
	
	//menu bar
		Paint.menub = new CustomMenuBar();
		
	//tab pane
		Paint.tab = new TabPane();
		Paint.tab.getTabs().add(
			new CustomTab("Welcome!")
		);
		
	//color picker
		Paint.colorpick.setValue(Color.BLACK);
		
	//root
		VBox root = new VBox(); //set up how the windows will laid out
		root.getChildren().addAll(menub, edittoolbar, tab);
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
	
		//start up the autosave timer
		//AutoSaveTimer time = new AutoSaveTimer();
		//Platform.runLater(time);
		//Thread timeThread = new Thread(time);
		//timeThread.start();
		Timeline autosave = new Timeline(
			new KeyFrame(Duration.seconds(Paint.AUTOSAVE_INTERVAL),
				ev -> {
					System.out.println("Autosave is running");
					if (Paint.AUTOSAVEON) {
						System.out.println("Autosave is on");
						if (Paint.getCurrentTab().opened_file != null) {
							System.out.println("The opened file is not null");
							if (!Paint.getCurrentTab().imgHasBeenSaved) {
								System.out.println("Image has not been saved");
								CustomFileHandler.saveFile();
							}
						}
					}
				}
			)
		);
		autosave.setCycleCount(Animation.INDEFINITE);
		autosave.play();
		/*
		Timeline logger = new Timeline(
			new KeyFrame(Duration.seconds(1),
				ev -> {
					
				})
		);
		*/
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
		
	/**
	 * Closes Paint nicely.
	 */
	public static void close() {
		Paint.window.close(); //close the main window/stage
		System.exit(0); //Have a successful exit code.
	}
	/**
	 * 
	 * @return The "mode" of Paint
	 */
	public static int getMode(){
		return Paint.mode;
	}
	/**
	 * Sets Paint's mode, with idiot proofing built in!
	 * @param i 
	 */
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
	
	/**
	 * This method is intended to be a general purpose update script, currently
	 * it only handles showing and hiding the edit tool bar, but it could be expanded
	 * to do anything else that warrants updating the window. 
	 */
	public static void update() {
		if (getMode() == Paint.EDIT_MODE) {
			showEditToolBar();
		} else {
			hideEditToolBar();
		}
	}
	
	/**
	 * 
	 * Add a new CustomTab to Paint.tab (the TabPane). 
	 * 
	 * @param lbl A string of whatever you want the label to be
	 * @param i The image you want to be shown on the tab
	 */
	public static void addTab(String lbl, Image i) {
		
		CustomTab t = new CustomTab(lbl);
		Paint.tab.getTabs().add(
			t
		);
		Paint.tab.getSelectionModel().select(t);
		t.setImage(i); 
	}
	
	/**
	 * Add a new CustomTab to Paint.tab (the TabPane). The tab title will be 
	 * whatever the file name is.
	 * 
	 * @param f A file that you want to have a tab for.
	 * @throws FileNotFoundException 
	 */
	public static void addTab(File f) throws FileNotFoundException {
		CustomTab t = new CustomTab(f.getName());
		t.opened_file = f;
		Paint.tab.getTabs().add(
			t
		);
		Paint.tab.getSelectionModel().select(t);
		t.setImage(new Image(new FileInputStream(f)));
	}
	
	/**
	 * 
	 * @return The current tab that is selected by Paint.
	 */
	public static CustomTab getCurrentTab() {
		return (CustomTab) Paint.tab.getSelectionModel().getSelectedItem();
	}
	
}
