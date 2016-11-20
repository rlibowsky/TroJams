package logic;

import java.io.Serializable;

//regular song, gets uploaded to playlists but not parties!
public class Song implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double length;
	
	public Song(String name, double length) {
		this.name = name;
		this.length = length;
	}
	
	public Song() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public double getLength() {
		return length;
	}
	
}
