package logic;

public class Guest extends Account{
	
	private Party party;

	public Guest() {
		super();
	}
	
	//called when a guest logs out
	public void leaveParty() {
		party.leaveParty(this);
	}
	

}
