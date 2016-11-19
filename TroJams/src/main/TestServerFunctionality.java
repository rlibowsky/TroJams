package main;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import frames.SelectionWindow;
import logic.Party;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;
import networking.TrojamClient;

public class TestServerFunctionality {
	public static void main (String [] args) {
		User u = new User("testUser", "testPassword");
		User u2 = new User("Hunter", "mwahahaha");
		User u3 = new User("Clairisse", "fightON");
		User u4 = new User("Adam", "ooooo");
		new TrojamClient(u, "localhost", 1111);
		Image image = new ImageIcon("images/party-purple.jpg").getImage();
		ImageIcon tempImage = new ImageIcon(image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
		PrivateParty p1 = new PrivateParty("party1", "password1", u2, tempImage);
		PrivateParty p2 = new PrivateParty("party2", "password2", u3, tempImage);
		PublicParty p3 = new PublicParty("party3", u4, tempImage);
		ArrayList <Party> parties = new ArrayList <Party>();
		parties.add(p1);
		parties.add(p2);
		parties.add(p3);
		new SelectionWindow(u, parties).setVisible(true);
	}
}
