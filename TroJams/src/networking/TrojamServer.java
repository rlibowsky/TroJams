package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import logic.Account;
import logic.Party;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;
import resources.Util;

public class TrojamServer extends Thread{
	private ServerSocket serverSocket;
	private ArrayList <TrojamServerThread> trojamServerThreads;
	private int port;
	private ArrayList <Party> parties;
	private HashMap<Account, TrojamServerThread> accountToThreadMap;
	
	public TrojamServer(int port) {
		this.port = port;
		this.parties = new ArrayList <Party> ();
		trojamServerThreads = new ArrayList <TrojamServerThread>();
		accountToThreadMap = new HashMap<>();
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
				accountToThreadMap.put(newThread.getAccount(), newThread);
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("io in server "+e.getMessage());
		}
	}
	
//	public void sendToHost(Party p, Message message) {
//		p.getHost().st.sendMessage(message);
//	}
//	
//	public void sendToGuests(Party p, Message message) {
//		for (Account act : p.getPartyMembers()) {
//			act.st.sendMessage(message);
//		}
//	}
	
	public void sendMessageToAll(Message message){
		for (TrojamServerThread currentThread : trojamServerThreads){
			if (currentThread != null) currentThread.sendMessage(message);
		}
	}
	
	

	public void addParty(User user, NewPartyMessage pm) {
		System.out.println("adding a party");
		Party p;
		if (pm.getPartyPassword().length() == 0) {
			System.out.println("sending public party");
			p = new PublicParty(pm.getPartyName(), user, null);
			parties.add(p);
			sendMessageToAll(new PartyMessage("newParty", p));
		}
		else {
			System.out.println("sending private party");
			p = new PrivateParty(pm.getPartyName(), pm.getPartyPassword(), user, null);
			parties.add(p);
			sendMessageToAll(new PartyMessage("newParty", p));
		}
	}

	public boolean authenticateLogin(LoginMessage lm) {
		String usernameString = lm.getUsername();
		String passwordString = lm.getPassword();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			passwordString = Util.hashPassword(lm.getPassword());
		
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/Trojams?user=root&password=root&userSSL=false");
				st = conn.createStatement();
				rs = st.executeQuery("SELECT username, password  FROM Trojams.Users WHERE username = '"+usernameString+ 
						"' AND password = 'H'");


				return(rs.next());
//				if(rs.next()){
//					return true;
//
//				}else{
//					return false;
//					///warningLabel.setText("this password and username combination does not exist");
//				}
			} catch (SQLException sqle){
				System.out.println("sqle: " + sqle.getMessage());
			} catch (ClassNotFoundException cnfe) {
				System.out.println("cnfe: " + cnfe.getMessage());
			}
		} catch (NoSuchAlgorithmException e1) {
			//passwordString = password.getText();
			//TODO have some sort of message to the gui about picking a different password or something
			e1.printStackTrace();
			return false;
		}finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch(SQLException sqle){
				System.out.println(sqle.getMessage());
			}
		}
		return false;
	}

	public void sendMessageToOne(Account account, Message message) {
		accountToThreadMap.get(account).sendMessage(message);
	}
	
	public static void main (String [] args) {
		TrojamServer tjs = new TrojamServer(1111);
	}

	public boolean createAccount(CreateAccountMessage cam) {
		String usernameString = cam.getUsername();
		String passwordString = cam.getPassword();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			passwordString = Util.hashPassword(cam.getPassword());
		
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/Trojams?user=root&password=root&userSSL=false");
				st = conn.createStatement();
				rs = st.executeQuery("SELECT username, password  FROM Trojams.Users WHERE username = '"+usernameString+"'");
				System.out.println("trojamServer line 164, username: "+usernameString);

				if(rs.next()){
					return false;
				}else{
					//inserts values into the db
					st.executeUpdate("INSERT INTO Users (Username, Password, First_Name, Last_Name, email, filepath_to_pic) "
							+ "VALUES ('"+usernameString+"', '"+passwordString+"', '"+ cam.getFirstName()+"', '"
							+ cam.getLastName()+"', '"+ cam.getEmail()+"', '"+ cam.getFilepath() +"')");
					
					//saves the file on the machine on the server
					return true;
				}
			} catch (SQLException sqle){
				System.out.println("sqle: " + sqle.getMessage());
			} catch (ClassNotFoundException cnfe) {
				System.out.println("cnfe: " + cnfe.getMessage());
			}
		} catch (NoSuchAlgorithmException e1) {
			//passwordString = password.getText();
			//TODO have some sort of message to the gui about picking a different password or something
			e1.printStackTrace();
			return false;
		}finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch(SQLException sqle){
				System.out.println(sqle.getMessage());
			}
		}
		return false;
	}
}
