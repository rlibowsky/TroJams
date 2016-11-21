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
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;

import frames.PartyWindow.SingleSongPanel;
//import frames.SelectionWindow.MyScrollBarUI;
import logic.Party;
import logic.PartySong;
import logic.User;
import networking.SongVoteMessage;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class PartyWindow extends JPanel {
	
	private JButton refreshButton, addNewSongButton, searchButton, leaveButton, viewProfileButton;
	private JList <SingleSongPanel>songList;
	private JPanel buttonsPanel, centerPanel, currentlyPlayingPanel, hostPanel, addSongPanel, bottomButtonPanel;
	private JScrollPane songScrollPane, partyPeopleScrollPane;
	private ImageIcon backgroundImage, currentlyPlayingImage, partyImage, hostImage;
	DefaultListModel <SingleSongPanel> df;
	private JLabel hostLabel;
	private JLabel partyLabel;
	private JList partyPeopleList;
	
	//private ArrayList <SingleSongPanel> songs;
	private Party party;
	private JLabel currentSongName, currentSongTime, currentlyPlayingLabel, hostImageLabel, searchedSong;
	private JTextField searchBar;
	private CardLayout cl;
	private SelectionWindow sw;
	private DefaultListModel<SingleSongPanel> listModel;
	
	//argument will be taken out once we turn this into a JPanel
	public PartyWindow(Party partayTime, SelectionWindow sw) {
		super();
		this.party = partayTime;
		System.out.println(party.getPartyName());
		this.sw = sw;
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	//plays next song in party and updates display to show current song name and time
	public void updateCurrentlyPlaying() {
		// Uncomment when party isn't null
//		this.currentSongName.setText(this.party.getSongs().get(0).getName());
//		this.currentSongTime.setText(Double.toString(this.party.getSongs().get(0).getLength()) + "s");
//		this.party.playNextSong();
	}
	
	//shows song name, upvote and downvote buttons, and total votes for the song
	public class SingleSongPanel extends JPanel {
		PartySong partySong;
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
//					PartyWindow.this.party.upvoteSong(ps);
//					votesLabel.setText(Integer.toString(ps.getVotes()));
					sw.client.sendVotesChange(party, partySong, "upvote");
					//
				}
				
			});
			downvoteButton = new JButton();
			
			downvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
//					PartyWindow.this.party.downvoteSong(ps);
//					votesLabel.setText(Integer.toString(ps.getVotes()));
					sw.client.sendVotesChange(party, partySong, "downvote");
					//setSongs();
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
			AppearanceSettings.setFont(AppearanceConstants.fontLarge, currentSongName, currentSongTime, currentlyPlayingLabel);
			revalidate();
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
		buttonsPanel.setLayout(new BorderLayout());
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
//		addSongButton = new JButton();
//		ImageIcon addSongButtonImage = new ImageIcon("images/button_add-song.png");
//		addSongButton.setIcon(addSongButtonImage);
//		addSongButton.setOpaque(false);
//		addSongButton.setBorderPainted(false);
//		addSongButton.setContentAreaFilled(false);
//		
		bottomButtonPanel = new JPanel();
		
		bottomButtonPanel.setOpaque(false);
		
		
		refreshButton = new JButton();
		ImageIcon refreshButtonImage = new ImageIcon("images/button_refresh.png");
		refreshButton.setIcon(refreshButtonImage);
		refreshButton.setOpaque(false);
		refreshButton.setBorderPainted(false);
		refreshButton.setContentAreaFilled(false);
		
		//buttonsPanel.add(addSongButton, BorderLayout.NORTH);
		bottomButtonPanel.add(refreshButton);
		
//		buttonsPanel.add(addSongButton, BorderLayout.SOUTH);
		//JPanel topHostPanel = new JPanel();
		//topHostPanel.setLayout(new FlowLayout());
		//topHostPanel.setOpaque(false);
		partyLabel = new JLabel("Party: " + party.getPartyName());
		AppearanceSettings.setForeground(Color.white, partyLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, partyLabel);
		partyLabel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,50));
		partyLabel.setOpaque(false);
		partyImage = party.getPartyImage();
		hostLabel = new JLabel("Host: " + party.getHostName());
		hostLabel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,50));
		
		hostImage = party.getPartyImage();
		JLabel hostImageLabel = new JLabel(hostImage);
		hostImageLabel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,100));
		
		//Image image = new ImageIcon(this.party.getHost().getImageFilePath()).getImage();
		//hostImage.setIcon(new ImageIcon(image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH)));
		viewProfileButton = new JButton("View Profile");
		// TODO: CHANGE THIS URL
		//ImageIcon viewProfileImage = new ImageIcon("images/button_leave-party.png");
		//viewProfileButton.setIcon(viewProfileImage);
		viewProfileButton.setOpaque(false);
		viewProfileButton.setBorderPainted(false);
		viewProfileButton.setContentAreaFilled(false);
		
		leaveButton = new JButton();
		ImageIcon leaveButtonImage = new ImageIcon("images/button_leave-party.png");
		leaveButton.setIcon(leaveButtonImage);
		leaveButton.setOpaque(false);
		leaveButton.setBorderPainted(false);
		leaveButton.setContentAreaFilled(false);
		
		JPanel leftButtonPanel = new JPanel();
		leftButtonPanel.add(viewProfileButton);
		leftButtonPanel.add(leaveButton);
		leftButtonPanel.setOpaque(false);
		leftButtonPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/4, 125));
		
		hostPanel = new JPanel();
		hostPanel.setLayout(new FlowLayout());
		hostLabel.setOpaque(false);
		//hostPanel.add(partyLabel);
		hostPanel.add(partyLabel);
		hostPanel.add(hostLabel);
		hostPanel.add(hostImageLabel);
		//hostPanel.add(topHostPanel, BorderLayout.NORTH);
		
		
		hostPanel.setOpaque(false);
		
		//User [] temp = (User[]) party.getPartyMembers().toArray();
		//partyPeopleList = new JList(temp);
		JPanel scrollPanel = new JPanel();
		scrollPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/4, 400));
		scrollPanel.setOpaque(false);
		partyPeopleList = new JList();
		partyPeopleScrollPane = new JScrollPane(partyPeopleList);
		partyPeopleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		partyPeopleScrollPane.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/4, 400));
		partyPeopleScrollPane.setOpaque(false);
		partyPeopleScrollPane.getViewport().setOpaque(false);
		partyPeopleScrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPanel.add(partyPeopleScrollPane);
//		
//		//custom scroll bar
//		partyPeopleScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
//		UIManager.put("ScrollBarUI", "my.package.MyScrollBarUI");
//				
		hostPanel.add(scrollPanel, BorderLayout.CENTER);
		hostPanel.add(leftButtonPanel, BorderLayout.SOUTH);
		
		currentlyPlayingPanel = new JPanel();
		currentlyPlayingImage = new ImageIcon("images/purplePlay.png");
		JLabel currentlyPlayingImageLabel = new JLabel(currentlyPlayingImage);
		currentlyPlayingPanel.setLayout(new BoxLayout(currentlyPlayingPanel, BoxLayout.LINE_AXIS));
		currentlyPlayingLabel = new JLabel("Now Playing: ");
		currentSongName = new JLabel("");
		currentSongTime = new JLabel("");
		AppearanceSettings.setNotOpaque(currentSongName, currentSongTime, currentlyPlayingPanel, currentlyPlayingLabel);
		AppearanceSettings.setForeground(Color.WHITE, currentSongName, currentSongTime, currentlyPlayingPanel, currentlyPlayingLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, currentSongName);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, currentlyPlayingLabel, currentSongTime);
		
		currentlyPlayingPanel.add(currentlyPlayingImageLabel);
		currentlyPlayingPanel.add(currentlyPlayingLabel);
		currentlyPlayingPanel.add(currentSongName);
		currentlyPlayingPanel.add(currentSongTime);
//		if (this.party.getSongs().size() != 0) {
//			this.updateCurrentlyPlaying();
//		}
		
		
		centerPanel.add(currentlyPlayingPanel, BorderLayout.NORTH);
		listModel = new DefaultListModel<SingleSongPanel>();

//		df = new DefaultListModel<>();
//		songList = new JList<SingleSongPanel>(df);
		songList= new JList<SingleSongPanel>();
		songList.setLayout(new FlowLayout());
		setSongs(this.party);
		
		// Initializing components for add song panel 
		addNewSongButton = new JButton();
		ImageIcon addNewSongButtonImage = new ImageIcon("images/button_add-song.png");
		addNewSongButton.setIcon(addNewSongButtonImage);
		addNewSongButton.setOpaque(false);
		addNewSongButton.setBorderPainted(false);
		addNewSongButton.setContentAreaFilled(false);
		
		searchButton = new JButton();
		ImageIcon searchButtonImage = new ImageIcon("images/button_search.png");
		searchButton.setIcon(searchButtonImage);
		searchButton.setOpaque(false);
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		
		searchedSong = new JLabel();
		searchBar = new JTextField();
		AppearanceSettings.setForeground(Color.WHITE, searchBar);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, searchBar, searchedSong);

//		cards = new JPanel(new CardLayout());
		
		songList.setPreferredSize(new Dimension (600, 1000));
		songList.setOpaque(false);
		songScrollPane = new JScrollPane(songList);
		songScrollPane.setPreferredSize(new Dimension(600, 700));
		songScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//songScrollPane.setViewportBorder(null);
		songScrollPane.setBorder(null);
		songScrollPane.setOpaque(false);
		songScrollPane.getViewport().setOpaque(false);
		centerPanel.setOpaque(false);
		
		centerPanel.add(songScrollPane, BorderLayout.CENTER);
		centerPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
		revalidate();
		
		
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//create the panel that shows songs in order of votes, called when partywindow is created
	//and whenever someone upvotes or downvotes a song
	public void setSongs(Party receivedParty) {
		System.out.println("in setsongs");
		if (songList != null) {
			songList.removeAll();
		} else {
			songList = new JList <SingleSongPanel>();
		}
		//add songs in party to songs arraylist
		for (PartySong ps : receivedParty.getSongs()) {
			SingleSongPanel ssp = new SingleSongPanel(ps);
			//songs.add(ssp);
			System.out.println("adding song " + ps.getName() + " with " + ps.getVotes() + " votes");
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
		setLayout(new BorderLayout());
		
		// Set appearance settings
		AppearanceSettings.setForeground(Color.white, refreshButton, hostLabel);
		AppearanceSettings.setSize(150, 80, refreshButton);
		//AppearanceSettings.setSize(150, 150, hostLabel);
		//AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, addSongButton, refreshButton, hostLabel);
		//AppearanceSettings.setOpaque(addSongButton, refreshButton);
		//AppearanceSettings.setNotOpaque(hostLabel);
		AppearanceSettings.unSetBorderOnButtons(refreshButton);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, refreshButton, hostLabel);
		
		
		//AppearanceSettings.setSize(x, y, components);
		//AppearanceSettings.setBackground(Color.black, mainPanel, songPanel, leftPanel, profilePanel, mainPanel, songScrollPane);
		
		//songPanel.add(songScrollPane);
		
		addSongPanel = createAddSongPanel();
//		cards.add(buttonsPanel, "button panel");
//		cards.add(addSongPanel, "add song panel");
//		add(swMainPanel, BorderLayout.CENTER);
		
		//add(cards, BorderLayout.CENTER);
		//add(pwMainPanel, BorderLayout.EAST); 
		
		centerPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/2,AppearanceConstants.GUI_HEIGHT));
		hostPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,AppearanceConstants.GUI_HEIGHT));
		//addSongPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,AppearanceConstants.GUI_HEIGHT));
		addSongPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,AppearanceConstants.GUI_HEIGHT));
		
		add(hostPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(addSongPanel, BorderLayout.EAST);
		
//		cl = (CardLayout) cards.getLayout();
//		cl.show(cards, "button panel");
		
	}
	
	public void addListeners() {
		
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(songList.getModel().getSize());
				if (songList.getModel().getSize()>0) {
					currentSongName.setText(songList.getModel().getElementAt(0).partySong.getName());
				}
				revalidate();
			}
			
		});
		
		addNewSongButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSongPanel ssp = new SingleSongPanel(new PartySong(searchedSong.getText(), 0.0));
//				df.addElement(ssp);
//				//listModel.addElement(ssp);
//				System.out.println(songList.getModel().getSize());
				songList.add(ssp);
				
				revalidate();
			}
			
		});
 
		leaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sw.showEndWindow();
				
			}
			
			
		});
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Search database to get song and set text
				searchedSong.setText(searchBar.getText());
				searchBar.setText("");
				//revalidate();
				
			}
			
		});

		
	}
	
	public JPanel createAddSongPanel() {
		JPanel tempPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel dummyPanel = new JPanel();
		JPanel dummyPanel2 = new JPanel();
		//JPanel searchBarPanel = new JPanel();
		//searchBarPanel.setLayout(new FlowLayout());
		centerPanel.setLayout(new FlowLayout());
		//tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
		
		tempPanel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH/4, AppearanceConstants.GUI_HEIGHT));
		AppearanceSettings.setNotOpaque(tempPanel, centerPanel, searchedSong, searchBar, buttonsPanel, dummyPanel, dummyPanel2);
		AppearanceSettings.setSize(150,50, searchButton, addNewSongButton);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH/4,AppearanceConstants.GUI_HEIGHT, centerPanel);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH/4,50, searchBar);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH/4, 200, searchedSong);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH/4, 175, dummyPanel);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH/4, 200, dummyPanel2);
		AppearanceSettings.setForeground(Color.white, addNewSongButton, searchButton, searchedSong);
		//AppearanceSettings.setSize(150, 80, addSongButton, refreshButton, hostLabel);
		//AppearanceSettings.setSize(150, 150, hostLabel);
		//AppearanceSettings.setOpaque(addSongButton, refreshButton, hostLabel);
		AppearanceSettings.unSetBorderOnButtons(addNewSongButton, searchButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addNewSongButton, searchButton, hostLabel);
		
		
		//searchBarPanel.add(searchBar);
		//searchBarPanel.add(searchButton);
		
		searchedSong.setText("");
		searchedSong.setFont(AppearanceConstants.fontSmall);
		//centerPanel.add(Box.createVerticalStrut(275));
		centerPanel.add(dummyPanel);
		centerPanel.add(searchBar);
		centerPanel.add(searchButton);
		centerPanel.add(searchedSong);
		centerPanel.add(addNewSongButton);
		centerPanel.add(dummyPanel2);
		//centerPanel.add(Box.createVerticalStrut(275));
		//centerPanel.setPreferredSize(new Dimension(450,400));
		
		tempPanel.add(centerPanel);
		
		
		return tempPanel;
		
	}
	
	//Paint background image -- needs to be outside of other methods
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = new ImageIcon("images/backgroundImage.png").getImage();
		//backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void sendSongVoteUpdate(SongVoteMessage svm) {
		System.out.println("received update");
		Party receivedParty = svm.getParty();
		//PartyWindow.this.party.upvoteSong(receivedSong);
		setSongs(receivedParty);
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


	//CITE: http://www.java2s.com/Tutorials/Java/Swing_How_to/JScrollPane/Create_custom_JScrollBar_for_JScrollPane.htm
//	public class MyScrollBarUI extends BasicScrollBarUI {
//
//		private final Dimension d = new Dimension();
//		
//		  @Override
//		  protected JButton createDecreaseButton(int orientation) {
//		    return new JButton() {
//		      @Override
//		      public Dimension getPreferredSize() {
//		        return d;
//		      }
//		    };
//		  }
//
//		  @Override
//		  protected JButton createIncreaseButton(int orientation) {
//		    return new JButton() {
//		      @Override
//		      public Dimension getPreferredSize() {
//		        return d;
//		      }
//		    };
//		  }
//
//		  
//	    @Override
//	    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
//	        // your code
//	    }
//
//	    @Override
//	    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
//	    	  Graphics2D g2 = (Graphics2D) g.create();
//	    	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//	    	        RenderingHints.VALUE_ANTIALIAS_ON);
//	    	    Color color = null;
//	    	    JScrollBar sb = (JScrollBar) c;
//	    	    if (!sb.isEnabled() || r.width > r.height) {
//	    	      return;
//	    	    } else if (isDragging) {
//	    	      color = Color.DARK_GRAY;
//	    	    } else if (isThumbRollover()) {
//	    	      color = Color.LIGHT_GRAY;
//	    	    } else {
//	    	      color = Color.GRAY;
//	    	    }
//	    	    g2.setPaint(color);
//	    	    g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
//	    	    g2.setPaint(Color.WHITE);
//	    	    g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
//	    	    g2.dispose();
//	    }
//	    
//	    @Override
//	    protected void setThumbBounds(int x, int y, int width, int height) {
//	      super.setThumbBounds(x, y, width, height);
//	      scrollbar.repaint();
//	    }
//	}
	
}
