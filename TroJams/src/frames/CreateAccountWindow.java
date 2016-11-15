package frames;

import javax.swing.JFrame;

import logic.User;

public class CreateAccountWindow extends JFrame {

	private User newUser;
	private LoginScreenWindow loginScreenWindow;
	
	public CreateAccountWindow(User newUser, LoginScreenWindow loginScreenWindow){
		this.newUser = newUser;
		this.loginScreenWindow = loginScreenWindow;
	}
	
	
	
	
}
