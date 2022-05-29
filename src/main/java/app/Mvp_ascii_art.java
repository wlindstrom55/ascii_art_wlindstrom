package app;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.lang3.math.NumberUtils;
import org.imgscalr.Scalr;

public class Mvp_ascii_art {

//fields
	//changing these allowedAscii strings inverts the brightness/darkness of the image.
	//TODO implement a button
	//private final String allowedAscii = " `^\",:;Il!i~+_-?][}{1)(|\'/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
	private final String allowedAscii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\'|()1{}[]?-_+~i!lI;:,\"^` ";
	private BufferedImage image;
	private int[][] bMatrixArray;
	private int resizeWidth = 320;
	private int resizeHeight = 320;
	
//some getters to expose class for testing:
	public String getAllowedAscii() {
		return allowedAscii;
	}
	public BufferedImage getImage() { 
		return image;
	}		
	public int[][] getBMatrixArray() {
		return bMatrixArray;
	}
	public int getResizeWidth() {
		return resizeWidth;
	}
	public int getResizeHeight() {
		return resizeHeight;
	}

	
//methods:
	public void loadImage(File file) throws IOException { 
			 image = ImageIO.read(file);
	}
	
	public boolean isImage() {
        try {
        	image.getRGB(0, 0);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
	
	//start() calls the class logic methods below; passes the int counter for doc naming
	public int start() throws Exception {
		resizeImage(resizeWidth, resizeHeight);
	    createBrightnessMatrix(image, "Average"); 
	    return createAsciiMatrix(bMatrixArray, allowedAscii);
	    }
	 
	 public void rotateImage90() {
	 image = Scalr.rotate(image, Scalr.Rotation.CW_90, Scalr.OP_ANTIALIAS);
	}
	
	 public void rotateImage180() {
	 image = Scalr.rotate(image, Scalr.Rotation.CW_180, Scalr.OP_ANTIALIAS);
	}
	
	 public void rotateImage270() {
	 image = Scalr.rotate(image, Scalr.Rotation.CW_270, Scalr.OP_ANTIALIAS);
	}
	
	 public void resizeImage(int resizeWidth, int resizeHeight) { 
	 image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
		resizeWidth, resizeHeight, Scalr.OP_ANTIALIAS); 
	//80x80 seems to work well for smaller size images. Then 160x160, 320x320
	//Corresponding console width is 240, 480, 960 (if not writing to file)
	}

	public void createBrightnessMatrix(BufferedImage resizedInput, String algorithmChoice) throws Exception {
		int width = resizedInput.getWidth(); 
		int height = resizedInput.getHeight();
		int[][] pixels = new int[width][height];
	
		for(int i=0; i < width; i++) {
			for(int j=0; j < height; j++) {
				Color c = new Color(resizedInput.getRGB(i, j));
				if (algorithmChoice == "Average") {
					pixels[i][j] = ((c.getRed() + c.getGreen() + c.getBlue()) / 3);
				}
				else if (algorithmChoice == "Max/Min") {
					pixels[i][j] = (NumberUtils.max(c.getRed(), c.getGreen(), c.getBlue()) +
							NumberUtils.min(c.getRed(), c.getGreen(), c.getBlue())) / 2; 
				}
				else if (algorithmChoice == "Luminosity") { 
					pixels[i][j] = (int) ((0.21 * c.getRed()) + (0.72 * c.getGreen()) + (0.07 * c.getBlue()));	
				}
				else {
					throw new Exception("Please make sure the brightness algorithm selection is appropriate.");
				}
			}
		} //end for loops
		bMatrixArray = pixels.clone();
	}

	public int createAsciiMatrix(int[][] input, String asciiChars) {
		String asciiTxt = "";
		FileWriter fw = null;
		File fileCont;
		int count = 0;
		int widthArr = input.length;
		int heightArr = input[0].length;
    
		while (true) {
			//runs until file is created, iterates count otherwise
			fileCont = new File("ASCIIedImage" + count + ".txt");
			if (!fileCont.isFile()) {
				try {
					fileCont.createNewFile();
				} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			break;
			}
		count++;
		}
		
		try {
            fw = new FileWriter("ASCIIedImage" + count + ".txt");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
		
		//these for loops traverse the image backwards 
		//so that it saves in the string correctly for printing
		for (int i = 0; i < heightArr; i ++) { 
			for( int e = 0; e < widthArr; e++) {
				for(int p = 0; p < 3; p++ ) { //prints the char 3 times to space image out
					asciiTxt += (asciiChars.charAt(((int)((float)input[e][i]/255 * (asciiChars.length() - 1)))));
				}}
			asciiTxt += "\n"; //newline insert after each line
			}
		
		//now write (formerly) blank String to file
			try {
				fw.write(asciiTxt);
				fw.close();
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		return count; //hand the count back up to start(), then to a JOptionPane for later use
		} //end createAsciiMatrix method
	}//end class
