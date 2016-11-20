package networking;

import logic.Party;

public class PartyMessage extends Message {
	private Party party;

	public PartyMessage(String name, Party party) {
		super(name);
		this.party = party;
	}
	
	public Party getParty() {
		return party;
	}

}
