package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import logic.Account;

public class TrojamServer extends Thread{
	private ServerSocket serverSocket;
	private ArrayList <TrojamServerThread> trojamServerThreads;
	private int port;
	
	public TrojamServer(int port) {
		this.port = port;
		this.start();
		trojamServerThreads = new ArrayList <TrojamServerThread>();
	}
	
	@Override
	public void run(){
		try {
			serverSocket = new ServerSocket(port);
			// for now, while true
			while (true){
				Socket socket = serverSocket.accept();
				TrojamServerThread newThread = new TrojamServerThread(socket, this);
				trojamServerThreads.add(newThread);
			}

		} catch (NumberFormatException | IOException e) {
			System.out.println("io in server "+e.getMessage());
		}
	}
	
	public void sendMessage(Message message){
		for (TrojamServerThread currentThread : trojamServerThreads){
			if (currentThread != null) currentThread.sendMessage((Message)message);
		}
	}
}
