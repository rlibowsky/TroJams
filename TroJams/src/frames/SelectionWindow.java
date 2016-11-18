package frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/*
 * SELECTION WINDOW: THIS IS OUR MAIN WINDOW, WHERE WE CHOOSE WHICH PARTY TO JOIN/CREATE A PARTY
 */

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import listeners.TextFieldFocusListener;
import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;


public class SelectionWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel swMainPanel, swCenterPanel, cards;
	
	private User user;
	private JMenuBar menuBar;
	private JMenu profile;
	private JMenu logout;
	private JButton createAPartyButton;
//	private CreatePartyWindow cpw;
	
	private JPanel cpwMainPanel, cpwTopPanel, cpwBottomPanel, cpwRadioButtonPanel, cpwButtonPanel;
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
		
	public SelectionWindow(User user){
		super("TroJams");
		this.user = user;
		
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
		swCenterPanel = new JPanel();
		swMainPanel = new JPanel();
		createAPartyButton = new JButton("Create a Party");
		profile = new JMenu("Profile");
		logout = new JMenu("Logout");
		menuBar = new JMenuBar();
		cards = new JPanel(new CardLayout());
		//cpw = new CreatePartyWindow(this);
		
		cpwMainPanel = new JPanel();
		cpwTopPanel = new JPanel();
		cpwBottomPanel = new JPanel();
		//dummyPanel = new JPanel();
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
		cpwButtonPanel = new JPanel();
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
		
		createMenu();
		createCPWMenu();
		createSWCenterPanel();
		AppearanceSettings.setNotOpaque(swCenterPanel, swMainPanel, cards);

		
		swMainPanel.setPreferredSize(new Dimension(1280, 800));
		swMainPanel.add(swCenterPanel);
		
		createPWPanel();
		
		cards.add(swMainPanel, "selection window");
		cards.add(cpwMainPanel, "create party window");
//		add(swMainPanel, BorderLayout.CENTER);
		
		add(cards, BorderLayout.CENTER);
		add(pwMainPanel, BorderLayout.EAST); 
		
		cl = (CardLayout) cards.getLayout();
		//cl.show(cards, "selection window");
		//cl.show(cards, "create party window");
		cl.show(cards, "profile window");
	}
	
	// creates Profile Window
	private void createPWPanel() {
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT, pwMainPanel);
		AppearanceSettings.setNotOpaque(pwMainPanel);
		AppearanceSettings.setOpaque(pwUsernameLabel, pwNameLabel, profileLabel);
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT/5, profileLabel);
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT/3, profileIconLabel);
		AppearanceSettings.setSize((AppearanceConstants.GUI_WIDTH)/5, AppearanceConstants.GUI_HEIGHT/10, pwUsernameLabel, pwNameLabel);
		pwUsernameLabel.setHorizontalAlignment(JLabel.CENTER);
	    pwUsernameLabel.setVerticalAlignment(JLabel.CENTER);
		pwNameLabel.setHorizontalAlignment(JLabel.CENTER);
	    pwNameLabel.setVerticalAlignment(JLabel.CENTER);
	    profileLabel.setHorizontalAlignment(JLabel.CENTER);
	    profileLabel.setVerticalAlignment(JLabel.CENTER);
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
	
	// creates the top panel, which houses the Create a Party button
	private JPanel createSWTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setPreferredSize(new Dimension(AppearanceConstants.GUI_WIDTH, (AppearanceConstants.GUI_HEIGHT)/4));

		AppearanceSettings.setFont(AppearanceConstants.fontMedium, createAPartyButton);
		topPanel.add(createAPartyButton);
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(Box.createHorizontalGlue());
		AppearanceSettings.setNotOpaque(topPanel);
		return topPanel;
	}
	
	// creates the bottom panel, which houses a jscrollpane
	private JPanel createSWBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(1280,800));
		
		for (int i = 0; i < 10; i++) {
			JPanel tempPanel = new JPanel();
			tempPanel.setLayout(new BorderLayout());
			JButton button = new JButton("Party " + i);
			button.setPreferredSize(new Dimension(200, 200));
			button.setOpaque(true);
			tempPanel.add(button, BorderLayout.WEST);
			JLabel label = new JLabel("Host Name " + i);
			label.setPreferredSize(new Dimension(300, 200));
			tempPanel.add(label, BorderLayout.EAST);
			
			tempPanel.setPreferredSize(new Dimension(1280,200));
			bottomPanel.add(tempPanel);
		}
		
//		JPanel bottomPanel = new JPanel(new SpringLayout());
//		JScrollPane scroll = new JScrollPane();
//		
//		SpringLayout layout = new SpringLayout();
//		JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(layout);
//		bottomPanel.setLayout(new BorderLayout());
//		
//		int j = 25;
//        for (int i = 0; i < 10; i++) {
//        	//Image img = new ImageIcon("images/bluebutton.png").getImage();
//        	//JButton button = new JButton("Party " + i, new ImageIcon(img.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH)));
//        	JButton button = new JButton("Party " + i);
//        	//button.setHorizontalTextPosition(JButton.CENTER);
//            //button.setVerticalTextPosition(JButton.CENTER);
//            //button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//            //button.setOpaque(true);
//            //button.setPreferredSize(new Dimension(100, 100));
//            JLabel label = new JLabel("Host Name ");
//
//            //AppearanceSettings.setNotOpaque(button, label);
//            AppearanceSettings.setFont(AppearanceConstants.fontMedium, label, button);
//            AppearanceSettings.setForeground(Color.WHITE, label, button);
//            mainPanel.add(button);
//            mainPanel.add(label);
//          
//            layout.putConstraint(SpringLayout.WEST, button, 20, SpringLayout.WEST, bottomPanel);
//            layout.putConstraint(SpringLayout.NORTH, button, j, SpringLayout.NORTH, bottomPanel);
//            layout.putConstraint(SpringLayout.NORTH, label, j, SpringLayout.NORTH, bottomPanel);
//            layout.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.EAST, button);
//            j+=30;
//        }
//        //mainPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
//        scroll.setPreferredSize(new Dimension(500, 500));
//        JViewport viewport = new MyViewport();
//        viewport.setView(mainPanel);
//        scroll.setViewport(viewport);
//        //scroll.setViewportView(mainPanel);
////		scroll.setBorder(BorderFactory.createEmptyBorder()); 
//        scroll.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
//        bottomPanel.add(scroll);
//        AppearanceSettings.setNotOpaque(bottomPanel, scroll, mainPanel);
		return bottomPanel;
	}
	
	// creates the main panel
	private void createSWCenterPanel() {
		swCenterPanel.setPreferredSize(new Dimension(800, 800));
		swCenterPanel.setLayout(new BoxLayout(swCenterPanel, BoxLayout.PAGE_AXIS));
		// getting the panel that holds the "create a party" button
		JPanel topPanel = createSWTopPanel();
		// getting the panel that holds the scroll pane with parties
		JPanel bottomPanel = createSWBottomPanel();
		AppearanceSettings.setNotOpaque(bottomPanel, topPanel, swCenterPanel);
		//swCenterPanel.add(Box.createHorizontalStrut(1000));
		//swCenterPanel.add(Box.createVerticalStrut(50));
		swCenterPanel.add(topPanel);
		//swCenterPanel.add(Box.createVerticalStrut(25));
		//swCenterPanel.add(bottomPanel);
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
		//focus listeners
				cpwPartyNameTextField.addFocusListener(new TextFieldFocusListener("Party name", cpwPartyNameTextField));
				cpwPasswordTextField.addFocusListener(new TextFieldFocusListener("Password", cpwPasswordTextField));
				
				//document listeners
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
						}
						else {
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
						}
						else {
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
		//cpwTopPanel.setBackground(Color.orange);
		AppearanceSettings.setNotOpaque(cpwTopPanel, cpwMainPanel, cpwBottomPanel, cpwRadioButtonPanel);
//		cpwMainPanel.setBackground(Color.black);
		//cpwRadioButtonPanel.setBackground(Color.black);
		//cpwBottomPanel.setBackground(Color.black);
		cpwPrivateRadioButton.setForeground(Color.white);
		cpwPublicRadioButton.setForeground(Color.white);
		
		//add(cpwMainPanel);
		
	}
	
	private boolean canPressButtons() {
		//usernameTextField, passwordTextField, firstNameTextField, lastNameTextField
		if (cpwPrivateRadioButton.isSelected()) {
			if (!cpwPartyNameTextField.getText().equals("Party name") && cpwPartyNameTextField.getText().length() != 0) {
				if (!cpwPasswordTextField.getText().equals("Password") && cpwPasswordTextField.getText().length() != 0) {
					return true;
				}
			}
		}
		
		else if (cpwPublicRadioButton.isSelected()) {
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

	private static class MyViewport extends JViewport {

		private static final long serialVersionUID = 1L;

		public MyViewport() {
			this.setOpaque(false);
//			this.setOpaque(true);
		}
	}
	
	public static void main(String [] args) {
		User user = new User("username", "password");
		new SelectionWindow(user).setVisible(true);
	}
	
}