package client.graphics;

import client.ClientSettings;
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
    private float speed;
    private float strokeWidth;
    private float buffer;
    private int newPhase;
    private boolean maxRadius;
    private int max;
    private boolean showOtherPhase;


    Pulse(Vector2 start, float radius, float red, float green, float blue, float speed, float strokeWidth, int newPhase, boolean showOtherPhase) {
        this.start = start;
        this.radius = radius;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.speed = speed;
        this.strokeWidth = strokeWidth;
        this.newPhase = newPhase;
        this.showOtherPhase = showOtherPhase;
        buffer = 100;
        alive = true;
        maxRadius = false;
        max = 1;

    }

    Pulse(Vector2 start, float radius, float red, float green, float blue, float speed, float strokeWidth, int newPhase, int max, boolean showOtherPhase) {
        this.start = start;
        this.radius = radius;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.speed = speed;
        this.strokeWidth = strokeWidth;
        this.newPhase = newPhase;
        this.showOtherPhase = showOtherPhase;
        buffer = 100;
        alive = true;
        this.max = max;
        maxRadius = true;
    }

    void draw() {
        GL11.glColor4f(red, green, blue, 0);
        GL11.glBegin(GL_QUAD_STRIP);
        float cx = start.getX();
        float cy = ClientSettings.SCREEN_HEIGHT - start.getY();
        GL11.glVertex2f(cx, cy + (radius - strokeWidth));
        GL11.glColor4f(red, green, blue, 1);
        GL11.glVertex2f(cx, cy + radius);
        for (int i = 1; i < 360; i += 3) {
            //y = hcosTheta
            //x = hsinTheta
            GL11.glColor4f(red, green, blue, 0);
            GL11.glVertex2d(cx + ((radius - strokeWidth) * Math.sin(Math.toRadians(i))), cy + ((radius - strokeWidth) * Math.cos(Math.toRadians(i))));
            GL11.glColor4f(red, green, blue, 1);
            GL11.glVertex2d(cx + ((radius) * Math.sin(Math.toRadians(i))), cy + ((radius) * Math.cos(Math.toRadians(i))));
        }
        GL11.glColor4f(red, green, blue, 0);
        GL11.glVertex2f(cx, cy + (radius - strokeWidth));
        GL11.glColor4f(red, green, blue, 1);
        GL11.glVertex2f(cx, cy + radius);
        GL11.glEnd();
        radius += speed;
        if (maxRadius && max < radius) {
            alive = false;
        }
        if (start.getX() + radius - buffer > ClientSettings.SCREEN_WIDTH && start.getX() - radius + buffer < 0 && start.getY() + radius - buffer > ClientSettings.SCREEN_HEIGHT && start.getY() - radius + buffer < 0) {
            alive = false;
        }
    }

    boolean isAlive() {
        return alive;
    }

    Vector2 getStart() {
        return start;
    }

    float getRadius() {
        return radius;
    }

    int getNewPhase() {
        return newPhase;
    }

    public boolean isShowOtherPhase() {
        return showOtherPhase;
    }
}
