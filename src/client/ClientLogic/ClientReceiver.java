package client.ClientLogic;

import networking.Connection;
import server.game.Entity;
import server.game.MovableEntity;
import server.game.Vector2;
import server.game.Wall;

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

    /**
     *
     *
     *
     * @param conn the connection to the server
     * @param pconn
     */
    public ClientReceiver(Connection conn,PlayerConnection pconn) {

        this.connection = conn;
        this.pconn = pconn;

        connection.addFunctionEvent("String", this::getData);



    }



     public void getData(Object o) {



        ArrayList<MovableEntity> es = new ArrayList<>(pconn.getEntities());

        for (MovableEntity e : es) {


        }


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
