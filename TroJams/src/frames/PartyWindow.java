package frames;

import java.awt.BorderLayout;

/*
 * PARTY WINDOW - SHOULD BE A PANEL. THIS IS WHERE THE SONGS LIST/QUEUE WILL BE. CARD LAYOUT WITH SELECTIONWINDOW AS MAIN 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import logic.Party;
import logic.PartySong;
import logic.PublicParty;
import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class PartyWindow extends JFrame {
	
	private JButton addSongButton, refreshButton;
	private JList songPanel;
	private JPanel buttonsPanel;
	private JScrollPane songScrollPane;
	private ImageIcon backgroundImage;
	//private ArrayList <SingleSongPanel> songs;
	private Party party;
	
	//argument will be taken out once we turn this into a JPanel
	public PartyWindow(Party partayTime) {
		super("");
		this.party = partayTime;
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	private class SingleSongPanel extends JPanel {
		private PartySong partySong;
		private JButton upvoteButton, downvoteButton;
		private JLabel votesLabel, songNameLabel;
		
		public SingleSongPanel (PartySong ps) {
			AppearanceSettings.setSize(600, 100, this);
			partySong = ps;
			setLayout(new GridLayout(1,4));
			songNameLabel = new JLabel(ps.getName());
			
			upvoteButton = new JButton();
			
			upvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("upvote clicked for song " + ps.getName());
					PartyWindow.this.party.upvoteSong(ps);
					votesLabel.setText(Integer.toString(ps.getVotes()));
					setSongs();
				}
				
			});
			downvoteButton = new JButton();
			
			downvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("downvote clicked for song " + ps.getName());
					PartyWindow.this.party.downvoteSong(ps);
					votesLabel.setText(Integer.toString(ps.getVotes()));
					setSongs();
				}
				
			});
			votesLabel = new JLabel(Integer.toString(ps.getVotes()));
			
			AppearanceSettings.setForeground(Color.white, songNameLabel, votesLabel);
			AppearanceSettings.setSize(100, 40, songNameLabel, votesLabel);
			AppearanceSettings.setBackground(AppearanceConstants.mediumGray, songNameLabel, votesLabel);
			AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, this);
			AppearanceSettings.setOpaque(songNameLabel, votesLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontSmall, songNameLabel, votesLabel);
			
			add(songNameLabel);
			add(upvoteButton);
			add(downvoteButton);
			add(votesLabel);
			
			Image image = new ImageIcon("images/thumbsup.png").getImage();
			ImageIcon thumbsUpImage = new ImageIcon(image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			upvoteButton.setIcon(thumbsUpImage);
			
			Image image2 = new ImageIcon("images/thumbsDown.png").getImage();
			ImageIcon thumbsDownImage = new ImageIcon(image2.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			downvoteButton.setIcon(thumbsDownImage);
		}
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
		refreshButton = new JButton("Refresh");
		songPanel = new JList<SingleSongPanel>();
		songPanel.setLayout(new FlowLayout());
		setSongs();
		buttonsPanel = new JPanel();
		songPanel.setVisibleRowCount(10);
		songScrollPane = new JScrollPane(songPanel);
		songScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
	}
	
	//create the panel that shows songs in order of votes, called when partywindow is created
	//and whenever someone upvotes or downvotes a song
	public void setSongs() {
		if (songPanel != null) {
			songPanel.removeAll();
		} else {
			songPanel = new JList <SingleSongPanel>();
		}
		//add songs in party to songs arraylist
		for (PartySong ps : party.getSongs()) {
			SingleSongPanel ssp = new SingleSongPanel(ps);
			//songs.add(ssp);
			songPanel.add(ssp);
		}
		
		//set at least 10
		if (songPanel.getVisibleRowCount()< 10) {
			for (int i = 0; i < 10-songPanel.getVisibleRowCount(); i ++) {
				SingleSongPanel ssp = new SingleSongPanel(new PartySong("", 0.0));
				//songs.add(ssp);
				songPanel.add(ssp);
			}
		}
		revalidate();
	}
	
	public void createGUI() {
		setSize(1280, 800);
		setLayout(new BorderLayout());
		
		// Set appearance settings
		AppearanceSettings.setForeground(Color.white, addSongButton, refreshButton);
		AppearanceSettings.setSize(200, 80, addSongButton, refreshButton);
		AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, addSongButton, refreshButton);
		AppearanceSettings.setOpaque(addSongButton, refreshButton);
		AppearanceSettings.unSetBorderOnButtons(addSongButton, refreshButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addSongButton, refreshButton);
		//AppearanceSettings.setSize(x, y, components);
		//AppearanceSettings.setBackground(Color.black, mainPanel, songPanel, leftPanel, profilePanel, mainPanel, songScrollPane);
		
		//songPanel.add(songScrollPane);
		
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.add(addSongButton, BorderLayout.WEST);
		buttonsPanel.add(refreshButton, BorderLayout.EAST);
		
		add(addSongButton, BorderLayout.WEST);
		add(songPanel, BorderLayout.CENTER);
		add(refreshButton, BorderLayout.EAST);
		
	}
	
	public void addListeners() {
		
		addSongButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//new AddSongWindow().setVisible(true);
			}
			
		});
		
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSongs();
			}
			
		});
		
	}
	
	public static void main(String [] args) {
		PublicParty partayTime = new PublicParty("theBestParty", new User("testUsername", "testPassword"));
		partayTime.addSong(new PartySong("Song1", 3.0));
		partayTime.addSong(new PartySong("Song2", 3.0));
		partayTime.addSong(new PartySong("Song3", 3.0));
		new PartyWindow(partayTime).setVisible(true);
	}
}
