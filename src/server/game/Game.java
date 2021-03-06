package server.game;

import networking.Connection_Server;
import objects.*;
import server.ai.decision.OrbIntel;
import server.ai.decision.PlayerIntel;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static server.game.ServerConfig.*;

/**
 * Created by peran on 27/01/17.
 * Controls the main game logic
 */
public class Game implements Runnable {
    private int countdown;
    private Map map;
    private CollisionManager collisions;

    private HashMap<Integer, Connection_Server> playerConnections;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private HashMap<Integer, Projectile> projectiles;
    private HashMap<Integer, PowerUp> powerUps;

    private Random rand;

    private Scoreboard scoreboard;
    private int IDCounter;
    private Timer timer;

    private boolean gameRunning;

    //for debugging
    private int tickCount = 0;
    private long lastTime =System.currentTimeMillis();


    /**
     * Creates a new game object.  This runs the entire game logic
     * @param playerConnections the list of the player connections
     * @param maxPlayers the maximum number of players in a game
     * @param mapID the map ID to set up the map
     * @param ld the lobby data of the game
     */
    public Game(HashMap<Integer, Connection_Server> playerConnections, int maxPlayers, int mapID, LobbyData ld) {
        IDCounter = 0;
        countdown = TIME_LIMIT;

        this.playerConnections = playerConnections;

        //try to load the map
        this.map = new Map(mapID);


        rand = new Random();
        scoreboard = new Scoreboard(ServerConfig.MAX_SCORE, maxPlayers);

        players = new ConcurrentHashMap<>();
        orbs = new HashMap<>();
        projectiles = new HashMap<>();
        powerUps = new HashMap<>();

        collisions = new CollisionManager(players, orbs, map, powerUps);

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
                    break;
            }

            Player p = new Player(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            Connection_Server con = playerConnections.get(i);
            con.addFunctionEvent("MoveObject", this::receivedMove);
            con.addFunctionEvent("FireObject", this::toggleFire);
            con.addFunctionEvent("PhaseObject", this::switchPhase);
            con.addFunctionEvent("SwitchObject", this::switchWeapon);

            players.put(IDCounter, p);
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
                    break;
            }
            Player p = new AIPlayer(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            players.put(IDCounter, p);
            IDCounter++;
        }
        //create team orbs
        for (int i = 0; i < maxPlayers; i++) {
            Orb o = new Orb(respawnCoords(), randomDir(), rand.nextInt(2), IDCounter);
            respawn(o);
            orbs.put(IDCounter, o);
            IDCounter++;

            // Ready Orbs for game.
            OrbIntel intel = new OrbIntel(players, map, powerUps);
            o.prepareOrbForGame(intel, orbs);
        }

        // Ready AI Players for game.
        for (int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            if (p instanceof AIPlayer){
                PlayerIntel intel = new PlayerIntel(players, map, powerUps);
                ((AIPlayer) p).preparePlayerForGame(intel, orbs);
            }
        }

        //Sets up the new powerups
        for (int i = 0; i < maxPlayers/2; i++) {
            PowerUp p;
            int phase = rand.nextInt(2);
            if (i%2 == 0) {
                p = new PowerUp(respawnCoords(), PowerUp.Type.health, i, phase);
            }
            else {
                p = new PowerUp(respawnCoords(), PowerUp.Type.heat, i, phase);
            }

            powerUps.put(i, p);
        }

        gameRunning = true;

        InitGame g = new InitGame(orbs, players, mapID, scoreboard, powerUps, ld);
        //notifies clients
        sendGameStart(g);
    }


    /**
     * Starts running the game loop
     */
    public void run() {
        long timeDelay = 1000/(long) SERVER_TICK;
        gameRunning = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (gameRunning) {
                    gameTick();
                    countTime();
                }
                else {
                    cancel();
                }
            }
        }, 1000, timeDelay);
    }

    /**
     * Stops the game
     */
    void stop() {
        timer.cancel();
        timer.purge();
    }

    /**
     * The game tick runs.  This is the master function for a running game
     */
     void gameTick() {
        boolean scoreboardChanged = false;

        //checks every payer
        for (Player p : players.values()) {
            //if dead start respawning
            if (!p.isAlive()) {
                if (p.canRespawn()) {
                    respawn(p);
                    //tell the player
                    if (!(p instanceof AIPlayer)) {
                        p.incMove();
                        Connection_Server connection = playerConnections.get(p.getID());
                        try {
                            connection.send(new MoveObject(p.getPos(), p.getDir(), p.getID(), p.getMoveCount()));
                        } catch (Exception e) {
                            dealWithConnectionLoss(connection);
                        }
                    }
                }
                else {
                    //death animation
                    shrinkRadius(p);
                }
            }

            //checks firing
            if (p.isFiring()) fire(p);

            p.live();
            PowerUp pu = (collisions.collidesWithPowerUp(p));
            //if found a powerup
            if (pu != null) {
                pu.setChanged(true);
                pu.setHealth(0);
                if (pu.getType() == PowerUp.Type.health) {
                    p.setHealth(p.getMaxHealth());
                }

                else if (pu.getType() == PowerUp.Type.heat) {
                    p.setWeaponOutHeat(0);
                    p.getActiveWeapon().setCurrentHeat(0);
                }
            }
        }

        //checks every powerup
        for (PowerUp p: powerUps.values()) {
            if (!p.isAlive()) {
                p.live();
                if (p.canRespawn()) {
                    respawn(p);
                }
            }
        }
        //checks every orb
        for (Orb o : orbs.values()) {
            if (!o.isAlive()) {
                if (o.canRespawn()) {
                    respawn(o);
                } else {
                    shrinkRadius(o);
                }
            }
            o.live();
        }

        //projectiles to remove
        ArrayList<Integer> keys = new ArrayList<>();
        //particles for an array list
        ArrayList<Projectile> hurtAni = new ArrayList<>();

        //checks every projectile
        for (Projectile p : projectiles.values()) {
            //if it hits something
            MovableEntity e = collisions.collidesWithPlayerOrBot(p);
            if (e != null && !e.equals(p.getPlayer())) {
//                System.out.println(p.getPlayerID()+" just hit "+e.getID());
                //can't damage your team
                if (e.getTeam() != p.getTeam() && e.isAlive()) {
                    //gets the direction the damage is taking from for animation
                    Vector2 damageDir = e.getPos().vectorTowards(p.getPos()).normalise();
                    //creates the damage projectiles
                    for (int i = 0; i < p.getDamage()/10; i++) {
                        double ang = Math.atan(damageDir.getX()/damageDir.getY());
                        if (Double.isInfinite(ang)) {
                            ang = 0;
                        } else if (damageDir.getY() < 0) {
                            ang += Math.PI;
                        }
                        ang += Math.toRadians(rand.nextInt(2*(int)(HURT_SPREAD))-(HURT_SPREAD));
                        float newX = (float)(Math.sin(ang));
                        float newY = (float)(Math.cos(ang));

                        hurtAni.add(new DistDropOffProjectile(0, (int) HURT_LIFE, HURT_RADIUS, e.getPos(), new Vector2(newX, newY).normalise(), 5, e.getPhase(), e, IDCounter));
                        IDCounter++;
                    }
                    //damage the entity
                    e.damage(p.getDamage());
                    //if the player has been killed
                    if (!e.isAlive()) {
                        if (e instanceof Orb) {
                            scoreboard.killedOrb((Player) p.getPlayer());
                        } else {
                            scoreboard.killedPlayer((Player) p.getPlayer());
                        }
                        scoreboardChanged = true;
                    }
                }
                p.kill();
            }

            //checks to see if collided with wall
            if (collisions.projectileWallCollision(p.getPos(), p.getDir(), p.getSpeed(), p.getPhase())) p.kill();

            p.live();
            //if dead list for removal
            if (!p.isAlive()) {
                keys.add(p.getID());
            }
        }

        //add hurt projectiles
        for (Projectile p: hurtAni) {
            projectiles.put(p.getID(), p);
        }


        countdown--;

        if (scoreboardChanged) {
            sendToAllConnected(scoreboard);
        }

        //stops the countdown when the timer has run out
        if (countdown <= 0 || scoreboard.scoreReached()) {
            endGame();
        } else {
            sendAllObjects();
            //remove all dead projectiles
            for (Integer i: keys) {
                projectiles.remove(i);
            }
        }
    }

    /**
     * if a player drops out
     */
    private void dealWithConnectionLoss(Connection_Server p) {
        if (!playerConnections.isEmpty()) {
            playerConnections.remove(p);
        }
    }

    /**
     * shrinks the radius based for death animation
     * @param e the entity who's radius to shrink
     */
    private void shrinkRadius(MovableEntity e) {
        float radius = e.getRadius();
        if (radius > 1) {
            radius -= radius * 0.005f;
            e.setRadius(radius);
        } else if (radius != 0) {
            e.setRadius(0);
        }
    }

    /**
     * Ends the game and msgs all clients
     */
    private void endGame() {
        gameRunning = false;
        sendToAllConnected(new GameOver(scoreboard.clone()));
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
        for (PowerUp p: powerUps.values()) {
            if (p.isChanged()) {
                p.setChanged(false);
                sendToAllConnected(p);
            }
        }
    }

    /**
     * Respawns an entity with a random position, dir, and phase
     * @param e the entity to be respawned
     */
    private void respawn(MovableEntity e) {
        e.resetTimeTillRespawn();
        e.setPos(respawnCoords());
        e.setDir(randomDir());
        e.setHealth(e.getMaxHealth());
        e.setPhase(rand.nextInt(2));
        if (e instanceof Player) {
            ((Player) e).setFiring(false);
            ((Player) e).setPhasePercentage(e.getPhase());
            e.setRadius(20);
        }
        else if (e instanceof PowerUp) {
            ((PowerUp) e).setChanged(true);
        }
        else if (e instanceof Orb) {
            e.setRadius(10);
        }
    }

    /**
     * returns a valid respawn coord
     */
    private Vector2 respawnCoords() {
        //get map bounds
        int boundX = map.getMapWidth()-200;
        int boundY = map.getMapLength()-200;

        boolean valid = false;
        Vector2 v = new Vector2(0,0);

        while (!valid) {
            valid = true;
            v = new Vector2(rand.nextInt(boundX)+100, rand.nextInt(boundY)+100);

            if (collisions.pointWallCollision(v, RESPAWN_DISTANCE, 0)) valid = false;
            else if (collisions.pointWallCollision(v, RESPAWN_DISTANCE, 1)) valid = false;
            else if (collisions.collidesWithPlayerOrBot(RESPAWN_DISTANCE, v) != null) valid = false;
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
     * sends an object to all connected plays
     * @param s the object to send
     */
    private void sendToAllConnected(Sendable s) {
        for (Connection_Server c: playerConnections.values()) {
            if (s instanceof Scoreboard) {
                sendScoreboard(c);
            }
            else {
                try {
                    c.send(s);
                } catch (Exception e) {
                    dealWithConnectionLoss(c);
                }
            }
        }
    }

    /**
     * Sends the game start to all players
     * @param g the init game object
     */
    private void sendGameStart(InitGame g) {
        for (Connection_Server c: playerConnections.values()) {
            try {
                c.send(g);
            } catch (Exception e) {
                dealWithConnectionLoss(c);
            }
        }
    }

    /**
     * sends a scoreboard to a player
     * @param c the connected player
     */
    private void sendScoreboard(Connection_Server c) {
        try {
            c.send(scoreboard.clone());
        } catch (Exception e) {
            dealWithConnectionLoss(c);
        }
    }

    /**
     * updates a received player to the received state
     * @param s a player object
     */
    private void receivedMove(Sendable s) {
        MoveObject m = (MoveObject) s;
        updatePlayerMove(m);
    }

    /**
     * When recieved a move object, update the player in the hasmap
     * @param m the recieved move
     */
    private synchronized void updatePlayerMove(MoveObject m) {
        Player p = players.get(m.getID());
        MoveObject old = new MoveObject(p.getPos(), p.getDir(), p.getID(), p.getMoveCount());
        //checks to see if it's not an old move
        if (m.getMoveCounter() == p.getMoveCount()) {
            p.setDir(m.getDir());
            p.setPos(m.getPos());
            if (collisions.validPosition(p)) {
                players.put(m.getID(), p);
            }
            else {
                Connection_Server conn = playerConnections.get(p.getID());
                try {
                    conn.send(old);
                } catch (Exception e) {
                    dealWithConnectionLoss(conn);
                }
            }
        }
        else {
            Connection_Server conn = playerConnections.get(p.getID());
            try {
                //conn.send(old);
            } catch (Exception e) {
                dealWithConnectionLoss(conn);
            }
        }
    }

    private void toggleFire(Sendable s) {
        FireObject f = (FireObject) s;
        Player p = players.get(f.getPlayerID());
        if (p.isAlive()) {
            p.setFiring(f.isStartFire());
        }
    }

    private void switchPhase(Sendable s) {
        PhaseObject phase = (PhaseObject) s;
        Player p = players.get(phase.getID());
        p.togglePhase();
    }

    private void switchWeapon(Sendable s) {
        SwitchObject sw = (SwitchObject) s;
        Player p = players.get(sw.getID());
        p.setWeaponOut(sw.takeWeaponOneOut());
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
                projectiles.put(p.getID(), p);
            }
        }
    }

    private void countTime() {
        if (tickCount  > 240) {
            tickCount = 0;
            lastTime = System.currentTimeMillis();
        }
        tickCount++;
    }

}
