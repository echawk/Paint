/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ethan
 */
public class ModeMenuTest {
	
	public ModeMenuTest() {
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
	 * Test of setMode method, of class ModeMenu.
	 */
	@Test
	public void testSetMode() {
		System.out.println("setMode");
		int i = 0;
		ModeMenu instance = new ModeMenu();
		instance.setMode(i);
		Assert.assertEquals(0, instance.getMode());
	}

	/**
	 * Test of getMode method, of class ModeMenu.
	 */
	@Test
	public void testGetMode() {
		System.out.println("getMode");
		ModeMenu instance = new ModeMenu();
		int expResult = 0;
		int result = instance.getMode();
		Assert.assertEquals(expResult, result);
	}
	
}
