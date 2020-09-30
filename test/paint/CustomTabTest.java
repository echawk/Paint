/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ethan
 */
public class CustomTabTest {
	
	public CustomTabTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of setImage method, of class CustomTab.
	 */
	@Test
	public void testSetImage() {
		System.out.println("setImage");
		String label = "label";
		Image img = null;
		CustomTab instance = new CustomTab(label);
		instance.setImage(img);
		assertEquals(null, instance.imgcanvas.getImage());
	}

	/**
	 * Test of clearImage method, of class CustomTab.
	 */
	@Test
	public void testClearImage() {
		System.out.println("clearImage");
		String label = "label";
		CustomTab instance = new CustomTab(label);
		instance.setImage(new Image("example"));
		instance.clearImage();
		assertEquals(null, instance.imgcanvas.getImage());
	}

	/**
	 * Test of setScrollPrefSize method, of class CustomTab.
	 */
	@Test
	public void testSetScrollPrefSize() {
		System.out.println("setScrollPrefSize");
		double x = 0.0;
		double y = 0.0;
		CustomTab instance = null;
		instance.setScrollPrefSize(x, y);
	}

	/**
	 * Test of undo method, of class CustomTab.
	 */
	@Test
	public void testUndo() {
		System.out.println("undo");
		CustomTab instance = new CustomTab("label");
		Image i1 = (Image) new WritableImage(50, 50);
		instance.setImage(i1);
		instance.imgcanvas.gc.fillRect(0, 0, 10, 10);
		instance.undo();
		assertEquals(i1, instance.imgcanvas.getImage());
	}

	/**
	 * Test of redo method, of class CustomTab.
	 */
	@Test
	public void testRedo() {
		System.out.println("redo");
		CustomTab instance = new CustomTab("label");
		Image i1 = (Image) new WritableImage(50, 50);
		instance.setImage(i1);
		instance.imgcanvas.gc.fillRect(0, 0, 10, 10);
		Image i2 = instance.imgcanvas.getImage();
		instance.undo();
		instance.redo();
		assertEquals(i2, instance.imgcanvas.getImage());
	}
	
}
