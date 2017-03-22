package client.audio;

import server.game.Player;

import static client.ClientSettings.*;
import static client.ClientSettings.WARNING_THRES;

/**
 * Created by peran on 3/22/17.
 * Used to control when to play audio
 */
public class AudioManager {

    private static int shotgunCount = 0;

    public static void playMusic() {
        Audio.MUSIC.delayStart(1, 1);
    }

    public static void playAmbiance() {
        Audio.AMBIANCE.loop(AMBIENT_VOL);
    }

    public static void playPulseSound() {
        Audio.PULSE.replay(PULSE_VOL);
    }

    public static void muteEverything() {
        Audio.SNIPER.stopClip();
        Audio.SMG.stopClip();
        Audio.SHOTGUN.stopClip();
        Audio.HEALTH_UP.stopClip();
        Audio.MUSIC.stopClip();
        Audio.AMBIANCE.stopClip();
        Audio.PULSE.stopClip();
        //Audio.WARNING.stopClip();
    }

    public static void playHealthUp() {
        if (!Audio.HEALTH_UP.isPlaying())  Audio.HEALTH_UP.play(HEALTH_UP_VOL);
    }

    public static void playShooting(Player me) {
        if (me.getActiveWeapon().toString().equals("SMG")) {
            Audio.SMG.replay(SHOOTING_VOL);
        } else if (me.getActiveWeapon().toString().equals("Shotgun")) {
            if (!Audio.SHOTGUN.isPlaying()) {
                if (shotgunCount == 0) {
                    Audio.SHOTGUN.replay(SHOOTING_VOL);
                }
                shotgunCount++;
                if (shotgunCount > 4) shotgunCount = 0;
            }
        } else {
            Audio.SNIPER.replay(SHOOTING_VOL);
        }
    }

    public static void playWarningSounds(int health) {
        if (health < WARNING_THRES) {
            float volume = Math.min(1, Math.max(0, WARNING_VOL-health/100f));
            Audio.PULSE.changeVolume(volume);
            if (!Audio.PULSE.isPlaying()) {
                Audio.PULSE.loop(volume);
            }
        }
        else if (health >= WARNING_THRES && Audio.PULSE.isPlaying()) {
            Audio.PULSE.stopClip();
        }
    }
}
