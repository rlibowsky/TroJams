package main;

import frames.TrojamWelcomeWindow;
import networking.TrojamClient;

public class Main {

    public static void main (String [] args) {
    	
    	new TrojamWelcomeWindow(new TrojamClient("10.121.25.95", 6789)).setVisible(true);
    }
}
