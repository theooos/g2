package server.game;

import networking.Connection;
import objects.*;
import server.ai.Intel;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.lang.String;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by peran on 27/01/17.
 * Controls the main game logic
 */
public class Game implements Runnable {
    private int countdown;
    private Map map;

    private ArrayList<Connection> playerConnections;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private HashMap<Integer, Projectile> projectiles;

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

        players = new ConcurrentHashMap<>();
        orbs = new HashMap<>();
        projectiles = new HashMap<>();

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
            Connection con = playerConnections.get(i);
            con.send(new objects.String("ID"+IDCounter));
            con.addFunctionEvent("String", this::decodeString);
            con.addFunctionEvent("MoveObject", this::receivedMove);
            con.addFunctionEvent("FireObject", this::toggleFire);
            con.addFunctionEvent("PhaseObject", this::switchPhase);
            con.addFunctionEvent("SwitchObject", this::switchWeapon);

            players.put(IDCounter, p);
            IDCounter++;
        }
        //create AI players
        /*for (int i = playerConnections.size(); i < maxPlayers; i++) {
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
            players.put(IDCounter, p);
            IDCounter++;
        }*/
        //create team orbs
        for (int i = 0; i < maxPlayers; i++) {
            Orb o = new Orb(respawnCoords(), randomDir(),i % 2, rand.nextInt(2), IDCounter);
            respawn(o);
            orbs.put(IDCounter, o);
            IDCounter++;

            // Inform the Orbs.
            Intel intel = new Intel(players, map);
            o.prepareOrbForGame(intel, orbs);
        }

        countdown = 10*60*tick; //ten minutes

        InitGame g = new InitGame(orbs, players, mapID);
        sendGameStart(g);
    }


    /**
     * The game tick runs.  This is the master function for a running game
     */
    public void run() {

        boolean isRunning = true;
        while(isRunning){
            for (Player p : players.values()) {
                if (!p.isAlive()) {
                    respawn(p);
                    if (!(p instanceof AIPlayer)) {
                        playerConnections.get(p.getID()).send(new MoveObject(p.getPos(), p.getDir(), p.getID()));
                    }
                }
                if (p.isFiring()) fire(p);
                p.live();
            }
            for (Orb o : orbs.values()) {
                if (!o.isAlive()) respawn(o);
                o.live();
            }

            ArrayList<Integer> keys = new ArrayList<>();

            for (Projectile p : projectiles.values()) {
                MovableEntity e = collidesWithPlayerOrBot(p);
                if (e != null) {
                    out(p.getPlayerID()+" just hit "+e.getID());
                    e.damage(p.getDamage());
                    if (!e.isAlive()) {
                        if (e instanceof Orb) {
                            sb.killedZombie(p.getPlayerID());
                        } else {
                            sb.killedPlayer(p.getPlayerID());
                        }
                    }
                    p.kill();
                    keys.add(p.getID());
                }

                if (projectileWallCollision(p.getRadius(), p.getPos(), p.getDir(), p.getSpeed(), p.getPhase())) p.kill();

                p.live();
            }

            for (Integer i: keys) {
                projectiles.remove(i);
            }

            countdown--;

            //stops the countdown when the timer has run out
            if (countdown <= 0 || sb.scoreReached()) {
                endGame();
                isRunning = false;
            } else {
                sendAllObjects();
            }
            try {
                Thread.sleep(1000 /60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean pointWallCollision(int r, Vector2 point, int phase) {
        for (Wall w: map.wallsInPhase(phase, true, false)) {
            if (linePointDistance(w.getStartPos(), w.getEndPos(), point) < (r+5)) return true;
        }

        return false;
    }

    public static boolean pointWallCollision(int r, Vector2 point, int phase, Map map) {
        for (Wall w: map.wallsInPhase(phase, true, false)) {
            if (linePointDistance(w.getStartPos(), w.getEndPos(), point) < (r+5)) return true;
        }

        return false;
    }

    private boolean projectileWallCollision(int r, Vector2 p1, Vector2 dir, float speed, int phase) {
        Vector2 p2 = p1.add(dir.mult(speed));
        Line2D l1 = new Line2D.Float(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        for (Wall w: map.wallsInPhase(phase, true, false)) {
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
        for (Player p: players.values()) {
            sendToAllConnected(p);
        }
        for (Orb o: orbs.values()) {
            sendToAllConnected(o);
        }
        for (Projectile p: projectiles.values()) {
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
        e.setPhase(rand.nextInt(2));
        if (e instanceof Player) {
            ((Player) e).setFiring(false);
            out("ID"+e.getID()+" Respawning at "+e.getPos()+" and phase "+e.getPhase());
        }
    }

    /**
     * returns a valid respawn coord
     */
    private Vector2 respawnCoords() {
        //get map bounds
        int boundX = map.getMapWidth()-200;
        int boundY = map.getMapLength()-200;
        int minDist = 20;

        boolean valid = false;
        Vector2 v = new Vector2(0,0);

        while (!valid) {
            valid = true;
            v = new Vector2(rand.nextInt(boundX)+100, rand.nextInt(boundY)+100);

            if (pointWallCollision(minDist, v, 0)) valid = false;
            else if (pointWallCollision(minDist, v, 1)) valid = false;
            else if (collidesWithPlayerOrBot(minDist, v) != null) valid = false;
        }
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
        for (Player p: players.values()) {
            if (p.isAlive() && collided(r, pos, p.getRadius(), p.getPos())) return p;
        }

        for (Orb o: orbs.values()) {
            if (o.isAlive() && collided(r, pos, o.getRadius(), o.getPos())) return o;
        }

        return null;
    }

    /**
     * Given a radius and a position, it checks to see if it collided with a player that
     * is still alive
     * @param r the radius of the object
     * @param pos the centre of the object
     * @return the player it is collided with.  Null if no collision
     */
    public static MovableEntity collidesWithPlayer(int r, Vector2 pos, ConcurrentHashMap<Integer, Player> players) {
        for (Player p: players.values()) {
            if (p.isAlive() && collided(r, pos, p.getRadius(), p.getPos())) return p;
        }

        return null;
    }

    /**
     * Given a radius and a position, it checks to see if it collided with a bot
     * that is still alive
     * @param r the radius of the object
     * @param pos the centre of the object
     * @return the bot it is collided with.  Null if no collision
     */
    public static MovableEntity collidesWithBot(int r, Vector2 pos, HashMap<Integer, Orb> orbs) {

        for (Orb o: orbs.values()) {
            if (o.isAlive() && collided(r, pos, o.getRadius(), o.getPos())) return o;
        }

        return null;
    }

    /**
     * Checks to see if the entity collides with an entity that isn't itself.
     * @param e the movable entity to check collisions with
     * @return the entity if there is a collision, null if there isn't
     */
    private MovableEntity collidesWithPlayerOrBot(MovableEntity e) {
        HashMap<Integer, MovableEntity> entities = new HashMap<>();

        for(Player p: players.values()) {
            if (!(p.equals(e) ||  p.getPhase() != e.getPhase())) {
                if (!(e instanceof Projectile)) {
                    entities.put(p.getID(), p);
                }
                else if (!((Projectile) e).getPlayer().equals(p)){
                    entities.put(p.getID(), p);
                }
            }
        }

        for(Orb o: orbs.values()) {
            if (!(o.equals(e) ||  o.getPhase() != e.getPhase())) {
                entities.put(o.getID(), o);
            }
        }

        Vector2 nextPos = e.getPos().add(e.getDir().mult(e.getSpeed()));

        for (MovableEntity et: entities.values()) {
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
    public static boolean collided(int r1, Vector2 p1, int r2, Vector2 p2) {
        return p1.getDistanceTo(p2) < (r1 + r2);
    }

    
    public static float linePointDistance(Vector2 v, Vector2 w, Vector2 p) {
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
        out(s);
    }


    private boolean validPosition(Player player) {
        return !pointWallCollision(player.getRadius(), player.getPos(), player.getPhase()) && collidesWithPlayerOrBot(player) == null;
    }

    /**
     * updates a received player to the received state
     * @param s a player object
     */
    private void receivedMove(Sendable s) {
        MoveObject m = (MoveObject) s;
        updatePlayerMove(m);
    }

    /*private synchronized void removePlayer(int id) {
        players.removeIf(p -> p.getID() == id);
    }*/

    private synchronized void updatePlayerMove(MoveObject m) {
        Player p = players.get(m.getID());
        //MoveObject old = new MoveObject(p.getPos(), p.getDir(), p.getID());
        p.setDir(m.getDir());
        p.setPos(m.getPos());
        players.put(m.getID(), p);
    }

    private void toggleFire(Sendable s) {
        out("Toggling fire");
        FireObject f = (FireObject) s;
        Player p = players.get(f.getPlayerID());
        p.setFiring(f.isStartFire());
    }

    private void switchPhase(Sendable s) {
        PhaseObject phase = (PhaseObject) s;
        Player p = players.get(phase.getID());
        p.togglePhase();
        out("ID"+p.getID()+": Switching phase");
    }

    private void switchWeapon(Sendable s) {
        SwitchObject sw = (SwitchObject) s;
        Player p = players.get(sw.getID());
        p.setWeaponOut(sw.takeWeaponOneOut());
        out("ID"+p.getID()+": Switching weapon");
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
                projectiles.put(p.getID(), p);
                out("Shot Fired");
            }
        }
    }

    private void out(Object o) {
        if (DEBUG) {
            System.out.println(o);
        }
    }

}
