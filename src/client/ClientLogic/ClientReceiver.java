package client.ClientLogic;

import client.graphics.GameRendererCreator;
import networking.Connection;
import objects.InitGame;
import objects.MoveObject;
import objects.Sendable;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Patrick on 2/11/2017.
 * Deals with the objects received from the Server
 */
public class ClientReceiver {

    private Connection connection;
    private int playerID;
    private static boolean DEBUG = true;
    private GameData gd;

    public ClientReceiver(Connection conn) {
        this.connection = conn;

        connection.addFunctionEvent("String", this::getID);
        connection.addFunctionEvent("InitGame", this::setupGame);
        connection.addFunctionEvent("Player", this::updatedPlayer);
        connection.addFunctionEvent("AIPlayer", this::updatedPlayer);
        connection.addFunctionEvent("Orb", this::updatedOrb);
        connection.addFunctionEvent("Projectile", this::updatedProjectile);
        connection.addFunctionEvent("MoveObject", this::movePlayer);
        connection.addFunctionEvent("DistDropOffProjectile", this::updatedDistProjectile);
        connection.addFunctionEvent("Scoreboard", this::updatedScoreboard);
        connection.addFunctionEvent("PowerUp", this::updatedPowerUp);

    }

    private void setupGame(Sendable s) {
        InitGame i = (InitGame) s;
        ConcurrentHashMap<Integer, Player> players = i.getPlayers();
        HashMap<Integer, Orb> orbs = i.getOrb();
        int mapID = i.getMapID();
        ConcurrentHashMap<Integer, Projectile> projectiles = new ConcurrentHashMap<>();

        gd = new GameData(players, orbs, projectiles, mapID, i.getSb(), i.getPowerUps());
        out("Setting up game");
        new Thread(new GameRendererCreator(gd,connection,getID())).start();

        out("The game is now executing.");
    }

    private void getID(Object o) {
        String information = o.toString();
        String t = information.substring(0, 2);

        switch (t) {
            case "ID":
                String idS = information.substring(2);

                int id = Integer.parseInt(idS);

                this.setID(id);

            default:
                System.out.println("[CLIENT] " + information);
                break;
        }
    }

    public void out(Object o) {
        if (DEBUG) System.out.println("[CLIENT] " + o);
    }


    private void setID(int id) {
        this.playerID = id;
    }

    private int getID() {
        return playerID;
    }

    private void updatedPlayer(Sendable s) {
        Player p = (Player) s;
        if (p.getID() != playerID) {
            gd.updatePlayer(p);
        }
        else {
            gd.updateMe(p);
        }
    }

    private void updatedOrb(Sendable s) {
        Orb o = (Orb) s;
        gd.updateOrb(o);

    }

    private void updatedProjectile(Sendable s) {
        Projectile p = (Projectile) s;
        gd.updateProjectile(p);
    }

    private void updatedPowerUp(Sendable s) {
        PowerUp p = (PowerUp) s;
        gd.updatePowerUp(p);
    }

    private void updatedScoreboard(Sendable s) {
        Scoreboard sb = (Scoreboard) s;
        out(s.toString());
        gd.updateScoreboard(sb);
    }

    private void updatedDistProjectile(Sendable s) {
        DistDropOffProjectile p = (DistDropOffProjectile) s;
        gd.updateProjectile(p);
    }

    private void movePlayer(Sendable s) {
        MoveObject m = (MoveObject) s;
        Player p = gd.getPlayer(m.getID());
        p.setPos(m.getPos());
        p.setMoveCount(m.getMoveCounter());
        gd.updatePlayer(p);
    }
}
