package logic;

//to be added to song queues for parties
public class PartySong extends Song{
	
	private int votes;

	public PartySong(String name, double length) {
		super(name, length);
		votes = 0;
	}
	
	public void upvote() {
		votes++;
	}
	
	public void downvote() {
		votes--;
	}

	public int getVotes() {
		return votes;
	}

}
