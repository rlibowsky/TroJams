package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import listeners.TextFieldFocusListener;
import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class CreateAccountWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private User newUser;
	private LoginScreenWindow loginScreenWindow;
	
	private JTextField usernameTextField, passwordTextField, firstNameTextField, lastNameTextField;
	private JLabel infoLabel, instructionsLabel, imageLabel, imageText;
	private JButton submitButton;
	private ImageIcon userImage;
	private ImageIcon backgroundImage;
	
	private String imageFilePath;
	private JFileChooser fileChooser;
	
	public CreateAccountWindow(User newUser, LoginScreenWindow loginScreenWindow){
		super("Create Account");
		this.newUser = newUser;
		this.loginScreenWindow = loginScreenWindow;
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	
	private void addListeners() {
		//focus listeners
		firstNameTextField.addFocusListener(new TextFieldFocusListener("First Name", firstNameTextField));
		lastNameTextField.addFocusListener(new TextFieldFocusListener("Last Name", lastNameTextField));
		usernameTextField.addFocusListener(new TextFieldFocusListener(newUser.getUsername(), usernameTextField));
		passwordTextField.addFocusListener(new TextFieldFocusListener(newUser.getPassword(), passwordTextField));
		
		//document listeners
		firstNameTextField.getDocument().addDocumentListener(new MyDocumentListener());
		lastNameTextField.getDocument().addDocumentListener(new MyDocumentListener());
		usernameTextField.getDocument().addDocumentListener(new MyDocumentListener());
		passwordTextField.getDocument().addDocumentListener(new MyDocumentListener());
		
		//image upload
		imageLabel.addMouseListener(new MouseAdapter() {
			 @Override
             public void mouseClicked(MouseEvent e) {
				fileChooser.showOpenDialog(CreateAccountWindow.this);
				File f = fileChooser.getSelectedFile();
				if (f != null) {
					setUserImage(f.getPath());
					imageText.setVisible(false);
				}
             }
		});
		
		
		submitButton.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent ae){
				newUser = new User(usernameTextField.getText(), passwordTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), imageFilePath);
				loginScreenWindow.insertUserIntoDB(newUser);
				SelectionWindow sw = new SelectionWindow(newUser, null);
				sw.setVisible(true);
			}
		});
	}


	private void initializeComponents(){
		
		this.setContentPane(new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image image = new ImageIcon("images/backgroundImage.png").getImage();
				backgroundImage = new ImageIcon(image.getScaledInstance(1280, 800, java.awt.Image.SCALE_SMOOTH));
				g.drawImage(image, 0, 0, 1280, 800, this);
			}
		});
		
		
		
		usernameTextField= new JTextField();
		passwordTextField= new JTextField();
		firstNameTextField= new JTextField();
		lastNameTextField= new JTextField();
		infoLabel = new JLabel("Welcome to TroJams!");
		infoLabel.setForeground(Color.white);
		instructionsLabel = new JLabel("Please enter your information");
		instructionsLabel.setForeground(Color.white);
		imageLabel = new JLabel();
		fileChooser = new JFileChooser();
		submitButton = new JButton();
		ImageIcon submitButtonImage = new ImageIcon("images/button_submit.png");
		submitButton.setIcon(submitButtonImage);
		submitButton.setOpaque(false);
		submitButton.setBorderPainted(false);
		submitButton.setContentAreaFilled(false);
		imageText = new JLabel("Click to upload a profile picture");
	}
	
	private void createGUI(){
		setSize(700, 600);
		setLayout(new BorderLayout());
		JPanel infoPanel = new JPanel();
		JPanel textFieldPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		//set font for the labels
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, instructionsLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, infoLabel);
		
		//set alignment
		AppearanceSettings.setTextAlignment(instructionsLabel, infoLabel);
		
		//set sizes
		//AppearanceSettings.setSize(400, 60, usernameTextField, passwordTextField, firstNameTextField, lastNameTextField);
		
		//file chooser settings
		fileChooser.setPreferredSize(new Dimension(400, 500));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("IMAGE FILES", "jpeg", "png", "jpg"));
				
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(infoLabel, BorderLayout.NORTH);
		infoPanel.add(instructionsLabel, BorderLayout.SOUTH);
		
		firstNameTextField.setPreferredSize(new Dimension(80, 60));
		lastNameTextField.setPreferredSize(new Dimension(80, 60));
		usernameTextField.setPreferredSize(new Dimension(80, 60));
		passwordTextField.setPreferredSize(new Dimension(80, 60));
		JPanel credentialsPanel = new JPanel();
		credentialsPanel.setLayout(new BorderLayout());
		credentialsPanel.add(firstNameTextField, BorderLayout.NORTH);
		credentialsPanel.add(lastNameTextField, BorderLayout.SOUTH);
		credentialsPanel.setOpaque(false);
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(usernameTextField, BorderLayout.NORTH);
		namePanel.add(passwordTextField, BorderLayout.SOUTH);
		namePanel.setOpaque(false);
		
		textFieldPanel.setLayout(new BorderLayout());
		textFieldPanel.add(credentialsPanel, BorderLayout.NORTH);
		textFieldPanel.add(namePanel, BorderLayout.SOUTH);
		
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		imageLabel.setPreferredSize(new Dimension(200,200));
		setUserImage("images/silhouette.png");
		AppearanceSettings.addGlue(bottomPanel, BoxLayout.LINE_AXIS, true, imageLabel, submitButton);
		submitButton.setEnabled(false);
		
		infoPanel.setOpaque(false);
		textFieldPanel.setOpaque(false);
		bottomPanel.setOpaque(false);
		
		add(infoPanel, BorderLayout.NORTH);
		add(textFieldPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	private void setUserImage(String filepath) {
		this.imageFilePath = filepath;
		Image image = new ImageIcon(filepath).getImage();
		userImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		newUser.setUserImage(userImage);
		imageLabel.setIcon(userImage);
		imageText.setSize(imageLabel.getPreferredSize());
		imageText.setLocation(imageText.getLocation());
		imageLabel.add(imageText);
		
		//write image to local file in order to retrieve when user logs in
		 BufferedImage image1 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		 File inputFile = new File(filepath);	    
		 try {
			 image1 = ImageIO.read(inputFile);
			 File outputfile = new File("silhouette - " + newUser.getUsername() + ".png");
			ImageIO.write(image1, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	private boolean canPressButtons() {
		//usernameTextField, passwordTextField, firstNameTextField, lastNameTextField
		if ((usernameTextField.getText().length() != 0) && (passwordTextField.getText().length() != 0)) {
			if (!firstNameTextField.getText().equals("First Name") && firstNameTextField.getText().length() != 0) {
				if (!lastNameTextField.getText().equals("Last Name") && lastNameTextField.getText().length() != 0) {
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
			submitButton.setEnabled(canPressButtons());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			submitButton.setEnabled(canPressButtons());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			submitButton.setEnabled(canPressButtons());
		}
	}
	
	public static void main(String [] args) {
		System.out.println("test!");
		CreateAccountWindow caw = new CreateAccountWindow(new User("test", "test"), new LoginScreenWindow());
		caw.setVisible(true);
	}
	
	
}
