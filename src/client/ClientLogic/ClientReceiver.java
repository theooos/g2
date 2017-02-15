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
public class ClientReceiver extends Entity {


    private Connection connection;
    private int mapID;
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
       // this.pconn = pconn;

        connection.addFunctionEvent("String", this::getData);
        connection.addFunctionEvent("InitGame",this::setupGame);
        //



    }

    //InitGame object -
    //Create other object GameData

    public void setupGame(Sendable s)
    {
        InitGame i = (InitGame) s;
        HashMap<Integer,Player> players = i.getPlayers();
        HashMap<Integer,Zombie> zombies = i.getZombies();
        int mapID = i.getMapID();

         gd = new GameData(players,zombies,mapID);
        new GameRenderer(gd).execute();
    }

    public GameData getGameData()
    {

        return gd;
    }

    /**
     *
     *
     * @param o method to get some data.
     */
     public void getData(Object o) {
    }

    /**
     *
     * @return the position at a certain moment.
     */
    public Vector2 getPosition()
    {

        return getPos();

    }





}
