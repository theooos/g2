package server.game;

import networking.Connection;
import objects.*;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.lang.String;
import java.util.*;

import static sun.audio.AudioPlayer.player;

/**
 * Created by peran on 27/01/17.
 * Controls the main game logic
 */
public class Game implements Runnable {
    private int countdown;
    private Map map;

    private ArrayList<Connection> playerConnections;
    private List<Player> players;
    private ArrayList<Zombie> zombies;
    private ArrayList<Projectile> projectiles;

    private Random rand;

    private Scoreboard sb;
    private int IDCounter;

    private final boolean DEBUG = true;


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

        out("Total players: "+playerConnections.size());

        rand = new Random();
        sb = new Scoreboard(100, maxPlayers);

        players = Collections.synchronizedList(new ArrayList<>());
        zombies = new ArrayList<>();
        projectiles = new ArrayList<>();

        //create players
        for (int i = 0; i < playerConnections.size(); i++) {
            //randomly select weapons for players
            Weapon w1;
            Weapon w2;
            int w = rand.nextInt(3);
            switch (w) {
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
                    out("Error selecting weapon");
                    break;
            }

            Player p = new Player(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            playerConnections.get(i).send(new objects.String("ID"+IDCounter));
            playerConnections.get(i).addFunctionEvent("String", this::decodeString);
            playerConnections.get(i).addFunctionEvent("MoveObject", this::receivedMove);
            playerConnections.get(i).addFunctionEvent("FireObject", this::toggleFire);
            playerConnections.get(i).addFunctionEvent("PhaseObject", this::switchPhase);

            addPlayer(p);
            IDCounter++;
        }
        //create AI players
        for (int i = playerConnections.size(); i < maxPlayers; i++) {
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
                    out("Error selecting weapon");
                    break;
            }
            Player p = new AIPlayer(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            addPlayer(p);
            IDCounter++;
        }
        //create team zombies
        for (int i = 0; i < maxPlayers; i++) {
            Zombie z = new Zombie(respawnCoords(), randomDir(),rand.nextInt(2), IDCounter);
            respawn(z);
            zombies.add(z);
            IDCounter++;
        }

        countdown = 10*60*tick; //ten minutes

        InitGame g = new InitGame(zombies, players, mapID);
        sendGameStart(g);
    }


    private void addPlayer(Player p) {
        List<Player> players = Collections.synchronizedList(this.players);
        players.add(p);
        this.players = players;
    }


    /**
     * The game tick runs.  This is the master function for a running game
     */
    public void run() {
        boolean isRunning = true;
        while(isRunning){
            for (Player p : players) {
                if (!p.isAlive()) {
                    respawn(p);
                    if (!(p instanceof AIPlayer)) {
                        playerConnections.get(p.getID()).send(new MoveObject(p.getPos(), p.getDir(), p.getID()));
                    }
                }
                if (p.isFiring()) fire(p);
                p.live();
            }
            for (Zombie z : zombies) {
                if (!z.isAlive()) respawn(z);
                z.live();
                z.setDir(randomDir());
            }

            for (Projectile p : projectiles) {
                MovableEntity e = collidesWithPlayerOrBot(p);
                if (e != null) {
                    out(p.getPlayerID()+" just hit "+e.getID());
                    e.damage(p.getDamage());
                    if (!e.isAlive()) {
                        if (e instanceof Zombie) {
                            sb.killedZombie(p.getPlayerID());
                        } else {
                            sb.killedPlayer(p.getPlayerID());
                        }
                    }
                    p.kill();
                }

                if (projectileWallCollision(p.getRadius(), p.getPos(), p.getDir(), p.getSpeed(), p.getPhase())) p.kill();

                p.live();
            }

            //deletes the projectile from the list if it's dead
            projectiles.removeIf(p -> !p.isAlive());

            countdown--;

            //stops the countdown when the timer has run out
            if (countdown <= 0 || sb.scoreReached()) {
                endGame();
                isRunning = false;
            } else {
                sendAllObjects();
            }
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean pointWallCollision(int r, Vector2 point, int phase) {
        for (Wall w: map.wallsInPhase(phase, true)) {
            if (linePointDistance(w.getStartPos(), w.getEndPos(), point) < (r+5)) return true;
        }

        return false;
    }

    private boolean projectileWallCollision(int r, Vector2 p1, Vector2 dir, float speed, int phase) {
        Vector2 p2 = p1.add(dir.mult(speed));
        Line2D l1 = new Line2D.Float(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        for (Wall w: map.wallsInPhase(phase, true)) {
            Line2D l2 = new Line2D.Float(w.getStartPos().getX(), w.getStartPos().getY(), w.getEndPos().getX(), w.getEndPos().getY());
            if (l2.intersectsLine(l1)) return true;
        }

        return false;
    }

    /**
     * Ends the game and msgs all clients
     */
    private void endGame() {
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
        if (e instanceof Player) {
            ((Player) e).setFiring(false);
        }
    }

    /**
     * returns a valid respawn coord
     */
    private Vector2 respawnCoords() {
        //get map bounds
        int boundX = map.getMapLength()-100;
        int boundY = map.getMapWidth()-100;
        int minDist = 20;

        boolean valid = false;
        Vector2 v = new Vector2(0,0);

        while (!valid) {
            valid = true;
            v = new Vector2(rand.nextInt(boundX)+50, rand.nextInt(boundY)+50);

            if (pointWallCollision(minDist, v, 0)) valid = false;
            else if (pointWallCollision(minDist, v, 1)) valid = false;
            else if (collidesWithPlayerOrBot(minDist, v) != null) valid = false;
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
     * Checks to see if the entity collides with an entity that isn't itself.
     * @param e the movable entity to check collisions with
     * @return the entity if there is a collision, null if there isn't
     */
    private MovableEntity collidesWithPlayerOrBot(MovableEntity e) {
        ArrayList<MovableEntity> entities = new ArrayList<>();
        entities.addAll(players);
        entities.addAll(zombies);
        entities.removeIf(et -> et.equals(e));
        entities.removeIf(et -> et.getPhase() != e.getPhase());
        if (e instanceof Projectile) {
            entities.removeIf(et -> et.equals(((Projectile) e).getPlayer()));
        }

        Vector2 nextPos = e.getPos().add(e.getDir().mult(e.getSpeed()));

        for (MovableEntity et: entities) {
            float minDist = linePointDistance(e.getPos(), nextPos, et.getPos());
            if (minDist < (e.getRadius() + et.getRadius())) {
                out("Collided with: ID"+et.getID());
                return et;
            }
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
        return p1.getDistanceTo(p2) < (r1 + r2);
    }



    private float linePointDistance(Vector2 v, Vector2 w, Vector2 p) {
        // Return minimum distance between line segment vw and point p
        float l2 = Math.abs(v.getDistanceTo(w));
        l2 = l2*l2; // i.e. |w-v|^2 -  avoid a sqrt
        if (l2 == 0.0) return p.getDistanceTo(v);   // v == w case
        // Consider the line extending the segment, parameterized as v + t (w - v).
        // We find projection of point p onto the line.
        // It falls where t = [(p-v) . (w-v)] / |w-v|^2
        // We clamp t from [0,1] to handle points outside the segment vw.
        float dot = ((p.sub(v)).dot(w.sub(v)) / l2);
        float t = Math.max(0, Math.min(1, dot));
        Vector2 projection = v.add(w.sub(v).mult(t));  // Projection falls on the segment
        return p.getDistanceTo(projection);
    }

    /**
     * sends the string to all players in the lobby
     * @param s the string to be sent
     */
    private void msgToAllConnected(String s) {
        for (Connection c: playerConnections) {
            c.send(new objects.String(s));
        }
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

    private void sendGameStart(InitGame g) {
        out("Sending init game");
        for (Connection c: playerConnections) {
            c.send(g);
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
                //switch weapon
                case 'w':
                    toggleWeapon(getPlayer(Integer.parseInt(s1)));
                    break;
                //"say" sends a message
                case 's':
                    out("SAY: "+s.substring(1));
                    break;
                default:
                    out(s);
            }
        }
        catch (Exception e) {
            out(s);
        }
    }


    private boolean validPosition(Player player) {
        return !pointWallCollision(player.getRadius(), player.getPos(), player.getPhase()) && collidesWithPlayerOrBot(player) == null;
    }

    /**
     * updates a received player to the received state
     * @param s a player object
     */
    private void receivedMove(Sendable s) {
        try {
            MoveObject m = (MoveObject) s;
            updatePlayerMove(m);

        }
        catch (Exception e) {
            out("Not a move" + e);
        }
    }


    /**
     * gets a player from an id
     * @param id the player id
     * @return returns the player of null if no player exist
     */
    private synchronized Player getPlayer(int id) {
        for (Player p: players) {
            if (p.getID() == id) {
                return p;
            }
        }
        return null;
    }

    /*private synchronized void removePlayer(int id) {
        players.removeIf(p -> p.getID() == id);
    }*/

    private synchronized void updatePlayerMove(MoveObject m) {
        for (int i = 0; i < players.size(); i++){
            if(players.get(i).getID() == m.getID()){
                Player p = players.get(i);
                p.setDir(m.getDir());
                p.setPos(m.getPos());
                players.set(i, p);
            }
        }
    }

    private void toggleFire(Sendable s) {
        out("Toggling fire");
        FireObject f = (FireObject) s;
        int id = f.getPlayerID();
        for (Player p : players) {
            if (p.getID() == id) {
                p.setFiring(f.isStartFire());
                break;
            }
        }
    }

    private void switchPhase(Sendable s) {
        PhaseObject phase = (PhaseObject) s;
        for (Player p: players) {
            if (p.getID() == phase.getID()) {
                p.togglePhase();
                out("ID"+p.getID()+": Switching phase");
                break;
            }
        }
    }

    /**
     * fires the active gun of the given player
     * @param player the gun to fire
     */
    private void fire(Player player) {
        Weapon w = player.getActiveWeapon();
        if (w.canFire()) {
            out("ID"+player.getID()+": Just Fired");
            ArrayList<Projectile> ps = w.getShots(player);
            for (Projectile p: ps) {
                p.setID(IDCounter);
                IDCounter++;
                projectiles.add(p);
                out("Shot Fired");
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

    private void out(Object o) {
        if (DEBUG) {
            System.out.println(o);
        }
    }

}
