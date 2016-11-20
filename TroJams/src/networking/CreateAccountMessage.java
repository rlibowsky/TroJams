package networking;

import logic.User;

public class CreateAccountMessage extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private String password;
	
	public CreateAccountMessage(User newUser, String password){
		super("createAccount");
		this.user = newUser;
		this.password = password;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getUsername(){
		return user.getUsername();
	}
	
	public String getFirstName(){
		return user.getFirstName();
	}
	
	public String getLastName(){
		return user.getLastName();
	}
	
	public String getEmail(){
		return user.getEmail();
	}
	
	public String getFilepath(){
		return user.getImageFilePath();
	}
	
	
	
}

