package frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;

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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import logic.Party;
import logic.PartySong;
import logic.PublicParty;
import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class PartyWindow extends JPanel {
	
	private JButton addSongButton, refreshButton, addNewSongButton, searchButton, leaveButton;
	private JList <SingleSongPanel>songList;
	private JPanel buttonsPanel, centerPanel, currentlyPlayingPanel, hostPanel, addSongPanel, cards;
	private JScrollPane songScrollPane;
	private ImageIcon backgroundImage;
	private JTextArea hostLabel;
	//private ArrayList <SingleSongPanel> songs;
	private Party party;
	private JLabel currentSongName, currentSongTime, currentlyPlayingLabel, hostImage, searchedSong;
	private JTextField searchBar;
	private CardLayout cl;
	private User user;
	private ArrayList<Party> parties;
	
	//argument will be taken out once we turn this into a JPanel
	public PartyWindow(Party partayTime, User user, ArrayList<Party> parties) {
		super();
		this.party = partayTime;
		this.user = user;
		this.parties = parties;
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	//plays next song in party and updates display to show current song name and time
	public void updateCurrentlyPlaying() {
		this.currentSongName.setText(this.party.getSongs().get(0).getName());
		this.currentSongTime.setText(Double.toString(this.party.getSongs().get(0).getLength()) + "s");
		this.party.playNextSong();
	}
	
	//shows song name, upvote and downvote buttons, and total votes for the song
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
					PartyWindow.this.party.upvoteSong(ps);
					votesLabel.setText(Integer.toString(ps.getVotes()));
					setSongs();
				}
				
			});
			downvoteButton = new JButton();
			
			downvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					PartyWindow.this.party.downvoteSong(ps);
					votesLabel.setText(Integer.toString(ps.getVotes()));
					setSongs();
				}
				
			});
			votesLabel = new JLabel(Integer.toString(ps.getVotes()));
			
			AppearanceSettings.setForeground(Color.white, songNameLabel, votesLabel);
			AppearanceSettings.setForeground(Color.white, currentSongName, currentSongTime, currentlyPlayingLabel);
			AppearanceSettings.setSize(100, 40, songNameLabel, votesLabel, currentSongName, currentSongTime, currentlyPlayingLabel);
			//AppearanceSettings.setBackground(AppearanceConstants.mediumGray, songNameLabel, votesLabel, songList, upvoteButton, downvoteButton, this);
			//AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, currentSongName, currentSongTime, currentlyPlayingLabel);
			//AppearanceSettings.setOpaque(songNameLabel, votesLabel, currentSongName, currentSongTime, currentlyPlayingLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontSmall, songNameLabel, votesLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontMedium, currentSongName, currentSongTime, currentlyPlayingLabel);
			
			this.setOpaque(false);
			AppearanceSettings.setNotOpaque(songNameLabel, upvoteButton, downvoteButton, votesLabel);
			upvoteButton.setContentAreaFilled(false);
			downvoteButton.setContentAreaFilled(false);
			upvoteButton.setBorderPainted(false);
			downvoteButton.setBorderPainted(false);
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
		
//		this.setContentPane(new JPanel() {
//			public void paintComponent(Graphics g) {
//				super.paintComponent(g);
//				Image image = new ImageIcon("images/backgroundImage.png").getImage();
//				backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
//				g.drawImage(image, 0, 0, 1280, 800, this);
//			}
//		});
//		
		buttonsPanel = new JPanel();
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
		addSongButton = new JButton("Add Song");
		refreshButton = new JButton("Refresh");
		buttonsPanel.add(addSongButton, BorderLayout.NORTH);
		buttonsPanel.add(refreshButton, BorderLayout.SOUTH);
		
		hostLabel = new JTextArea(party.getHostName() + "'s \nparty!");
		hostLabel.setEditable(false);
		hostLabel.setLineWrap(true);
		hostImage = new JLabel();
		Image image = new ImageIcon(this.party.getHost().getImageFilePath()).getImage();
		hostImage.setIcon(new ImageIcon(image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH)));
		leaveButton = new JButton("Leave this party");
		hostPanel = new JPanel();
		hostPanel.setLayout(new BorderLayout());
		hostLabel.setOpaque(false);
		hostPanel.add(hostLabel, BorderLayout.NORTH);
		hostPanel.add(hostImage, BorderLayout.CENTER);
		hostPanel.add(leaveButton, BorderLayout.SOUTH);
		hostPanel.setOpaque(false);
		currentlyPlayingPanel = new JPanel();
		currentlyPlayingPanel.setLayout(new GridLayout(1,3));
		currentlyPlayingLabel = new JLabel("Current Song");
	
		currentSongName = new JLabel("closer");
		currentSongTime = new JLabel("3");
		AppearanceSettings.setNotOpaque(currentSongName, currentSongTime, currentlyPlayingPanel, currentlyPlayingLabel);
		AppearanceSettings.setForeground(Color.WHITE, currentSongName, currentSongTime, currentlyPlayingPanel, currentlyPlayingLabel);
		currentlyPlayingLabel.setForeground(Color.white);
		currentlyPlayingPanel.add(currentlyPlayingLabel);
		currentlyPlayingPanel.add(currentSongName);
		currentlyPlayingPanel.add(currentSongTime);
		if (this.party.getSongs().size() != 0) {
			this.updateCurrentlyPlaying();
		}
		centerPanel.add(currentlyPlayingPanel, BorderLayout.NORTH);
		
		songList = new JList<SingleSongPanel>();
		songList.setLayout(new FlowLayout());
		setSongs();
		
		// Initializing components for add song panel 
		addNewSongButton = new JButton("Add song");
		searchButton = new JButton("Search");
		searchedSong = new JLabel();
		searchBar = new JTextField();

		cards = new JPanel(new CardLayout());
		
		songList.setPreferredSize(new Dimension (600, 1000));
		songList.setOpaque(false);
		songScrollPane = new JScrollPane(songList);
		songScrollPane.setPreferredSize(new Dimension(600, 700));
		songScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		songScrollPane.setOpaque(false);
		songScrollPane.getViewport().setOpaque(false);
		centerPanel.setOpaque(false);
		
		centerPanel.add(songScrollPane, BorderLayout.SOUTH);
		revalidate();
		
		
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//create the panel that shows songs in order of votes, called when partywindow is created
	//and whenever someone upvotes or downvotes a song
	public void setSongs() {
		if (songList != null) {
			songList.removeAll();
		} else {
			songList = new JList <SingleSongPanel>();
		}
		//add songs in party to songs arraylist
		for (PartySong ps : party.getSongs()) {
			SingleSongPanel ssp = new SingleSongPanel(ps);
			//songs.add(ssp);
			songList.add(ssp);
		}
		
		//set at least 10
		if (songList.getVisibleRowCount()< 10) {
			for (int i = 0; i < 10-songList.getVisibleRowCount(); i ++) {
				SingleSongPanel ssp = new SingleSongPanel(new PartySong("", 0.0));
				songList.add(ssp);
			}
		}
		revalidate();
	}
	
	public void createGUI() {
		setSize(1280, 800);
		//setLayout(new BorderLayout());
		
		// Set appearance settings
		AppearanceSettings.setForeground(Color.white, addSongButton, refreshButton, hostLabel);
		AppearanceSettings.setSize(150, 80, addSongButton, refreshButton, hostLabel);
		AppearanceSettings.setSize(150, 150, hostLabel);
		AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, addSongButton, refreshButton, hostLabel);
		AppearanceSettings.setOpaque(addSongButton, refreshButton);
		//AppearanceSettings.setNotOpaque(hostLabel);
		AppearanceSettings.unSetBorderOnButtons(addSongButton, refreshButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addSongButton, refreshButton, hostLabel);
		
		
		//AppearanceSettings.setSize(x, y, components);
		//AppearanceSettings.setBackground(Color.black, mainPanel, songPanel, leftPanel, profilePanel, mainPanel, songScrollPane);
		
		//songPanel.add(songScrollPane);
		
		addSongPanel = createAddSongPanel();
		JPanel endPartyWindow = new EndPartyWindow(new SelectionWindow(user, parties));
		cards.add(buttonsPanel, "button panel");
		cards.add(addSongPanel, "add song panel");
//		add(swMainPanel, BorderLayout.CENTER);
		
		//add(cards, BorderLayout.CENTER);
		//add(pwMainPanel, BorderLayout.EAST); 
		
		
		
		add(hostPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(cards, BorderLayout.EAST);
		
		//cl = (CardLayout) cards.getLayout();
		//cl.show(cards, "button panel");
		
	}
	
	public void addListeners() {
		
		addSongButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl2 = (CardLayout) cards.getLayout();
				cl2.show(cards, "add song panel");	
			}
			
		});
		
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSongs();
			}
			
		});
		
		addNewSongButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CardLayout cl3 = (CardLayout) cards.getLayout();
				cl3.show(cards, "button panel");	
			}
			
		});
		
		leaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
				
			}
			
		});
		
	}
	
	public JPanel createAddSongPanel() {
		JPanel tempPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel searchBarPanel = new JPanel();
		searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
		centerPanel.setLayout(new BorderLayout());
		
		tempPanel.setLayout(new BorderLayout());
		
		tempPanel.setSize(new Dimension(450, 800));
		AppearanceSettings.setNotOpaque(tempPanel, centerPanel, searchBarPanel, searchedSong, searchBar, cards, buttonsPanel);
		AppearanceSettings.setSize(150,50, searchButton, addNewSongButton);
		AppearanceSettings.setSize(450,400, centerPanel);
		AppearanceSettings.setSize(450,150, searchBar);
		
		AppearanceSettings.setForeground(Color.white, addNewSongButton, searchButton, searchedSong);
		//AppearanceSettings.setSize(150, 80, addSongButton, refreshButton, hostLabel);
		//AppearanceSettings.setSize(150, 150, hostLabel);
		AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, addNewSongButton, searchButton);
		//AppearanceSettings.setOpaque(addSongButton, refreshButton, hostLabel);
		AppearanceSettings.unSetBorderOnButtons(addNewSongButton, searchButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addNewSongButton, searchButton, hostLabel);
		
		AppearanceSettings.setSize(350, 50, searchBar);
		
		searchBarPanel.add(searchBar);
		searchBarPanel.add(searchButton);
		
		searchedSong.setText("Closer");
		centerPanel.add(searchBarPanel, BorderLayout.NORTH);
		centerPanel.add(searchedSong, BorderLayout.CENTER);
		centerPanel.add(addNewSongButton, BorderLayout.SOUTH);
		
		tempPanel.add(centerPanel, BorderLayout.CENTER);
		
		
		return tempPanel;
		
	}
	
	//Paint background image -- needs to be outside of other methods
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = new ImageIcon("images/backgroundImage.png").getImage();
		//backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	};
	
//	public static void main(String [] args) {
//		PublicParty partayTime = new PublicParty("theBestParty", new User("testUsername", "testPassword"), new ImageIcon("party-purple.jpg"));
//		partayTime.addSong(new PartySong("Song1", 3.0));
//		partayTime.addSong(new PartySong("Song2", 3.0));
//		partayTime.addSong(new PartySong("Song3", 3.0));
//		partayTime.addSong(new PartySong("Song4", 3.0));
//		partayTime.addSong(new PartySong("Song5", 3.0));
//		partayTime.addSong(new PartySong("Song6", 3.0));
//		partayTime.addSong(new PartySong("Song7", 3.0));
//		partayTime.addSong(new PartySong("Song8", 3.0));
//		partayTime.addSong(new PartySong("Song9", 3.0));
//		partayTime.addSong(new PartySong("Song10", 3.0));
//		partayTime.addSong(new PartySong("Song11", 3.0));
//		partayTime.addSong(new PartySong("Song12", 3.0));
//		new PartyWindow(partayTime, user, parties).setVisible(true);
//	}
	
	public void dispose() {
		this.dispose();
	}
	
	
}
