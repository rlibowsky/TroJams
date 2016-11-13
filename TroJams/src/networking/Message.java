package networking;

public class Message {
	private String name, message;
	
	public Message(String name, String message) {
		this.name = name;
		this.message = message;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMessage() {
		return message;
	}

}
