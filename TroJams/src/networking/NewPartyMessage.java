package networking;

import logic.Account;
import logic.Party;

public class NewPartyMessage extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String partyName, partyPassword;

	public NewPartyMessage(String name, String partyName, String partyPassword) {
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
