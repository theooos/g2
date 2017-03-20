package server.game;

import networking.Connection;
import networking.Connection_Server;
import objects.*;
import server.ai.Intel;

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

    private ArrayList<Connection_Server> playerConnections;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private HashMap<Integer, Projectile> projectiles;
    private HashMap<Integer, PowerUp> powerUps;

    private Random rand;

    private Scoreboard scoreboard;
    private int IDCounter;

    private boolean gameRunning;

    //for debugging
    private int tickCount = 0;
    private long lastTime =System.currentTimeMillis();


    public Game(ArrayList<Connection_Server> playerConnections, int maxPlayers, int mapID) {
        IDCounter = 0;

        this.playerConnections = playerConnections;

        //try to load the map
        try {
            this.map = new Map(mapID);
        }
        catch(IOException e) {
            System.err.println("Failed to load map");
        }

        out("Total players: "+playerConnections.size());

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
                    out("Error selecting weapon");
                    break;
            }

            Player p = new Player(respawnCoords(), randomDir(), i % 2, rand.nextInt(2), w1, w2, IDCounter);
            Connection_Server conn = playerConnections.get(i);
            try {
                conn.send(new objects.String("ID"+IDCounter));
            } catch (Exception e) {
                dealWithConnectionLoss(conn);
            }
            conn.addFunctionEvent("String", this::out);
            conn.addFunctionEvent("MoveObject", this::receivedMove);
            conn.addFunctionEvent("FireObject", this::toggleFire);
            conn.addFunctionEvent("PhaseObject", this::switchPhase);
            conn.addFunctionEvent("SwitchObject", this::switchWeapon);

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
                    out("Error selecting weapon");
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

            // Inform the Orbs.
            Intel intel = new Intel(players, map);
            o.prepareOrbForGame(intel, orbs);
        }

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

        countdown = 4*60*SERVER_TICK; //four minutes

        gameRunning = true;

        InitGame g = new InitGame(orbs, players, mapID, scoreboard, powerUps);
        sendGameStart(g);
    }


    public void run() {
        long timeDelay = 1000/(long) SERVER_TICK;
        gameRunning = true;
        Timer timer = new Timer();
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
        }, timeDelay, timeDelay);
    }

    /**
     * The game tick runs.  This is the master function for a running game
     */
    private void gameTick() {

        boolean scoreboardChanged = false;

        for (Player p : players.values()) {
            if (!p.isAlive()) {
                if (p.canRespawn()) {
                    respawn(p);
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
                    shrinkRadius(p);
                }
            }

            if (p.isFiring()) fire(p);
            p.live();
            PowerUp pu = (collisions.collidesWithPowerUp(p));
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

        for (PowerUp p: powerUps.values()) {
            if (!p.isAlive()) {
                p.live();
                if (p.canRespawn()) {
                    respawn(p);
                }
            }
        }
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

        ArrayList<Integer> keys = new ArrayList<>();

        for (Projectile p : projectiles.values()) {
            MovableEntity e = collisions.collidesWithPlayerOrBot(p);
            if (e != null) {
                out(p.getPlayerID()+" just hit "+e.getID());
                //can't damage your team
                if (e.getTeam() != p.getTeam() && p.isAlive()) {
                    e.damage(p.getDamage());
                    //if the player has been killed
                    if (!e.isAlive()) {
                        if (e instanceof Orb) {
                            scoreboard.killedOrb(p.getPlayer());
                        } else {
                            scoreboard.killedPlayer(p.getPlayer());
                        }
                        scoreboardChanged = true;
                    }
                }
                p.kill();
            }

            if (collisions.projectileWallCollision(p.getPos(), p.getDir(), p.getSpeed(), p.getPhase())) p.kill();

            p.live();
            if (!p.isAlive()) {
                keys.add(p.getID());
            }
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
            for (Integer i: keys) {
                projectiles.remove(i);
            }
        }
    }

    private void dealWithConnectionLoss(Connection_Server p) {
        out("Connection to "+p+"dropped.");
        //TODO THIS.
    }

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
        int minDist = 60;

        boolean valid = false;
        Vector2 v = new Vector2(0,0);

        while (!valid) {
            valid = true;
            v = new Vector2(rand.nextInt(boundX)+100, rand.nextInt(boundY)+100);

            if (collisions.pointWallCollision(minDist, v, 0)) valid = false;
            else if (collisions.pointWallCollision(minDist, v, 1)) valid = false;
            else if (collisions.collidesWithPlayerOrBot(minDist, v) != null) valid = false;
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
        for (Connection_Server c: playerConnections) {
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

    private void sendGameStart(InitGame g) {
        out("Sending init game");
        for (Connection_Server c: playerConnections) {
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

    private synchronized void updatePlayerMove(MoveObject m) {
        Player p = players.get(m.getID());
        MoveObject old = new MoveObject(p.getPos(), p.getDir(), p.getID(), p.getMoveCount());
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
                conn.send(old);
            } catch (Exception e) {
                dealWithConnectionLoss(conn);
            }
        }
    }

    private void toggleFire(Sendable s) {
        out("Toggling fire");
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

    private void countTime() {
        if (tickCount  > 240) {
            tickCount = 0;
            out("Time for 240 ticks: "+(System.currentTimeMillis()-lastTime));
            lastTime = System.currentTimeMillis();
        }
        tickCount++;
    }

    private void out(Object o) {
        if (DEBUG) {
            System.out.println(o);
        }
    }

}
