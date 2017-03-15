package client.audio;

/**
 * Created by Patrick on 2/21/2017.
 */

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.initGameSprite() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */
public enum Audio {


    MUSIC(Audio.class.getResource("../moz.wav")),        // music
    SHOOT(Audio.class.getResource("../lasershoot.wav")),
    INTERFACEBACKGROUND(Audio.class.getResource("../backmusic.wav"));


    // Nested class for specifying volume
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;
    private ArrayList<Clip> allClips = new ArrayList<>();

    // Constructor to construct each element of the enum with its own sound file.
    Audio(URL soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            //URL url = this.getClass().getClassLoader().getResource(soundFileName);


            //File soundFile = new File(url);

            // Set up an audio input stream piped from the sound file.

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

    public void playallTime(Clip clip)
    {
        clip.loop(5);
    }
    public void stopCurrentClip()
    {
        clip.stop();
    }

    public void muteEverything()
    {
        for (Clip c : allClips)
        {
            c.stop();
        }
    }


    // Optional static method to pre-load all the sound files.
    public static void init() {
        values(); // calls the constructor for all the elements
    }
}