package frames;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
		setSize(1280,800);
		setLocation(100,100);
		
		//To paint Trojams photo on JFrame and make it possible to put opaque button on top of it
		this.setContentPane(new JPanel() {
	        public void paintComponent(Graphics g) {
	            super.paintComponent(g);
	        	Image image = new ImageIcon("images/TroJams.png").getImage();
				trojamsImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
	            g.drawImage(image, 0, 0, 1280, 800, this);
	        }
	    });
		
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		int buttonWidth = width / 4;
		int buttonHeight = height / 4;
		int buttonX = 440;
		int buttonY = 250;
		
		
		System.out.println(width + " " + height + " " + buttonWidth + " " + buttonHeight + " " + buttonX + " " + buttonY);
		startButton = new JButton("Click to Party");
		
		startButton.setForeground(AppearanceConstants.trojamPurple);
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		startButton.setLocation(buttonX, buttonY);
		startButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		startButton.setFont(AppearanceConstants.fontHuge);
		
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		this.add(Box.createGlue());
		this.add(startButton);
		this.add(Box.createGlue());
		this.add(Box.createGlue());
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		startButton.setAlignmentY(buttonY);
		
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
//		mainPanel.add(Box.createVerticalGlue());
//		mainPanel.add(Box.createHorizontalGlue());
//		mainPanel.add(startButton);
//		mainPanel.add(Box.createVerticalGlue());
//		mainPanel.add(Box.createHorizontalGlue());
//		add(mainPanel);
		
	}
	
	private void createGUI(){
		setVisible(true);
	}
	
	private void addListeners(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				new LoginScreenWindow().setVisible(true);
				dispose();
			}
		});
		
		
		
	}
}

