package client.graphics;

import org.lwjgl.opengl.GL11;
import server.game.Vector2;

import static org.lwjgl.opengl.GL11.*;


/**
 * Created by peran on 3/5/17.
 *  used for draw methods
 */
class Draw {
    private int width;
    private int height;
    private int oldHealth;
    private double displayHealth;
    private double oldHeat;
    private double displayHeat;

    Draw(int width, int height) {
        this.width = width;
        this.height = height;
        oldHealth = 0;
        displayHealth = 100;
        oldHeat = 0;
        displayHeat = 0;
    }

    void shadeScreen() {
        GL11.glColor4f(1,1,1,0.6f);
        verticalDraw(0,0,width,height);
    }

    void drawHeatBar(double heat, double maxHeat) {
        float heatBarSensitivity = 0.1f;
        if (heat != oldHeat) {
            oldHeat = heat;
        }

        double heatTick = heatBarSensitivity*(oldHeat-displayHeat);

        if (displayHeat > oldHeat && displayHeat+heatTick < oldHeat) {
            displayHeat = oldHeat;
        }
        else if (displayHeat < oldHeat && displayHeat+heatTick > oldHeat) {
            displayHeat = oldHeat;
        } else {
            displayHeat += heatTick;
        }

        if (displayHeat > maxHeat) displayHeat = maxHeat;
        else if (displayHeat < 0) displayHeat = 0;

        int heatWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float heatRatio = (float) (displayHeat/maxHeat);
        float green = (204f/255f)*(1-heatRatio);

        GL11.glColor3f(heatRatio, green, 0f);
        verticalDraw(width - (buffer+heatWidth), buffer+(1-heatRatio)*maxHeight, heatWidth, maxHeight*heatRatio);
    }

    void drawHealthBar(double health, double maxHealth) {
        float healthBarSensitivity = 0.1f;
        if (health != oldHealth) {
            oldHealth = (int) health;
        }

        double healthTick = healthBarSensitivity*(oldHealth-displayHealth);

        if (displayHealth > oldHealth && displayHealth+healthTick < oldHealth) {
            displayHealth = oldHealth;
        }
        else if (displayHealth < oldHealth && displayHealth+healthTick > oldHealth) {
            displayHealth = oldHealth;
        } else {
            displayHealth += healthTick;
        }

        if (displayHealth > maxHealth) displayHealth = maxHealth;
        else if (displayHealth < 0) displayHealth = 0;
        int healthWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float healthRatio = (float) (displayHealth/maxHealth);
        float green = (204f/255f)*healthRatio;
        GL11.glColor3f(1-healthRatio, green, 0f);
        verticalDraw(buffer, buffer+(1-healthRatio)*maxHeight, healthWidth, maxHeight*healthRatio);
    }

    void drawCircle(float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float) Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        GL11.glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i < num_segments; i++) {
            glVertex2f(x + cx, y + cy);//output vertex

            //calculate the tangential vector; remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them
            float tx = -y;
            float ty = x;

            //add the tangential vector
            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor
            x *= radial_factor;
            y *= radial_factor;
        }
        GL11.glEnd();
    }

    private void verticalDraw(float xStart, float yStart, float rectWidth, float rectHeight) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(checkX(xStart), checkY(height - yStart));
        GL11.glVertex2f(checkX(xStart + rectWidth), checkY(height - yStart));
        GL11.glVertex2f(checkX(xStart + rectWidth), checkY(height - (yStart+rectHeight)));
        GL11.glVertex2f(checkX(xStart), checkY(height - (yStart+rectHeight)));
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
