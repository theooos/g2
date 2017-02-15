package client;

import networking.Connection;
import objects.Sendable;
import server.game.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;


public class SendMove extends KeyAdapter implements Sendable  {

    private Player player;
    private Zombie zombie;
    private Map map;
    private ArrayList<MovableEntity> entities;
    private int x;
    private int y;
    private Connection conn;
    public SendMove(Connection com)
    {
        super();
        this.conn = com;
        entities = new ArrayList<>();
        x = 0;
        y = 0;


        conn.addFunctionEvent("String", this::out);
        conn.addFunctionEvent("Player", this::addEntity);
        conn.addFunctionEvent("AIPlayer", this::addEntity);
        conn.addFunctionEvent("Zombie", this::addEntity);
        conn.addFunctionEvent("Projectile", this::addEntity);


        // Create Map.
        map = null;
        try {
            map = new Map(0);
        } catch (IOException e) {
            System.out.println("Invalid MapID.");
            e.printStackTrace();
            System.exit(0);
        }

        /* Create player.
        player = new Player(new Vector2(400, 500),
                new Vector2(0, 1), 0, 1,
                new Weapon(), new Weapon(),1);
        */



    }


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            y = -1;
        }
        else if (key == KeyEvent.VK_A) {
            x = -1;

        }
        else if (key == KeyEvent.VK_S) {
            y = 1;

        }
        else if (key == KeyEvent.VK_D) {
            x = 1;
        }
        this.sendPosition(new Vector2(x, y));
    }



    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            y = 0;
        } else if (key == KeyEvent.VK_A) {
            x = 0;
        } else if (key == KeyEvent.VK_S) {
            y = 0;

        } else if (key == KeyEvent.VK_D) {
            x = 0;
        }

    }

    public void sendPosition(Vector2 v)
    {
        System.out.println("do we get here");
        conn.send(v);
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
        return map.getMapLength();
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

        //setChanged();
        //notifyObservers();
    }

    synchronized public ArrayList<MovableEntity> getEntities() {
        return entities;
    }
}
