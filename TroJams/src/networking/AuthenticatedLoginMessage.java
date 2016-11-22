package networking;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticatedLoginMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean authenticated;
	private String firstName, lastName, imageFilePath;
	
	
	public AuthenticatedLoginMessage(boolean loggedIn) {
		super("AuthenticatedLoginMessage");
		authenticated = loggedIn;
	}
	
	public AuthenticatedLoginMessage(ResultSet rs) {
		super("AuthenticatedLoginMessage");
		//username, password, first_name, last_name, filepath_to_pic
		try {
			firstName = rs.getString("first_name");
			lastName = rs.getString("last_name");
			imageFilePath = rs.getString("filepath_to_pic");
		} catch (SQLException e) {
			authenticated = false;
		}
	}

	public boolean isAuthenticated(){
		return authenticated;
	}
	
	public String getfirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getFilepath(){
		return imageFilePath;
	}
}
