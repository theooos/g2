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

    public static final long ENG_GAME_TIME = 8000;

    // ***** NETWORK SETTINGS *****

    public static final boolean LOCAL = true;
    public static final boolean MAP_LOCAL = true;
    public static final String SERVER_IP = "46.101.84.55";
    public static final int PORT = 3000;

}
