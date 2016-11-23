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
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sun.javafx.application.PlatformImpl;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logic.Account;
//import frames.SelectionWindow.MyScrollBarUI;
import logic.Party;
import logic.PartySong;
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
	ArrayList<SongData> returnedSongs;
	private String song_name;
	private String song_artist;
	private String song_album;
	private String song_artwork_filepath;
	private String song_mp3_filepath;
	private ImageIcon song_artwork;
	String filePath;

	// argument will be taken out once we turn this into a JPanel
	public PartyWindow(Party partayTime, SelectionWindow sw) {
		super();
		this.party = partayTime;
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
		PartySong partySong;
		private JButton upvoteButton, downvoteButton;
		private JLabel votesLabel, songNameLabel;

		public SingleSongPanel(PartySong ps) {
			AppearanceSettings.setSize(600, 100, this);
			partySong = ps;
			setLayout(new GridLayout(1, 4));
			songNameLabel = new JLabel(ps.getName());

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
					// setSongs();
				}

			});
			votesLabel = new JLabel(Integer.toString(ps.getVotes()));

			AppearanceSettings.setForeground(Color.white, songNameLabel, votesLabel);
			AppearanceSettings.setForeground(Color.white, currentSongName, currentSongTime, currentlyPlayingLabel);
			AppearanceSettings.setSize(100, 40, songNameLabel, votesLabel);
			// , currentSongName, currentSongTime, currentlyPlayingLabel);
			// AppearanceSettings.setBackground(AppearanceConstants.mediumGray,
			// songNameLabel, votesLabel, songList, upvoteButton,
			// downvoteButton, this);
			// AppearanceSettings.setBackground(AppearanceConstants.trojamPurple,
			// currentSongName, currentSongTime, currentlyPlayingLabel);
			// AppearanceSettings.setOpaque(songNameLabel, votesLabel,
			// currentSongName, currentSongTime, currentlyPlayingLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontSmall, songNameLabel, votesLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontLarge, currentSongName, currentSongTime,
					currentlyPlayingLabel);
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
		bottomButtonPanel.add(refreshButton);

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
		leftButtonPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 125));

		hostPanel = new JPanel();
		hostPanel.setLayout(new FlowLayout());
		// hostLabel.setOpaque(false);
		// hostPanel.add(partyLabel);
		hostPanel.add(partyLabel);
		// hostPanel.add(hostLabel);
		hostPanel.add(hostImageLabel);
		// hostPanel.add(topHostPanel, BorderLayout.NORTH);

		hostPanel.setOpaque(false);

		Account[] temp = (Account[]) party.getPartyMembers().toArray(new Account[party.getPartyMembers().size()]);
		Vector<User> tempUsers = new Vector<User>();
		for (Account a : temp) {
			if (a instanceof User) {
				tempUsers.add((User) a);
			}
		}
		partyPeopleList = new JList(tempUsers);
		JPanel scrollPanel = new JPanel();
		scrollPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 400));
		scrollPanel.setOpaque(false);

		partyPeopleList = new JList();
		partyPeopleList.setOpaque(false);
		partyPeopleScrollPane = new JScrollPane(partyPeopleList);
		partyPeopleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		partyPeopleScrollPane.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, 400));
		partyPeopleScrollPane.setOpaque(false);
		partyPeopleScrollPane.getViewport().setOpaque(false);
		partyPeopleScrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPanel.add(partyPeopleScrollPane);
		//
		// //custom scroll bar
		// partyPeopleScrollPane.getVerticalScrollBar().setUI(new
		// MyScrollBarUI());
		// UIManager.put("ScrollBarUI", "my.package.MyScrollBarUI");
		//
		// hostPanel.add(scrollPanel, BorderLayout.CENTER);
		hostPanel.add(leftButtonPanel, BorderLayout.SOUTH);

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
		swingFXWebView = new SwingFXWebView();
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
		currentlyPlayingPanel.add(swingFXWebView);
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

	// create the panel that shows songs in order of votes, called when
	// partywindow is created
	// and whenever someone upvotes or downvotes a song
	public void setSongs(Party receivedParty) {
		System.out.println("setting songs ... ");
		songList.removeAll();
		for (int i = 0; i < receivedParty.getSongs().size(); i++) {
			songList.add(new SingleSongPanel(receivedParty.getSongs().get(i)));
			System.out.println("adding song " + receivedParty.getSongs().get(i).getName());
		}
		revalidate();
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
			JPanel PartyProfilePanel = new ProfilePanel((User) account, sw);
			PartyProfilePanel.setOpaque(false);
			PartyProfilePanel
					.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH / 4, AppearanceConstants.GUI_HEIGHT));
			cards.add(PartyProfilePanel, "profile panel");
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
				sw.client.addNewSong(searchedSong.getText(), PartyWindow.this.party.getPartyName());
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
				if (sw.getAccount() instanceof User) {
					if (((User) sw.getAccount()).isHost()) {
						// TODO Send message to all user in that party that the
						// party is over and send them them to the end window
						// RUTH
					}
				} else { // user is not a host
							// TODO Send message to server that user left the
							// party
							// and remove user from and update the party's user
							// set
							// Ruth
				}
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
				returnedSongs = JsonReader.getSongData(searchedText);
				String[] songList;
				if (returnedSongs != null) {
					if (!returnedSongs.isEmpty()) {
						songList = new String[returnedSongs.size()];
						for (int i = 0; i < returnedSongs.size(); i++) {
							String song = returnedSongs.get(i).getName() + " by " + returnedSongs.get(i).getArtist();
							songList[i] = song;
							System.out.println(song);
						}
						returnedSongsList.setListData(songList);
						revalidate();
						repaint();
					} else {
						songList = new String[1];
						songList[0] = "Song not found. Choose another song!";
						returnedSongsList.setListData(songList);
					}
				} else {
					songList = new String[1];
					songList[0] = "Song not found. Choose another song!";
					returnedSongsList.setListData(songList);
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
		centerPanel.add(searchedSong);
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

	public class PartyProfilePanel extends ProfilePanel {
		JButton viewParty;

		PartyProfilePanel(User user, SelectionWindow sw) {
			super(user, sw);
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
		}
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

	public SwingFXWebView() {
		initComponents();
	}

	public static void main(String... args) {
		// Run this later:
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();

				frame.getContentPane().add(new SwingFXWebView());

				frame.setMinimumSize(new Dimension(640, 480));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void initComponents() {

		jfxPanel = new JFXPanel();
		createScene();

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

	/**
	 * createScene
	 *
	 * Note: Key is that Scene needs to be created and run on "FX user thread"
	 * NOT on the AWT-EventQueue Thread
	 *
	 */
	private void createScene() {
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
				webEngine.load("https://embed.spotify.com/?uri=spotify:track:7BKLCZ1jbUBVqRi2FVlTVw&theme=dark");

				ObservableList<Node> children = root.getChildren();
				children.add(browser);

				jfxPanel.setScene(scene);
			}
		});
	}
}