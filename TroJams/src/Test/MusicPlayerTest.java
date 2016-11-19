package test;

import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MusicPlayerTest {

	
	// retrieve text from search, check if vector contains that song
	// call method with that song name
	public MusicPlayerTest(){
	    try{
		    //FileInputStream fis = new FileInputStream("music/sunset_lover.mp3");
	    	FileInputStream fis = new FileInputStream("song_bin/curr_song.mp3");
		    Player playMP3 = new Player(fis);
		    playMP3.play();
		    while (!playMP3.isComplete()) {
		    	
		    }
		    System.out.println("finished");
		    FileInputStream fis2 = new FileInputStream("music/kid.mp3");
		    Player playMP3_2 = new Player(fis2);
		    playMP3_2.play();
		    
	    }catch(Exception e){
	    	System.out.println(e);
	    }
	}
	public static void main(String[] args){
		MusicPlayerTest m = new MusicPlayerTest();
	}
}








//
//import java.io.File;
//
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//
//public class MusicPlayerTest {
//
//	public static void main(String[] args){
//		//playSound();
//	}
//
//
////	public static synchronized void playSound(final String url) {
////        new Thread(new Runnable() {
////          public void run() {
////            try {
////              Clip clip = AudioSystem.getClip();
////              AudioInputStream inputStream = AudioSystem.getAudioInputStream(ChatClient.class.getResourceAsStream("/resources/" + url));
////              clip.open(inputStream);
////              clip.start();
////            } catch (Exception e) {
////              System.err.println(e.getMessage());
////            }
////          }
////        }).start();
////      }
//
//
//
//
//
//	public static void playSound() {
//		while(true){
//		    try {
//		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("pig.wav").getAbsoluteFile());
//		        Clip clip = AudioSystem.getClip();
//		        clip.open(audioInputStream);
//		        clip.start();
//		    } catch(Exception ex) {
//		        System.out.println("Error with playing sound.");
//		        ex.printStackTrace();
//		    }
//		}
//	}
//}
