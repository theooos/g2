package client.audio;

/**
 * Created by Patrick on 2/21/2017.
 */

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.initGameSprite() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */

public enum GameEffects {


    GAMEMUSIC(GameEffects.class.getResource("../../moz.wav")),        // music
    SHOOT(GameEffects.class.getResource("../../lasershoot.wav")),
    SHOOT2(GameEffects.class.getResource("../../shoot2.wav")),
    INTERFACEBACKGROUND(GameEffects.class.getResource("../../backmusic.wav")),
    PHASE(GameEffects.class.getResource("../../phase.wav")),
    COUNTDOWN(GameEffects.class.getResource("../../countdown.wav")),
    WARNING(GameEffects.class.getResource("../../warning.wav"));

    // Nested class for specifying volume
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    // Each sound effect has its own clip, loaded with its own sound file.
    private  Clip clip;
    private ArrayList<Clip> allClips = new ArrayList<>();
    private Timer timer;

    // Constructor to construct each element of the enum with its own sound file.
    GameEffects(URL soundFileName) {
        try {
            timer = new Timer();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFileName);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            allClips.add(clip);
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play or Re-play the sound effect from the beginning, by rewinding.
    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();   // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();
            // Start playing
        }
    }
    //play the music continously
    public void playallTime(){clip.loop(Clip.LOOP_CONTINUOUSLY);}
    //stop a certain music
    public void stopClip()
    {
        clip.stop();
    }
    //verify if the clip is running or not
    public boolean checkStop()
    {
        if(clip.isActive())
            return true;
        else
            return false;
    }
    //pause for the given seconds the clip
    public void pause(long seconds){
        clip.stop();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playallTime();
            }
        }, seconds*1000);

    }

    public void SetLowVolume()
    {
        GameEffects.volume = GameEffects.Volume.LOW;
    }
    // Optional static method to pre-load all the sound files.
    public static void init() {
        values(); // calls the constructor for all the elements
    }
}