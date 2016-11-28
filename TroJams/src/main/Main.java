package main;

import frames.TrojamWelcomeWindow;
import networking.TrojamClient;

public class Main {

    public static void main (String [] args) {
    	System.out.println("jfieowa");
    	new TrojamWelcomeWindow(new TrojamClient("107.170.232.140", 6789));
    	//new TrojamWelcomeWindow(new TrojamClient("localhost", 6789));
    	//new TrojamWelcomeWindow(new TrojamClient("10.121.30.155", 6789));
    }
}
