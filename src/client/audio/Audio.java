package client.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static client.ClientSettings.MIN_VOLUME;
import static client.ClientSettings.MUSIC_VOL;
import static client.ClientSettings.SOUND_VOL;

/**
 * Created by Patrick on 2/21/2017.
 *
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke Audio.SOUND_NAME.play().
 * 3. You might optionally invoke the static method Audio.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */

public enum Audio {


    MUSIC(Audio.class.getResource("../../game_music.wav")),        // music
    //SNIPER(Audio.class.getResource("../../lasershoot.wav")),
    //SHOTGUN(Audio.class.getResource("../../shoot2.wav")),
    //SMG(Audio.class.getResource("../../shoot2.wav")),
    AMBIANCE(Audio.class.getResource("../../background_sound.wav")),
    PHASE(Audio.class.getResource("../../phase.wav")),
    //COUNTDOWN(Audio.class.getResource("../../countdown.wav")),
    // WARNING(Audio.class.getResource("../../warning.wav"));
    ;
    // Each sound effect has its own clip, loaded with its own sound file.
    private  Clip clip;
    private Timer timer;
    private FloatControl gainControl;

    private boolean muted;

    // Constructor to construct each element of the enum with its own sound file.
    Audio(URL soundFileName) {
        try {
            timer = new Timer();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFileName);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound effect at max volume
     */
    public void play() {
        play(1);
    }

    /**
     * Player the sound effect.  If it's already running restart it
     * @param volume How loud it is, 0 is min, 1 is max
     */
    public void play(float volume) {
        changeVolume(volume);
        if (!muted) {
            clip.start();
            if (clip.isRunning()) {
                clip.stop();   // Stop the player if it is still running
            }
            clip.setFramePosition(0); // rewind to the beginning
            // Start playing
            clip.start();
        }
    }

    //play the music continously
    public void loop(float volume){
        changeVolume(volume);
        if (!muted) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    //stop a certain music
    public void stopClip() {
        clip.stop();
    }

    //verify if the clip is running or not
    public boolean isPlaying() {
        return clip.isRunning();
    }

    public void setVolume(float volume) {
        gainControl.setValue(volume);
    }

    public void delayStart(long seconds, float volume) {
        clip.stop();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loop(volume);
            }
        }, seconds*1000);
    }

    private void changeVolume(float volume) {
        //makes the sound proportional to min vol
        volume = (1-volume)*MIN_VOLUME;

        if (this == MUSIC) {
            System.out.println("Playing music");
            if (MUSIC_VOL == 0) muted = true;
            else {
                muted = false;
                volume = (1-MUSIC_VOL)*volume;
            }
        } else {
            if (SOUND_VOL == 0) muted = true;
            else {
                muted = false;
                volume = (1-SOUND_VOL)*volume;
            }
        }

        gainControl.setValue(volume); // changes the volume
    }


    // Optional static method to pre-load all the sound files.
    public static void init() {
        values(); // calls the constructor for all the elements
    }
}