package client.testgui;

import networking.Connection;
import server.game.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * Created by rhys on 19/01/17.
 * Represents the testing environment in which the sprites interact.
 */
public class TestEnvironment extends Observable {

    private Player player;
    private Zombie zombie;
    private Map map;
    private ArrayList<MovableEntity> entities;


    public TestEnvironment(){

        super();
        entities = new ArrayList<>();

        Connection connection = new Connection();

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
      /*  player = new Player(new Vector2(400, 500),
                new Vector2(0, 1), 0, 1,
                new Weapon(), new Weapon()); */

        // Spawn zombie a reasonable distance away from the player.
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

    }

    public boolean movePlayer(Vector2 direction){

        // Face the player in the appropriate direction.
        player.setDir(direction);

        // Check the movement isn't obstructed before making it.
        Vector2 hypoLoc = player.hypoMove();
        if (hypoLoc.getX() >= 0 && hypoLoc.getX() < map.getMapWidth()){
            if (hypoLoc.getY() >= 0 && hypoLoc.getY() < map.getMapHeight()){
                player.live();      // Execute the movement??
                setChanged();
                notifyObservers();
                return true;
            } else return false;
        } else return false;
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
    }

    synchronized public ArrayList<MovableEntity> getEntities() {
        return entities;
    }

}
