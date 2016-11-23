package networking;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

import logic.Account;
import logic.Party;
import logic.PartySong;
import logic.PrivateParty;
import logic.PublicParty;
import logic.User;
import music.MusicPlayer;
import music.SongData;
import resources.Util;

public class TrojamServer extends Thread{
	private ServerSocket serverSocket;
	private Vector <TrojamServerThread> trojamServerThreads;
	private int port;
	private Vector <Party> parties;
	private HashMap <String, Party> partyNamesToObjects;
	private HashMap<String, TrojamServerThread> accountToThreadMap;
	private int numThreads;
	
	public TrojamServer(int port) {
		System.out.println("making new tjs");
		this.port = port;
		this.parties = new Vector <Party> ();
		trojamServerThreads = new Vector <TrojamServerThread>();
		accountToThreadMap = new HashMap<>();
		partyNamesToObjects = new HashMap<>();
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
				TrojamServerThread newThread = new TrojamServerThread(socket, this, trojamServerThreads.size());
				trojamServerThreads.add(newThread);
				
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
		//TODO update this to use a vector
		for (TrojamServerThread currentThread : trojamServerThreads){
			if (currentThread != null) currentThread.sendMessage(message);
		}
	}
	
	

	public void addParty(User user, NewPartyMessage pm) {
		System.out.println("adding a party");
		Party p;
		if (pm.getPartyPassword().length() == 0) {
			System.out.println("sending public party");
			p = new PublicParty(pm.getPartyName(), user, pm.getFilePath());
			parties.add(p);
			partyNamesToObjects.put(p.getPartyName(), p);
			sendMessageToAll(new AllPartiesMessage("allParties", parties));
		}
		else {
			System.out.println("sending private party");
			p = new PrivateParty(pm.getPartyName(), pm.getPartyPassword(), user, pm.getFilePath());
			parties.add(p);
			sendMessageToAll(new AllPartiesMessage("allParties", parties));
		}
	}

	public AuthenticatedLoginMessage authenticateLogin(LoginMessage lm, TrojamServerThread tjs) {
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
				rs = st.executeQuery("SELECT username, first_name, last_name, filepath_to_pic  FROM Trojams.Users "
						+ "WHERE username = '"+usernameString+ "' AND password = '"+passwordString+"'");

				if(rs.next()){
					System.out.println("found in db");
					AuthenticatedLoginMessage alm = new AuthenticatedLoginMessage(rs);
					tjs.setAccount(new User(alm.getUsername(), alm.getfirstName(), alm.getLastName(), alm.getFilepath()));
					accountToThreadMap.put(alm.getUsername(), tjs);
					System.out.println("added serverthread for " + tjs + " " + tjs.getAccount());
					return new AuthenticatedLoginMessage(rs);
				}else{
					return new AuthenticatedLoginMessage(false);
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
			return new AuthenticatedLoginMessage(false);
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
		return new AuthenticatedLoginMessage(false);
	}

	public void sendMessageToOne(int threadNum, Message message) {
//		for (Map.Entry<Account, TrojamServerThread> entry : accountToThreadMap.entrySet())
//		{
//			System.out.println("we have a user");
//		  //  System.out.println(entry.getKey() + "/" + entry.getValue());
//		}
		trojamServerThreads.get(threadNum).sendMessage(message);
	}

	public boolean createAccount(CreateAccountMessage cam, TrojamServerThread tjs) {
		String usernameString = cam.getUsername();
		String passwordString = cam.getPassword();
		String filepath = cam.getFilepath();
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
					//checks to see if it is a default picture
					if(filepath.equals("images/silhouette.png")){
						filepath = "profile_pics/silhouette.png";
					} else if(filepath.endsWith(".jpeg")){
						filepath = "profile_pics/"+usernameString+".jpeg";
					} else if(filepath.endsWith(".png")){
						filepath = "profile_pics/"+usernameString+".png";
					} else if(filepath.endsWith(".jpg")){
						filepath = "profile_pics/"+usernameString+".jpg";
					} else {
						filepath = "profile_pics/corrupt.png";
					}
					try {
						File f = new File(filepath);
						byte[] content = cam.getFileAsByteArray();
						if(content == null){ System.out.println("asdlfkjas;ldkjf;alskdfj;laskjdf;lkasjfd;lk"); }
						Files.write(f.toPath(), content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//inserts values into the db
					st.executeUpdate("INSERT INTO Users (Username, Password, First_Name, Last_Name, email, filepath_to_pic) "
							+ "VALUES ('"+usernameString+"', '"+passwordString+"', '"+ cam.getFirstName()+"', '"
							+ cam.getLastName()+"', '"+ cam.getEmail()+"', '"+ filepath +"')");
					
					//saves the file on the machine that is hosting the server
					accountToThreadMap.put(usernameString, tjs);
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

	public void voteOnSong(SongVoteMessage svm) {
		Party receivedParty = partyNamesToObjects.get(svm.getParty().getPartyName());
		SongData receivedSong = receivedParty.songList.get(receivedParty.songSet.get(svm.getSong().getName()));
		if (svm.getName().equals("upvote")) {
			System.out.println("upvoting a song");
			receivedParty.upvoteSong(receivedSong);
		} else if (svm.getName().equals("downvote")){
			System.out.println("downvoting a song");
			receivedParty.downvoteSong(receivedSong);
		}
		this.sendMessageToParty(receivedParty, svm);
	}

	public void addPartyGuest(NewPartierMessage npm) {
		partyNamesToObjects.get(npm.getPartyName()).addAccount(npm.getAccount());
		System.out.println("added guest to party");
	}

	public void getParties() {
		System.out.println("sending parties");
		sendMessageToAll(new AllPartiesMessage("allParties", parties));
	}

	public FoundSongMessage searchForSong(String songName) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Trojams?user=root&password=root&userSSL=false");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT song_name, artist, album, artwork_filepath, mp3_filepath "
					+ " FROM Trojams.Songs WHERE song_name = '"+songName+"'");

			if(rs.next()){
				return new FoundSongMessage(rs);
			}else{
				return new FoundSongMessage(false);
			}
		} catch (SQLException sqle){
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
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
		return new FoundSongMessage(false);
	}

	public void addNewSong(AddSongMessage asm) {
		System.out.println("server got song");
		//check if song list is empty
		Party p = partyNamesToObjects.get(asm.partyName);
		p.addSong(asm.songInfo);
		System.out.println("size list is " + p.getSongs().size());
		if (p.getSongs().size() == 1) {
			System.out.println("STARTING THE MUSIC PLAYER");
			//accountToThreadMap.get((p.getHost().getUsername())).
			//MusicPlayer mp = new MusicPlayer("music/" + p.getSongs().get(0).getName() + ".mp3", p, this);
			nextSong(p.getPartyName());
			//send message to party to update currently playing
		}
		
		sendMessageToParty(partyNamesToObjects.get(asm.partyName), asm);
	}

	private void sendMessageToParty(Party party, Message msg) {
		if (msg instanceof AddSongMessage) {
			AddSongMessage asm = (AddSongMessage) msg;
			for (Account a : party.getPartyMembers()) {
				TrojamServerThread currentThread = accountToThreadMap.get(((User)a).getUsername());
				if (a instanceof User) {
					System.out.println("sending message to " + ((User)a).getUsername());
				}
				if (currentThread != null) {
					currentThread.sendMessage(new SongVoteMessage("svm", party, asm.songInfo));
				} else {
					System.out.println("is null");
				}
			}
		}
		else if (msg instanceof SongVoteMessage) {
			SongVoteMessage asm = (SongVoteMessage) msg;
			for (Account a : party.getPartyMembers()) {
				TrojamServerThread currentThread = accountToThreadMap.get(((User)a).getUsername());
				if (a instanceof User) {
					System.out.println("sending message to " + ((User)a).getUsername());
				}
				if (currentThread != null) {
					currentThread.sendMessage(new SongVoteMessage("svm", party, asm.getSong()));
				} else {
					System.out.println("is null");
				}
			}
		}
		else if (msg instanceof PlayNextSongMessage) {
			PlayNextSongMessage psm = (PlayNextSongMessage) msg;
			for (Account a : party.getPartyMembers()) {
				TrojamServerThread currentThread = accountToThreadMap.get(((User)a).getUsername());
				if (a instanceof User) {
					System.out.println("sending message to " + ((User)a).getUsername());
				}
				if (currentThread != null) {
					currentThread.sendMessage(psm);
				} else {
					System.out.println("is null");
				}
			}
			
		}
	}

	public void nextSong(String partyName) {
		System.out.println("in nextSong");
		System.out.println("hi");
		Party p = partyNamesToObjects.get(partyName);
		MusicPlayer mp = new MusicPlayer("music/" + p.getSongs().get(0).getName() + ".mp3", p, this);
		String s = p.getSongs().get(0).getName();
		p.playNextSong();
		//sendMessageToParty(p, new AddSongMessage("string", "string", "string"));
		System.out.println("sending message to update currently playing");
		sendMessageToParty(p, new PlayNextSongMessage(p, s));
		
	}
	
	public static void main (String [] args) {
		System.out.println("in main of server...");
		TrojamServer tjs = new TrojamServer(6789);
	}
}
