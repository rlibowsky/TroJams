package logic;

import java.awt.Image;
import java.util.HashSet;

import javax.swing.ImageIcon;

public class User extends Account{
	
	private static final long serialVersionUID = 1L;
	private String username, firstName, lastName, password, imageFilePath;
	public ImageIcon userImage;
	private HashSet <Party> parties;
	private boolean isHost;
	public Party hostedParty; //null if user is hosting no parties
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		if (imageFilePath == null) {
			imageFilePath = "images/silhouette.png";
		}
		Image image = new ImageIcon(imageFilePath).getImage();
		userImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		this.parties = new HashSet<Party>();
	}
	
	public User(String username, String password, String firstName, String lastName, String imageFilePath) {
		this(username,password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.imageFilePath = imageFilePath;
		if (imageFilePath == null) {
			imageFilePath = "images/silhouette.png";
		}
		Image image = new ImageIcon(imageFilePath).getImage();
		userImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
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

	public ImageIcon getUserImage() {
		return userImage;
	}

	public void setUserImage(ImageIcon userImage) {
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

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}
	
	
	
	
}
