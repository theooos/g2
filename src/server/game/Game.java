package server.game;

import networking.Connection;
import objects.Sendable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by peran on 27/01/17.
 * Controls the main game logic
 */
public class Game {
    private Timer t;
    private int countdown;
    private Map map;

    private ArrayList<Connection> playerConnections;
    private ArrayList<Player> players;
    private ArrayList<Zombie> zombies;
    private ArrayList<Projectile> projectiles;

    private Random rand;

    private Scoreboard sb;
    private int IDCounter;


    public Game(ArrayList<Connection> playerConnections, int maxPlayers, int mapID) {
        int tick = 60;
        IDCounter = 0;

        this.playerConnections = playerConnections;

        //try to load the map
        try {
            this.map = new Map(mapID);
        }
        catch(IOException e) {
            msgToAllConnected("Failed to load map");
        }

        System.out.println(playerConnections.size());

        rand = new Random();
        sb = new Scoreboard(100);

        players = new ArrayList<>();
        zombies = new ArrayList<>();
        projectiles = new ArrayList<>();

        //create players
        for (int i = 0; i < playerConnections.size(); i++) {


            //Player p = new Player(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), new Weapon(), new Weapon(), IDCounter);
            //players.add(p);
            //IDCounter++;
            playerConnections.get(i).addFunctionEvent("Vector2",this::printVector2);
            playerConnections.get(i).addFunctionEvent("String",this::parsingStringClient);



        }





        //create AI players
        for (int i = 0; i < maxPlayers-playerConnections.size(); i++) {
          //  Player p = new AIPlayer(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), new Weapon(), new Weapon(), IDCounter);
          //  players.add(p);
            //IDCounter++;
        }
        //create team zombies
        for (int i = 0; i < 1; i++) {
            Zombie z = new Zombie(respawnCoords(), randomDir(),i % 2, rand.nextInt(2), IDCounter);
            zombies.add(z);
            IDCounter++;
        }


        t = new Timer();
        countdown = 10*60*tick; //ten minutes

        int rate = 1000/60;

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            gameTick();
            }
        }, rate, rate);
    }


    /**
     * parsing text from the Client
     * @param s sendable object
     */
    private void parsingStringClient(Sendable s)
    {

        if(s.toString().equals("fire"))
        {

            System.out.println("this is fire");

            //working with one player at the moment.
            //this.fire(players.get(0));

        }

        if(s.toString().equals("back"))
        {

            System.out.println("Back has been pressed");

        }
       if(s.toString().equals("ID:"))
       {

           String q = s.toString().substring(3);
           int id = this.getClientID(q);
           this.setIDCounter(id);
           this.createPlayer();


       }


    }
    //setting the id
    private void setIDCounter(int id)
    {

        this.IDCounter = id;
    }

    //creating a player

    private void createPlayer()
    {
        Player p = new Player(respawnCoords(), randomDir(), 5, rand.nextInt(2), new Weapon(), new Weapon(), IDCounter);
        System.out.println("Id Counter" + IDCounter);

    }

    /**
     * changing the string id to int
     * @param id the id of the Client
     */
    private int getClientID(String id)
    {

        int clientId = Integer.parseInt(id);
        return clientId;


    }


    /**
     * printing the new position of the player.
     */

    private void printVector2(Sendable s) {

        System.out.println(s);
    }

    /**
     * The game tick runs.  This is the master function for a running game
     */


    private void gameTick() {
        for (Player p: players) {
            if (!p.isAlive()) respawn(p);
        }
        for (Zombie z: zombies) {
            if(!z.isAlive()) respawn(z);
            z.live();
            respawn(z);
        }

        for (Projectile p: projectiles) {
            MovableEntity e = collidesWithPlayerOrBot(p.getRadius(), p.getPos(), p.getPhase());
            if (e != null) {
                e.damage(p.getDamage());
                p.kill();
            }
            //collides with walls
            p.live();
        }

        //deletes the projectile from the list if it's dead
        projectiles.removeIf(p -> !p.isAlive());


        countdown--;

        //stops the countdown when the timer has run out
        if (countdown <= 0 || sb.scoreReached()) {
            endGame();
        }
        else {
            sendAllObjects();
        }
    }

    private void endGame() {
        t.cancel();
        t.purge();
        msgToAllConnected("Game Ended");
    }

    private void sendAllObjects() {
        for (Player p: players) {
            sendToAllConnected(p);
        }
        for (Zombie z: zombies) {
            sendToAllConnected(z);
        }
        for (Projectile p: projectiles) {
            sendToAllConnected(p);
        }
    }

    private void respawn(MovableEntity e) {
        e.setPos(respawnCoords());
        e.setDir(randomDir());
        e.setHealth(e.getMaxHealth());
       // msgToAllConnected("respawn in progress");
    }

    /**
     * returns a valid respawn coord
     */
    private Vector2 respawnCoords() {
        //get map bounds
        int boundX = map.getMapHeight();
        int boundY = map.getMapWidth();
        int minDist = 20;

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
        //msgToAllConnected(v.toString());
        return v;
    }


    /**
     * Will get a random vector of length 1 from 0,0
     */
    private Vector2 randomDir() {
        return new Vector2(0,1);
    }

    /**
     * Given a radius and a position, it checks to see if it collided with a player or bot
     * that is still alive
     * @param r the radius of the object
     * @param pos the centre of the object
     * @return the player or bot it is collided with.  Null if no collision
     */
    private MovableEntity collidesWithPlayerOrBot(int r, Vector2 pos) {
        for (Player p: players) {
            if (p.isAlive() && collided(r, pos, p.getRadius(), p.getPos())) return p;
        }

        for (Zombie z: zombies) {
            if (z.isAlive() && collided(r, pos, z.getRadius(), z.getPos())) return z;
        }

        return null;
    }

    /**
     * Given a radius and a position, it checks to see if it collided with a player or bot
     * that is still alive
     * @param r the radius of the object
     * @param pos the centre of the object
     * @return the player or bot it is collided with.  Null if no collision
     */
    private MovableEntity collidesWithPlayerOrBot(int r, Vector2 pos, int phase) {
        for (Player p: players) {
            if (p.isAlive() && collided(r, pos, p.getRadius(), p.getPos()) && phase == p.phase) return p;
        }

        for (Zombie z: zombies) {
            if (z.isAlive() && collided(r, pos, z.getRadius(), z.getPos()) && phase == z.phase) return z;
        }

        return null;
    }

    /**
     * returns true if the two entity have collided
     * @param r1 the radius of the first entity
     * @param p1 the position of the first entity
     * @param r2 the radius of the second entity
     * @param p2 the position of the second entity
     */
    private boolean collided(int r1, Vector2 p1, int r2, Vector2 p2) {
        return p1.getDistanceTo(p2) < r1 + r2;
    }

    /**
     * sends the string to all players in the lobby
     * @param s the string to be sent
     */
    private void msgToAllConnected(String s) {
        for (Connection c: playerConnections) {
            c.send(new objects.String(s));
        }
        //System.out.println(s);
    }

    private void sendToAllConnected(Entity e) {
        for (Connection c: playerConnections) {
            c.send(e);
        }
    }

    private void fire(Player player) {

        //testing the player who fired.
        System.out.println("player is" + player.getID());
        Weapon w = player.getActiveWeapon();
        if (w.canFire()) {
            Projectile p = w.getShotType();
            p.setDir(player.getDir());
            p.setPos(player.getPos());
            p.setID(IDCounter);
            IDCounter++;
            p.setPhase(player.phase);
            p.setPlayerID(player.getID());
            projectiles.add(p);
        }
    }

}
