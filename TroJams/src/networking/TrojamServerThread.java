package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import logic.Account;

public class TrojamServerThread extends Thread{
	private Socket socket;
	private TrojamServer trojamServer;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Account account;
	
	public TrojamServerThread(Socket socket, TrojamServer trojamServer) {
		this.socket = socket;
		this.trojamServer = trojamServer;
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			//handle when a new person joins the party
			try {
				account = (Account) ois.readObject();
			} catch (ClassNotFoundException e) {}
			
			this.start();

		} catch (IOException e) {}
		
		this.start();
	}
	
	@Override
	public void run(){
		try {
			while (true){
				Message message = (Message) ois.readObject();
			}
			
		} 
		catch (ClassNotFoundException e) {}
		catch (IOException e) {}
	}
	
	public void sendMessage(Message message) {
		
	}

}
