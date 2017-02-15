package client.ClientLogic;

import networking.Connection;
import server.game.MovableEntity;

import java.util.ArrayList;

/**
 * Created by Patrick on 2/11/2017.
 * class used for getting the entities.
 */
public class PlayerConnection  {


    private ArrayList<MovableEntity> entities;
    private Connection connection;

    /**
     * constructs the entities.
     * @param conn the connection
     */
    public PlayerConnection(Connection conn){

        super();
        //this.connection = con;
        entities = new ArrayList<>();

        this.connection=conn;



        connection.addFunctionEvent("String", this::out);
        connection.addFunctionEvent("Player", this::addEntity);
        connection.addFunctionEvent("AIPlayer", this::addEntity);
        connection.addFunctionEvent("Zombie", this::addEntity);
        connection.addFunctionEvent("Projectile", this::addEntity);




    }



    private void out(Object o){
       // System.out.println("[CLIENT] "+o);
    }

    synchronized private void addEntity(Object e) {
        MovableEntity a = (MovableEntity)e;
        out(a.getPos());


        if (entities.size() > 100) {
            entities.clear();
        }
        entities.add((MovableEntity) e);


    }

    synchronized public ArrayList<MovableEntity> getEntities() {
        return entities;
    }


}
