package cs3331.hw4;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * The code for this was inspired by many youtube turoials, the main video link was https://www.youtube.com/watch?v=QVrxiJyLTqU
 * the sound file is from https://www.freesfx.co.uk/rx2/mp3s/5/16961_1461335343.mp3
 * @author Andrea Fernanda Torres
 */
public class Sound{
    /**
     *
     */
    static void playWinSound(){
        playSound("src\\Sound\\winSound.wav");
    }
    static void playTileSound(){
        playSound("src\\Sound\\click (1).wav");
    }
    static void playInvalidTileSound(){
        playSound("src\\Sound\\errorSound.wav");
    }

    public static void playAlertSound(){
        playSound("src\\Sound\\alertSound.wav");
    }
    private static void playSound(String fileName){
        try{
            Clip audioClip=AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(new File(fileName)));
            audioClip.start();
            Thread.sleep(audioClip.getMicrosecondLength()/1000);
        }catch(Exception e){
            System.out.println("Sound Problem");
        }
    }



}