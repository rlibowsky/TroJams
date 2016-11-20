package networking;

import logic.Account;
import logic.Party;

public class PartyMessage extends Message{
	private String partyName, partyPassword;

	public PartyMessage(String name, String partyName, String partyPassword) {
		super(name);
		this.partyName = partyName;
		this.partyPassword = partyPassword;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public String getPartyPassword() {
		return partyPassword;
	}

}
