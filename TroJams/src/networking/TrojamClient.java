package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Account;

public class TrojamClient extends Thread{
	private Account account;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

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
}
