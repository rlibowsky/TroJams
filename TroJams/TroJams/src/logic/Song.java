package logic;

//regular song, gets uploaded to playlists but not parties!
public class Song {
	
	private String name;
	private double length;
	
	public Song(String name, double length) {
		this.name = name;
		this.length = length;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLength() {
		return length;
	}
	
}
