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
	private int threadNum;
	
	public TrojamServerThread(Socket socket, TrojamServer trojamServer, int numThreads) {
		this.socket = socket;
		this.trojamServer = trojamServer;
		this.account = null;
		this.threadNum = numThreads;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			this.start();

		} catch (IOException e) {}
	}
	
	public void setAccount(Account a) {
		System.out.println("setting account");
		this.account = a;
	}
	
	@Override
	public void run(){
		try {
			while (true){
				Object obj = ois.readObject();
				 if (obj instanceof Account) {
					 System.out.println("received account");
					this.account = (Account) obj;
					//this.account.st = this;
				} 
				 else if (obj instanceof String) {
					 String str = (String) obj;
					 if (str.equals("partyRequest")) {
						 trojamServer.getParties();
					 }
				 }
				else if (obj instanceof NewPartyMessage) {
					System.out.println("new party received by serverthread");
					NewPartyMessage pm = (NewPartyMessage) obj;
					User user = (User) account;
					trojamServer.addParty(user, pm);
				} 
				else if (obj instanceof NewPartierMessage) {
					trojamServer.addPartyGuest((NewPartierMessage) obj);
				} 
				else if (obj instanceof LoginMessage) {
					System.out.println("login message received by serverthread");
					//returns a boolean saying whether or not the password matched
					boolean goodLogin = trojamServer.authenticateLogin((LoginMessage)obj );
					sendMessage(new AuthenticatedLoginMessage(goodLogin));
				} 
				else if(obj instanceof CreateAccountMessage){ 
					CreateAccountMessage cam = (CreateAccountMessage) obj;
					//returns a boolean of whether or not the account was created
					boolean accountCreated = trojamServer.createAccount(cam);
					sendMessage(new AccountCreatedMessage(accountCreated, cam.getUser()));
				} 
				else if (obj instanceof SongVoteMessage) {
					trojamServer.voteOnSong((SongVoteMessage) obj);
				}
				else if(obj instanceof SearchSongMessage){
					FoundSongMessage fsm = trojamServer.searchForSong(((SearchSongMessage) obj).getSongName());
					sendMessage(fsm);
				}
				else if (obj instanceof Message) {
					System.out.println("got a generic message");
					Message message = (Message) obj;
					trojamServer.sendMessageToAll(message);
				} 
			}
		} 
		catch (ClassNotFoundException e) {}
		catch (IOException e) {}
	}
	
	public void sendMessage(Message message){
		try {
			System.out.println("sending a message with name : " + message.getName());
			oos.reset();
			oos.writeObject(message);
			oos.flush();
			System.out.println("message was sent to client");
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("exception in sendMessage in server: " + e.getMessage() + " " + e.getLocalizedMessage());
		}
	}

	public Account getAccount() {
		return account;
	}
	
	public int getThreadNum(){
		return threadNum;
	}

}
