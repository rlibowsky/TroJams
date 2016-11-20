package networking;

import logic.Party;
import logic.PartySong;

public class SongVoteMessage extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Party party;
	private PartySong song;

	public SongVoteMessage(String name, Party party, PartySong song) {
		super(name);
		this.party = party;
		this.song = song;
	}
	
	public Party getParty() {
		return party;
	}
	
	public PartySong getSong() {
		return song;
	}

	

}
