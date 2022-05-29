package app;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class Mvp_ascii_art_Test {
	Mvp_ascii_art mvp;
	String strang; //the string of allowed ascii characters
	BufferedImage bImage;
	BufferedImage bImage2;
	int[][] vals;
	FileReader fr;
	File file = new File("C:\\Users\\wlind\\Pictures\\flag.jpg\\");
	int count;
	int resizeWidth;
	int resizeHeight;
	
	@Before
	public void setUp() throws Exception {
		this.mvp = new Mvp_ascii_art();
		this.resizeWidth = mvp.getResizeWidth();
		this.resizeHeight = mvp.getResizeHeight();
	}
	
	@Test //tries to read image for tuple values, checks for null
	public void testLoadImage() throws IOException {
		mvp.loadImage(file);
		bImage = mvp.getImage();
		assertNotNull(bImage);
		assertTrue(mvp.isImage());
	}
	
	@Test
	public void testResize() throws IOException {
		mvp.loadImage(file);
		mvp.resizeImage(resizeWidth,resizeHeight);
		bImage = mvp.getImage();
		assertTrue("Width is: " + bImage.getWidth(), bImage.getWidth() <= 320);
		assertTrue("Height is: " + bImage.getHeight(), bImage.getHeight() <= 320);
	}
	
	@Test
	public void testRotate() throws IOException {
		//using a 480x640 image, will our rotate method turn it 90 degrees correctly?
		File testRotateFile = new File("C:\\Users\\wlind\\Pictures\\chloeTeeth.JPG");
		mvp.loadImage(testRotateFile);
		bImage = mvp.getImage();
		assertTrue("Width is: " + bImage.getWidth() + " and Height is: " + bImage.getHeight(),
				bImage.getWidth() == 480 && bImage.getHeight() == 640);
		mvp.rotateImage90();
		bImage2 = mvp.getImage();
		assertTrue("Width is: " + bImage2.getWidth() + " and Height is: " + bImage2.getHeight(),
				bImage2.getWidth() == 640 && bImage2.getHeight() == 480);
	}
	
	@Test
	public void testCreateBMatrixWidthAndHeight() throws Exception {
		boolean isValidWidthAndHeight = true;
		mvp.loadImage(file);
		mvp.resizeImage(resizeWidth, resizeHeight);
		bImage = mvp.getImage();
		mvp.createBrightnessMatrix(bImage, "Average");
		vals = mvp.getBMatrixArray();
		int width = vals.length; //should be 320 or less depending on resize parameters
		int height = vals[0].length;
		if(width > 320 || width < 0) {
			isValidWidthAndHeight = false;
		}
		if(height > 320 || height < 0) {
			isValidWidthAndHeight = false;
		}
		assertTrue(isValidWidthAndHeight);
	}
	
	@Test
	public void testBMatrixValWithinBounds() throws IOException {
		boolean isOOB = false;
		mvp.loadImage(file);
		mvp.resizeImage(resizeWidth, resizeHeight);
		bImage = mvp.getImage();
		
		try {
			mvp.createBrightnessMatrix(bImage, "Average");
		} catch (Exception e) {
			e.printStackTrace();
		}
		vals = mvp.getBMatrixArray();

		outerloop: //label for below
		for (int[] innerArray : vals) {
			for(int val : innerArray) {
				if ( val > 255 || val < 0) {
					isOOB = true;
					break outerloop;
				}else {
					continue;
				}
			}
		} //end for each loops
		assertFalse(isOOB);
	} //end method
	
	@Test
	public void testCreateAsciiMatrixAndFileWrite() throws Exception {
	
		strang = mvp.getAllowedAscii();
		mvp.loadImage(file);
		mvp.resizeImage(resizeWidth, resizeHeight);
		bImage = mvp.getImage();
		mvp.createBrightnessMatrix(bImage, "Average");
		vals = mvp.getBMatrixArray();
		count = mvp.createAsciiMatrix(vals, strang);
		//test for counter is implicit in file reader test below
		//test bounds of String? total = pixels + 1x '/n' per unit of height
		fr = new FileReader("ASCIIedImage" + count + ".txt");
		assertNotNull(fr);
	}
}
