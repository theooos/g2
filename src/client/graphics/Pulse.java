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


    /**
     * Creates a new pulse effect to change phase
     * @param start the start pos
     * @param radius the start radius
     * @param red the red percentage
     * @param green the green percentage
     * @param blue the blue percentage
     * @param speed the speed of the pulse
     * @param strokeWidth the stroke width of the pulse
     * @param newPhase which is the new phase
     * @param showOtherPhase whether to show the layer underneath
     */
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
    /**
     * Creates a new pulse effect to change phase
     * @param start the start pos
     * @param radius the start radius
     * @param red the red percentage
     * @param green the green percentage
     * @param blue the blue percentage
     * @param speed the speed of the pulse
     * @param strokeWidth the stroke width of the pulse
     * @param newPhase which is the new phase
     * @param max the max radius the pulse can reach
     * @param showOtherPhase whether to show the layer underneath
     */
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

    /**
     * Draws the pulse
     */
    void draw() {
        //sets up the shape
        GL11.glColor4f(red, green, blue, 0);
        GL11.glBegin(GL_QUAD_STRIP);

        float cx = start.getX();
        float cy = ClientSettings.SCREEN_HEIGHT - start.getY();

        GL11.glVertex2f(cx, cy + (radius - strokeWidth));
        GL11.glColor4f(red, green, blue, 1);
        GL11.glVertex2f(cx, cy + radius);
        //sets the fade out
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
        //increase the radius of the pulse
        radius += speed;
        //checks to see if the pulse should end
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

    boolean isShowOtherPhase() {
        return showOtherPhase;
    }
}
