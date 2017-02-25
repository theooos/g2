package client.ClientLogic;

import client.graphics.GameRendererCreator;
import networking.Connection;
import objects.InitGame;
import objects.Sendable;
import server.game.Player;
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
    private GameData gd;

    public ClientReceiver(Connection conn) {
        this.connection = conn;

        connection.addFunctionEvent("String", this::getID);
        connection.addFunctionEvent("InitGame", this::setupGame);
        connection.addFunctionEvent("Player", this::updatedPlayer);
        connection.addFunctionEvent("AIPlayer", i -> {});
        connection.addFunctionEvent("Zombie", i -> {});
        connection.addFunctionEvent("Projectile", i -> {});
    }

    public void setupGame(Sendable s) {
        InitGame i = (InitGame) s;
        HashMap<Integer, Player> players = i.getPlayers();
        HashMap<Integer, Zombie> zombies = i.getZombies();
        int mapID = i.getMapID();

        gd = new GameData(players, zombies, mapID);

        new Thread(new GameRendererCreator(gd,connection,getID())).start();

        out("The game is now executing.");
    }

    public void getID(Object o) {
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
        System.out.println("[CLIENT] " + o);
    }


    public void setID(int id) {
        this.playerID = id;
    }

    public int getID() {
        return playerID;
    }

    public void updatedPlayer(Sendable s) {
        Player p = (Player) s;
        gd.updatePlayer(p);
    }
}
