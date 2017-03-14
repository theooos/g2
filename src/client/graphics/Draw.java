package client.graphics;

import static org.lwjgl.opengl.GL11.*;

import server.game.Vector2;


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

    void drawQuad(Vector2 centre, float rotation, float radius, float red, float green, float blue) {
        glColor4f(red, green, blue, 1);
        glPushMatrix();
        float cx = centre.getX();
        float cy = height-centre.getY();
        glTranslatef(cx, cy, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glTranslatef(-cx, -cy, 0);
        glBegin(GL_QUADS);
        glVertex2f(cx-radius, height-(centre.getY()-radius));
        glVertex2f(cx+radius, height-(centre.getY()-radius));
        glVertex2f(cx+radius, height-(centre.getY()+radius));
        glVertex2f(cx-radius, height-(centre.getY()+radius));
        glEnd();

        glBegin(GL_QUAD_STRIP);

        float intensity = 0.8f;
        glColor4f(red, green, blue, 0);
        glVertex2f(cx-radius*2,cy-radius*2);  //outer bottom left
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx-radius, cy-radius); //inner bottom left
        glColor4f(red, green, blue, 0);
        glVertex2f(cx+radius*2,cy-radius*2); //outer bottom right
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx+radius, cy-radius); //inner bottom right
        glColor4f(red, green, blue, 0);
        glVertex2f(cx+radius*2, cy+radius*2); //outer top right
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx+radius, cy+radius); //inner top right
        glColor4f(red, green, blue, 0);
        glVertex2f(cx-radius*2,cy+radius*2); //outer top left
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx-radius, cy+radius); //inner top left
        glColor4f(red, green, blue, 0);
        glVertex2f(cx-radius*2,cy-radius*2);  //outer bottom left
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx-radius, cy-radius); //inner bottom left

        glEnd();

        glPopMatrix();
    }

    private void flashDamage(float intensity, boolean hurt) {
        intensity = Math.min(1, intensity);
        glBegin(GL_QUAD_STRIP);
        float buffer = 60;
        float red = 0;
        float green = 1;
        if (hurt) {
            red = 1;
            green = 0;
        }

        glColor4f(red, green, 0, intensity/2);
        glVertex2f(0,0);
        glColor4f(red, green, 0, 0);
        glVertex2f(buffer, buffer);
        glColor4f(red, green, 0, intensity);
        glVertex2f(width,0);
        glColor4f(red, green, 0, 0);
        glVertex2f(width-buffer, buffer);
        glColor4f(red, green, 0, intensity);
        glVertex2f(width, height);
        glColor4f(red, green, 0, 0);
        glVertex2f(width-buffer, height-buffer);
        glColor4f(red, green, 0, intensity);
        glVertex2f(0,height);
        glColor4f(red, green, 0, 0);
        glVertex2f(buffer, height-buffer);
        glColor4f(red, green, 0, intensity/2);
        glVertex2f(0,0);
        glColor4f(red, green, 0, 0);
        glVertex2f(buffer, buffer);

        glEnd();
    }

    void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue) {
        drawAura(centre, radius, strokeWidth, red, green, blue, 1);
    }

    void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue, float intensity) {
        glColor4f(red, green, blue, intensity);
        glBegin(GL_QUAD_STRIP);
        float cx = centre.getX();
        float cy = height-centre.getY();
        glVertex2f(cx, cy+(radius-strokeWidth));
        glColor4f(red, green, blue, 0);
        glVertex2f(cx, cy+radius);
        for (int i = 1; i < 360; i++) {
            glColor4f(red, green, blue, intensity);
            glVertex2d(cx+((radius-strokeWidth)*Math.sin(Math.toRadians(i))), cy+((radius-strokeWidth)*Math.cos(Math.toRadians(i))));
            glColor4f(red, green, blue, 0);
            glVertex2d(cx+((radius)*Math.sin(Math.toRadians(i))), cy+((radius)*Math.cos(Math.toRadians(i))));
        }
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx, cy+(radius-strokeWidth));
        glColor4f(red, green, blue, 0);
        glVertex2f(cx, cy+radius);
        glEnd();
    }

    void shadeScreen() {
        glColor4f(1,1,1,0.6f);
        verticalDraw(0,0,width,height);
    }

    void colourBackground(int phase) {
        glColor4f(phase*0.1f,0,(1-phase)*0.1f,1f);
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

        glColor3f(heatRatio, green, 0f);
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

        if ((displayHealth-oldHealth) < 0.3 && (displayHealth-oldHealth) > -0.3) {
            displayHealth = oldHealth;
        }

        if (displayHealth != oldHealth) {
            flashDamage((float) Math.min(1, Math.abs(healthTick)), (healthTick < 0));
        }

        int healthWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float healthRatio = (float) (displayHealth/maxHealth);
        float green = (204f/255f)*healthRatio;
        glColor3f(1-healthRatio, green, 0f);
        verticalDraw(buffer, buffer+(1-healthRatio)*maxHeight, healthWidth, maxHeight*healthRatio);
    }

    void drawCircle(float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float) Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        glBegin(GL_TRIANGLE_FAN);

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
        glEnd();
    }

    private void verticalDraw(float xStart, float yStart, float rectWidth, float rectHeight) {
        glBegin(GL_QUADS);
        glVertex2f(checkX(xStart), checkY(height - yStart));
        glVertex2f(checkX(xStart + rectWidth), checkY(height - yStart));
        glVertex2f(checkX(xStart + rectWidth), checkY(height - (yStart+rectHeight)));
        glVertex2f(checkX(xStart), checkY(height - (yStart+rectHeight)));
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
