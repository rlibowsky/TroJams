
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/*
 * SELECTION WINDOW: THIS IS OUR MAIN WINDOW, WHERE WE CHOOSE WHICH PARTY TO JOIN/CREATE A PARTY
 */

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import listeners.TextFieldFocusListener;
import logic.Party;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;
import networking.NewPartyMessage;
import networking.TrojamClient;
import resources.AppearanceConstants;
import resources.AppearanceSettings;


public class SelectionWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel swMainPanel, cards;
	private JList swcurrentParties;
	private JButton createAPartyButton;
	private JScrollPane partyScrollPane;
	
	private User user;
	
	private JPanel cpwMainPanel, cpwTopPanel, cpwBottomPanel, cpwRadioButtonPanel;
	private JLabel dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6;
	private JTextField cpwPartyNameTextField;
	private JTextField cpwPasswordTextField;
	private JRadioButton cpwPublicRadioButton;
	private JRadioButton cpwPrivateRadioButton;
	private JButton cpwCreateButton, cpwBackButton;
	private JLabel imageLabel, imageText;
	private ImageIcon partyImage;
	private String imageFilePath;
	private JFileChooser fileChooser;
	
	private CardLayout cl;
	
	private JPanel pwMainPanel;
	private JLabel pwUsernameLabel, pwNameLabel, profileLabel, profileIconLabel;
	private ImageIcon profileIcon;
	private ArrayList <Party> currentParties;
	private SelectionWindow sw;
	private TrojamClient client;
		
	public SelectionWindow(User user, ArrayList<Party> parties){
		super("TroJams");
		this.user = user;
		sw = this;
		this.client = new TrojamClient(user, "localhost", 1111, this);
		this.currentParties = parties;
		if (currentParties == null) {
			System.out.println("No parties :(");
			currentParties = new ArrayList<Party> ();
		}
		initializeComponents();
		createGUI();
		addActionListeners();
	}
	
	public void addNewParty(Party p) {
		currentParties.add(p);
		setParties();
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
		swMainPanel.setLayout(new BorderLayout());
		createAPartyButton = new JButton();
		ImageIcon createButtonImage = new ImageIcon("images/button_create-a-party.png");
		createAPartyButton.setIcon(createButtonImage);
		createAPartyButton.setOpaque(false);
		createAPartyButton.setContentAreaFilled(false);
		createAPartyButton.setBorderPainted(false);
		cards = new JPanel(new CardLayout());
		
		
		//create party image selection		
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
		cpwCreateButton = new JButton();
		ImageIcon cButtonImage = new ImageIcon("images/button_create-a-party.png");
		cpwCreateButton.setIcon(cButtonImage);
		cpwCreateButton.setContentAreaFilled(false);
		cpwCreateButton.setBorderPainted(false);
		cpwCreateButton.setOpaque(false);
		cpwBackButton = new JButton();
		ImageIcon bButtonImage = new ImageIcon("images/button_nah-never-mind.png");
		cpwBackButton.setIcon(bButtonImage);
		cpwBackButton.setContentAreaFilled(false);
		cpwBackButton.setBorderPainted(false);
		cpwBackButton.setOpaque(false);
		
		fileChooser = new JFileChooser();
		imageText = new JLabel("Click to upload a party picture");
		imageText.setForeground(Color.white);
		imageLabel = new JLabel();
		
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
		createCPWMenu();
		createSWPanel();
		AppearanceSettings.setNotOpaque(swMainPanel, cards);
		//createPWPanel();
//		addSongPanel = createAddSongPanel();
		JPanel endPartyPanel = new EndPartyWindow(this);
		cards.add(swMainPanel, "selection window");
		cards.add(cpwMainPanel, "create party window");
		cards.add(endPartyPanel, "end party panel");
		
//		EndPartyWindow epw = new EndPartyWindow(new SelectionWindow(user, currentParties));
//		cards.add(epw, "end party window");

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
		
		
		// getting the panel that holds the scroll pane with parties
		swcurrentParties.setPreferredSize(new Dimension (600, 1000));
		swcurrentParties.setOpaque(false);
		partyScrollPane = new JScrollPane(swcurrentParties);
		partyScrollPane.setPreferredSize(new Dimension(600, 700));
		partyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		partyScrollPane.setOpaque(false);
		partyScrollPane.getViewport().setOpaque(false);
		partyScrollPane.setBorder(BorderFactory.createEmptyBorder());
		//partyScrollPane.getVerticalScrollBar().setPreferredSize (new Dimension(0,0));
		swMainPanel.add(partyScrollPane, BorderLayout.CENTER);

		// getting the panel that holds the "create a party" button
		JPanel topPanel = new JPanel();
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, createAPartyButton);
		createAPartyButton.setOpaque(true);
		//topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		topPanel.add(createAPartyButton);
		createAPartyButton.setOpaque(false);
		createAPartyButton.setContentAreaFilled(false);
		createAPartyButton.setBorderPainted(false);
		topPanel.setOpaque(false);
		AppearanceSettings.setBackground(Color.WHITE, topPanel);
		topPanel.setOpaque(false);
		swMainPanel.add(topPanel, BorderLayout.SOUTH);
		
		ProfilePanel profilePanel = new ProfilePanel(user);
		profilePanel.setOpaque(false);
		swMainPanel.add(profilePanel, BorderLayout.WEST);
		
	}
	
	private class SinglePartyPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private Party party;
		private JButton partyButton;
		private JLabel hostLabel, hostImageLabel;
		private JLabel partyIconLabel;
		private ImageIcon partyImageIcon;
		
		public SinglePartyPanel (Party p) {
			AppearanceSettings.setSize(600, 100, this);
			this.party = p;
			setLayout(new GridLayout(1,4));
			hostLabel = new JLabel(party.getHostName());
			AppearanceSettings.setFont(AppearanceConstants.fontMedium, hostLabel);
			partyImageIcon = party.getPartyImage();
			partyIconLabel = new JLabel(partyImageIcon);
			hostImageLabel = new JLabel();
			Image image = new ImageIcon(party.getHost().getImageFilePath()).getImage();
			ImageIcon img = new ImageIcon(image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
			hostImageLabel.setIcon(img);
			partyButton = new JButton(party.getPartyName());
			
			partyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//join that party
					//switch gui so it shows that party (asking for password if the party is private)
					//create a new client for that party
				}	
			});
			AppearanceSettings.setForeground(Color.white, hostLabel);
			AppearanceSettings.setForeground(AppearanceConstants.trojamPurple, partyButton);
			AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, partyButton, hostLabel);
			AppearanceSettings.setSize(100, 40, partyButton, hostLabel);
			AppearanceSettings.setOpaque(partyButton, hostLabel);
			AppearanceSettings.setFont(AppearanceConstants.fontSmall, partyButton, hostLabel);

			add(partyIconLabel);
			add(partyButton);
			add(hostLabel);
			
			Border raisedbevel, loweredbevel;
			raisedbevel = BorderFactory.createRaisedBevelBorder();
			loweredbevel = BorderFactory.createLoweredBevelBorder();
			Border compound = BorderFactory.createCompoundBorder(
                    raisedbevel, loweredbevel);
			this.setBorder(compound);
			add(hostImageLabel);
		}
	}
	
	private void addActionListeners(){
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
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
				revalidate();
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
				revalidate();
			}
		});
				
		cpwBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(cards, "selection window");		
				
				cpwPartyNameTextField.addFocusListener(new TextFieldFocusListener("Party name", cpwPartyNameTextField));
				cpwPasswordTextField.addFocusListener(new TextFieldFocusListener("Password", cpwPasswordTextField));
				
				cpwPublicRadioButton.setSelected(true);
				cpwPasswordTextField.setVisible(false);
			}
		});
		
		cpwCreateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				// CHANGE TO PARTY WINDOW
				String pName = cpwPartyNameTextField.getText();
				ImageIcon pImage = (ImageIcon) imageLabel.getIcon();
//				Party p = null;
//				if(cpwPublicRadioButton.isSelected()){
//					p = new PublicParty(pName, user, pImage);
//				}
//				else if(cpwPrivateRadioButton.isSelected()){
//					String password = cpwPasswordTextField.getText();
//					p = new PrivateParty(pName, password, user, pImage);
//				}
				
				//ADD TO PARTIES LIST
				
				String password = "";
				if (cpwPrivateRadioButton.isSelected()) {
					password = cpwPasswordTextField.getText();
				}
				System.out.println("about to send new party info to server");
				//client.sendNewPartyMessage(new NewPartyMessage("newParty", pName, password));
				
				//user.st.createParty(p);
				
				PartyWindow pw = new PartyWindow(null, sw);
				cards.add(pw, "party window");
				CardLayout cl = (CardLayout) cards.getLayout();
				cl.show(cards, "party window");		
				
				cpwPartyNameTextField.addFocusListener(new TextFieldFocusListener("Party name", cpwPartyNameTextField));
				cpwPasswordTextField.addFocusListener(new TextFieldFocusListener("Password", cpwPasswordTextField));
				
				cpwPublicRadioButton.setSelected(true);
				cpwPasswordTextField.setVisible(false);
				
			}		
		});
		
		imageLabel.addMouseListener(new MouseAdapter() {
			 @Override
           public void mouseClicked(MouseEvent e) {
				fileChooser.showOpenDialog(SelectionWindow.this);
				File f = fileChooser.getSelectedFile();
				if (f != null) {
					setPartyImage(f.getPath());
					imageText.setVisible(false);
				}
           }
		});
	}
	
	public void createCPWMenu() {
		cpwMainPanel.setLayout(new BoxLayout(cpwMainPanel, BoxLayout.Y_AXIS));
		
		AppearanceSettings.setSize(1280, 50, dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel5, dummyLabel6);
		AppearanceSettings.setSize(1280, 20, dummyLabel4);
		AppearanceSettings.setSize(300, 50, cpwPartyNameTextField, cpwPasswordTextField);
		AppearanceSettings.setSize(1280, 50, cpwTopPanel);
		
		// Creates top panel with dummy labels so that the text field is at the bottom of the panel

		//file chooser settings
		fileChooser.setPreferredSize(new Dimension(400, 500));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("IMAGE FILES", "jpeg", "png", "jpg"));		
		setPartyImage("images/party-purple.jpg");
		JPanel cpwImagePanel = new JPanel();
		cpwImagePanel.add(imageLabel);
		cpwImagePanel.setOpaque(false);
		cpwTopPanel.add(dummyLabel3);
		cpwTopPanel.add(cpwImagePanel);
		cpwTopPanel.add(dummyLabel4);
		cpwTopPanel.add(cpwPartyNameTextField);
		cpwMainPanel.add(cpwTopPanel);
		
		// Makes it so you can only select one button
		ButtonGroup bg = new ButtonGroup();
		bg.add(cpwPublicRadioButton);
		bg.add(cpwPrivateRadioButton);
		cpwPublicRadioButton.setSelected(true);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, cpwPublicRadioButton, 
				cpwPrivateRadioButton, imageLabel, cpwPartyNameTextField, cpwPasswordTextField, imageText);
		
		// Adds radio buttons horizontally
		cpwRadioButtonPanel.setLayout(new BoxLayout(cpwRadioButtonPanel, BoxLayout.X_AXIS));
		cpwRadioButtonPanel.add(cpwPublicRadioButton);
		cpwRadioButtonPanel.add(cpwPrivateRadioButton);
		cpwMainPanel.add(cpwRadioButtonPanel);
		
		// Creates the bottom panel with password text field and create party button
		JPanel tempPanel1 = new JPanel();
		tempPanel1.add(cpwPasswordTextField);
		cpwPasswordTextField.setVisible(false);
		//cpwBottomPanel.add(cpwPasswordTextField);
		cpwBottomPanel.add(tempPanel1);
		JPanel tempPanel2 = new JPanel();
		tempPanel2.add(cpwCreateButton);
		tempPanel2.add(cpwBackButton);
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

	private void setPartyImage(String filepath) {
		this.imageFilePath = filepath;
		Image image = new ImageIcon(filepath).getImage();
		partyImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		imageLabel.setIcon(partyImage);
		imageText.setSize(imageLabel.getPreferredSize());
		imageText.setLocation(imageText.getLocation());
		imageLabel.add(imageText);
		if (canPressButtons()) {
			cpwCreateButton.setEnabled(true);
		} else {
			cpwCreateButton.setEnabled(false);
		}
		
//		//write image to local file in order to retrieve when user logs in
//		 BufferedImage image1 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
//		 File inputFile = new File(filepath);	    
//		 try {
//			 image1 = ImageIO.read(inputFile);
//			 File outputfile = new File("party - " + cpwPartyNameTextField.getText() + ".png");
//			ImageIO.write(image1, "png", outputfile);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println(e.getMessage());
//		}
	}
	
	private boolean canPressButtons() {
		//usernameTextField, passwordTextField, firstNameTextField, lastNameTextField
		if (cpwPrivateRadioButton.isSelected()) {
			if (!cpwPartyNameTextField.getText().equals("Party name") && cpwPartyNameTextField.getText().length() != 0) {
				if (!cpwPasswordTextField.getText().equals("Password") && cpwPasswordTextField.getText().length() != 0) {
					if(imageLabel.getIcon() != null){
						return true;
					}
				}
			}
		} else if (cpwPublicRadioButton.isSelected()) {
			if (!cpwPartyNameTextField.getText().equals("Party name") && cpwPartyNameTextField.getText().length() != 0) {
				if(imageLabel.getIcon() != null){
					return true;
				}
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
		Image image = new ImageIcon("images/party-purple.jpg").getImage();
		ImageIcon tempImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		PrivateParty p1 = new PrivateParty("party1", "password1", user, tempImage);
		PrivateParty p2 = new PrivateParty("party2", "password2", user, tempImage);
		PublicParty p3 = new PublicParty("party3", user, tempImage);
		PrivateParty p4 = new PrivateParty("party1", "password1", user, tempImage);
		PrivateParty p5 = new PrivateParty("party2", "password2", user, tempImage);
		PublicParty p6 = new PublicParty("party3", user, tempImage);
		PrivateParty p7 = new PrivateParty("party1", "password1", user, tempImage);
		PrivateParty p8 = new PrivateParty("party2", "password2", user, tempImage);
		PublicParty p9 = new PublicParty("party3", user, tempImage);
		ArrayList <Party> parties = new ArrayList <Party>();
		parties.add(p1);
		parties.add(p2);
		parties.add(p3);
		parties.add(p4);
		parties.add(p5);
		parties.add(p6);
		parties.add(p7);
		parties.add(p8);
		parties.add(p9);
		new SelectionWindow(user, parties).setVisible(true);
	}
	
	
	private class ProfilePanel extends JPanel{
		
		ImageIcon profilePic;
		JLabel profileName;
		JLabel profileUserName;
		User user;
		JScrollPane suggestedSongs;
		JTextArea partiesTextArea;
		JScrollPane joinedParties;
		JButton logout;
		
		public ProfilePanel(User user){
			this.user = user;
			profilePic = user.getUserImage();
			profileName = new JLabel(user.getFirstName() + " " + user.getLastName());
			profileUserName = new JLabel(user.getUsername());
			partiesTextArea = new JTextArea(5,20);
			joinedParties = new JScrollPane(partiesTextArea);
			for(Party p : user.getParties()){
				partiesTextArea.append(p.getPartyName() + "\n");
			}
			
			logout = new JButton();
			ImageIcon logoutButtonImage = new ImageIcon("images/button_log-out.png");
			logout.setIcon(logoutButtonImage);
			logout.setOpaque(false);
			logout.setBorderPainted(false);
			logout.setContentAreaFilled(false);
			
			logout.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new LoginScreenWindow().setVisible(true);
					dispose();
				}
			});
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			this.add(new JLabel(profilePic));
			this.add(profileName);
			this.add(profileUserName);
			this.add(joinedParties);
			this.add(logout);
		}
		
	}
	
	public void showEndWindow() {
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "end party panel");
	}
	
	public void showSelectionWindow() {
		CardLayout cl = (CardLayout) cards.getLayout();
		cl.show(cards, "selection window");
	}
	
	

}