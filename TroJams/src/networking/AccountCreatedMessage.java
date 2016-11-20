package networking;

public class AccountCreatedMessage extends Message {
	private boolean accountCreated;
	
	public AccountCreatedMessage(boolean accountCreated) {
		super("accountCreated");
		this.accountCreated = accountCreated;
	}
	
	public boolean accountCreated(){
		return accountCreated;
	}

}
