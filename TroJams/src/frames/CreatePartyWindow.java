package frames;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import listeners.TextFieldFocusListener;
import logic.Party;
import resources.AppearanceSettings;

/*
 * CREATE A PARTY. SHOULD BE A PANEL. CARD LAYOUT WITH SELECTIONWINDOW AS MAIN
 */
public class CreatePartyWindow extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JPanel cpwMainPanel, cpwTopPanel, cpwBottomPanel, cpwRadioButtonPanel, cpwButtonPanel;
	private JLabel dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6;
	private JTextField cpwPartyNameTextField;
	private JTextField cpwPasswordTextField;
	private JRadioButton cpwPublicRadioButton;
	private JRadioButton cpwPrivateRadioButton;
	private JButton cpwCreateButton;
	private SelectionWindow sw;

	
	public CreatePartyWindow(SelectionWindow sw) {
		super();
		this.sw = sw;
		initializeComponents();
		createGUI();
		addActionListeners();
		
	}
	
	public void initializeComponents() {
		cpwMainPanel = new JPanel();
		cpwTopPanel = new JPanel();
		cpwBottomPanel = new JPanel();
		//dummyPanel = new JPanel();
		dummyLabel1 = new JLabel();
		dummyLabel2 = new JLabel();
		dummyLabel3 = new JLabel();
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

		
	}
	
	public void createGUI() {
				
		this.setSize(new Dimension(500,800));
		createCPWMenu();
	}
	
	public void addActionListeners() {
		
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
				// 

			}
			
		});
	}
	
	
	private void setUserImage(String filepath) {
		this.imageFilePath = filepath;
		Image image = new ImageIcon(filepath).getImage();
		partyImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		imageLabel.setIcon(partyImage);
		imageText.setSize(imageLabel.getPreferredSize());
		imageText.setLocation(imageText.getLocation());
		imageLabel.add(imageText);
		
		//write image to local file in order to retrieve when user logs in
		 BufferedImage image1 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		 File inputFile = new File(filepath);	    
		 try {
			 image1 = ImageIO.read(inputFile);
			 File outputfile = new File("party - " + cpwPartyNameTextField.getText() + ".png");
			ImageIO.write(image1, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void createCPWMenu() {
		cpwMainPanel.setLayout(new BoxLayout(cpwMainPanel, BoxLayout.Y_AXIS));
		
		AppearanceSettings.setSize(300, 50, cpwPartyNameTextField, cpwPasswordTextField, dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6);
		
		//file chooser settings
		fileChooser.setPreferredSize(new Dimension(400, 500));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("IMAGE FILES", "jpeg", "png", "jpg"));		
		setUserImage("images/party-purple.jpg");
		cpwTopPanel.add(imageLabel);
		
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
		cpwBottomPanel.add(cpwPasswordTextField);
		cpwBottomPanel.add(cpwCreateButton);
		cpwMainPanel.add(cpwBottomPanel);

		// Appearance settings
		cpwMainPanel.setSize(new Dimension(500,800));
		cpwTopPanel.setBackground(Color.black);
		cpwMainPanel.setBackground(Color.black);
		cpwRadioButtonPanel.setBackground(Color.black);
		cpwBottomPanel.setBackground(Color.black);
		cpwPrivateRadioButton.setForeground(Color.white);
		cpwPublicRadioButton.setForeground(Color.white);
		
		add(cpwMainPanel);
		
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
	
}
