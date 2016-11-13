package frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public PartyWindow() {
		super("");
		initializeComponents();
		createGUI();
		addListeners();
		
	}
	
	public void initializeComponents() {
		
		addSongButton = new JButton("Add Song");
		songPanel = new JPanel();
		leftPanel = new JPanel();
		profilePanel = new JPanel();
		mainPanel = new JPanel();
		songScrollPane = new JScrollPane();
		
	}
	
	public void createGUI() {
		setSize(800,1000);
		
		// Set appearance settings
		AppearanceSettings.setForeground(Color.lightGray, addSongButton);
		AppearanceSettings.setSize(200, 80, addSongButton);
		AppearanceSettings.setBackground(Color.darkGray, addSongButton);
		AppearanceSettings.setOpaque(addSongButton);
		AppearanceSettings.unSetBorderOnButtons(addSongButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addSongButton);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, mainPanel, songPanel, leftPanel, profilePanel, mainPanel, songScrollPane);
		
		songPanel.add(songScrollPane);
		
		// Left panel has the scroll pane to display songs and the add song button and should take up about 2/3 of the screen 
		leftPanel.add(songPanel);
		leftPanel.add(addSongButton);
		
		mainPanel.add(leftPanel);
		mainPanel.add(profilePanel);
		
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
	
	public void main(String [] args) {
		new PartyWindow().setVisible(true);
	}
}
