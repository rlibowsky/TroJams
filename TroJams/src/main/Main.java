package main;

import frames.TrojamWelcomeWindow;
import networking.TrojamClient;

public class Main {

    public static void main (String [] args) {
    	
    	new TrojamWelcomeWindow(new TrojamClient("10.120.17.113", 6789)).setVisible(true);
    }
}
