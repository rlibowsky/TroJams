package logic;

import java.awt.Image;

public class User {
	
	private String username, password;
	private Image userImage;
	
	public User(String username, String password, Image userImage) {
		this.username = username;
		this.password = password;
		this.userImage = userImage;
	}
	
	public String getUsername() {
		return username;
	}
	
	//pass in a string to see if password matches the user's password
	public boolean verifyPassword(String pass) {
		return pass.equals(password);
	}
	
}
