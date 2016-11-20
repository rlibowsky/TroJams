package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import frames.SelectionWindow;
import logic.Account;

public class TrojamClient extends Thread{
	private Account account;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private SelectionWindow sw;

	public TrojamClient(Account account, String IPAddress, int port, SelectionWindow sw) {
		System.out.println("creating a new client!!!");
		this.account = account;
		this.sw = sw;
		try {
			s = new Socket(IPAddress, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			System.out.println("About to send account info to server");
			//before we enter the run method, we want to send our account
			oos.writeObject(account);
			oos.flush();
			System.out.println("Just sound account info");
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
			} else if (obj instanceof PartyMessage) {
				PartyMessage pm = (PartyMessage) obj;
				if (pm.getName().equals("newParty")) {
					System.out.println("new party sent to client");
					sw.addNewParty(pm.getParty());
				}
			}
		} catch (ClassNotFoundException | IOException e) {}
	}
	
	public void sendNewPartyMessage(NewPartyMessage npm) {
		try {
			System.out.println("trying to send a new party");
			oos.writeObject(npm);
			oos.flush();
			System.out.println("sent a new party");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
