package server.game;

/**
 * Created by peran on 3/20/17.
 * Used to have static variables
 */
class ServerConfig {
    static final boolean DEBUG = false;
    static final int SERVER_TICK = 60;
    static final float PHASE_FADE_TIME = 0.25f*SERVER_TICK;
    static final float HURT_SPREAD = 30;
    static final float HURT_LIFE = 0.15f*SERVER_TICK;
    static final int HURT_RADIUS = 4;

    static final int MAX_SCORE = 300;
}
