package client.testgui;

import networking.Connection;
import server.game.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import objects.String;

/**
 * Created by rhys on 19/01/17.
 * Represents the testing environment in which the sprites interact.
 */
public class TestEnvironment extends Observable {

    private Player player;
    private Zombie zombie;
    private Map map;
    private ArrayList<MovableEntity> entities;
    private Connection connection;

    public TestEnvironment(){

        super();
        //this.connection = con;
        entities = new ArrayList<>();

         connection = new Connection();

        connection.addFunctionEvent("String", this::out);
        connection.addFunctionEvent("Player", this::addEntity);
        connection.addFunctionEvent("AIPlayer", this::addEntity);
        connection.addFunctionEvent("Zombie", this::addEntity);
        connection.addFunctionEvent("Projectile", this::addEntity);


        // Create Map.
        map = null;
        try {
            map = new Map(0);
        } catch (IOException e) {
            System.out.println("Invalid MapID.");
            e.printStackTrace();
            System.exit(0);
        }

        // Create player.
        player = new Player(new Vector2(400, 500),
                new Vector2(0, 1), 0, 1,
                new Weapon(), new Weapon(),1);


        // Spawn zombie a reasonable distance away from the player.
        /*
        Random gen = new Random();
        boolean validDistance = false;
        Vector2 botPos = null;
        while (!validDistance){
            int botX = gen.nextInt(map.getMapWidth());
            int botY = gen.nextInt(map.getMapHeight());
            botPos = new Vector2(botX, botY);
            validDistance = player.getPos().getDistanceTo(botPos) >= 50;
        }
       // zombie = new Zombie(botPos, new Vector2(0, 1), 1, 1);
        */
    }

    public void sendFiringAlert(String message)
    {

        connection.send(message);

    }

    public void sendFirefire(String m)
    {

        connection.send(m);

    }

    /**
     * send the updated position to the server
     * @param v the vector of the new position
     */
    public void sendPosition(Vector2 v)
    {

        connection.send(v);
    }

    public boolean movePlayer(Vector2 direction){

        //System.out.println("do we get here?");
        // Face the player in the appropriate direction.
      player.setDir(direction);
        System.out.println("in move player");
       //Vector2 hypoLoc = player.hypoMove();

        // Check the movement isn't obstructed before making it.
        /*
        Vector2 hypoLoc = player.hypoMove();
        if (hypoLoc.getX() >= 0 && hypoLoc.getX() < map.getMapWidth()){

            //System.out.println("hypox: " + hypoLoc.getX());
            //System.out.println("hypoY: " + hypoLoc.getY());
            //System.out.println(map.getMapWidth()+ "the width value");
            if (hypoLoc.getY() >= 0 && hypoLoc.getY() < map.getMapHeight()){
                //player.live();
                player.move();
                // Execute the movement??
                System.out.println("yep");
                setChanged();
                notifyObservers();

                return true;
            } else return false;
        } else return false;

        */
        player.move();
        // Execute the movement??
        System.out.println("yep");
        setChanged();
        notifyObservers();
        return true;

    }

    public Player getPlayer(){
        return player;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public ArrayList<Wall> getWalls(){
        return map.wallsInPhase(1, true);
    }

    public int getMapHeight() {
        return map.getMapHeight();
    }

    public int getMapWidth() {
        return map.getMapWidth();
    }

    private void out(Object o){
        System.out.println("[CLIENT] "+o);
    }

    synchronized private void addEntity(Object e) {
        MovableEntity a = (MovableEntity)e;
        out(a.getPos());


        if (entities.size() > 100) {
            entities.clear();
        }
        entities.add((MovableEntity) e);

        setChanged();
        notifyObservers();
    }

    synchronized public ArrayList<MovableEntity> getEntities() {
        return entities;
    }

}
