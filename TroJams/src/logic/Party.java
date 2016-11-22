package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.swing.ImageIcon;

public abstract class Party implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String partyName;
	public User host;
	public ArrayList <PartySong> songList = new ArrayList<PartySong>();
	public HashMap <String, Integer> songSet = new HashMap<String, Integer>();
	private HashSet <Account> partyMembers;
	private ImageIcon partyImage;
	private String imageFilePath;
	
	//abstract class for a party
	public Party (String partyName, User host) {
		this.partyName = partyName;
		this.host = host;
		partyMembers = new HashSet<Account>();
		//partyImage = new ImageIcon("images/party-purple.jpg");
		imageFilePath = "party_pics/party-purple.jpg";
	}
	
	public Party (String partyName, User host, String fp) {
		this(partyName, host);
		System.out.println("filepath in constructor for party is: "+fp);
		this.imageFilePath = "party_pics/"+fp;
	}
	
	public String getImageFilePath(){
		return imageFilePath;
	}
	
	public ArrayList<PartySong> getSongs() {
		return songList;
	}
	
	public HashSet <Account> getPartyMembers() {
		return partyMembers;
	}
	
	public User getHost() {
		return host;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public String getHostName() {
//		if (host == null) {
//			host = new User("u", "u", "u", "u");
//		}
		return host.getUsername();
	}
	
	public ImageIcon getPartyImage() {
		return partyImage;
	}

	public void setPartyImage(ImageIcon partyImage) {
		this.partyImage = partyImage;
	}

	public void leaveParty(Account account) {
		partyMembers.remove(account);
	}
	
	//add a user to the party
	public void addAccount(Account account) {
		partyMembers.add(account);
	}
	
	public void addSong(PartySong song) {
		if (songSet.containsKey(song)) {
			return;
		}
		songList.add(song);
		songSet.put(song.getName(), songList.size()-1);
	}
	
	public void upvoteSong(PartySong song) {
		
		int loc = songSet.get(song.getName());
		System.out.println("loc is ..." + loc);
		songList.get(loc).upvote();
		//look at the indices before in the array and keep swapping while the
		//number of votes of loc - 1 is less than the number of votes of song
		while (loc > 0 && songList.get(loc - 1).getVotes() < songList.get(loc).getVotes()) {
			PartySong tempSong = songList.get(loc-1);
			songSet.put(tempSong.getName(), loc);
			songSet.put(song.getName(), loc - 1);
			songList.set(loc, tempSong);
			songList.set(loc - 1, song);
			loc --;
		}
	}
	
	public void downvoteSong(PartySong song) {
		int loc = songSet.get(song.getName());
		songList.get(loc).downvote();
		//look at the indices after in the array and keep swapping while the
		//number of votes of loc + 1 is greater than the number of votes of song
		while (loc < songList.size() && songList.get(loc + 1).getVotes() > songList.get(loc).getVotes()) {
			PartySong tempSong = songList.get(loc+1);
			songSet.put(tempSong.getName(), loc);
			songSet.put(song.getName(), loc + 1);
			songList.set(loc, tempSong);
			songList.set(loc + 1, song);
			loc ++;
		}
		//if votes < 0, remove from list
		if (song.getVotes() < 0) {
			songSet.remove(song);
			songList.remove(songList.size()-1);
		}
	}
	
	public void playNextSong() {
		songList.remove(0);
		//decrement indices of songs since the 0th song has been removed from the array
		for (Entry<String, Integer>  e: songSet.entrySet()) {
			e.setValue(e.getValue()-1);
		}
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	
	
}
