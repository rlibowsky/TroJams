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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logic.Account;
//import frames.SelectionWindow.MyScrollBarUI;
import logic.Party;
import logic.User;
import music.JsonReader;
import music.SongData;
import networking.FoundSongMessage;
import networking.PlayNextSongMessage;
import networking.SongVoteMessage;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class PartyWindow extends JPanel {

	private JButton refreshButton, addNewSongButton, searchButton, leaveButton, viewProfileButton;
	private JList<SingleSongPanel> songList;
	private Vector<String> songFilePaths;
	private JPanel buttonsPanel, centerPanel, currentlyPlayingPanel, hostPanel, addSongPanel, bottomButtonPanel, cards;
	private JScrollPane songScrollPane, partyPeopleScrollPane;
	private ImageIcon backgroundImage, currentlyPlayingImage, partyImage, hostImage;
	DefaultListModel<SingleSongPanel> df;
	private JLabel hostLabel;
	private JLabel partyLabel;
	private JList partyPeopleList;

	// private ArrayList <SingleSongPanel> songs;
	private Party party;
	private JLabel currentSongName, currentSongTime, currentlyPlayingLabel, hostImageLabel, searchedSong,
			searchedSongArtist, searchedSongAlbum, searchedSongArtwork;
	private JPanel searchedSongPanel;
	private JTextField searchBar;
	private JList<String> returnedSongsList;
	private JScrollPane returnedSongsScrollPane;
	private CardLayout cl;
	private SelectionWindow sw;
	private DefaultListModel<SingleSongPanel> listModel;
	private Account account;
	private SwingFXWebView swingFXWebView;
	private String spotify_Url;
	ArrayList<SongData> returnedSongs;
	private String song_name;
	private String song_artist;
	private String song_album;
	private String song_artwork_filepath;
	private String song_mp3_filepath;
	private ImageIcon song_artwork;
	private String filePath;

	// argument will be taken out once we turn this into a JPanel
	public PartyWindow(Party partayTime, SelectionWindow sw) {
		super();
		this.party = partayTime;
		sw.account.p = party;
		System.out.println(party.getPartyName());
		this.sw = sw;
		account = sw.getAccount();
		initializeComponents();
		createGUI();
		addListeners();
	}

	// plays next song in party and updates display to show current song name
	// and time
	public void updateCurrentlyPlaying(PlayNextSongMessage psm) {
		// Uncomment when party isn't null
		System.out.println("updating currently playing to be " + psm.getSongName());
		this.currentSongName.setText(psm.getSongName());
		setSongs(psm.getParty());
		// this.currentSongTime.setText(Double.toString(this.party.getSongs().get(0).getLength())
		// + "s");
		// this.party.playNextSong();
	}

	// shows song name, upvote and downvote buttons, and total votes for the
	// song
	public class SingleSongPanel extends JPanel {
		SongData partySong;
		private JButton upvoteButton, downvoteButton;
		private JLabel votesLabel, songNameLabel, artistLabel, albumImageLabel;
		private JPanel songAndArtistPanel;
		private ImageIcon albumImage;

		public SingleSongPanel(SongData ps) {
			AppearanceSettings.setSize(600, 100, this);
			partySong = ps;
			//setLayout(new GridLayout(1, 5));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			//setLayout(new FlowLayout());
			 URL url;
			try {
				url = new URL(ps.getImageURL());
				 BufferedImage image;
				image = ImageIO.read(url);
			      System.out.println("Load album image into single panel...");
		            albumImage = new ImageIcon(image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
		            albumImageLabel = new JLabel(albumImage);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			songNameLabel = new JLabel(ps.getName());
			artistLabel = new JLabel(ps.getArtist());
			songAndArtistPanel = new JPanel();
			songAndArtistPanel.setLayout(new BoxLayout(songAndArtistPanel, BoxLayout.Y_AXIS));

			upvoteButton = new JButton();

			upvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// PartyWindow.this.party.upvoteSong(ps);
					// votesLabel.setText(Integer.toString(ps.getVotes()));
					sw.client.sendVotesChange(party, partySong, "upvote");
					//
				}

			});
			downvoteButton = new JButton();

			downvoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// PartyWindow.this.party.downvoteSong(ps);
					// votesLabel.setText(Integer.toString(ps.getVotes()));
					sw.client.sendVotesChange(party, partySong, "downvote");
				}

			});
			votesLabel = new JLabel(Integer.toString(ps.getVotes()));

			Image image1 = new ImageIcon("images/thumbsup.png").getImage();
			ImageIcon thumbsUpImage = new ImageIcon(image1.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			upvoteButton.setIcon(thumbsUpImage);

			Image image2 = new ImageIcon("images/thumbsDown.png").getImage();
			ImageIcon thumbsDownImage = new ImageIcon(image2.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			downvoteButton.setIcon(thumbsDownImage);

			AppearanceSettings.setForeground(Color.white, songNameLabel, votesLabel);
			AppearanceSettings.setForeground(Color.white, currentSongName, currentSongTime, currentlyPlayingLabel);
			AppearanceSettings.setSize(180, 40, songNameLabel, votesLabel);
			// , currentSongName, currentSongTime, currentlyPlayingLabel);
			// AppearanceSettings.setBackground(AppearanceConstants.mediumGray,
			// songNameLabel, votesLabel, songList, upvoteButton,
			// downvoteButton, this);
			// AppearanceSettings.setBackground(AppearanceConstants.trojamPurple,
			// currentSongName, currentSongTime, currentlyPlayingLabel);
			// AppearanceSettings.setOpaque(songNameLabel, votesLabel,
			// currentSongName, currentSongTime, currentlyPlayingLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontSmall, songNameLabel, artistLabel, votesLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontLarge, currentSongName, currentSongTime,
					currentlyPlayingLabel);
			revalidate();
			this.setOpaque(false);
			AppearanceSettings.setNotOpaque(songNameLabel, songAndArtistPanel, albumImageLabel, artistLabel, upvoteButton, downvoteButton, votesLabel);
			upvoteButton.setContentAreaFilled(false);
			downvoteButton.setContentAreaFilled(false);
			upvoteButton.setBorderPainted(false);
			downvoteButton.setBorderPainted(false);
			songAndArtistPanel.add(songNameLabel);
			songAndArtistPanel.add(artistLabel);
			add(albumImageLabel);
			add(songAndArtistPanel);
			add(upvoteButton);
			add(downvoteButton);
			add(votesLabel);
		}
	}

	public void initializeComponents() {

		songFilePaths = new Vector<String>();
		//
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());

		// addSongButton = new JButton();
		// ImageIcon addSongButtonImage = new
		// ImageIcon("images/button_add-song.png");
		// addSongButton.setIcon(addSongButtonImage);
		// addSongButton.setOpaque(false);
		// addSongButton.setBorderPainted(false);
		// addSongButton.setContentAreaFilled(false);
		//
		bottomButtonPanel = new JPanel();

		bottomButtonPanel.setOpaque(false);

		refreshButton = new JButton();
		ImageIcon refreshButtonImage = new ImageIcon("images/button_refresh.png");
		refreshButton.setIcon(refreshButtonImage);
		refreshButton.setOpaque(false);
		refreshButton.setBorderPainted(false);
		refreshButton.setContentAreaFilled(false);

		// buttonsPanel.add(addSongButton, BorderLayout.NORTH);
		//bottomButtonPanel.add(refreshButton);

		// buttonsPanel.add(addSongButton, BorderLayout.SOUTH);
		// JPanel topHostPanel = new JPanel();
		// topHostPanel.setLayout(new FlowLayout());
		// topHostPanel.setOpaque(false);
		partyLabel = new JLabel("<html>" + party.getPartyName() + " by " + party.getHostName() + "</html>");
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, 150, partyLabel);
		AppearanceSettings.setForeground(Color.white, partyLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, partyLabel);
		// partyLabel.setSize(new
		// Dimension(AppearanceConstants.GUI_WIDTH/4,150));
		partyLabel.setOpaque(false);
		Image image = new ImageIcon(party.getImageFilePath()).getImage();
		System.out.println("CLAIRISSE 2: " + party.getImageFilePath());
		partyImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		// hostLabel = new JLabel("Host: " + party.getHostName());
		// hostLabel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH/4,50));
		// hostImage = party.getPartyImage();

		JLabel hostImageLabel = new JLabel(partyImage);
		hostImageLabel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 100));

		// Image image = new
		// ImageIcon(this.party.getHost().getImageFilePath()).getImage();
		// hostImage.setIcon(new ImageIcon(image.getScaledInstance(150, 150,
		// java.awt.Image.SCALE_SMOOTH)));
		viewProfileButton = new JButton();
		ImageIcon viewProfileButtonImage = new ImageIcon("images/button_view-profile.png");
		viewProfileButton.setIcon(viewProfileButtonImage);
		// ImageIcon viewProfileImage = new
		// ImageIcon("images/button_leave-party.png");
		// viewProfileButton.setIcon(viewProfileImage);
		viewProfileButton.setOpaque(false);
		viewProfileButton.setBorderPainted(false);
		viewProfileButton.setContentAreaFilled(false);

		leaveButton = new JButton();
		ImageIcon leaveButtonImage;
		if (sw.getAccount() instanceof User) {
			if (((User) sw.getAccount()).isHost()) {
				leaveButtonImage = new ImageIcon("images/button_end-party.png");
			} else {

				leaveButtonImage = new ImageIcon("images/button_leave-party.png");
			}
		} else {

			leaveButtonImage = new ImageIcon("images/button_leave-party.png");
		}
		leaveButton.setIcon(leaveButtonImage);
		leaveButton.setOpaque(false);
		leaveButton.setBorderPainted(false);
		leaveButton.setContentAreaFilled(false);

		JPanel leftButtonPanel = new JPanel();
		leftButtonPanel.add(viewProfileButton);
		leftButtonPanel.add(leaveButton);
		leftButtonPanel.setOpaque(false);
		//leftButtonPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 125));
		leftButtonPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT / 7));
		
		hostPanel = new JPanel();
		hostPanel.setLayout(new FlowLayout());
		// hostLabel.setOpaque(false);
		// hostPanel.add(partyLabel);
		partyLabel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT / 8));
		hostPanel.add(partyLabel);
		// hostPanel.add(hostLabel);
		hostImageLabel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT*2 / 8));
		hostPanel.add(hostImageLabel);
		// hostPanel.add(topHostPanel, BorderLayout.NORTH);

		hostPanel.setOpaque(false);

		Account[] temp = (Account[]) party.getPartyMembers().toArray(new Account[party.getPartyMembers().size()]);
		Vector<String> tempUsers = new Vector<String>();
		for (Account a : temp) {
			if (a instanceof User) {
				tempUsers.add(((User) a).getUsername());
			}
		}
		partyPeopleList = new JList(tempUsers);
		for (String u : tempUsers) {
		System.out.println(u);
	} 
		JPanel scrollPanel = new JPanel();
		scrollPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT*2 / 8));
		scrollPanel.setOpaque(false);

		//partyPeopleList = new JList();
		partyPeopleList.setOpaque(false);
		partyPeopleScrollPane = new JScrollPane(partyPeopleList);
		partyPeopleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		partyPeopleScrollPane.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT*2 / 8));
		partyPeopleScrollPane.setOpaque(false);
		//JViewport viewport = new JViewport();
		//viewport.setView(new JPanel());
		//viewport.setOpaque(false);
		//partyPeopleScrollPane.setViewport(viewport);
		partyPeopleScrollPane.getViewport().setOpaque(false);
		//partyPeopleScrollPane.setBorder(BorderFactory.createEmptyBorder());
		partyPeopleScrollPane.setBorder(null);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, partyPeopleScrollPane, scrollPanel, partyPeopleList);
		partyPeopleScrollPane.setOpaque(false);
		/*for (User u : tempUsers) {
			System.out.println(u.getUsername());
		} */
		//scrollPanel.setOpaque(false);
		scrollPanel.add(partyPeopleScrollPane);
		revalidate();
		//
		// //custom scroll bar
		// partyPeopleScrollPane.getVerticalScrollBar().setUI(new
		// MyScrollBarUI());
		// UIManager.put("ScrollBarUI", "my.package.MyScrollBarUI");
		//
		//hostPanel.add(scrollPanel);
		hostPanel.add(leftButtonPanel);
		revalidate();
		
		currentlyPlayingPanel = new JPanel();

		Image i = null;
		try {
			i = ImageIO.read(new File("images/purplePlay.png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		i = getScaledImage(i, 100, 100);
		currentlyPlayingImage = new ImageIcon(i);

		JPanel currentlyPlayingPanelWithImage = new JPanel();
		currentlyPlayingPanelWithImage.setLayout(new BoxLayout(currentlyPlayingPanelWithImage, BoxLayout.X_AXIS));
		currentlyPlayingPanelWithImage.setOpaque(false);

		// currentlyPlayingImage = new ImageIcon("images/purplePlay.png");
		JLabel currentlyPlayingImageLabel = new JLabel(currentlyPlayingImage);
		currentlyPlayingPanel.setLayout(new BoxLayout(currentlyPlayingPanel, BoxLayout.Y_AXIS));
		JPanel currentlyPlayingInfo = new JPanel();
		currentlyPlayingInfo.setLayout(new BoxLayout(currentlyPlayingInfo, BoxLayout.X_AXIS));

		currentlyPlayingLabel = new JLabel("Now Playing: ");
		spotify_Url = "<iframe width=\"600\" height=\"50\" src=\"https://embed.spotify.com/?uri=spotify:track:7BKLCZ1jbUBVqRi2FVlTVw&theme=white\" frameborder=\"0\" allowtransparency></iframe>";;
		//swingFXWebView = new SwingFXWebView(spotify_Url);
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// swingFXWebView.initComponents("https://embed.spotify.com/?uri=spotify:track:7BKLCZ1jbUBVqRi2FVlTVw&theme=dark");
		// }
		// });
		// swingFXWebView.setOpaque(false);
		currentSongName = new JLabel("");
		currentSongTime = new JLabel("");
		AppearanceSettings.setNotOpaque(currentSongName, currentSongTime, currentlyPlayingPanel, currentlyPlayingLabel);
		AppearanceSettings.setForeground(Color.WHITE, currentSongName, currentSongTime, currentlyPlayingPanel,
				currentlyPlayingLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, currentSongName);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, currentlyPlayingLabel, currentSongTime);

		currentlyPlayingInfo.setOpaque(false);
		currentlyPlayingLabel.setOpaque(false);

		currentlyPlayingInfo.add(currentSongName);
		currentlyPlayingInfo.add(currentSongTime);
		currentlyPlayingPanel.add(currentlyPlayingLabel);
		//currentlyPlayingPanel.add(swingFXWebView);
		currentlyPlayingPanel.add(currentlyPlayingInfo);
		currentlyPlayingPanelWithImage.add(currentlyPlayingImageLabel);
		currentlyPlayingPanelWithImage.add(currentlyPlayingPanel);
		// currentlyPlayingPanel.add(currentlyPlayingInfo);

		// currentlyPlayingPanel.add(currentSongTime);
		// if (this.party.getSongs().size() != 0) {
		// this.updateCurrentlyPlaying();
		// }

		centerPanel.add(currentlyPlayingPanelWithImage, BorderLayout.NORTH);
		listModel = new DefaultListModel<SingleSongPanel>();

		// df = new DefaultListModel<>();
		// songList = new JList<SingleSongPanel>(df);
		songList = new JList<SingleSongPanel>();
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
		searchedSongArtist = new JLabel();
		searchedSongAlbum = new JLabel();
		searchedSongArtwork = new JLabel();
		searchedSongPanel = new JPanel();
		searchBar = new JTextField();
		returnedSongsList = new JList<String>();
		returnedSongsScrollPane = new JScrollPane(returnedSongsList);
		// AppearanceSettings.setForeground(Color.WHITE, searchBar);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, searchBar, searchedSong);

		// cards = new JPanel(new CardLayout());

		songList.setPreferredSize(new Dimension(600, 1000));
		songList.setOpaque(false);
		songScrollPane = new JScrollPane(songList);
		songScrollPane.setPreferredSize(new Dimension(600, 700));
		songScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// songScrollPane.setViewportBorder(null);
		songScrollPane.setBorder(null);
		songScrollPane.setOpaque(false);
		songScrollPane.getViewport().setOpaque(false);
		centerPanel.setOpaque(false);

		centerPanel.add(songScrollPane, BorderLayout.CENTER);
		centerPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
		revalidate();

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static class MyViewport extends JViewport {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyViewport() {
			this.setOpaque(false);
		}
	}
	
	// create the panel that shows songs in order of votes, called when
	// partywindow is created
	// and whenever someone upvotes or downvotes a song
	public void setSongs(Party receivedParty) {
		System.out.println("setting songs ... ");
		songList.removeAll();
		revalidate();
		if (receivedParty.getSongs().size() > 0) {
			this.currentSongName.setText(receivedParty.getSongs().get(0).getName());
		}
		for (int i = 1; i < receivedParty.getSongs().size(); i++) {
			songList.add(new SingleSongPanel(receivedParty.getSongs().get(i)));
			System.out.println("adding song " + receivedParty.getSongs().get(i).getName());
		}
		revalidate();
		repaint();
		// System.out.println("in setsongs");
		// if (songList != null) {
		// songList.removeAll();
		// } else {
		// songList = new JList <SingleSongPanel>();
		// }
		// //add songs in party to songs arraylist
		// for (PartySong ps : receivedParty.getSongs()) {
		// SingleSongPanel ssp = new SingleSongPanel(ps);
		// //songs.add(ssp);
		// System.out.println("adding song " + ps.getName() + " with " +
		// ps.getVotes() + " votes");
		// songList.add(ssp);
		// }
		//
		// //set at least 10
		// if (songList.getVisibleRowCount()< 10) {
		// for (int i = 0; i < 10-songList.getVisibleRowCount(); i ++) {
		// SingleSongPanel ssp = new SingleSongPanel(new PartySong("", 0.0));
		// songList.add(ssp);
		// }
		// }
		// revalidate();
	}

	public void createGUI() {
		setSize(1280, 800);
		setLayout(new BorderLayout());

		// Set appearance settings
		AppearanceSettings.setForeground(Color.white, refreshButton);
		AppearanceSettings.setSize(150, 80, refreshButton);
		// AppearanceSettings.setSize(150, 150, hostLabel);
		// AppearanceSettings.setBackground(AppearanceConstants.trojamPurple,
		// addSongButton, refreshButton, hostLabel);
		// AppearanceSettings.setOpaque(addSongButton, refreshButton);
		// AppearanceSettings.setNotOpaque(hostLabel);
		AppearanceSettings.unSetBorderOnButtons(refreshButton);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, refreshButton);

		// AppearanceSettings.setSize(x, y, components);
		// AppearanceSettings.setBackground(Color.black, mainPanel, songPanel,
		// leftPanel, profilePanel, mainPanel, songScrollPane);

		// songPanel.add(songScrollPane);

		addSongPanel = createAddSongPanel();
		// cards.add(buttonsPanel, "button panel");
		// cards.add(addSongPanel, "add song panel");
		// add(swMainPanel, BorderLayout.CENTER);

		// add(cards, BorderLayout.CENTER);
		// add(pwMainPanel, BorderLayout.EAST);

		centerPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 2, AppearanceConstants.GUI_HEIGHT));
		hostPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT));
		// addSongPanel.setPreferredSize(new
		// Dimension(AppearanceConstants.GUI_WIDTH/4,AppearanceConstants.GUI_HEIGHT));
		addSongPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT));

		cards = new JPanel(new CardLayout());
		cards.setOpaque(false);
		cards.add(hostPanel, "host panel");

		if (account instanceof User) {
			//JPanel PartyProfilePanel = new ProfilePanel((User) account, sw);
			PartyProfilePanel ppp = new PartyProfilePanel((User) account, sw);
			ppp.setOpaque(false);
			ppp.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT));
			cards.add(ppp, "profile panel");
		}

		add(cards, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(addSongPanel, BorderLayout.EAST);

		cl = (CardLayout) cards.getLayout();
		cl.show(cards, "host panel");

	}

	public void addListeners() {

		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(songList.getModel().getSize());
				if (songList.getModel().getSize() > 0) {
					currentSongName.setText(songList.getModel().getElementAt(0).partySong.getName());
				}
				revalidate();
			}

		});

		addNewSongButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// FOR SPOTIFY
				//spotify_Url = "https://embed.spotify.com/?uri=spotify:track:"+returnedSongs.get(returnedSongsList.getSelectedIndex()).getId()+"&theme=dark";
				//System.out.println(" weeeeeeeeeeee" + spotify_Url );
				SongData songInfo = returnedSongs.get(returnedSongsList.getSelectedIndex());
				// Platform.runLater(new Runnable() {
				// @Override
				// public void run() {
				// System.out.println("getting url........... " +
				// returnedSongsList.getSelectedIndex());
				// String trackID =
				// returnedSongs.get(returnedSongsList.getSelectedIndex()).getId();
				// String songUrl =
				// "https://embed.spotify.com/?uri=spotify:track:" + trackID +
				// "&theme=dark";
				// swingFXWebView.loadSong(songUrl);
				// swingFXWebView.revalidate();
				// swingFXWebView.repaint();
				// }
				// });

				searchButton.setEnabled(true);
				// if (!searchedSong.getText().equals("")) {
				System.out.println(songInfo.getName());
				PartyWindow.this.searchBar.setText("");
				sw.client.addNewSong(songInfo, PartyWindow.this.party.getPartyName());
				// SingleSongPanel ssp = new SingleSongPanel(new
				// PartySong(searchedSong.getText()));
				// df.addElement(ssp);
				// //listModel.addElement(ssp);
				// System.out.println(songList.getModel().getSize());
				// <<<<<<< Updated upstream
				// addSongToQueue();
				// songList.add(ssp);
				// if (songFilePaths.isEmpty()) {
				// songFilePaths.add(filePath);
				// MusicPlayer mp = new MusicPlayer(songFilePaths.get(0),
				// PartyWindow.this);
				// }
				// else {
				// songFilePaths.add(filePath);
				// }
				// //currentSongName.setText(searchedSong.getText());
				// searchedSong.setText("");
				//
				// =======
				// songList.add(ssp);
				// currentSongName.setText(searchedSong.getText());
				// searchedSong.setText("");
				// addSongToQueue();
				// >>>>>>> Stashed changes
				revalidate();
				// }
			}

		});

		// searchedSong.

		leaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				if (sw.getAccount() instanceof User) {
//					if (((User) sw.getAccount()).isHost()) {
//						// TODO Send message to all user in that party that the
//						// party is over and send them them to the end window
//						// RUTH
//					}
//				} else { // user is not a host
//							// TODO Send message to server that user left the
//							// party
//							// and remove user from and update the party's user
//							// set
//							// Ruth
//				}
				//sw.client.close();
				sw.client.leaveParty();
				repaint();
				sw.showEndWindow();
			}

		});

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Search database to get song and set text

				// TODO: Commented out lines below to make song search local
				// new SongSearch(searchBar.getText(), sw.getClient());
				// searchedSong.setText(searchBar.getText());
				// searchedSong.setText(song_name);
				// searchedSongArtist.setText(song_artist);
				// searchedSongAlbum.setText(song_album);
				// set image icon

				// ****************NON SPOTIFY FUNCTIONALITY**************
				String searchedText = searchBar.getText();
				/*
				 * addSongToPanel(searchedText); searchBar.setText("");
				 * searchButton.setEnabled(false); revalidate();
				 */
				// ********************************************************

				// Testing out spotify search
				// ********************************************************
				//returnedSongs is an array of songData objects
				//everytime you click search, it repopulates with that songs data
				//go through one by one and add to songList
				returnedSongs = JsonReader.getSongData(searchedText);
				String[] spotifySongArray;
				if (returnedSongs != null) {
					if (!returnedSongs.isEmpty()) {
						spotifySongArray = new String[returnedSongs.size()];
						for (int i = 0; i < returnedSongs.size(); i++) {
							String song = returnedSongs.get(i).getName() + " by " + returnedSongs.get(i).getArtist();
							spotifySongArray[i] = song;
							System.out.println(song);
						}
						returnedSongsList.setListData(spotifySongArray);
						revalidate();
						repaint();
					} else {
						spotifySongArray = new String[1];
						spotifySongArray[0] = "Song not found. Choose another song!";
						returnedSongsList.setListData(spotifySongArray);
					}
				} else {
					spotifySongArray = new String[1];
					spotifySongArray[0] = "Song not found. Choose another song!";
					returnedSongsList.setListData(spotifySongArray);
				}
				// ********************************************************

			}

		});

		viewProfileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "profile panel");
			}

		});

	}

	public JPanel createAddSongPanel() {
		JPanel tempPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel dummyPanel = new JPanel();
		JPanel dummyPanel2 = new JPanel();
		JPanel searchedSongCenterPanel = new JPanel();
		// JPanel searchBarPanel = new JPanel();
		// searchBarPanel.setLayout(new FlowLayout());
		centerPanel.setLayout(new FlowLayout());
		// tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));

		tempPanel.setSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT));
		AppearanceSettings.setNotOpaque(tempPanel, centerPanel, searchedSong, searchBar, returnedSongsList,
				returnedSongsScrollPane, buttonsPanel, dummyPanel, dummyPanel2, searchedSongPanel,
				searchedSongCenterPanel);
		AppearanceSettings.setSize(150, 50, searchButton, addNewSongButton);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT, centerPanel);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, 50, searchBar);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, 50, searchedSong, searchedSongArtist,
				searchedSongAlbum);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, 100, searchedSongArtwork);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT / 4,
				searchedSongCenterPanel, searchedSongPanel);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, 175, dummyPanel);
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH / 4, 200, dummyPanel2, returnedSongsList,
				returnedSongsScrollPane);
		AppearanceSettings.setForeground(Color.white, addNewSongButton, searchButton, searchedSong);
		AppearanceSettings.setForeground(AppearanceConstants.trojamPurple, returnedSongsList);
		// AppearanceSettings.setSize(150, 80, addSongButton, refreshButton,
		// hostLabel);
		// AppearanceSettings.setSize(150, 150, hostLabel);
		// AppearanceSettings.setOpaque(addSongButton, refreshButton,
		// hostLabel);
		AppearanceSettings.unSetBorderOnButtons(addNewSongButton, searchButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, addNewSongButton, searchButton);

		// searchBarPanel.add(searchBar);
		// searchBarPanel.add(searchButton);

		searchedSong.setText("");
		searchedSongArtist.setText("");
		searchedSongAlbum.setText("");
		Image img = new ImageIcon("images/colorparty.jpg").getImage();
		ImageIcon testIcon = new ImageIcon(
				img.getScaledInstance(AppearanceConstants.GUI_WIDTH / 4, 100, java.awt.Image.SCALE_SMOOTH));
		searchedSongArtwork.setIcon(testIcon);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, searchedSong, searchedSongArtist, searchedSongAlbum);
		searchedSongCenterPanel.add(searchedSong);
		searchedSongCenterPanel.add(searchedSongArtist);
		searchedSongCenterPanel.add(searchedSongAlbum);
		searchedSongCenterPanel.add(searchedSongArtwork);
		searchedSongPanel.setLayout(new BorderLayout());
		searchedSongPanel.add(searchedSongCenterPanel, BorderLayout.CENTER);
		// searchedSongPanel.add(searchedSongArtwork, BorderLayout.WEST);
		// centerPanel.add(Box.createVerticalStrut(275));
		JLabel addSongLabel = new JLabel("Add a Jam!");
		addSongLabel.setAlignmentY(this.BOTTOM_ALIGNMENT);
		addSongLabel.setForeground(Color.white);
		AppearanceSettings.setFont(AppearanceConstants.fontHuge, addSongLabel);
		returnedSongsScrollPane.getViewport().setOpaque(false);
		dummyPanel.add(addSongLabel);
		centerPanel.add(dummyPanel);
		centerPanel.add(searchBar);
		centerPanel.add(returnedSongsScrollPane);
		centerPanel.add(searchButton);
		//centerPanel.add(searchedSong);
		// centerPanel.add(searchedSongPanel);
		// addNewSongButton.setEnabled(false);
		centerPanel.add(addNewSongButton);
		centerPanel.add(dummyPanel2);
		// centerPanel.add(Box.createVerticalStrut(275));
		// centerPanel.setPreferredSize(new Dimension(450,400));

		tempPanel.add(centerPanel);

		return tempPanel;

	}

	// Paint background image -- needs to be outside of other methods
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = new ImageIcon("images/backgroundImage.png").getImage();
		// backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800,
		// java.awt.Image.SCALE_SMOOTH));
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void sendSongVoteUpdate(SongVoteMessage svm) {
		System.out.println("received update");
		Party receivedParty = svm.getParty();
		// PartyWindow.this.party.upvoteSong(receivedSong);
		setSongs(receivedParty);
	};

	// public static void main(String [] args) {
	// User user = new User("testUsername", "Adam", "Moffitt",
	// "images/JeffreyMiller-cropped.png");
	// PublicParty partayTime = new PublicParty("theBestParty", user, new
	// ImageIcon("party-purple.jpg"));
	// partayTime.addSong(new PartySong("Song1", 3.0));
	// partayTime.addSong(new PartySong("Song2", 3.0));
	// partayTime.addSong(new PartySong("Song3", 3.0));
	// partayTime.addSong(new PartySong("Song4", 3.0));
	// partayTime.addSong(new PartySong("Song5", 3.0));
	// partayTime.addSong(new PartySong("Song6", 3.0));
	// partayTime.addSong(new PartySong("Song7", 3.0));
	// partayTime.addSong(new PartySong("Song8", 3.0));
	// partayTime.addSong(new PartySong("Song9", 3.0));
	// partayTime.addSong(new PartySong("Song10", 3.0));
	// partayTime.addSong(new PartySong("Song11", 3.0));
	// partayTime.addSong(new PartySong("Song12", 3.0));
	// new PartyWindow(partayTime, new SelectionWindow(user, null,
	// null)).setVisible(true);
	// }

	// CITE:
	// http://www.java2s.com/Tutorials/Java/Swing_How_to/JScrollPane/Create_custom_JScrollBar_for_JScrollPane.htm
	// public class MyScrollBarUI extends BasicScrollBarUI {
	//
	// private final Dimension d = new Dimension();
	//
	// @Override
	// protected JButton createDecreaseButton(int orientation) {
	// return new JButton() {
	// @Override
	// public Dimension getPreferredSize() {
	// return d;
	// }
	// };
	// }
	//
	// @Override
	// protected JButton createIncreaseButton(int orientation) {
	// return new JButton() {
	// @Override
	// public Dimension getPreferredSize() {
	// return d;
	// }
	// };
	// }
	//
	//
	// @Override
	// protected void paintTrack(Graphics g, JComponent c, Rectangle
	// trackBounds) {
	// // your code
	// }
	//
	// @Override
	// protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
	// Graphics2D g2 = (Graphics2D) g.create();
	// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	// Color color = null;
	// JScrollBar sb = (JScrollBar) c;
	// if (!sb.isEnabled() || r.width > r.height) {
	// return;
	// } else if (isDragging) {
	// color = Color.DARK_GRAY;
	// } else if (isThumbRollover()) {
	// color = Color.LIGHT_GRAY;
	// } else {
	// color = Color.GRAY;
	// }
	// g2.setPaint(color);
	// g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
	// g2.setPaint(Color.WHITE);
	// g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
	// g2.dispose();
	// }
	//
	// @Override
	// protected void setThumbBounds(int x, int y, int width, int height) {
	// super.setThumbBounds(x, y, width, height);
	// scrollbar.repaint();
	// }
	// }

	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	public void receiveSongInfo(FoundSongMessage fsm) {
		// TODO for catherine
		if (fsm.getFoundSong()) {
			song_name = fsm.getSongName();
			System.out.println(song_name);
			song_artist = fsm.getArtist();
			System.out.println(song_artist);
			song_album = fsm.getAlbum();
			System.out.println(song_album);
			song_artwork_filepath = fsm.getArtworkFilepath();
			System.out.println(song_artwork_filepath);
			song_mp3_filepath = fsm.getmp3FilePath();
			System.out.println(song_artwork);
			song_artwork = fsm.getActualImage();
		} else {
			// TODO some error message
		}
		// call method to construct song panel

	}

	public class PartyProfilePanel extends JPanel {
		JButton viewParty;
		
		private static final long serialVersionUID = 1;
		
		ImageIcon profilePic;
		JLabel profileName, dummyLabel;
		JLabel profileUserName;
		User user;
		JScrollPane userHistorySP;
		JTextArea partiesTextArea;
		JButton logout;
		JLabel profilePanelTitle;
		
		public PartyProfilePanel(User user, SelectionWindow sw){
			this.user = user;
			profilePic = user.getUserImage();
			
			profilePanelTitle = new JLabel("Profile Info:");
			profilePanelTitle.setForeground(Color.white);
			AppearanceSettings.setFont(AppearanceConstants.fontLarge, profilePanelTitle);
			
			dummyLabel = new JLabel(" ");
			
			profileName = new JLabel("Name: " + user.getFirstName() + " " + user.getLastName(), SwingConstants.CENTER);
			AppearanceSettings.setFont(AppearanceConstants.fontMedium, profileName);
			profileName.setForeground(Color.white);
			//profileName.setHorizontalAlignment(SwingConstants.CENTER);
			//profileName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			profileUserName = new JLabel("Username: " + user.getUsername(), SwingConstants.CENTER);
			AppearanceSettings.setFont(AppearanceConstants.fontMedium, profileUserName);
			profileUserName.setForeground(Color.white);
			//profileUserName.setHorizontalAlignment(SwingConstants.CENTER);
			//profileUserName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			partiesTextArea = new JTextArea(5,20);
			partiesTextArea.setOpaque(false);
			partiesTextArea.setFont(AppearanceConstants.fontSmall);
			partiesTextArea.setForeground(Color.white);
			//partiesTextArea.append("Party History: \n");
			partiesTextArea.setLineWrap(true);
			partiesTextArea.setWrapStyleWord(true);
			partiesTextArea.setEditable(false);
			
			userHistorySP = new JScrollPane(partiesTextArea);
			userHistorySP.setOpaque(false);
			userHistorySP.getViewport().setOpaque(false);
			Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
			userHistorySP.setViewportBorder( border );
			userHistorySP.setBorder( border );
			
//			if(user.getParties().isEmpty()){
//				partiesTextArea.append("Looks like you haven't joined a party yet. Are you a CS student? You really should talk to Jeffrey Miller about giving you some easier assignments");
//			}
			
//			else{
//				for(Party p : user.getParties()){
//					partiesTextArea.append(p.getPartyName() + "\n\n");
//				}
//			}
			
			logout = new JButton();
			ImageIcon logoutButtonImage = new ImageIcon("images/button_log-out.png");
			logout.setIcon(logoutButtonImage);
			logout.setOpaque(false);
			logout.setBorderPainted(false);
			logout.setContentAreaFilled(false);
			
			logout.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new LoginScreenWindow(sw.getClient()).setVisible(true);
					sw.dispose();
				}
			});
			
			viewParty = new JButton();
			ImageIcon viewPartyImage = new ImageIcon("images/button_view-party-info.png");
			viewParty.setIcon(viewPartyImage);
			viewParty.setOpaque(false);
			viewParty.setBorderPainted(false);
			viewParty.setContentAreaFilled(false);
			viewParty.setSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 50));
			viewParty.setAlignmentX(this.CENTER_ALIGNMENT);

			viewParty.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout) cards.getLayout();
					cl.show(cards, "host panel");
				}
			});
			
			//setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setLayout(new FlowLayout());
			//this.add(Box.createVerticalGlue());
			this.add(profilePanelTitle);
			this.add(new JLabel(profilePic));
			this.add(profileName);
			this.add(profileUserName);
			this.add(dummyLabel);
			//this.add(userHistorySP);
			this.add(viewParty);
			this.add(logout);
			this.setSize(AppearanceConstants.GUI_WIDTH/4, AppearanceConstants.GUI_HEIGHT);
		}

		/*PartyProfilePanel(User user, SelectionWindow sw) {
			super();
			//super(user, sw);
			viewParty = new JButton();
			ImageIcon viewPartyImage = new ImageIcon("images/button_view-party-info.png");
			viewParty.setIcon(viewPartyImage);
			viewParty.setOpaque(false);
			viewParty.setBorderPainted(false);
			viewParty.setContentAreaFilled(false);
			viewParty.setSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 50));
			viewParty.setAlignmentX(this.CENTER_ALIGNMENT);

			viewParty.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CardLayout cl = (CardLayout) cards.getLayout();
					cl.show(cards, "host panel");
				}
			});
			this.add(viewParty);
		} */
	}

	public void addSongToPanel(String songName) {

		filePath = "music/" + songName.trim().toLowerCase() + ".mp3";

		searchedSong.setText(songName);
		for (String s : songFilePaths) {
			System.out.println(s);
		}

	}

	public void addSongToQueue() {

	}

	public void endParty() {
		cards.remove(sw.endPartyPanel);
		sw.endPartyPanel = new EndPartyWindow(sw, true);
		sw.cards.add(sw.endPartyPanel, "end party panel");
		sw.showEndWindow();
	}

	// public void advanceSong() {
	// songFilePaths.remove(0);
	// songList.remove(0);
	// System.out.println("next song");
	// if (!songFilePaths.isEmpty()) {
	// MusicPlayer mp = new MusicPlayer(songFilePaths.get(0), this);
	// }
	// revalidate();
	// }
}

class SwingFXWebView extends JPanel {

	private Stage stage;
	private WebView browser;
	private JFXPanel jfxPanel;
	private JButton swingButton;
	private WebEngine webEngine;
	public String content_Url;

	public SwingFXWebView(String content_Url) {
		this.content_Url = content_Url;
		initComponents();
	}

	public static void main(String... args) {
		// Run this later:
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();
				String url = "<iframe width=\"300\" height=\"80\" "
						+ "src=\"https://embed.spotify.com/?uri=spotify:track:7BKLCZ1jbUBVqRi2FVlTVw&theme=white\" "
						+ "frameborder=\"0\" scrolling=\"no\" style=\"border:0\" allowtransparency></iframe>";
				frame.getContentPane().add(new SwingFXWebView(url));

				frame.setMinimumSize(new Dimension(640, 480));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void initComponents() {

		jfxPanel = new JFXPanel();
		createScene(content_Url);

		setLayout(new BorderLayout());
		add(jfxPanel, BorderLayout.CENTER);
	}

	public void reload() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				webEngine.reload();
			}
		});

	}


	public void reload(int width, int height, String url) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String content_Url = "<iframe width=\""+width+"\" height=\""+height+"\" src="+url+"frameborder=\"0\" allowtransparency></iframe>";
				createScene(content_Url);
			}
		});

	}


	/**
	 * createScene
	 *
	 * Note: Key is that Scene needs to be created and run on "FX user thread"
	 * NOT on the AWT-EventQueue Thread
	 *
	 */
	private void createScene(String content_Url) {
		PlatformImpl.startup(new Runnable() {
			@Override
			public void run() {

				stage = new Stage();

				stage.setTitle("Hello Java FX");
				stage.setResizable(true);

				Group root = new Group();
				Scene scene = new Scene(root, 80, 20);
				stage.setScene(scene);

				// Set up the embedded browser:
				browser = new WebView();
				webEngine = browser.getEngine();

				System.out.println(content_Url);
				webEngine.loadContent(content_Url);

				ObservableList<Node> children = root.getChildren();
				children.add(browser);

				jfxPanel.setScene(scene);
			}
		});
	}
}