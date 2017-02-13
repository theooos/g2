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
        sb = new Scoreboard(100, maxPlayers);

        players = new ArrayList<>();
        zombies = new ArrayList<>();
        projectiles = new ArrayList<>();

        //create players
        for (int i = 0; i < playerConnections.size(); i++) {
            //randomly select weapons for players
            Weapon w1;
            Weapon w2;
            switch (rand.nextInt(3)) {
                case 0:
                    w1 = new WeaponShotgun();
                    w2 = new WeaponSniper();
                    break;
                case 1:
                    w1 = new WeaponSniper();
                    w2 = new WeaponSMG();
                    break;
                case 2:
                    w1 = new WeaponSMG();
                    w2 = new WeaponShotgun();
                    break;
                default:
                    w1 = new WeaponSniper();
                    w2 = new WeaponShotgun();
                    System.out.println("Error selecting weapon");
                    break;
            }

            Player p = new Player(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            playerConnections.get(i).send(new objects.String("ID"+IDCounter));
            playerConnections.get(i).addFunctionEvent("String", this::decodeString);
            playerConnections.get(i).addFunctionEvent("Player", this::updatePlayer);
            players.add(p);
            IDCounter++;
        }
        //create AI players
        for (int i = 0; i < maxPlayers-playerConnections.size(); i++) {
            //randomly select weapons for players
            Weapon w1;
            Weapon w2;
            switch (rand.nextInt(3)) {
                case 0:
                    w1 = new WeaponShotgun();
                    w2 = new WeaponSniper();
                    break;
                case 1:
                    w1 = new WeaponSniper();
                    w2 = new WeaponSMG();
                    break;
                case 2:
                    w1 = new WeaponSMG();
                    w2 = new WeaponShotgun();
                    break;
                default:
                    w1 = new WeaponSniper();
                    w2 = new WeaponShotgun();
                    System.out.println("Error selecting weapon");
                    break;
            }
            Player p = new AIPlayer(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            players.add(p);
            IDCounter++;
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
            MovableEntity e = collidesWithPlayerOrBot(p.getRadius(), p.getPos(), p.getPhase(), p.getDir(), p.getSpeed());
            if (e != null) {
                e.damage(p.getDamage());
                if (!e.isAlive()) {
                    if (e instanceof Zombie) {
                        sb.killedZombie(p.getPlayerID());
                    }
                    else {
                        sb.killedPlayer(p.getPlayerID());
                    }
                }
                p.kill();
            }
            //collides with walls
            p.live();
        }

        //deletes the projectile from the list if it's dead
        projectiles.removeIf(p -> !p.isAlive());

        System.out.println(randomDir());

        countdown--;

        //stops the countdown when the timer has run out
        if (countdown <= 0 || sb.scoreReached()) {
            endGame();
        }
        else {
            sendAllObjects();
        }
    }

    /**
     * Ends the game and msgs all clients
     */
    private void endGame() {
        t.cancel();
        t.purge();
        msgToAllConnected("Game Ended");
        for (Connection c: playerConnections) {
            sendScoreboard(c);
        }
    }

    /**
     * Sends all objects to all players
     */
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

    /**
     * Respawns an entity with a random position, dir, and phase
     * @param e the entity to be respawned
     */
    private void respawn(MovableEntity e) {
        e.setPos(respawnCoords());
        e.setDir(randomDir());
        e.setHealth(e.getMaxHealth());
        e.setPhase(rand.nextInt(1));
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
        int ang = rand.nextInt(359);
        return new Vector2((float)(Math.cos(Math.toRadians(ang))),(float)(Math.sin(Math.toRadians(ang))));
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
    private MovableEntity collidesWithPlayerOrBot(int r, Vector2 pos, int phase, Vector2 dir, float speed) {
        ArrayList<MovableEntity> entities = new ArrayList<>();
        entities.addAll(zombies);
        entities.addAll(players);
        entities.removeIf(e -> e.getPhase() != phase);

        for (MovableEntity e: entities) {
            Vector2 l1 = pos.add(dir.mult(speed));
            if (collided(r, getClosestPointOnLine(l1, pos, e.getPos()), e.getRadius(), e.getPos())) return e;
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


    //l1 is the start of a line, l2 is the end of a Line, point is the point to get closest point to
    private Vector2 getClosestPointOnLine(Vector2 l1, Vector2 l2, Vector2 point) {
        Vector2 a_to_p = new Vector2(point.getX() - l1.getX(), point.getY() - l1.getY());
        Vector2 a_to_b = new Vector2(l2.getX() - l1.getX(), l2.getY() - l1.getY());

        float atb2 = a_to_b.getX()*a_to_b.getX() + a_to_b.getY()*a_to_b.getY();

        float atp_dot_atb = a_to_p.getX()*a_to_b.getX() + a_to_p.getY()*a_to_b.getY();

        float t = atp_dot_atb / atb2;

        float temp1 = (l1.getX() + a_to_b.getX()*t);
        float temp2 = (l1.getY() + a_to_b.getY()*t);

        return new Vector2(temp1, temp2);
    }
    /**
     * sends the string to all players in the lobby
     * @param s the string to be sent
     */
    private void msgToAllConnected(String s) {
        for (Connection c: playerConnections) {
            c.send(new objects.String(s));
        }
        System.out.println(s);
    }

    /**
     * sends an entity to all connected players
     * @param e the entity to send
     */
    private void sendToAllConnected(Entity e) {
        for (Connection c: playerConnections) {
            c.send(e);
        }
    }

    /**
     * sends a scoreboard to a player
     * @param c the connected player
     */
    private void sendScoreboard(Connection c) {
        c.send(sb);
    }

    /**
     * decodes a string from a sendable string
     * @param s0 to sendable string to decode
     */
    private void decodeString(Sendable s0) {
        String s = s0.toString();
        try{
            String s1 = s.substring(1);
            switch (s.charAt(0)) {
                //fire
                case 'f':
                    fire(getPlayerFromID(Integer.parseInt(s1)));
                    break;
                //switch phase
                case 'p':
                    togglePhase(getPlayerFromID(Integer.parseInt(s1)));
                    break;
                //switch weapon
                case 'w':
                    toggleWeapon(getPlayerFromID(Integer.parseInt(s1)));
                    break;
                //"say" sends a message
                case 's':
                    System.out.println(s.substring(1));
                    break;
                default:
                    System.out.println(s);
            }
        }
        catch (Exception e) {
            System.out.println(s);
        }
    }

    /**
     * updates a received player to the received state
     * @param s a player object
     */
    private void updatePlayer(Sendable s) {
        try {
            Player player = (Player) s;
            players.removeIf(p -> p.equals(player));
            players.add(player);
        }
        catch (Exception e) {
            System.out.println("Not a player");
        }
    }

    /**
     * gets a player from an id
     * @param id the player id
     * @return returns the player of null if no player exist
     */
    private Player getPlayerFromID(int id) {
        for (Player p: players) {
            if (p.getID() == id) return p;
        }
        System.out.println("No player with that ID");
        return null;
    }

    /**
     * fires the active gun of the given player
     * @param player the gun to fire
     */
    private void fire(Player player) {
        Weapon w = player.getActiveWeapon();
        if (w.canFire()) {
            ArrayList<Projectile> ps = w.getShots(player);
            for (Projectile p: ps) {
                p.setID(IDCounter);
                IDCounter++;
                projectiles.add(p);
            }
        }
    }

    /**
     * switches the weapon of the given player
     * @param player the player to switch weapon
     */
    private void toggleWeapon(Player player) {
        player.toggleWeapon();
    }

    /**
     * Switches the phase of the given player
     * @param player the player to switch phase
     */
    private void togglePhase(Player player) {
        player.togglePhase();
    }

}
