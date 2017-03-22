package client;

/**
 * Stores all variables that should be accessible anywhere for the Client.
 */
public class ClientSettings {

    public static final boolean DEBUG = true;

    // ***** DISPLAY SETTINGS *****

    static final String WINDOW_TITLE = "PhaseShift";
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    static final boolean FULLSCREEN = false;

    // ***** NETWORK SETTINGS *****

    public static final boolean LOCAL = true;
    public static final boolean MAP_LOCAL = true;
    public static final String SERVER_IP = "46.101.84.55";
    public static final int PORT = 3000;

    // ***** AUDIO SETTINGS *****
    public static final float MIN_VOLUME = -40f;
    public static final float AMBIENT_VOL = 0.2f;
    public static final float SHOOTING_VOL = 1f;
    public static final float HEALTH_UP_VOL = 1f;
    public static final float PULSE_VOL = 1f;
    public static final float WARNING_VOL = 0.8f;
    public static final float WARNING_THRES = 50;
    public static final float ORB_VOL = 0.4f;
    //User configurable
    public static float MUSIC_VOL = 1f;
    public static float SOUND_VOL = 1f;

    // ***** GRAPHICS SETTINGS *****
    public static final float ORB_VIS = 150;

}
