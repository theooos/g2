package client.graphics;

import org.lwjgl.opengl.GL11;
import server.game.Map;
import server.game.Wall;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by bianca on 14/02/2017.
 * Used to display maps when in game
 */
class MapRenderer {

    private Map map;
    private int width;
    private int height;

    private final int EXTENT = 10;
    private float red;
    private float green;
    private float blue;

    MapRenderer(int mapID) {
        try {
            map = new Map(mapID);
            this.height = map.getMapLength();
            this.width = map.getMapWidth();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Create all components of the map
     */
    void renderMap(int phase) {

        ArrayList<Wall> walls = map.wallsInPhase(phase, true, false);

        for (Wall w : walls) {
            float xStart = w.getStartPos().getX();
            float yStart = w.getStartPos().getY();
            float xEnd = w.getEndPos().getX();
            float yEnd = w.getEndPos().getY();

            if (phase == 0) {
                red = 0.2f;
                green = 0.2f;
                blue = 1f;
            }
            else {
                red = 0.8f;
                green = 0f;
                blue = 0f;
            }

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
        GL11.glColor3f(red, green, blue);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(checkX(xStart - EXTENT), checkY(height - yStart+EXTENT));
        GL11.glVertex2f(checkX(xStart + EXTENT), checkY(height - yStart+EXTENT));
        GL11.glVertex2f(checkX(xEnd + EXTENT), checkY(height - yEnd + EXTENT));
        GL11.glVertex2f(checkX(xEnd - EXTENT), checkY(height - yEnd + EXTENT));
        GL11.glEnd();

        glBegin(GL_QUAD_STRIP);

        float intensity = 0.6f;
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart-EXTENT*2,height-(yEnd+EXTENT*2));  //outer bottom left
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart-EXTENT, height-(yEnd+EXTENT)); //inner bottom left
        glColor4f(red, green, blue, 0);
        glVertex2f(xEnd+EXTENT*2,height-(yEnd+EXTENT*2)); //outer bottom right
        glColor4f(red, green, blue, intensity);
        glVertex2f(xEnd+EXTENT, height-(yEnd+EXTENT)); //inner bottom right
        glColor4f(red, green, blue, 0);
        glVertex2f(xEnd+EXTENT*2, height-(yStart-EXTENT*2)); //outer top right
        glColor4f(red, green, blue, intensity);
        glVertex2f(xEnd+EXTENT, height-(yStart-EXTENT)); //inner top right
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart-EXTENT*2,height-(yStart-EXTENT*2)); //outer top left
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart-EXTENT, height-(yStart-EXTENT)); //inner top left
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart-EXTENT*2,height-(yEnd+EXTENT*2));  //outer bottom left
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart-EXTENT, height-(yEnd+EXTENT)); //inner bottom left

        glEnd();
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
        GL11.glColor3f(red, green, blue);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(checkX(xStart - EXTENT), checkY(height - yStart - EXTENT));
        GL11.glVertex2f(checkX(xEnd + EXTENT), checkY(height - yEnd - EXTENT));
        GL11.glVertex2f(checkX(xEnd + EXTENT), checkY(height - yEnd + EXTENT));
        GL11.glVertex2f(checkX(xStart - EXTENT), checkY(height - yStart + EXTENT));
        GL11.glEnd();

        glBegin(GL_QUAD_STRIP);

        float intensity = 0.6f;
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart-EXTENT*2,height-(yEnd-EXTENT*2));  //outer bottom left
        glColor4f(red, green, blue, height-intensity);
        glVertex2f(xStart-EXTENT, height-(yEnd-EXTENT)); //inner bottom left
        glColor4f(red, green, blue, 0);
        glVertex2f(xEnd+EXTENT*2,height-(yEnd-EXTENT*2)); //outer bottom right
        glColor4f(red, green, blue, intensity);
        glVertex2f(xEnd+EXTENT, height-(yEnd-EXTENT)); //inner bottom right
        glColor4f(red, green, blue, 0);
        glVertex2f(xEnd+EXTENT*2, height-(yStart+EXTENT*2)); //outer top right
        glColor4f(red, green, blue, intensity);
        glVertex2f(xEnd+EXTENT, height-(yStart+EXTENT)); //inner top right
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart-EXTENT*2,height-(yStart+EXTENT*2)); //outer top left
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart-EXTENT, height-(yStart+EXTENT)); //inner top left
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart-EXTENT*2,height-(yEnd-EXTENT*2));  //outer bottom left
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart-EXTENT, height-(yEnd-EXTENT)); //inner bottom left

        glEnd();
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
