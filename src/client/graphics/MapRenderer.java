package client.graphics;

import org.lwjgl.opengl.GL11;
import server.game.Map;
import server.game.Vector2;
import server.game.Wall;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bianca on 14/02/2017.
 */
public class MapRenderer {

    private Map map;
    private int mapID;
    private int phase;
    private int width;
    private int height;
    private ArrayList<Wall> walls;

    public MapRenderer(int mapID, int phase){
        try {
            map = new Map(mapID);
            this.mapID = mapID;
            this.phase = phase;
            this.height = map.getMapWidth();
            this.width = map.getMapLength();
        } catch (IOException e) {
            System.out.println("Invalid MapID.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void renderMap(){

        walls = map.wallsInPhase(phase, true);

        for(Wall w: walls) {
            System.out.println("In map");

            float startX = w.getStartPos().getX();
            float startY = w.getStartPos().getY();
            float endX = w.getEndPos().getX();
            float endY = w.getEndPos().getY();

            /*
            Vector2 startPos = w.getStartPos();

            Vector2 endPos = w.getEndPos();

            GL11.glColor3f(245.0f, 225.0f, 65.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startPos.getX(), height - startPos.getY());
            GL11.glVertex2f(startPos.getX(), height - startPos.getY() + 10);
            GL11.glVertex2f(endPos.getX(), height - endPos.getY() + 10);
            GL11.glVertex2f(endPos.getX(), height - endPos.getX());
            GL11.glEnd();        System.out.print("Map");
            */

            System.out.println((startX - 10) + " " + startY + " ; " + (endX - 10) + " " + endY);
            GL11.glColor3f(245.0f, 225.0f, 65.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX - 10, height - startY - 10);
            GL11.glVertex2f(endX + 10, height - endY - 10);
            GL11.glVertex2f(endX + 10, height - endY + 10);
            GL11.glVertex2f(startX - 10, height - startY + 10);
            GL11.glEnd();
        }

        System.out.println("OUT map");

    }
}
