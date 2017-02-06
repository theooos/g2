package server.game;

import networking.Connection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
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
    private ArrayList<Player> players;
    private ArrayList<Zombie> zombies;

    private int maxPlayers;
    private Random rand;


    public Game(ArrayList<Connection> playerConnections, int maxPlayers, int map) {
        this.map = map;
        this.playerConnections = playerConnections;
        this.maxPlayers = maxPlayers;
        rand = new Random();

        players = new ArrayList<>();
        zombies = new ArrayList<>();

        //create players
        for (int i = 0; i < playerConnections.size(); i++) {
            Player p = new Player(respawn(), new Vector2(0, 1), i % 2, rand.nextInt(2), new Weapon(), new Weapon());
            players.add(p);
        }
        //create AI players
        for (int i = 0; i < maxPlayers-playerConnections.size(); i++) {
            Player p = new AIPlayer(respawn(), new Vector2(0, 1), i % 2, rand.nextInt(2), new Weapon(), new Weapon());
            players.add(p);
        }
        //create team zombies
        for (int i = 0; i < maxPlayers*2; i++) {
            Zombie z = new Zombie(respawn(), new Vector2(0, 1),i % 2, rand.nextInt(2));
            zombies.add(z);
        }


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
     * returns a valid respawn coord
     */
    public Vector2 respawn() {
        //get map bounds
        int boundX = 1000;
        int boundY = 1000;
        int minDist = 50;

        boolean valid = false;
        Vector2 v = new Vector2(0,0);

        while (!valid) {
            valid = true;
            v = new Vector2(rand.nextInt(boundX), rand.nextInt(boundY));

            //check for collisions with walls

            if (collidesWithPlayerOrBot(minDist, v) != null) {
                valid = false;
            }
        }
        return v;
    }

    public MovableEntity collidesWithPlayerOrBot(int r, Vector2 pos) {
        for (Player p: players) {
            if (p.isAlive() && collided(r, pos, p.getRadius(), p.getPos())) return p;
        }

        for (Zombie z: zombies) {
            if (z.isAlive() && collided(r, pos, z.getRadius(), z.getPos())) return z;
        }

        return null;
    }

    /**
     * returns true if the two entity have collided
     * @param r1 the radius of the first entity
     * @param p1 the position of the first entity
     * @param r2 the radius of the second entity
     * @param p2 the position of the second entity
     * @return
     */
    public boolean collided(int r1, Vector2 p1, int r2, Vector2 p2) {
        if (p1.getDistanceTo(p2) < r1+r2) {
            return true;
        }
        return false;
    }

    //public Wall collidesWithWalls(int r, Vector2 p1) {}
}
