package client.audio;

import server.game.Player;

import static client.ClientSettings.*;
import static client.graphics.GameManager.out;

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
        Audio.WARNING.stopClip();
        Audio.CAUTION.stopClip();
        Audio.GAME_END.stopClip();
        Audio.GAME_START.stopClip();
    }

    public static void playHealthUp() {
        if (!Audio.HEALTH_UP.isPlaying()) Audio.HEALTH_UP.play(HEALTH_UP_VOL);
    }

    public static void playGameStart() {
        Audio.GAME_START.play(VOICE_VOL);
    }

    public static void playGameOver() {
        Audio.GAME_END.play(VOICE_VOL);
    }

    public static void playShooting(Player me) {
        if (me.getActiveWeapon().toString().equals("SMG")) {
            Audio.SMG.play(SHOOTING_VOL);
        } else if (me.getActiveWeapon().toString().equals("Shotgun")) {
            if (!Audio.SHOTGUN.isPlaying()) {
                if (shotgunCount == 0) {
                    Audio.SHOTGUN.play(SHOOTING_VOL);
                }
                shotgunCount++;
                if (shotgunCount > 4) shotgunCount = 0;
            }
        } else {
            Audio.SNIPER.play(SHOOTING_VOL);
        }
    }

    public static void playWarningSounds(int health) {
        if (health < WARNING_THRES) {
            float volume = Math.min(1, Math.max(0, WARNING_VOL - health / 100f));
            Audio.WARNING.changeVolume(volume);
            if (!Audio.WARNING.isPlaying()) {
                Audio.WARNING.loop(volume);
                Audio.CAUTION.play(CAUTION_VOL);
            }
        } else if (health >= WARNING_THRES && Audio.WARNING.isPlaying()) {
            Audio.WARNING.stopClip();
        }
    }

    public static void playOrbHum(float closestDist) {
        if (closestDist < ORB_VIS) {
            float volume = Math.min(1, Math.max(0, ORB_VOL - closestDist / ORB_VIS));
            Audio.PULSE.changeVolume(volume);
            if (!Audio.PULSE.isPlaying()) {
                Audio.PULSE.loop(volume);
            }
        } else if (closestDist >= ORB_VIS && Audio.PULSE.isPlaying()) {
            Audio.PULSE.stopClip();
        }
    }

    public static void applyVolume() {
        Audio.MUSIC.changeVolume(MUSIC_VOL);
        Audio.AMBIANCE.changeVolume(AMBIENT_VOL);
        Audio.SHOTGUN.changeVolume(SHOOTING_VOL);
        Audio.SMG.changeVolume(SHOOTING_VOL);
        Audio.SNIPER.changeVolume(SHOOTING_VOL);
        Audio.HEALTH_UP.changeVolume(HEALTH_UP_VOL);
        Audio.PULSE.changeVolume(PULSE_VOL);
        Audio.WARNING.changeVolume(WARNING_VOL);
        Audio.CAUTION.changeVolume(CAUTION_VOL);
        Audio.GAME_START.changeVolume(VOICE_VOL);
        Audio.GAME_END.changeVolume(VOICE_VOL);
    }
}
