package app;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

 //trying our main method in this class
public class App extends JFrame {
	
	private final Mvp_ascii_art mvp = new Mvp_ascii_art();
	private boolean hasFile = false;
	private int angle;
	
	public App() {
		this.setSize(300, 300); //width, height of window
		this.setTitle("Image To ASCII - by Will Lindstrom");
		//this.setResizable(false); //window resize
		UiComponents(); //method that will do the UI work
		this.setLocationRelativeTo(null); 
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main (String[] args) {
		App app = new App();
	}
	
	private void UiComponents() {
		JPanel jp = new JPanel();
        jp.setLayout(null); //NO layout manager
        
        JLabel title = new JLabel("Convert an image to ASCII art!");
        title.setBounds(0, 0, 300, 40); // x , y, width, height
        title.setFont(new Font("consolas", 15, 15));
        title.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel path1 = new JLabel(); //label for our chosen literal file-path
        path1.setBounds(0, 30, 300, 40);
        path1.setFont(new Font("consolas", 12, 12));
        path1.setHorizontalAlignment(JLabel.CENTER);
        
        JFileChooser jfc = new JFileChooser();
        
        JButton image = new JButton("Choose Image");
        image.setBounds(70, 70, 150, 40);
        image.setFont(new Font("consolas", 12, 12));
        image.addActionListener((ActionEvent e) -> {
            int returnVal = jfc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile(); //file set here
                path1.setText(file.getAbsolutePath()); //our chosen absolute path set to jlabel here
                try {
					mvp.loadImage(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
                hasFile = true;
            }
        });
        
        JButton rotate = new JButton("Rotate 90 degrees");
        rotate.setBounds(70, 130, 150, 40); 
        rotate.setFont(new Font("consolas", 12, 12));
        rotate.addActionListener((ActionEvent e) -> {
        	if(mvp.isImage()) {
        		mvp.rotateImage90();
        		angle += 90;
        		JOptionPane.showMessageDialog(null, "The image will be rotated " + angle + " degrees.");
        	} else {
        		JOptionPane.showMessageDialog(null, "Please upload an image first.");
        	}
        	
        });
        
        JButton convert = new JButton("Convert!");
        convert.setBounds(70, 190, 150, 40);
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
        
//TODO progress bar - need fill method and orientation
//        JProgressBar bar = new JProgressBar();
//        bar.setValue(0);
//        bar.setStringPainted(true);

//here we add all our components to the JPanel
        jp.add(convert);
        jp.add(title);
        jp.add(image);
        jp.add(path1);
//      jp.add(bar);
        jp.add(rotate);
        this.getContentPane().add(jp); 
	}
}//end app class
