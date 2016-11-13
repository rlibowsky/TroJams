package logic;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Party {
	
	private String partyName;
	private User host;
	private ArrayList <PartySong> songList = new ArrayList<PartySong>();
	private HashMap <PartySong, Integer> songSet = new HashMap<PartySong, Integer>();
	
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
		if (songSet.containsKey(song)) {
			return;
		}
		songList.add(song);
		songSet.put(song, songList.size()-1);
	}
	
	public void upvoteSong(PartySong song) {
		int loc = songSet.get(song);
		song.upvote();
		//look at the indices before in the array and keep swapping while the
		//number of votes of loc - 1 is less than the number of votes of song
		while (loc > 0 && songList.get(loc - 1).getVotes() < songList.get(loc).getVotes()) {
			PartySong tempSong = songList.get(loc-1);
			songSet.put(tempSong, loc);
			songSet.put(song, loc - 1);
			songList.set(loc, tempSong);
			songList.set(loc - 1, song);
			loc --;
		}
	}
	
	public void downvoteSong(PartySong song) {
		int loc = songSet.get(song);
		song.downvote();
		//look at the indices after in the array and keep swapping while the
		//number of votes of loc + 1 is greater than the number of votes of song
		while (loc < songList.size() && songList.get(loc + 1).getVotes() > songList.get(loc).getVotes()) {
			PartySong tempSong = songList.get(loc+1);
			songSet.put(tempSong, loc);
			songSet.put(song, loc + 1);
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
}
