package frames;

import javax.swing.JFrame;
import javax.swing.JTextField;

import logic.User;

public class CreateAccountWindow extends JFrame {

	private User newUser;
	private LoginScreenWindow loginScreenWindow;
	
	private JTextField emailTextField, usernameTextField, passwordTextField, ageTextField, firstNameTextField, lastNameTextField;
	
	
	
	
	public CreateAccountWindow(User newUser, LoginScreenWindow loginScreenWindow){
		this.newUser = newUser;
		this.loginScreenWindow = loginScreenWindow;
	}
	
	
	private void initializeComponents(){
		emailTextField = new JTextField();
		usernameTextField= new JTextField();
		passwordTextField= new JTextField();
		ageTextField= new JTextField();
		firstNameTextField= new JTextField();
		lastNameTextField= new JTextField();
	}
	
	private void createGUI(){
		
	}
	
	
}
