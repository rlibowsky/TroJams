package frames;

/*
 * PARTY WINDOW - SHOULD BE A PANEL. THIS IS WHERE THE SONGS LIST/QUEUE WILL BE. CARD LAYOUT WITH SELECTIONWINDOW AS MAIN 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class PartyWindow extends JFrame {
	
	private JButton addSongButton;
	private JPanel songPanel, leftPanel, profilePanel, mainPanel;
	private JScrollPane songScrollPane;
	private ImageIcon backgroundImage;
	
	public PartyWindow() {
		super("");
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	public void initializeComponents() {
		
		this.setContentPane(new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image image = new ImageIcon("images/backgroundImage.png").getImage();
				backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
				g.drawImage(image, 0, 0, 1280, 800, this);
			}
		});
		
		addSongButton = new JButton("Add Song");
		songPanel = new JPanel();
		leftPanel = new JPanel();
		profilePanel = new JPanel();
		mainPanel = new JPanel();
		songScrollPane = new JScrollPane();
		
	}
	
	public void createGUI() {
		setSize(1280, 800);
		
		// Set appearance settings
		AppearanceSettings.setForeground(Color.white, addSongButton);
		AppearanceSettings.setSize(200, 80, addSongButton);
		AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, addSongButton);
		AppearanceSettings.setOpaque(addSongButton);
		AppearanceSettings.unSetBorderOnButtons(addSongButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addSongButton);
		AppearanceSettings.setSize(1280, 800, mainPanel);
		//AppearanceSettings.setSize(x, y, components);
		AppearanceSettings.setNotOpaque(mainPanel);
		//AppearanceSettings.setBackground(Color.black, mainPanel, songPanel, leftPanel, profilePanel, mainPanel, songScrollPane);
		
//		songPanel.add(songScrollPane);
		
		// Left panel has the scroll pane to display songs and the add song button and should take up about 2/3 of the screen 
	//	leftPanel.add(songPanel);
		//leftPanel.add(addSongButton);
		
	//	mainPanel.add(leftPanel);
		//mainPanel.add(profilePanel);
		
		add(mainPanel);
		
	}
	
	public void addListeners() {
		
		addSongButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//new AddSongWindow().setVisible(true);
			}
			
		});
		
	}
	
	public static void main(String [] args) {
		new PartyWindow().setVisible(true);
	}
}
