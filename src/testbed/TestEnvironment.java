package testbed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import server.ai.Intel;
import server.game.*;

/**
 * Created by rhys on 19/01/17.
 * Represents the testing environment in which the sprites interact.
 */
public class TestEnvironment extends Observable {

    private Player player;
    private Orb orb;
    private Map map;
    private Intel env;


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
                new WeaponShotgun(), new WeaponSniper(), 0);
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        System.out.println("Player's initial location: " + player.getPos());

        env = new Intel(playerList, map);

        // Spawn orb a reasonable distance away from the player.
        Random gen = new Random();
        boolean validDistance = false;
        Vector2 orbPos = null;
        while (!validDistance){
            int botX = gen.nextInt(map.getMapWidth());
            int botY = gen.nextInt(map.getMapHeight());
            orbPos = new Vector2(botX, botY);
            validDistance = player.getPos().getDistanceTo(orbPos) >= 50;
        }

        orb = new Orb(orbPos, new Vector2(0, 1), 1, 1, 2, env);
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

    public Orb getOrb() {
        return orb;
    }

    public int getMapWidth() {
        return map.getMapWidth();
    }

    public int getMapLength() {
        return map.getMapHeight();
    }

    public ArrayList<Wall> getWalls(){
        return map.wallsInPhase(1, true);
    }
}
