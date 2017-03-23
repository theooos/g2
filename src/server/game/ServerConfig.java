package server.game;

/**
 * Created by peran on 3/20/17.
 * Used to hold static variables
 */
public class ServerConfig {
    static final boolean DEBUG = false;
    static final int SERVER_TICK = 60;
    static final float PHASE_FADE_TIME = 0.25f*SERVER_TICK;
    static final float HURT_SPREAD = 30;
    static final float HURT_LIFE = 0.15f*SERVER_TICK;
    static final int HURT_RADIUS = 4;

    public static final boolean NAME_LOCAL = true;

    static final int TIME_LIMIT = 4*60*SERVER_TICK; //4 minutes
    static final int MAX_SCORE = 300;
    static final int PLAYER_KILLED_SCORE = 10;
    static final int ORB_KILLED_SCORE = 3;
    static final int KILLED_BY_ORB_SCORE = -3;

    static final int RESPAWN_DISTANCE = 80;

}
