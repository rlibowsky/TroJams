package frames;

import java.awt.BorderLayout;

/*
 * PARTY WINDOW - SHOULD BE A PANEL. THIS IS WHERE THE SONGS LIST/QUEUE WILL BE. CARD LAYOUT WITH SELECTIONWINDOW AS MAIN 
 */

import java.awt.Color;
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
	private JPanel songPanel, buttonsPanel;
	private JScrollPane songScrollPane;
	private ImageIcon backgroundImage;
	private ArrayList <SingleSongPanel> songs;
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
			partySong = ps;
			setLayout(new GridLayout(1,4));
			songNameLabel = new JLabel(ps.getName());
			
			upvoteButton = new JButton("upvote");
			
			upvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("upvote clicked for song " + ps.getName());
					PartyWindow.this.party.upvoteSong(ps);
					votesLabel.setText(Integer.toString(ps.getVotes()));
					setSongs();
				}
				
			});
			downvoteButton = new JButton("downvote");
			
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
		songPanel = new JPanel();
		songPanel.setLayout(new GridLayout(0, 1));
		setSongs();
		buttonsPanel = new JPanel();
		songScrollPane = new JScrollPane(songPanel);
		
	}
	
	public void setSongs() {
		if (songs != null) {
			songs.clear();
			songPanel.removeAll();
		} else {
			songs = new ArrayList <SingleSongPanel>();
		}
		//add songs in party to songs arraylist
		for (PartySong ps : party.getSongs()) {
			SingleSongPanel ssp = new SingleSongPanel(ps);
			songs.add(ssp);
			songPanel.add(ssp);
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
		
		add(buttonsPanel, BorderLayout.NORTH);
		add(songPanel, BorderLayout.SOUTH);
		
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
