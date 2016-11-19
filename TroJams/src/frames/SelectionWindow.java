package frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/*
 * SELECTION WINDOW: THIS IS OUR MAIN WINDOW, WHERE WE CHOOSE WHICH PARTY TO JOIN/CREATE A PARTY
 */

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import listeners.TextFieldFocusListener;
import logic.Party;
import logic.PartySong;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;


public class SelectionWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel swMainPanel, cards;
	private JList swcurrentParties;
	private JButton createAPartyButton;
	private JScrollPane partyScrollPane;
	
	private User user;
	private JMenuBar menuBar;
	private JMenu profile;
	private JMenu logout;
	
	private JPanel cpwMainPanel, cpwTopPanel, cpwBottomPanel, cpwRadioButtonPanel;
	private JLabel dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6;
	private JTextField cpwPartyNameTextField;
	private JTextField cpwPasswordTextField;
	private JRadioButton cpwPublicRadioButton;
	private JRadioButton cpwPrivateRadioButton;
	private JButton cpwCreateButton;
	private CardLayout cl;
	
	private JPanel pwMainPanel;
	private JLabel pwUsernameLabel, pwNameLabel, profileLabel, profileIconLabel;
	private ImageIcon profileIcon;
	private ArrayList <Party> currentParties;
		
	public SelectionWindow(User user, ArrayList<Party> parties){
		super("TroJams");
		this.user = user;
		this.currentParties = parties;
		if (currentParties == null) {
			System.out.println("No parties :(");
			currentParties = new ArrayList<Party> ();
		}
		initializeComponents();
		createGUI();
		addActionListeners();
	}

	private void initializeComponents(){
		
		this.setContentPane(new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image image = new ImageIcon("images/backgroundImage.png").getImage();
				new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
				g.drawImage(image, 0, 0, 1280, 800, this);
			}
		});  	
		
		swMainPanel = new JPanel();
		createAPartyButton = new JButton("Create a Party");
		profile = new JMenu("Profile");
		logout = new JMenu("Logout");
		menuBar = new JMenuBar();
		cards = new JPanel(new CardLayout());
		
		cpwMainPanel = new JPanel();
		cpwTopPanel = new JPanel();
		cpwBottomPanel = new JPanel();
		dummyLabel1 = new JLabel();
		dummyLabel2 = new JLabel();
		dummyLabel3 = new JLabel("Create a Party!");
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, dummyLabel3);
		dummyLabel3.setHorizontalAlignment(JLabel.CENTER);
	    dummyLabel3.setVerticalAlignment(JLabel.CENTER);
	    dummyLabel3.setForeground(Color.WHITE);
		dummyLabel4 = new JLabel();
		dummyLabel5 = new JLabel();
		dummyLabel6 = new JLabel();
		cpwRadioButtonPanel = new JPanel();
		cpwPartyNameTextField = new JTextField();
		cpwPasswordTextField = new JTextField();
		cpwPublicRadioButton = new JRadioButton("Public");
		cpwPrivateRadioButton = new JRadioButton("Private");
		cpwCreateButton = new JButton("Create Party");
		
		pwMainPanel = new JPanel();
		pwUsernameLabel = new JLabel();
		//pwUsernameLabel.setText(user.getUsername());
		pwUsernameLabel.setText("Username");
		pwNameLabel = new JLabel();
		//pwNameLabel.setText(user.getFirstName() + user.getLastName());
		pwNameLabel.setText("First_Name Last_Name");
		profileLabel = new JLabel("Profile");
		profileIcon = new ImageIcon("images/silhouette.png");
		profileIconLabel = new JLabel();
		profileIconLabel.setIcon(profileIcon);
	}
	
	private void createGUI(){
		setSize(AppearanceConstants.GUI_WIDTH, AppearanceConstants.GUI_HEIGHT);
		setLayout(new BorderLayout());
		setParties();
		createMenu();
		createCPWMenu();
		createSWPanel();
		AppearanceSettings.setNotOpaque(swMainPanel, cards);
		//createPWPanel();
		
		cards.add(swMainPanel, "selection window");
		cards.add(cpwMainPanel, "create party window");
//		add(swMainPanel, BorderLayout.CENTER);
		
		add(cards, BorderLayout.CENTER);
		//add(pwMainPanel, BorderLayout.EAST); 
		
		cl = (CardLayout) cards.getLayout();
		cl.show(cards, "selection window");
	}
	
	// creates Profile Window
	private void createPWPanel() {
		// Appearance settings for size and opaqueness
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT, pwMainPanel);
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT/5, profileLabel);
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT/3, profileIconLabel);
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT/10, pwUsernameLabel, pwNameLabel);
		AppearanceSettings.setNotOpaque(pwMainPanel);
		AppearanceSettings.setOpaque(pwUsernameLabel, pwNameLabel, profileLabel);
		// centers text in labels
		pwUsernameLabel.setHorizontalAlignment(JLabel.CENTER);
	    pwUsernameLabel.setVerticalAlignment(JLabel.CENTER);
		pwNameLabel.setHorizontalAlignment(JLabel.CENTER);
	    pwNameLabel.setVerticalAlignment(JLabel.CENTER);
	    profileLabel.setHorizontalAlignment(JLabel.CENTER);
	    profileLabel.setVerticalAlignment(JLabel.CENTER);
	    // Appearance settings for font/color
	    AppearanceSettings.setFont(AppearanceConstants.fontSmall, pwUsernameLabel, pwNameLabel);
	    AppearanceSettings.setFont(AppearanceConstants.fontMedium, profileLabel);
	    AppearanceSettings.setForeground(Color.WHITE, pwUsernameLabel, pwNameLabel, profileLabel);
	    AppearanceSettings.setNotOpaque(pwUsernameLabel, pwNameLabel, profileLabel);
		pwMainPanel.add(profileLabel);
		pwMainPanel.add(profileIconLabel);
		pwMainPanel.add(pwUsernameLabel);
		pwMainPanel.add(pwNameLabel);
		pwMainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
	}
	
	// creates the JMenuBar
	private void createMenu() {
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, profile, logout);
		menuBar.add(profile);
		menuBar.add(logout);
		setJMenuBar(menuBar);
	}
	
	private void setParties() {
		System.out.println("setting parties ... " + currentParties.size());
		swcurrentParties = new JList<SinglePartyPanel>();
		swcurrentParties.setLayout(new FlowLayout());
		swcurrentParties.setVisibleRowCount(10);
		//add parties to list
		
		for (Party p : currentParties) {
			SinglePartyPanel spp = new SinglePartyPanel(p);
			System.out.println("adding a party");
			swcurrentParties.add(spp);
		}
		revalidate();
	}
	
	// creates the main panel
	private void createSWPanel() {
		AppearanceSettings.setSize(AppearanceConstants.GUI_WIDTH*(4/5), AppearanceConstants.GUI_HEIGHT, swMainPanel);
		
		// getting the panel that holds the "create a party" button
		JPanel topPanel = new JPanel();
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, createAPartyButton);
		createAPartyButton.setOpaque(true);
		topPanel.add(createAPartyButton);
		topPanel.setOpaque(true);
		AppearanceSettings.setBackground(Color.WHITE, topPanel);
		swMainPanel.add(topPanel);
		
		// getting the panel that holds the scroll pane with parties
		swcurrentParties.setPreferredSize(new Dimension (600, 1000));
		partyScrollPane = new JScrollPane(swcurrentParties);
		partyScrollPane.setPreferredSize(new Dimension(600, 700));
		partyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		swMainPanel.add(partyScrollPane);
	}
	
	private class SinglePartyPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private Party party;
		private JButton partyButton;
		private JLabel hostLabel;
		
		public SinglePartyPanel (Party party) {
			AppearanceSettings.setSize(600, 100, this);
			this.party = party;
			setLayout(new GridLayout(1,2));
			hostLabel = new JLabel(party.getHostName());
			partyButton = new JButton(party.getPartyName());
			partyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//join that party
					//switch gui so it shows that party (asking for password if the party is private)
				}	
			});
			
			AppearanceSettings.setForeground(Color.white, partyButton, hostLabel);
			AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, partyButton, hostLabel);
			AppearanceSettings.setSize(100, 40, partyButton, hostLabel);
			AppearanceSettings.setOpaque(partyButton, hostLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontSmall, partyButton, hostLabel);
			
			add(partyButton);
			add(hostLabel);
		}
	}
	
	private void addActionListeners(){
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		profile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(cards, "create party window");
			}
		});
		
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginScreenWindow().setVisible(true);
				dispose();
			}
		});
		
		createAPartyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "create party window");
			}
		});
		
		//focus listeners for create a party
		cpwPartyNameTextField.addFocusListener(new TextFieldFocusListener("Party name", cpwPartyNameTextField));
		cpwPasswordTextField.addFocusListener(new TextFieldFocusListener("Password", cpwPasswordTextField));
				
		//document listeners for create a party
		cpwPartyNameTextField.getDocument().addDocumentListener(new MyDocumentListener());
		cpwPasswordTextField.getDocument().addDocumentListener(new MyDocumentListener());
				
		cpwPrivateRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			// If private, show password field
				cpwPasswordTextField.setVisible(true);			
			// Check if text fields are filled: enable create button
				if (canPressButtons()) {
					cpwCreateButton.setEnabled(true);
				} else {
					cpwCreateButton.setEnabled(false);
				}					
			}
		});
				
		cpwPublicRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If public, get rid of password field
				cpwPasswordTextField.setVisible(false);
				// Check if text fields are filled: enable create button
				if (canPressButtons()) {
					cpwCreateButton.setEnabled(true);
				} else {
					cpwCreateButton.setEnabled(false);
				}
			}
		});
				
		cpwCreateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				// CHANGE TO PARTY WINDOW
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "selection window");					
			}		
		});
	}
	
	public void createCPWMenu() {
		cpwMainPanel.setLayout(new BoxLayout(cpwMainPanel, BoxLayout.Y_AXIS));
		
		AppearanceSettings.setSize(1280, 50, dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6);
		AppearanceSettings.setSize(300, 50, cpwPartyNameTextField, cpwPasswordTextField);
		AppearanceSettings.setSize(1280, 20, cpwTopPanel);
		
		// Creates top panel with dummy labels so that the text field is at the bottom of the panel

		cpwTopPanel.add(dummyLabel1);
		cpwTopPanel.add(dummyLabel2);
		cpwTopPanel.add(dummyLabel3);
		cpwTopPanel.add(dummyLabel4);
		cpwTopPanel.add(dummyLabel5);	
		cpwTopPanel.add(cpwPartyNameTextField);
		cpwMainPanel.add(cpwTopPanel);
		
		// Makes it so you can only select one button
		ButtonGroup bg = new ButtonGroup();
		bg.add(cpwPublicRadioButton);
		bg.add(cpwPrivateRadioButton);
		cpwPrivateRadioButton.setSelected(true);
		
		// Adds radio buttons horizontally
		cpwRadioButtonPanel.setLayout(new BoxLayout(cpwRadioButtonPanel, BoxLayout.X_AXIS));
		cpwRadioButtonPanel.add(cpwPublicRadioButton);
		cpwRadioButtonPanel.add(cpwPrivateRadioButton);
		cpwMainPanel.add(cpwRadioButtonPanel);
		
		// Creates the bottom panel with password text field and create party button
		JPanel tempPanel1 = new JPanel();
		tempPanel1.add(cpwPasswordTextField);
		//cpwBottomPanel.add(cpwPasswordTextField);
		cpwBottomPanel.add(tempPanel1);
		JPanel tempPanel2 = new JPanel();
		tempPanel2.add(cpwCreateButton);
		//cpwBottomPanel.add(cpwCreateButton);
		cpwBottomPanel.add(tempPanel2);
		AppearanceSettings.setNotOpaque(tempPanel1, tempPanel2);
		cpwMainPanel.add(cpwBottomPanel);
		AppearanceSettings.setSize(1280, 80, cpwBottomPanel);
		AppearanceSettings.setSize(1280, 100, tempPanel1, tempPanel2);

		// Appearance settings
		cpwMainPanel.setSize(new Dimension(500,800));
		AppearanceSettings.setNotOpaque(cpwTopPanel, cpwMainPanel, cpwBottomPanel, cpwRadioButtonPanel);
		cpwPrivateRadioButton.setForeground(Color.white);
		cpwPublicRadioButton.setForeground(Color.white);
		
	}
	
	private boolean canPressButtons() {
		//usernameTextField, passwordTextField, firstNameTextField, lastNameTextField
		if (cpwPrivateRadioButton.isSelected()) {
			if (!cpwPartyNameTextField.getText().equals("Party name") && cpwPartyNameTextField.getText().length() != 0) {
				if (!cpwPasswordTextField.getText().equals("Password") && cpwPasswordTextField.getText().length() != 0) {
					return true;
				}
			}
		} else if (cpwPublicRadioButton.isSelected()) {
			if (!cpwPartyNameTextField.getText().equals("Party name") && cpwPartyNameTextField.getText().length() != 0) {
				return true;
			}
		}
	
		return false;
	}
	
	//sets the buttons enabled or disabled
	private class MyDocumentListener implements DocumentListener{
			
		@Override
		public void insertUpdate(DocumentEvent e) {
			cpwCreateButton.setEnabled(canPressButtons());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			cpwCreateButton.setEnabled(canPressButtons());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			cpwCreateButton.setEnabled(canPressButtons());
		}
	}
	
	public User getUser() {
		return this.user;
	}

	public static void main(String [] args) {
		User user = new User("username", "password");
		PrivateParty p1 = new PrivateParty("party1", "password1", user);
		PrivateParty p2 = new PrivateParty("party2", "password2", user);
		PublicParty p3 = new PublicParty("party3", user);
		ArrayList <Party> parties = new ArrayList <Party>();
		parties.add(p1);
		parties.add(p2);
		parties.add(p3);
		new SelectionWindow(user, parties).setVisible(true);
	}
	
}