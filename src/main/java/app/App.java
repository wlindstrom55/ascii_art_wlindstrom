package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

 //trying our main method in this class
public class App extends JFrame {
	
	private final Mvp_ascii_art mvp = new Mvp_ascii_art();
	private boolean hasFile = false;
	private int angle;
	
	public App() {
		this.setSize(400, 300); //width, height of window
		this.setTitle("Image To ASCII - by Will Lindstrom");
		//this.setResizable(false); //window resize
		UiComponents(); //method that will do the UI work
		this.setLocationRelativeTo(null); 
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
//main method	
	public static void main (String[] args) {
	try {
      // Set System L&F (uses look and feel native to system)
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
		App app = new App();
	}
	
	private void UiComponents() {
		JPanel jp = new JPanel();
		//PAGE_AXIS aligns components vertically in box layout
        jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS)); 
        
        JLabel title = new JLabel("Convert an image to ASCII art!");
        title.setBounds(0, 0, 300, 40); // x , y, width, height
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("consolas", 15, 15));
        
        JLabel path1 = new JLabel(); //label for our chosen literal file-path
        path1.setAlignmentX(Component.CENTER_ALIGNMENT);
        path1.setFont(new Font("consolas", 12, 12));
        
        JFileChooser jfc = new JFileChooser();
         
//buttons
        JButton image = new JButton("Choose Image");
        image.setAlignmentX(Component.CENTER_ALIGNMENT);
        image.setAlignmentY(Component.CENTER_ALIGNMENT);
        image.setFont(new Font("consolas", 12, 12));
        image.addActionListener((ActionEvent e) -> {
            int returnVal = jfc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
              //file set here:
                File file = jfc.getSelectedFile(); 
              //our chosen absolute path set to jlabel here:
                path1.setText(file.getAbsolutePath()); 
                try {
					mvp.loadImage(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
                hasFile = true;
            }
        });
        
        JButton rotate = new JButton("Rotate 90 degrees");
        rotate.setAlignmentX(Component.CENTER_ALIGNMENT);
        rotate.setAlignmentY(Component.CENTER_ALIGNMENT);
        rotate.setFont(new Font("consolas", 12, 12));
        rotate.addActionListener((ActionEvent e) -> {
        	if(mvp.isImage()) {
        		mvp.rotateImage90();
        		if(angle == 270) {
        			angle = 0;
        		} else {
        			angle += 90;
        		}
        		JOptionPane.showMessageDialog(null, "The image will be rotated " + angle + " degrees.");
        	} else {
        		JOptionPane.showMessageDialog(null, "Please upload an image first.");
        	}
        	
        });
        
//TODO algorithm choice button - possible explainer popup window would be nice
        //TODO look at modality and dialogs for 2nd window popup
        //
//        JButton algo = new JButton("Change Brightess Algorithm");
//        algo.setBounds(46, 190, 200, 40);
//        //algo.setFont(new Font("consolas", 12, 12));
//        algo.addActionListener((ActionEvent e) -> {
//        	public void actionPerformed(ActionEvent e)
//            {
//        	JPanel algoChoices = new JPanel();
////        	algoChoices.setSize(300, 400); //width, height of window
////        	algoChoices.setTitle("Image To ASCII - by Will Lindstrom");
////        	algoChoices.setLayout(null);
////        	algoChoices.setLocationRelativeTo(null); 
////        	algoChoices.setVisible(true);
////        	algoChoices.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        	JLabel algolabel = new JLabel("Please choose an algorithm for determing brightness");
//        	algolabel.setBounds(0, 0, 300, 40); // x , y, width, height
//        	algolabel.setFont(new Font("consolas", 15, 15));
//        	algolabel.setHorizontalAlignment(JLabel.CENTER);
//        	algoChoices.add(algolabel);
//            }
//        });
        
//TODO progress bar - need fill method and orientation
//        JProgressBar bar = new JProgressBar();
//        bar.setValue(0);
//        bar.setStringPainted(true);

        
        JButton convert = new JButton("Convert!");
        //convert.setBounds(70, 310, 150, 40);
        convert.setAlignmentX(Component.CENTER_ALIGNMENT);
        convert.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        convert.setBackground(Color.RED);
        convert.setForeground(Color.RED);
        convert.setFont(new Font("consolas", 12, 12));
        convert.addActionListener((ActionEvent e) -> {
            if (hasFile && mvp.isImage()) {
                int num = 0;
				try {
					num = mvp.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
                
                File file = new File("ASCIIedImage" + num + ".txt");
                JOptionPane.showMessageDialog(null, "The file was successfully created in: " + file.getAbsolutePath());
            } else
                JOptionPane.showMessageDialog(null, "Please select a valid file");
        }); //end action listener
        
//here we add all our components to the JPanel
     //Box.CreateVerticalGlue() basically adds a buffer that grows
     //to fill available space. One on top and bottom
        	jp.add(Box.createVerticalGlue());
        jp.add(title);
        	jp.add(Box.createRigidArea(new Dimension(0, 20))); //(horizontal, vertical)
        jp.add(path1);
        	jp.add(Box.createRigidArea(new Dimension(0, 20)));
        jp.add(image);
        	jp.add(Box.createRigidArea(new Dimension(0, 20)));
//      jp.add(bar);
        jp.add(rotate);
      //jp.add(algo);
        	jp.add(Box.createRigidArea(new Dimension(0, 50)));
        jp.add(convert);
        	jp.add(Box.createVerticalGlue());
        this.getContentPane().add(jp); 
	}
}//end app class
