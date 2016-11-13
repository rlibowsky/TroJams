package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import listeners.TextFieldFocusListener;
import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

public class LoginScreenWindow extends JFrame {

	private JButton loginButton;
	private JButton createAccount;
	private JTextField username;
	private JTextField password;
	private JLabel alertLabel;
	//users map
	//could have use <String, String> instead of User object, but chose not to
	private HashMap<String, User> existingUsers;
	//the file that contains user account info
	private File file;

	public LoginScreenWindow() {
		
		file = new File("users.txt");
		existingUsers = new HashMap<>();
		//reads in stored users from file and populates existingUsers
		readFromFile();
		initializeComponents();
		createGUI();
		addListeners();
	}
	
	private void initializeComponents(){
		
		loginButton = new JButton("Login");
		createAccount = new JButton("Create Account");
		username = new JTextField("username");
		password = new JTextField("password");
		alertLabel = new JLabel();
	}
	
	private void createGUI(){
		
		JPanel mainPanel = new JPanel();
		JPanel textFieldOnePanel = new JPanel();
		JPanel textFieldTwoPanel = new JPanel();
		JLabel welcome = new JLabel("login or create an account to play", JLabel.CENTER);
		JLabel jeopardyLabel = new JLabel("Jeopardy!", JLabel.CENTER);
		JPanel alertPanel = new JPanel();
		JPanel textFieldsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel welcomePanel = new JPanel(new GridLayout(2,1));
		
		//set mass component appearances
		AppearanceSettings.setForeground(Color.lightGray, createAccount, loginButton, password, username);
		AppearanceSettings.setSize(300, 60, password, username);
		
		AppearanceSettings.setSize(200, 80, loginButton, createAccount);
		AppearanceSettings.setBackground(Color.darkGray, loginButton, createAccount);
		
		AppearanceSettings.setOpaque(loginButton, createAccount);
		AppearanceSettings.unSetBorderOnButtons(loginButton, createAccount);
		
		AppearanceSettings.setTextAlignment(welcome, alertLabel, jeopardyLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, password, alertLabel, username, loginButton, createAccount);
		
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, mainPanel, welcome, alertLabel, jeopardyLabel, alertPanel, textFieldsPanel, 
				buttonsPanel, welcomePanel, textFieldOnePanel, textFieldTwoPanel);
		
		//other appearance settings
		welcome.setFont(AppearanceConstants.fontMedium);
		jeopardyLabel.setFont(AppearanceConstants.fontLarge);
		
		loginButton.setEnabled(false);
		loginButton.setBackground(AppearanceConstants.mediumGray);
		createAccount.setBackground(AppearanceConstants.mediumGray);
		createAccount.setEnabled(false);
		
		//add components to containers
		welcomePanel.add(welcome);
		welcomePanel.add(jeopardyLabel);
		
		alertPanel.add(alertLabel);
		textFieldOnePanel.add(username);
		textFieldTwoPanel.add(password);
		
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		//adds components to the panel with glue in between each
		AppearanceSettings.addGlue(buttonsPanel, BoxLayout.LINE_AXIS, true, loginButton, createAccount);
		
		AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false, welcomePanel);
		//don't want glue in between the following two panels, so they are not passed in to addGlue
		mainPanel.add(alertPanel);
		mainPanel.add(textFieldOnePanel);
		AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false, textFieldTwoPanel);
		mainPanel.add(buttonsPanel);
		
		add(mainPanel, BorderLayout.CENTER);
		setSize(600, 600);
	}
	
	//returns whether the buttons should be enabled
	private boolean canPressButtons(){
		return (!username.getText().isEmpty() && !username.getText().equals("username") && 
				!password.getText().equals("password") && !password.getText().isEmpty());
	}
	//reads in users map from the file
	private void readFromFile(){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/JeopardyUsers?user=root&password=adam0601&useSSL=false");			
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Users");
			while(rs.next()){
				String username = rs.getString("username");
				String password = rs.getString("password");
				System.out.println(username + " " + password);
				User tempUser = new User(username, password);
				existingUsers.put(username, tempUser);
			}
			
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally{
			try{
				if(rs != null) {
					rs.close();
				}
				if (st!=null){
					st.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch( SQLException sqle) {
				
			}
		}

	}

	private void addListeners(){
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//focus listeners
		username.addFocusListener(new TextFieldFocusListener("username", username));
		password.addFocusListener(new TextFieldFocusListener("password", password));
		//document listeners
		username.getDocument().addDocumentListener(new MyDocumentListener());
		password.getDocument().addDocumentListener(new MyDocumentListener());
		//action listeners
		loginButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String usernameString = username.getText();
				String passwordString = password.getText();
				
				//if the username does not exist
				if (!existingUsers.containsKey(usernameString)){
					alertLabel.setText("This username does not exist.");
				}
				//else if the username exists
				else{
					User user = existingUsers.get(usernameString);
					//if the user gave the wrong password
					if (!user.verifyPassword(passwordString)) {
						alertLabel.setText("The password you provided does not match our records");
					}
					//login successful - GO TO MAIN TROJAMS WINDOW
					else{
						new SelectionWindow(user).setVisible(true);
						dispose();
					}
				}
			}
			
		});
		
		createAccount.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {	
				String usernameString = username.getText();
				String passwordString = password.getText();
				//if this username has already been chosen
				if (existingUsers.containsKey(usernameString)){
					alertLabel.setText("This username has already been chosen by another user");
				}
				//username has not been chosen, send newly created user with username and password to Create Account Window to then 
				//fill in the rest of the info about the user.
				else{
					User newUser = new User(usernameString, passwordString);
					insertUserIntoDB(newUser);
					new CreateAccountWindow(newUser, LoginScreenWindow.this).setVisible(true); //Pass in user and this GUI so that when the user is created, the 
						//create account window can call insertUserIntoDB 
					dispose();
				}
				
			}
			
		});
	}

	void insertUserIntoDB(User user){
		System.out.println("Insert");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/JeopardyUsers?user=root&password=adam0601&useSSL=false");	
			String query = "INSERT INTO Users (username, password) VALUES ('" + user.getUsername() + "','" + user.getPassword() + "')";
			ps = conn.prepareStatement(query);
			ps.execute();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally{
			try{
				if (ps!=null){
					ps.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch( SQLException sqle) {
				
			}
		}

	}
	
	//sets the buttons enabled or disabled
	private class MyDocumentListener implements DocumentListener{
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
	}
}