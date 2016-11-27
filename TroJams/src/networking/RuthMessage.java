package networking;

public class RuthMessage extends Message{
	String partyName;

	public RuthMessage(String name, String partyName) {
		super(name);
		this.partyName = partyName;
	}
	
	public String getPartyName() {
		return partyName;
	}

}
