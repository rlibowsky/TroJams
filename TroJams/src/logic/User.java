package logic;

import java.awt.Image;
import java.util.HashSet;

public class User extends Account{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username, password;
	public Image userImage;
	private HashSet <Party> parties;
	public Party hostedParty; //null if user is hosting no parties
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.userImage = null;
		this.parties = new HashSet<Party>();
	}
	
	public String getUsername() {
		return username;
	}
	
	//pass in a string to see if password matches the user's password
	public boolean verifyPassword(String pass) {
		return pass.equals(password);
	}

	public String getPassword() {
		return password;
	}
	
	//called when a user logs out
	public void leaveAllParties() {
		for (Party p : parties) {
			p.leaveParty(this);
		}
	}
	
}
