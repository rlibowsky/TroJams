package logic;

import java.awt.Image;
import java.util.HashSet;

public class User extends Account{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username, firstName, lastName, email, password;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Image getUserImage() {
		return userImage;
	}

	public void setUserImage(Image userImage) {
		this.userImage = userImage;
	}

	public HashSet<Party> getParties() {
		return parties;
	}

	public void setParties(HashSet<Party> parties) {
		this.parties = parties;
	}

	public Party getHostedParty() {
		return hostedParty;
	}

	public void setHostedParty(Party hostedParty) {
		this.hostedParty = hostedParty;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
