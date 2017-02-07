package testbed;

import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import server.game.*;

/**
 * Created by rhys on 19/01/17.
 * Represents the testing environment in which the sprites interact.
 */
public class TestEnvironment extends Observable {

    private Player player;
    private Zombie zombie;
    private Map map;


    public TestEnvironment(){

        super();

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
                new Weapon(), new Weapon());

        // Spawn zombie a reasonable distance away from the player.
        Random gen = new Random();
        boolean validDistance = false;
        Vector2 botPos = null;
        while (!validDistance){
            int botX = gen.nextInt(map.getMapWidth());
            int botY = gen.nextInt(map.getMapLength());
            botPos = new Vector2(botX, botY);
            validDistance = player.getPos().getDistanceTo(botPos) >= 50;
        }
        zombie = new Zombie(botPos, new Vector2(0, 1), 1, 1);

    }

    public boolean movePlayer(Vector2 direction){

        // Face the player in the appropriate direction.
        player.setDir(direction);
        System.out.println("Player now facing direction: " + player.getDir().toString());

        // Check the movement isn't obstructed before making it.
        Vector2 hypoLoc = player.hypoMove();
        if (hypoLoc.getX() >= 0 && hypoLoc.getX() < map.getMapWidth()){
            if (hypoLoc.getY() >= 0 && hypoLoc.getY() < map.getMapLength()){
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

    public int getMapWidth() {
        return map.getMapWidth();
    }

    public int getMapLength() {
        return map.getMapLength();
    }

    public ArrayList<Wall> getWalls(){
        return map.wallsInPhase(1, true);
    }
}
