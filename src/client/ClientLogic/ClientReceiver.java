package client.ClientLogic;

import client.graphics.GameRendererCreator;
import networking.Connection;
import objects.InitGame;
import objects.Sendable;
import server.game.Player;
import server.game.Projectile;
import server.game.Zombie;

import java.util.HashMap;


/**
 * Created by Patrick on 2/11/2017.
 * Deals with the objects received from the Server
 */
public class ClientReceiver {

    private Connection connection;
    private int mapID;
    private int playerID;
    private static boolean DEBUG = true;
    private GameData gd;

    public ClientReceiver(Connection conn) {
        this.connection = conn;

        connection.addFunctionEvent("String", this::getID);
        connection.addFunctionEvent("InitGame", this::setupGame);
        connection.addFunctionEvent("Player", this::updatedPlayer);
        connection.addFunctionEvent("AIPlayer", this::updatedPlayer);
        connection.addFunctionEvent("Zombie", this::updatedZombie);
        connection.addFunctionEvent("Projectile", this::updatedProjectile);
    }

    private void setupGame(Sendable s) {
        InitGame i = (InitGame) s;
        HashMap<Integer, Player> players = i.getPlayers();
        HashMap<Integer, Zombie> zombies = i.getZombies();
        int mapID = i.getMapID();
        HashMap<Integer, Projectile> projectiles = new HashMap<>();

        gd = new GameData(players, zombies, projectiles, mapID);
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
    }

    private void updatedZombie(Sendable s) {
        Zombie z = (Zombie) s;
        gd.updateZombie(z);

    }

    private void updatedProjectile(Sendable s) {
        Projectile p = (Projectile) s;
        gd.updateProjectile(p);
    }
}
