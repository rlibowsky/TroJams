package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import logic.Account;
import logic.Party;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;

public class TrojamServer extends Thread{
	private ServerSocket serverSocket;
	private ArrayList <TrojamServerThread> trojamServerThreads;
	private int port;
	private ArrayList <Party> parties;
	
	public TrojamServer(int port) {
		this.port = port;
		this.parties = new ArrayList <Party> ();
		trojamServerThreads = new ArrayList <TrojamServerThread>();
		this.start();
	}
	
	@Override
	public void run(){
		try {
			serverSocket = new ServerSocket(port);
			// for now, while true
			while (true){
				Socket socket = serverSocket.accept();
				System.out.println("new connection from " + socket.getInetAddress());
				TrojamServerThread newThread = new TrojamServerThread(socket, this);
				trojamServerThreads.add(newThread);
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("io in server "+e.getMessage());
		}
	}
	
	public void sendToHost(Party p, Message message) {
		p.getHost().st.sendMessage(message);
	}
	
	public void sendToGuests(Party p, Message message) {
		for (Account act : p.getPartyMembers()) {
			act.st.sendMessage(message);
		}
	}
	
	public void sendMessage(Message message){
		for (TrojamServerThread currentThread : trojamServerThreads){
			if (currentThread != null) currentThread.sendMessage((Message)message);
		}
	}
	
	public static void main (String [] args) {
		TrojamServer tjs = new TrojamServer(1111);
	}

	public void addParty(User user, String partyName, String partyPassword) {
		System.out.println("adding a party");
		Party p;
		if (partyPassword.length() == 0) {
			p = new PublicParty(partyName, user, null);
			parties.add(p);
			sendMessage(new PartyMessage("newParty", partyName, partyPassword));
		}
		else {
			p = new PrivateParty(partyName, partyPassword, user, null);
			parties.add(p);
			sendMessage(new PartyMessage("newParty", partyName, partyPassword));
		}
	}
}
