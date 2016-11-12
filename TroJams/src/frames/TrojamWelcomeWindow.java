package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppearanceConstants;

//This window is the first thing that the user should see when they start the program 
public class TrojamWelcomeWindow extends JFrame{

	private JPanel mainPanel;
	private ImageIcon trojamsImage;
	private JLabel imageLabel;
	private JButton startButton;
	
	public TrojamWelcomeWindow(){
		 super("TroJams");
		 
		 initializeComponents();
	        createGUI();
	        addListeners();
	}
	
	private void initializeComponents(){
		mainPanel = new JPanel();
		
		File dir1 = new File (".");
		System.out.println("current directory: " + dir1.getAbsolutePath());
		
		Image image = new ImageIcon("images/TroJams.png").getImage();
		trojamsImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
		
		imageLabel = new JLabel();
		imageLabel.setIcon(trojamsImage);
		
		int width = mainPanel.getWidth();
		int height = mainPanel.getHeight();
		
		int buttonWidth = width / 4;
		int buttonHeight = height / 8;
		int buttonX = 440;
		int buttonY = 250;
		startButton = new JButton("Let's Party");
		startButton.setForeground(AppearanceConstants.trojamPurple);
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		startButton.setLocation(buttonX, buttonY);
		startButton.setSize(new Dimension(buttonWidth, buttonHeight));
		
//		Image grayImage = new ImageIcon(filePath).getImage();
//		disabledIcon = new ImageIcon(grayImage.getScaledInstance(400, 400, java.awt.Image.SCALE_SMOOTH));
//		
		mainPanel.add(imageLabel,BorderLayout.CENTER);
		
				
		
	}
	
	private void createGUI(){
		
		add(mainPanel);
		setSize(1280,800);
		setLocation(100,100);
		setVisible(true);
	}
	
	private void addListeners(){
		
	}

    public static void main (String [] args) {
    	new TrojamWelcomeWindow().setVisible(true);
    }
}

