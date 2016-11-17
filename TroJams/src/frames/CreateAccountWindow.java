package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
		
	}


	private void initializeComponents(){
		usernameTextField= new JTextField();
		passwordTextField= new JTextField();
		firstNameTextField= new JTextField();
		lastNameTextField= new JTextField();
		infoLabel = new JLabel("New User");
		instructionsLabel = new JLabel("Please enter your information");
		imageLabel = new JLabel();
		fileChooser = new JFileChooser();
		submitButton = new JButton("Submit");
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
		fileChooser.setFileFilter(new FileNameExtensionFilter("IMAGE FILES", "jpeg", "png"));
				
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
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(usernameTextField, BorderLayout.NORTH);
		namePanel.add(passwordTextField, BorderLayout.SOUTH);
		
		textFieldPanel.setLayout(new BorderLayout());
		textFieldPanel.add(credentialsPanel, BorderLayout.NORTH);
		textFieldPanel.add(namePanel, BorderLayout.SOUTH);
		
		bottomPanel.setLayout(new BorderLayout());
		imageLabel.setPreferredSize(new Dimension(200,200));
		setUserImage("images/silhouette.png");
		bottomPanel.add(imageLabel, BorderLayout.WEST);
		bottomPanel.add(submitButton, BorderLayout.EAST);
		submitButton.setEnabled(false);
		
		add(infoPanel, BorderLayout.NORTH);
		add(textFieldPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	private void setUserImage(String filepath) {
		Image image = new ImageIcon(filepath).getImage();
		userImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		newUser.setUserImage(image);
		imageLabel.setIcon(userImage);
		imageText.setSize(imageLabel.getPreferredSize());
		imageText.setLocation(imageText.getLocation());
		imageLabel.add(imageText);
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
