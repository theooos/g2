package client.ClientLogic;

import networking.Connection;
import server.game.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;


/**
 * Created by Patrick on 2/11/2017.
 * Deals with the objects received from the Server
 */
public class ClientReceiver extends Entity {


    private Connection connection;
    private PlayerConnection pconn;
    private ArrayList<Zombie> zombies;
    private ArrayList<Player> players;
    private int mapID;

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



    }



     public void getData(Object o) {

        //ArrayList<>

        /*ArrayList<MovableEntity> es = new ArrayList<>(pconn.getEntities());

        for (MovableEntity e : es) {


        }
        */

    }

    /**
     *
     *
     *
     * @return the position at a certain moment.
     */
    public Vector2 getPosition()
    {

        return getPos();

    }





}
