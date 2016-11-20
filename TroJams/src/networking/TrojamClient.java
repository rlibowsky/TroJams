package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Account;
import logic.Party;

public class TrojamClient extends Thread{
	private Account account;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Party party;

	public TrojamClient(Account account, String IPAddress, int port) {
		this.account = account;
		try {
			s = new Socket(IPAddress, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			//before we enter the run method, we want to send our account
			oos.writeObject(account);
			oos.flush();
			this.start();
		} catch (NumberFormatException | IOException e) {}
	}
	
	public Account getAccount() {
		return account;
	}
	
	@Override
	public void run() {
		try {
			Object obj = ois.readObject();
			//handle different types of messages
			if (obj instanceof StringMessage) {
				StringMessage message = (StringMessage) obj;
				parseStringMessage(message);
			}
		} catch (ClassNotFoundException | IOException e) {}
	}

	//handle string messages sent to the client
	private void parseStringMessage(StringMessage message) {
		String name = message.getName();
		String content = message.getContent();
		if (name.equals("teamLeft")) {
			//perform action for when an account leaves the party 
		} else if (name.equals("songAdded")) {
			//perform action for when a song is added to the party
		} else if (name.equals("songUpvoted")) {
			//perform action for when a song is upvoted
		} else if (name.equals("songDownvoted")) {
			//perform action for when a song is downvoted
		}
		
	}
}
