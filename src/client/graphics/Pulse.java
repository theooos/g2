package client.graphics;

import org.lwjgl.opengl.GL11;
import server.game.Vector2;

import static org.lwjgl.opengl.GL11.GL_QUAD_STRIP;

/**
 * Created by peran on 3/5/17.
 * A graphical pulse
 */
class Pulse {
    private Vector2 start;
    private boolean alive;
    private float radius;
    private float red;
    private float green;
    private float blue;
    private float height;
    private float width;
    private float speed;


    Pulse(Vector2 start, float radius, float red, float green, float blue, float height, float width, float speed) {
        this.start = start;
        this.radius = radius;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.height = height;
        this.width = width;
        this.speed = speed;
        alive = true;
    }

    void draw(float radius, float strokeWidth) {
        GL11.glColor4f(red, green, blue, 0);
        GL11.glBegin(GL_QUAD_STRIP);
        float cx = start.getX();
        float cy = height-start.getY();
        GL11.glVertex2f(cx, cy+(radius-strokeWidth));
        GL11.glColor4f(red, green, blue, 1);
        GL11.glVertex2f(cx, cy+radius);
        for (int i = 1; i < 360; i++) {
            //y = hcosTheta
            //x = hsinTheta
            GL11.glColor4f(1f, 0, 0, 0);
            GL11.glVertex2d(cx+((radius-strokeWidth)*Math.sin(Math.toRadians(i))), cy+((radius-strokeWidth)*Math.cos(Math.toRadians(i))));
            GL11.glColor4f(1f, 0, 0, 1);
            GL11.glVertex2d(cx+((radius)*Math.sin(Math.toRadians(i))), cy+((radius)*Math.cos(Math.toRadians(i))));
        }
        GL11.glColor4f(1f, 0, 0, 0);
        GL11.glVertex2f(cx, cy+(radius-strokeWidth));
        GL11.glColor4f(1f, 0, 0, 1);
        GL11.glVertex2f(cx, cy+radius);
        GL11.glEnd();
        radius += speed;
        if (start.getX()+radius > width && start.getX()-radius < 0 && start.getY() + radius > height && start.getY() - radius < 0) {
            alive = false;
        }
    }

    boolean isAlive() {
        return alive;
    }
}
