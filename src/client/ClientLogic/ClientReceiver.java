package client.ClientLogic;

import client.graphics.GameRenderer;
import networking.Connection;
import objects.InitGame;
import objects.Sendable;
import server.game.Entity;
import server.game.Player;
import server.game.Vector2;
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

    /**
     *
     *
     *
     * @param conn the connection to the server
     * 
     */
    public ClientReceiver(Connection conn) {

        this.connection = conn;

        connection.addFunctionEvent("String", this::getID);
        connection.addFunctionEvent("InitGame",this::setupGame);
        connection.addFunctionEvent("Player", this::updatedPlayer);
        connection.addFunctionEvent("AIPlayer", this::out);
        connection.addFunctionEvent("Zombie", this::out);
        connection.addFunctionEvent("Projectile", this::out);

    }

    //InitGame object -
    //Create other object GameData

    public void setupGame(Sendable s)
    {
        System.out.println("Setting up game");
        InitGame i = (InitGame) s;
        HashMap<Integer,Player> players = i.getPlayers();
        HashMap<Integer,Zombie> zombies = i.getZombies();
        int mapID = i.getMapID();

        System.out.println("Vector size " + players.size());
        System.out.println("Player ID" + playerID);


        gd = new GameData(players,zombies,mapID);
         GameRenderer r = new GameRenderer(gd, connection);
         r.setID(this.getID());
         r.execute();
    }

    public void getID(Object o)
    {
        String information = o.toString();
        String t = information.substring(0,2);
        switch (t) {
            case "ID":

                String idS = information.substring(2);

                int id = Integer.parseInt(idS);

                this.setID(id);

            default:
                System.out.println("[CLIENT] LOL" + 0);
                break;
        }

    }

    public void out(Object o){
        System.out.println("[CLIENT] " + o);
    }


    public void setID(int id)
    {

        this.playerID = id;
    }

    public int getID()
    {

        return playerID;
    }

    public void updatedPlayer(Sendable s)
    {
        System.out.println("MOOORI");
        Player p = (Player) s;
        gd.updatePlayers(p);
    }
}
