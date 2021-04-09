/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * This class is responsible for making tabs in Paint. These tabs have had more
 * functionality added to them, and support images in a scrollpane.
 *
 * @author ethan
 */
public class CustomTab extends Tab {
    //variables
    public File opened_file;
    public Image opened_image;
    /**
     * Boolean that keeps track of edits to the image, when edits are made,
     * and the image hasn't been saved, it is false, when the user saves the image
     * or if the auto save feature save the image, it is true.
     */
    public boolean imgHasBeenSaved;
    //viewable elements
    public ScrollPane scroll;
    public CustomCanvas imgcanvas = new CustomCanvas();
    public Pane pane = new Pane();

    public CustomTab(String label) {
        super(label);

        this.scroll = new ScrollPane();
        this.pane.getChildren().add(this.imgcanvas);
        this.scroll.setContent(this.pane);

        this.setOnCloseRequest(e -> {
            //if the file hasn't been saved, ask if the user would
            //like to save it
            //Popup.closeConfirmation();
        });

        //set the tab to have the scroll
        this.setContent(this.scroll);
    }
    /**
     * Sets this Tab's image to be whatever Image object is provided; calls
     * updateDimensions, then draws the image using the Tab's imgcanvas.
     *
     * @param img The image object you want to be in this tab
     */
    public void setImage(Image img) {
        try {
            this.opened_image = img; //set the opened_image pointer to image
            this.imgcanvas.updateDimensions(); //update the canvas dimensions
            this.imgcanvas.gc.drawImage(
                opened_image,
                0,
                0
            );
        } catch (Exception e) {
            System.out.println("CustomTab.java; Failed to setImage:" + e);
        }
    }
    /**
     * Clears whatever the opened image is for the tab, it is made obsolete by
     * the fact Paint is no longer a single image editor, but is here for
     * historical reasons.
     */
    public void clearImage() {
        //set the image to be nothing
        this.opened_file = null;
        /* set the opened_file to be null,to prevent accidentally
        saving & deleting the image
        */
        this.opened_image = null; //Same reasoning as above ^^^
        this.setImage(null);
    }
    /**
     *
     * Set the ScrollPane's preferred size, this method is called whenever
     * either of the zoom methods are called, as to keep the imgcanvas and the
     * scrollpane the same size.
     *
     * @param x The width
     * @param y The height
     */
    public void setScrollPrefSize(double x, double y) {
        this.scroll.setPrefSize(x, y);
    }

    /**
     * Undo the last edit on the imgcanvas.
     */
    public void undo() {
        this.imgcanvas.undo();
    }

    /**
     * Redo the last action that was undone.
     */
    public void redo() {
        this.imgcanvas.redo();
    }

}
