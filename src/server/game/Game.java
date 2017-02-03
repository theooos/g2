package server.game;

import networking.Connection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by peran on 27/01/17.
 */
public class Game {
    private Timer t;
    private int countdown;
    private int tick = 60;
    private int map;
    private ArrayList<Connection> playerConnections;
    private int numPlayers;


    public Game(ArrayList<Connection> playerConnections, int numPlayers, int map) {
        this.map = map;
        this.playerConnections = playerConnections;
        this.numPlayers = numPlayers;

        loadMap(map);

        t = new Timer();
        countdown = 10*60*tick; //ten minutes

        int rate = 1000/60;

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countdown--;

                //stops the countdown when the timer has run out
                if (countdown < 0) {
                    endGame();
                }
            }
        }, rate, rate);
    }

    public void endGame() {
        t.cancel();
        t.purge();
    }

    public void loadMap(int map) {

    }

    /**
     * creates the player and places it on one of the inital spawn points on the loaded map
     * @param p the player
     */
    public void initalPlayerSpawn(Player p) {

    }

    /**
     * respawns a bot or player in a valid location
     * @param e the entity to be respawned
     */
    public void respawn(MovableEntity e) {

    }
}
