package logic;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Party {
	
	private String partyName;
	private User host;
	private ArrayList <PartySong> songList = new ArrayList<PartySong>();
	private HashSet <PartySong> songSet = new HashSet<PartySong>();
	
	//abstract class for a party
	public Party (String partyName, User host) {
		this.partyName = partyName;
		this.host = host;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	//add a user to the party
	public void addUser(User user) {
		//not sure if this will be done through a database
	}
	
	public void addSong(PartySong song) {
		
	}
}
