package client.graphics;

import org.lwjgl.opengl.GL11;
import server.game.Map;
import server.game.Wall;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bianca on 14/02/2017.
 */
public class MapRenderer {

    private Map map;
    private int mapID;
    private int width;
    private int height;
    private ArrayList<Wall> walls;

    private final int EXTENT = 10;

    public MapRenderer(int mapID) {
        try {
            map = new Map(mapID);
            this.mapID = mapID;
            this.height = map.getMapLength();
            this.width = map.getMapWidth();
            //System.out.println("height is: " + height);
            //System.out.println("width is: " + width);

        } catch (IOException e) {
//            System.out.println("Invalid MapID.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Create all components of the map
     */
    public void renderMap(int phase) {

        walls = map.wallsInPhase(phase, true);

        for (Wall w : walls) {
            float xStart = w.getStartPos().getX();
            float yStart = w.getStartPos().getY();
            float xEnd = w.getEndPos().getX();
            float yEnd = w.getEndPos().getY();

            if (yStart == yEnd)
                horizontalDraw(xStart, yStart, xEnd, yEnd);

            if (xStart == xEnd)
                verticalDraw(xStart, yStart, xEnd, yEnd);
        }
    }

    /**
     * Create vertical wall
     *
     * @param xStart X coordinate of the start position
     * @param yStart Y coordinate of the start position
     * @param xEnd   X coordinate of the end position
     * @param yEnd   Y coordinate of the end position
     */
    private void verticalDraw(float xStart, float yStart, float xEnd, float yEnd) {
        GL11.glColor3f(0.84f, 0.86f, 0.88f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(checkX(xStart - EXTENT), checkY(height - yStart - EXTENT));
        GL11.glVertex2f(checkX(xStart + EXTENT), checkY(height - yStart - EXTENT));
        GL11.glVertex2f(checkX(xEnd + EXTENT), checkY(height - yEnd + EXTENT));
        GL11.glVertex2f(checkX(xEnd - EXTENT), checkY(height - yEnd + EXTENT));
        GL11.glEnd();
    }

    /**
     * Create horizontal wall
     *
     * @param xStart X coordinate of the start position
     * @param yStart Y coordinate of the start position
     * @param xEnd   X coordinate of the end position
     * @param yEnd   Y coordinate of the end position
     */
    private void horizontalDraw(float xStart, float yStart, float xEnd, float yEnd) {
        GL11.glColor3f(0.84f, 0.86f, 0.88f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(checkX(xStart - EXTENT), checkY(height - yStart - EXTENT));
        GL11.glVertex2f(checkX(xEnd + EXTENT), checkY(height - yEnd - EXTENT));
        GL11.glVertex2f(checkX(xEnd + EXTENT), checkY(height - yEnd + EXTENT));
        GL11.glVertex2f(checkX(xStart - EXTENT), checkY(height - yStart + EXTENT));
        GL11.glEnd();
    }

    /**
     * Keep x coordinate on the screen
     *
     * @param x Initial x coordinate
     * @return Checked x coordinate
     */
    private float checkX(float x) {
        if (x < 0) x = 0;
        if (x > width) x = width;
        return x;
    }

    /**
     * Keep y coordinate on the screen
     *
     * @param y Initial y coordinate
     * @return Checked y coordinate
     */
    private float checkY(float y) {
        if (y < 0) y = 0;
        if (y > height) y = height;
        return y;
    }
}
