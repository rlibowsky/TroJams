package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SpringLayout;

import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class SelectionWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel swMainPanel, swCenterPanel;
	
	private User user;
	private JMenuBar menuBar;
	private JMenu profile;
	private JMenu logout;
	private JButton createAPartyButton;
	
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
	}
	
	private void createGUI(){
		setSize(AppearanceConstants.GUI_WIDTH, AppearanceConstants.GUI_HEIGHT);
		
		createMenu();
		createSWCenterPanel();
		AppearanceSettings.setNotOpaque(swCenterPanel, swMainPanel);
		
		swMainPanel.add(swCenterPanel);
		add(swMainPanel, BorderLayout.CENTER);

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
		topPanel.setSize(AppearanceConstants.GUI_WIDTH, (AppearanceConstants.GUI_HEIGHT)/4);

		AppearanceSettings.setFont(AppearanceConstants.fontMedium, createAPartyButton);
		topPanel.add(createAPartyButton);
		topPanel.add(Box.createHorizontalGlue());
		topPanel.add(Box.createHorizontalGlue());
		AppearanceSettings.setNotOpaque(topPanel);
		return topPanel;
	}
	
	// creates the bottom panel, which houses a jscrollpane
	private JPanel createSWBottomPanel() {
		JPanel bottomPanel = new JPanel(new SpringLayout());
		JScrollPane scroll = new JScrollPane();
		
		SpringLayout layout = new SpringLayout();
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(layout);
		bottomPanel.setLayout(new BorderLayout());
		
		int j = 25;
        for (int i = 0; i < 10; i++) {
        	//Image img = new ImageIcon("images/bluebutton.png").getImage();
        	//JButton button = new JButton("Party " + i, new ImageIcon(img.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH)));
        	JButton button = new JButton("Party " + i);
        	//button.setHorizontalTextPosition(JButton.CENTER);
            //button.setVerticalTextPosition(JButton.CENTER);
            //button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            //button.setOpaque(true);
            //button.setPreferredSize(new Dimension(100, 100));
            JLabel label = new JLabel("Host Name ");

            //AppearanceSettings.setNotOpaque(button, label);
            AppearanceSettings.setFont(AppearanceConstants.fontMedium, label, button);
            AppearanceSettings.setForeground(Color.WHITE, label, button);
            mainPanel.add(button);
            mainPanel.add(label);
          
            layout.putConstraint(SpringLayout.WEST, button, 20, SpringLayout.WEST, bottomPanel);
            layout.putConstraint(SpringLayout.NORTH, button, j, SpringLayout.NORTH, bottomPanel);
            layout.putConstraint(SpringLayout.NORTH, label, j, SpringLayout.NORTH, bottomPanel);
            layout.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.EAST, button);
            j+=30;
        }
        //mainPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
        scroll.setPreferredSize(new Dimension(500, 500));
        JViewport viewport = new MyViewport();
        viewport.setView(mainPanel);
        scroll.setViewport(viewport);
        //scroll.setViewportView(mainPanel);
//		scroll.setBorder(BorderFactory.createEmptyBorder()); 
        scroll.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        bottomPanel.add(scroll);
        AppearanceSettings.setNotOpaque(bottomPanel, scroll, mainPanel);
		return bottomPanel;
	}
	
	// creates the main panel
	private void createSWCenterPanel() {
		swCenterPanel.setSize(1280, 800);
		swCenterPanel.setLayout(new BoxLayout(swCenterPanel, BoxLayout.PAGE_AXIS));
		// getting the panel that holds the "create a party" button
		JPanel topPanel = createSWTopPanel();
		// getting the panel that holds the scroll pane with parties
		JPanel bottomPanel = createSWBottomPanel();
		AppearanceSettings.setNotOpaque(bottomPanel, topPanel, swCenterPanel);
		swCenterPanel.add(Box.createHorizontalStrut(1000));
		swCenterPanel.add(Box.createVerticalStrut(50));
		swCenterPanel.add(topPanel);
		swCenterPanel.add(Box.createVerticalStrut(25));
		swCenterPanel.add(bottomPanel);
	}
	
	private void addActionListeners(){
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		profile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ProfileWindow(user);
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
				
			}
		});
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