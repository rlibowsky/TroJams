package logic;

import java.io.Serializable;

import networking.TrojamServerThread;

//users and guests inherit from Account
public abstract class Account implements Serializable{
	
	/**
	 * 
	 */
	
	public TrojamServerThread st;
	private static final long serialVersionUID = 1L;

	public Account() {
	}

}
