package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Account;
import logic.User;

public class TrojamServerThread extends Thread{
	private Socket socket;
	private TrojamServer trojamServer;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Account account;
	
	public TrojamServerThread(Socket socket, TrojamServer trojamServer) {
		this.socket = socket;
		this.trojamServer = trojamServer;
		this.account = null;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			this.start();

		} catch (IOException e) {}
	}
	
	public void setAccount(Account a) {
		this.account = a;
	}
	
	@Override
	public void run(){
		try {
			while (true){
				Object obj = ois.readObject();
				if (obj instanceof Message) {
					Message message = (Message) obj;
				} else if (obj instanceof Account) {
					this.account = (Account) obj;
					this.account.st = this;
				} else if (obj instanceof PartyMessage) {
					PartyMessage pm = (PartyMessage) obj;
					User user = (User) account;
					trojamServer.addParty(user, pm.getPartyName(), pm.getPartyPassword());
				}
				
				
			}
		} 
		catch (ClassNotFoundException e) {}
		catch (IOException e) {}
	}
	
	public void sendMessage(Message message){
		try {
			oos.reset();
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {}
	}

}
