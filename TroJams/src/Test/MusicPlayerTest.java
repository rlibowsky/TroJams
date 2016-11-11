package Test;

import javazoom.jl.player.*;
import java.io.FileInputStream;

public class MusicPlayerTest {

	public MusicPlayerTest(){
	    try{
		    FileInputStream fis = new FileInputStream("closer.mp3");
		    Player playMP3 = new Player(fis);
		    playMP3.play();
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
