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
       // this.pconn = pconn;

        connection.addFunctionEvent("String", this::getID);
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
         GameRenderer r = new GameRenderer(gd);
         r.setID(this.getID());
         r.execute();

    }

    public void getID(Object o)
    {
        String information = o.toString();
        System.out.println(information);
        String t = information.substring(0,2);
        System.out.println("t este : " + t);
        switch (t) {
            case "ID":

                String idS = information.substring(2);

                int id = Integer.parseInt(idS);

                this.setID(id);

            //case "You are being cared for by the lobby manager.":
            //case "Player connected":
            //case "You are in a 2 player lobby with 1 players in it":
            //case "Minimum number of players is reached, countdown starting":
                /*
                case "Game loading....":


                    break;
                 */
            default:
                System.out.println("[CLIENT] LOL" + 0);
                break;
        }

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

    public void setID(int id)
    {

        this.playerID = id;
    }

    public int getID()
    {

        return playerID;
    }



}
