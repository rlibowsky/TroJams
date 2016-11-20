package networking;

public class AuthenticatedLoginMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean authenticated;
	public AuthenticatedLoginMessage(boolean loggedIn) {
		super("AuthenticatedLoginMessage");
		authenticated = loggedIn;
	}
	
	public boolean isAuthenticated(){
		return authenticated;
	}
	
}
