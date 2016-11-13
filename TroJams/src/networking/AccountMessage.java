package networking;

import logic.Account;

public class AccountMessage extends Message{
	private Account account;

	public AccountMessage(String name, Account account) {
		super(name);
		this.account = account;
	}
	
	public Account getAccount() {
		return account;
	}

}
