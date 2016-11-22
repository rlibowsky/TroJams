package networking;

public class AddSongMessage extends Message{
	public String songName, partyName;

	public AddSongMessage(String name, String songName, String partyName) {
		super(name);
		this.songName = songName;
		this.partyName = partyName;
	}

}
